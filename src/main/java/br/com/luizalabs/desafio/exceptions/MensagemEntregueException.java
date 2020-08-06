package br.com.luizalabs.desafio.exceptions;

import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

public class MensagemEntregueException extends MensagemException {

  public MensagemEntregueException(Long idMensagem) {
    super(MessageFormat.format("A mensagem de id: {0} já foi entregue.", idMensagem), HttpStatus.CONFLICT);
  }
}
