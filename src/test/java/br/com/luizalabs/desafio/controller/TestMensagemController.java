package br.com.luizalabs.desafio.controller;

import br.com.luizalabs.desafio.dtos.entrada.ComunicacoesMensagemDTO;
import br.com.luizalabs.desafio.dtos.entrada.InsereMensagemDTO;
import br.com.luizalabs.desafio.dtos.entrada.PaginacaoDTO;
import br.com.luizalabs.desafio.dtos.saida.ListaMensagensDTO;
import br.com.luizalabs.desafio.enums.EnumComunicacoes;
import br.com.luizalabs.desafio.exceptions.MensagemEntregueException;
import br.com.luizalabs.desafio.exceptions.MensagemNaoEncontradaException;
import br.com.luizalabs.desafio.exceptions.OrdenacaoInvalidaException;
import br.com.luizalabs.desafio.services.MensagemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TestMensagemController {

  private static final String URL = "/api/mensagem";

  private static final LocalDateTime DATA = LocalDateTime.now();
  private static final String DESTINATARIO = "Destinatario";
  private static final String MENSAGEM = "Essa é uma mensagem de teste";

  @MockBean
  private MensagemService mensagemService;

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testInsereMensagemVaiPassar() throws Exception {
    BDDMockito.given(mensagemService.insereMensagem(Mockito.any(InsereMensagemDTO.class))).willReturn(1L);

    mvc.perform(MockMvcRequestBuilders.post(URL)
        .content(objectMapper.writeValueAsString(getInsereMensagemDTO()))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void testInsereMensagemVaiDarErroValidacao() throws Exception {
    InsereMensagemDTO dto = getInsereMensagemDTO();
    dto.setMensagem("");
    mvc.perform(MockMvcRequestBuilders.post(URL)
        .content(objectMapper.writeValueAsString(dto))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCancelarEnvioVaiPassar() throws Exception {
    BDDMockito.given(mensagemService.cancelaEnvio(Mockito.anyLong())).willReturn(true);

    mvc.perform(MockMvcRequestBuilders.put(URL)
        .param("idMensagem", "1")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void testCancelarEnvioVaiDarErroMensagemNaoEcontrada() throws Exception {
    BDDMockito.given(mensagemService.cancelaEnvio(Mockito.anyLong())).willThrow(new MensagemNaoEncontradaException(1L));

    mvc.perform(MockMvcRequestBuilders.put(URL)
        .param("idMensagem", "1")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testCancelarEnvioVaiDarErroMensagemEntregue() throws Exception {
    BDDMockito.given(mensagemService.cancelaEnvio(Mockito.anyLong())).willThrow(new MensagemEntregueException(1L));

    mvc.perform(MockMvcRequestBuilders.put(URL)
        .param("idMensagem", "1")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict());
  }

  @Test
  public void testConsultaStatusMensagensSemBody() throws Exception {
    BDDMockito.given(mensagemService.consultaStatusMensagens(null)).willReturn(getListaMensagemDTO());
    mvc.perform(MockMvcRequestBuilders.get(URL)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void testConsultaStatusMensagensSemBodyVaiRetornarVazio() throws Exception {
    BDDMockito.given(mensagemService.consultaStatusMensagens(null)).willReturn(Page.empty());
    mvc.perform(MockMvcRequestBuilders.get(URL)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  public void testConsultaStatusMensagensComBody() throws Exception {
    BDDMockito.given(mensagemService.consultaStatusMensagens(Mockito.any(PaginacaoDTO.class))).willReturn(getListaMensagemDTO());
    mvc.perform(MockMvcRequestBuilders.get(URL)
        .content(objectMapper.writeValueAsString(PaginacaoDTO.builder().build()))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void testConsultaStatusMensagensComBodyVaiRetornarVazio() throws Exception {
    BDDMockito.given(mensagemService.consultaStatusMensagens(Mockito.any(PaginacaoDTO.class))).willReturn(Page.empty());
    mvc.perform(MockMvcRequestBuilders.get(URL)
        .content(objectMapper.writeValueAsString(PaginacaoDTO.builder().pagina(0).qtdDeRegistros(0).build()))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  public void testConsultaStatusMensagensComBodyVaiRetornarOrdemInvalida() throws Exception {
    BDDMockito.given(mensagemService.consultaStatusMensagens(Mockito.any(PaginacaoDTO.class))).willThrow(new OrdenacaoInvalidaException(""));
    mvc.perform(MockMvcRequestBuilders.get(URL)
        .content(objectMapper.writeValueAsString(PaginacaoDTO.builder().build()))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  private Page<ListaMensagensDTO> getListaMensagemDTO() {
    List<ListaMensagensDTO> lista = new ArrayList<>();
    lista.add(ListaMensagensDTO.builder().build());
    return new PageImpl<>(lista);
  }

  private InsereMensagemDTO getInsereMensagemDTO() {
    return InsereMensagemDTO.builder()
        .dataHoraEnvio(DATA)
        .destinatario(DESTINATARIO)
        .mensagem(MENSAGEM)
        .comunicacoes(getComunicacoesDTO())
        .build();
  }

  private List<ComunicacoesMensagemDTO> getComunicacoesDTO() {
    return Stream.of(EnumComunicacoes.values()).map(e -> ComunicacoesMensagemDTO.builder().tipo(e.getTipo()).build()).collect(Collectors.toList());
  }

}
