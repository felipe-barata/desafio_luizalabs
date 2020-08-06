package br.com.luizalabs.desafio.services;

import br.com.luizalabs.desafio.dtos.entrada.InsereMensagemDTO;
import br.com.luizalabs.desafio.dtos.entrada.PaginacaoDTO;
import br.com.luizalabs.desafio.dtos.saida.ListaMensagensDTO;
import br.com.luizalabs.desafio.exceptions.MensagemException;
import br.com.luizalabs.desafio.projections.ListaMensagensProjection;
import org.springframework.data.domain.Page;

public interface MensagemService {

  Long insereMensagem(InsereMensagemDTO dto) throws MensagemException;

  Boolean cancelaEnvio(Long idMensagem) throws MensagemException;

  Page<ListaMensagensDTO> consultaStatusMensagens(PaginacaoDTO paginacaoDTO) throws MensagemException;
}
