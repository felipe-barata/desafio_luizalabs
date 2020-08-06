package br.com.luizalabs.desafio.controllers;

import br.com.luizalabs.desafio.dtos.entrada.InsereMensagemDTO;
import br.com.luizalabs.desafio.dtos.entrada.PaginacaoDTO;
import br.com.luizalabs.desafio.dtos.saida.ListaMensagensDTO;
import br.com.luizalabs.desafio.dtos.saida.ResponseErroDTO;
import br.com.luizalabs.desafio.exceptions.ErroDeValidacaoException;
import br.com.luizalabs.desafio.exceptions.MensagemException;
import br.com.luizalabs.desafio.services.MensagemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/mensagem")
@CrossOrigin(origins = "*")
@Slf4j
public class MensagemController {

  @Autowired
  private MensagemService mensagemService;

  @Operation(summary = "Insere uma mensagem")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Mensagem criada", content = @Content(schema = @Schema(implementation = Long.class))),
      @ApiResponse(responseCode = "204", description = "Nenhuma informação foi inserida", content = @Content()),
      @ApiResponse(responseCode = "400", description = "Ocorreram erros de validação", content = @Content(schema = @Schema(implementation = ResponseErroDTO.class))),
      @ApiResponse(responseCode = "500", description = "Ocorreu um erro interno no servidor", content = @Content(schema = @Schema(implementation = ResponseErroDTO.class)))
  })
  @PostMapping(produces = "application/json", consumes = "application/json")
  public ResponseEntity<Long> insereMensagem(@RequestBody @Valid InsereMensagemDTO dto, BindingResult bindingResult) throws MensagemException {
    log.info("insereMensagem");

    if (bindingResult != null && bindingResult.hasErrors()) {
      log.warn("insereMensagem - erros de validacao");
      List<String> listaErro = new ArrayList<>();
      bindingResult.getAllErrors().forEach(error -> listaErro.add(error.getDefaultMessage()));
      throw new ErroDeValidacaoException(listaErro);
    }

    Long idMensagem = mensagemService.insereMensagem(dto);
    if (idMensagem > 0) {
      log.info("insereMensagem - mensagem criada com id: {}", idMensagem);
      return ResponseEntity.ok(idMensagem);
    }
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Cancela o envio de uma mensagem")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Envio cancelado", content = @Content()),
      @ApiResponse(responseCode = "204", description = "Não foi possível cancelar o envio", content = @Content()),
      @ApiResponse(responseCode = "409", description = "A mensagem já foi entregue", content = @Content(schema = @Schema(implementation = ResponseErroDTO.class))),
      @ApiResponse(responseCode = "404", description = "Mensagem não encontrada", content = @Content(schema = @Schema(implementation = ResponseErroDTO.class))),
      @ApiResponse(responseCode = "500", description = "Ocorreu um erro interno no servidor", content = @Content(schema = @Schema(implementation = ResponseErroDTO.class)))
  })
  @PutMapping(produces = "application/json")
  public ResponseEntity cancelarEnvio(@RequestParam(name = "idMensagem") Long idMensagem) throws MensagemException {
    log.info("cancelarEnvio - idMensagem: {}", idMensagem);

    if (mensagemService.cancelaEnvio(idMensagem)) {
      log.info("cancelarEnvio - envio cancelado para o idMensagem: {}", idMensagem);
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Retorna o status do agendamento das mensagens")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Retornou conteúdo"),
      @ApiResponse(responseCode = "204", description = "Não retornou conteúdo", content = @Content()),
      @ApiResponse(responseCode = "404", description = "Ocorreram erros de validação", content = @Content(schema = @Schema(implementation = ResponseErroDTO.class))),
      @ApiResponse(responseCode = "500", description = "Ocorreu um erro interno no servidor", content = @Content(schema = @Schema(implementation = ResponseErroDTO.class)))
  })
  @GetMapping(produces = "application/json", consumes = "application/json")
  public ResponseEntity<Page<ListaMensagensDTO>> consultaStatusMensagens(@RequestBody(required = false) PaginacaoDTO paginacaoDTO) throws MensagemException {
    log.info("consultaStatusMensagens");
    Page<ListaMensagensDTO> listaMensagensDTOS = mensagemService.consultaStatusMensagens(paginacaoDTO);
    if (listaMensagensDTOS != null && !listaMensagensDTOS.isEmpty()) {
      log.info("consultaStatusMensagens - retornou mensagens");
      return ResponseEntity.ok(listaMensagensDTOS);
    }
    return ResponseEntity.noContent().build();
  }
}
