package br.com.luizalabs.desafio.exceptions;

import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

public class OrdenacaoInvalidaException extends MensagemException {
  public OrdenacaoInvalidaException(String ordem) {
    super(MessageFormat.format("Ordem: {0} não é válida, são aceitos apenas ASC ou DESC", ordem), HttpStatus.BAD_REQUEST);
  }
}
