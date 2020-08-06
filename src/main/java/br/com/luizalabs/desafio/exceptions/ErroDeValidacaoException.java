package br.com.luizalabs.desafio.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ErroDeValidacaoException extends MensagemException {

  public ErroDeValidacaoException() {
    super("Ocorreram erros de validação.", HttpStatus.BAD_REQUEST);
  }

  public ErroDeValidacaoException(List<String> detalhesErro) {
    super("Ocorreram erros de validação.", HttpStatus.BAD_REQUEST, detalhesErro);
  }
}
