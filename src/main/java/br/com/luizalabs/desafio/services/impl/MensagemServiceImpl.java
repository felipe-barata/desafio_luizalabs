package br.com.luizalabs.desafio.services.impl;

import br.com.luizalabs.desafio.domain.ComunicacaoMensagem;
import br.com.luizalabs.desafio.domain.Mensagem;
import br.com.luizalabs.desafio.domain.chaves.ComunicacaoMensagemPK;
import br.com.luizalabs.desafio.dtos.entrada.ComunicacoesMensagemDTO;
import br.com.luizalabs.desafio.dtos.entrada.InsereMensagemDTO;
import br.com.luizalabs.desafio.enums.EnumStatusMensagem;
import br.com.luizalabs.desafio.repository.ComunicacaoMensagemRepository;
import br.com.luizalabs.desafio.repository.MensagemRepository;
import br.com.luizalabs.desafio.services.MensagemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MensagemServiceImpl implements MensagemService {

  @Autowired
  private MensagemRepository mensagemRepository;

  @Autowired
  private ComunicacaoMensagemRepository comunicacaoMensagemRepository;

  @Override
  public Long insereMensagem(InsereMensagemDTO dto) {
    log.info("insereMensagem");
    try {
      Mensagem mensagem = Mensagem.builder()
          .dataHoraEnvio(dto.getDataHoraEnvio())
          .destinatario(dto.getDestinatario())
          .mensagem(dto.getMensagem())
          .status(EnumStatusMensagem.PENDENTE)
          .build();
      log.debug("insereMensagem - persistir mensagem");
      mensagem = mensagemRepository.save(mensagem);
      if (mensagem == null || mensagem.getId() <= 0) {
        log.warn("insereMensagem - erro persistir a mensagem");
        return 0L;
      }
      Long idMensagem = mensagem.getId();
      log.debug("insereMensagem - mensagem criada com id: {}", idMensagem);
      List<ComunicacaoMensagem> comunicacaoMensagems = new ArrayList<>();
      dto.getComunicacoes().forEach(c -> comunicacaoMensagems.add(ComunicacaoMensagem.builder().chave(criaComunicacaoPK(c, idMensagem)).build()));
      List<ComunicacaoMensagem> retornoMensagem = comunicacaoMensagemRepository.saveAll(comunicacaoMensagems);
      if (retornoMensagem == null || retornoMensagem.isEmpty()) {
        log.warn("insereMensagem - erro persistir comunicacoes");
        return 0L;
      }
      return idMensagem;
    } catch (Exception e) {
      log.error("insereMensagem - erro: ", e);
    }
    return 0L;
  }

  @Override
  public Boolean cancelaEnvio(Long idMensagem) {
    log.info("cancelaEnvio - idMensagem: {}", idMensagem);
    try {
      Optional<Mensagem> optionalMensagem = mensagemRepository.findById(idMensagem);
      if (optionalMensagem.isEmpty()) {
        log.warn("cancelaEnvio - nao encontrou mensagem com id: {}", idMensagem);
        return false;
      }

      Mensagem mensagem = optionalMensagem.get();
      if (mensagem.getStatus().getStatus() == EnumStatusMensagem.ENTREGUE.getStatus()) {
        log.warn("cancelaEnvio - a mensagem com id: {} ja foi entregue", idMensagem);
        return false;
      }
      mensagem.setStatus(EnumStatusMensagem.CANCELADO);
      mensagem = mensagemRepository.save(mensagem);
      if (mensagem == null) {
        log.warn("cancelaEnvio - erro persistir mensagem");
        return false;
      }
    } catch (Exception e) {
      log.error("cancelaEnvio - erro: ", e);
    }
    return true;
  }

  private ComunicacaoMensagemPK criaComunicacaoPK(ComunicacoesMensagemDTO c, Long idMensagem) {
    return ComunicacaoMensagemPK.builder()
        .idMensagem(idMensagem)
        .tipo(c.getTipo())
        .build();
  }
}
