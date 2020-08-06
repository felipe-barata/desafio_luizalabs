package br.com.luizalabs.desafio.dtos.entrada;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComunicacoesMensagemDTO implements Serializable {

  @NotNull(message = "Tipo é obrigatório")
  @DecimalMin(value = "1", message = "Tipo inválido")
  private int tipo;

}
