package br.com.luizalabs.desafio.dtos.entrada;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsereMensagemDTO implements Serializable {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss'Z'")
  @NotNull(message = "Data inicial é obrigatória")
  private LocalDateTime dataHoraEnvio;

  @NotBlank(message = "Destinatário é obrigatório")
  private String destinatario;

  @NotBlank(message = "Mensagem é obrigatória")
  private String mensagem;

  @NotNull(message = "Obrigatório informar pelo menos uma comunicação")
  @NotEmpty(message = "Obrigatório informar pelo menos uma comunicação")
  private List<ComunicacoesMensagemDTO> comunicacoes;

}
