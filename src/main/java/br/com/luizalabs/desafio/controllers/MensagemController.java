package br.com.luizalabs.desafio.controllers;

import br.com.luizalabs.desafio.dtos.entrada.InsereMensagemDTO;
import br.com.luizalabs.desafio.dtos.entrada.PaginacaoDTO;
import br.com.luizalabs.desafio.dtos.saida.ListaMensagensDTO;
import br.com.luizalabs.desafio.exceptions.ErroDeValidacaoException;
import br.com.luizalabs.desafio.exceptions.MensagemException;
import br.com.luizalabs.desafio.services.MensagemService;
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

  @PutMapping(produces = "application/json")
  public ResponseEntity cancelarEnvio(@RequestParam(name = "idMensagem") Long idMensagem) throws MensagemException {
    log.info("cancelarEnvio - idMensagem: {}", idMensagem);

    if (mensagemService.cancelaEnvio(idMensagem)) {
      log.info("cancelarEnvio - envio cancelado para o idMensagem: {}", idMensagem);
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.noContent().build();
  }

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
