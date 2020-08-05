package br.com.luizalabs.desafio.services;

import br.com.luizalabs.desafio.dtos.entrada.InsereMensagemDTO;

public interface MensagemService {

  Long insereMensagem(InsereMensagemDTO dto);

  Boolean cancelaEnvio(Long idMensagem);

}
