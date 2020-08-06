package br.com.luizalabs.desafio.services.impl;

import br.com.luizalabs.desafio.domain.ComunicacaoMensagem;
import br.com.luizalabs.desafio.domain.Mensagem;
import br.com.luizalabs.desafio.domain.chaves.ComunicacaoMensagemPK;
import br.com.luizalabs.desafio.dtos.entrada.ComunicacoesMensagemDTO;
import br.com.luizalabs.desafio.dtos.entrada.InsereMensagemDTO;
import br.com.luizalabs.desafio.dtos.entrada.PaginacaoDTO;
import br.com.luizalabs.desafio.dtos.saida.ListaMensagensDTO;
import br.com.luizalabs.desafio.enums.EnumComunicacoes;
import br.com.luizalabs.desafio.enums.EnumStatusMensagem;
import br.com.luizalabs.desafio.exceptions.ErroInternoException;
import br.com.luizalabs.desafio.exceptions.MensagemEntregueException;
import br.com.luizalabs.desafio.exceptions.MensagemException;
import br.com.luizalabs.desafio.exceptions.MensagemNaoEncontradaException;
import br.com.luizalabs.desafio.exceptions.OrdenacaoInvalidaException;
import br.com.luizalabs.desafio.projections.ListaMensagensProjection;
import br.com.luizalabs.desafio.repository.ComunicacaoMensagemRepository;
import br.com.luizalabs.desafio.repository.MensagemRepository;
import br.com.luizalabs.desafio.services.MensagemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

  @Value("${paginacao.qtd_por_pagina}")
  private int qtdPorPagina;

  @Override
  public Long insereMensagem(InsereMensagemDTO dto) throws MensagemException {
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
      throw new ErroInternoException();
    }
  }

  @Override
  public Boolean cancelaEnvio(Long idMensagem) throws MensagemException {
    log.info("cancelaEnvio - idMensagem: {}", idMensagem);
    try {
      Optional<Mensagem> optionalMensagem = mensagemRepository.findById(idMensagem);
      if (optionalMensagem.isEmpty()) {
        log.warn("cancelaEnvio - nao encontrou mensagem com id: {}", idMensagem);
        throw new MensagemNaoEncontradaException(idMensagem);
      }

      Mensagem mensagem = optionalMensagem.get();
      if (mensagem.getStatus().getStatus() == EnumStatusMensagem.ENTREGUE.getStatus()) {
        log.warn("cancelaEnvio - a mensagem com id: {} ja foi entregue", idMensagem);
        throw new MensagemEntregueException(idMensagem);
      }
      mensagem.setStatus(EnumStatusMensagem.CANCELADO);
      mensagem = mensagemRepository.save(mensagem);
      if (mensagem == null) {
        log.warn("cancelaEnvio - erro persistir mensagem");
        return false;
      }
    } catch (Exception e) {
      if (e instanceof MensagemException) {
        throw e;
      }
      log.error("cancelaEnvio - erro: ", e);
      throw new ErroInternoException();
    }
    return true;
  }

  @Override
  public Page<ListaMensagensDTO> consultaStatusMensagens(PaginacaoDTO paginacaoDTO) throws MensagemException {
    Sort.Direction sort = Sort.Direction.ASC;
    try {
      if (paginacaoDTO != null && paginacaoDTO.getOrdem() != null && !paginacaoDTO.getOrdem().isEmpty()) {
        sort = Sort.Direction.fromString(paginacaoDTO.getOrdem());
      }
    } catch (IllegalArgumentException e) {
      throw new OrdenacaoInvalidaException(paginacaoDTO.getOrdem());
    }
    int pagina = paginacaoDTO != null ? paginacaoDTO.getPagina() : 0;
    int qtd = paginacaoDTO != null && paginacaoDTO.getQtdDeRegistros() > 0 ? paginacaoDTO.getQtdDeRegistros() : qtdPorPagina;
    log.info("consultaStatusMensagens - pagina: {}, qtd: {}, sort:{}", pagina, qtd, sort);
    try {
      PageRequest pageRequest = PageRequest.of(pagina, qtd, sort, "idMensagem");
      Page<ListaMensagensProjection> listaMensagensProjections = mensagemRepository.consultaStatusMensagens(pageRequest);
      if (listaMensagensProjections != null && !listaMensagensProjections.isEmpty()) {
        return listaMensagensProjections.map(this::converterParaListaDTO);
      }
    } catch (Exception e) {
      log.error("consultaStatusMensagens - erro: ", e);
      throw new ErroInternoException();
    }
    return Page.empty();
  }

  private ListaMensagensDTO converterParaListaDTO(ListaMensagensProjection p) {
    EnumStatusMensagem e = EnumStatusMensagem.get(p.getStatus());
    return ListaMensagensDTO.builder()
        .descricaoStatus(e.getDescricao())
        .idMensagem(p.getIdMensagem())
        .status(p.getStatus())
        .build();
  }

  private ComunicacaoMensagemPK criaComunicacaoPK(ComunicacoesMensagemDTO c, Long idMensagem) {
    return ComunicacaoMensagemPK.builder()
        .idMensagem(idMensagem)
        .tipo(EnumComunicacoes.get(c.getTipo()))
        .build();
  }
}
