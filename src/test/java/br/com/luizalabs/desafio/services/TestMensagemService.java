package br.com.luizalabs.desafio.services;

import br.com.luizalabs.desafio.domain.ComunicacaoMensagem;
import br.com.luizalabs.desafio.domain.Mensagem;
import br.com.luizalabs.desafio.domain.chaves.ComunicacaoMensagemPK;
import br.com.luizalabs.desafio.dtos.entrada.ComunicacoesMensagemDTO;
import br.com.luizalabs.desafio.dtos.entrada.InsereMensagemDTO;
import br.com.luizalabs.desafio.dtos.entrada.PaginacaoDTO;
import br.com.luizalabs.desafio.enums.EnumComunicacoes;
import br.com.luizalabs.desafio.enums.EnumStatusMensagem;
import br.com.luizalabs.desafio.exceptions.MensagemEntregueException;
import br.com.luizalabs.desafio.exceptions.MensagemException;
import br.com.luizalabs.desafio.exceptions.MensagemNaoEncontradaException;
import br.com.luizalabs.desafio.projections.ListaMensagensProjection;
import br.com.luizalabs.desafio.repository.ComunicacaoMensagemRepository;
import br.com.luizalabs.desafio.repository.MensagemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestMensagemService {


  private static final LocalDateTime DATA = LocalDateTime.now();
  private static final String DESTINATARIO = "Destinatario";
  private static final String MENSAGEM = "Essa é uma mensagem de teste";

  @MockBean
  private MensagemRepository mensagemRepository;

  @MockBean
  private ComunicacaoMensagemRepository comunicacaoMensagemRepository;

  @Autowired
  private MensagemService mensagemService;

  @Test
  public void testInsereMensagemVaiPassar() throws MensagemException {
    BDDMockito.given(mensagemRepository.save(Mockito.any(Mensagem.class))).willReturn(getMensagem(EnumStatusMensagem.PENDENTE));
    BDDMockito.given(comunicacaoMensagemRepository.saveAll(Mockito.anyCollection())).willReturn(getComunicacao());
    Assertions.assertTrue(mensagemService.insereMensagem(getInsereMensagemDTO()) > 0);
  }

  @Test
  public void testCancelaEnvioVaiPassar() throws MensagemException {
    BDDMockito.given(mensagemRepository.findById(Mockito.anyLong())).willReturn(Optional.of(getMensagem(EnumStatusMensagem.PENDENTE)));
    BDDMockito.given(mensagemRepository.save(Mockito.any(Mensagem.class))).willReturn(getMensagem(EnumStatusMensagem.PENDENTE));
    Assertions.assertTrue(mensagemService.cancelaEnvio(1L));
  }

  @Test
  public void testCancelaEnvioVaiRetornarMensagemNaoEncontrada() throws MensagemException {
    BDDMockito.given(mensagemRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());
    BDDMockito.given(mensagemRepository.save(Mockito.any(Mensagem.class))).willReturn(getMensagem(EnumStatusMensagem.PENDENTE));
    Assertions.assertThrows(MensagemNaoEncontradaException.class, () -> mensagemService.cancelaEnvio(1L));
  }

  @Test
  public void testCancelaEnvioVaiRetornarMensagemJaEnviada() throws MensagemException {
    BDDMockito.given(mensagemRepository.findById(Mockito.anyLong())).willReturn(Optional.of(getMensagem(EnumStatusMensagem.ENTREGUE)));
    BDDMockito.given(mensagemRepository.save(Mockito.any(Mensagem.class))).willReturn(getMensagem(EnumStatusMensagem.ENTREGUE));
    Assertions.assertThrows(MensagemEntregueException.class, () -> mensagemService.cancelaEnvio(1L));
  }

  @Test
  public void testConsultaStatusMensagens() throws MensagemException {
    List<ListaMensagensProjection> c = new ArrayList<>();
    c.add(new ListaMensagensProjection() {
      @Override
      public Integer getIdMensagem() {
        return 1;
      }

      @Override
      public Integer getStatus() {
        return 1;
      }
    });
    Page<ListaMensagensProjection> pages = new PageImpl<>(c);
    BDDMockito.given(mensagemRepository.consultaStatusMensagens(Mockito.any(PageRequest.class))).willReturn(pages);

    Assertions.assertFalse(mensagemService.consultaStatusMensagens(PaginacaoDTO.builder().build()).isEmpty());
  }

  @Test
  public void testConsultaStatusMensagensSemResultados() throws MensagemException {
    List<ListaMensagensProjection> c = new ArrayList<>();
    c.add(new ListaMensagensProjection() {
      @Override
      public Integer getIdMensagem() {
        return 1;
      }

      @Override
      public Integer getStatus() {
        return 1;
      }
    });
    BDDMockito.given(mensagemRepository.consultaStatusMensagens(Mockito.any(PageRequest.class))).willReturn(Page.empty());

    Assertions.assertTrue(mensagemService.consultaStatusMensagens(PaginacaoDTO.builder().build()).isEmpty());
  }

  private List<ComunicacaoMensagem> getComunicacao() {
    List<ComunicacaoMensagem> l = new ArrayList<>();
    for (EnumComunicacoes e : EnumComunicacoes.values()) {
      l.add(ComunicacaoMensagem.builder()
          .chave(ComunicacaoMensagemPK.builder()
              .idMensagem(1L)
              .tipo(e)
              .build())
          .build());
    }
    return l;
  }

  private Mensagem getMensagem(EnumStatusMensagem status) {
    return Mensagem.builder()
        .status(status)
        .mensagem(MENSAGEM)
        .destinatario(DESTINATARIO)
        .dataHoraEnvio(DATA)
        .id(1L)
        .build();
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
