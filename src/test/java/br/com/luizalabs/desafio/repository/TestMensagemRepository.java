package br.com.luizalabs.desafio.repository;

import br.com.luizalabs.desafio.domain.Mensagem;
import br.com.luizalabs.desafio.enums.EnumStatusMensagem;
import br.com.luizalabs.desafio.projections.ListaMensagensProjection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestMensagemRepository {

  @Autowired
  private MensagemRepository mensagemRepository;

  @BeforeAll
  public void setUp() {
    List<Mensagem> mensagems = Arrays.asList(criaMensagem(), criaMensagem(), criaMensagem());
    mensagemRepository.saveAll(mensagems);
  }

  @Test
  public void testConsultaStatusMensagens() {
    Page<ListaMensagensProjection> mensagensProjections = mensagemRepository.consultaStatusMensagens(PageRequest.of(0, 25));
    Assertions.assertEquals(3, mensagensProjections.getContent().size());
  }

  @AfterAll
  public final void tearDown() {
    mensagemRepository.deleteAll();
  }

  private Mensagem criaMensagem() {
    return Mensagem.builder()
        .dataHoraEnvio(LocalDateTime.now())
        .destinatario("Destinatario")
        .mensagem("teste de mensagem")
        .status(EnumStatusMensagem.PENDENTE)
        .build();
  }
}
