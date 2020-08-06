package br.com.luizalabs.desafio.services;

import br.com.luizalabs.desafio.dtos.entrada.InsereMensagemDTO;
import br.com.luizalabs.desafio.exceptions.MensagemException;

public interface MensagemService {

  Long insereMensagem(InsereMensagemDTO dto) throws MensagemException;

  Boolean cancelaEnvio(Long idMensagem) throws MensagemException;

}
