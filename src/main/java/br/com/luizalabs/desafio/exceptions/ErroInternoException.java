package br.com.luizalabs.desafio.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ErroInternoException extends MensagemException {

  public ErroInternoException() {
    super("Ocoreu um erro interno no servidor.", HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public ErroInternoException(List<String> errosDetalhe) {
    super("Ocoreu um erro interno no servidor.", HttpStatus.INTERNAL_SERVER_ERROR, errosDetalhe);
  }
}
