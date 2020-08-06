package br.com.luizalabs.desafio.exceptions;

import br.com.luizalabs.desafio.dtos.saida.ResponseErroDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value
      = {MensagemException.class})
  protected ResponseEntity<ResponseErroDTO> gerenciaExceptions(
      MensagemException ex, WebRequest request) {

    ResponseErroDTO responseErroDTO = ResponseErroDTO.builder()
        .codigoErro(ex.getCodigoErro().value())
        .detalhesErro(ex.getDetalhesErro())
        .error(ex.getMensagem())
        .timestamp(ZonedDateTime.now(ZoneId.of("UTC")))
        .build();

    return ResponseEntity.status(ex.getCodigoErro()).body(responseErroDTO);
  }

}
