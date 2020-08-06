package br.com.luizalabs.desafio.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class MensagemException extends Exception {

  private String mensagem;
  private HttpStatus codigoErro;
  private List<String> detalhesErro;

  public MensagemException(String mensagem, HttpStatus codigoErro) {
    super(mensagem);
    this.mensagem = mensagem;
    this.codigoErro = codigoErro;
  }

  public MensagemException(String mensagem, HttpStatus codigoErro, List<String> detalhesErro) {
    super(mensagem);
    this.mensagem = mensagem;
    this.codigoErro = codigoErro;
    this.detalhesErro = detalhesErro;
  }

}
