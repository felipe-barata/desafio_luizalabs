package br.com.luizalabs.desafio.exceptions;

import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

public class MensagemNaoEncontradaException extends MensagemException {
  public MensagemNaoEncontradaException(Long idMensagem) {
    super(MessageFormat.format("Não encontrou a mensagem com o id: {0}", idMensagem), HttpStatus.NOT_FOUND);
  }
}
