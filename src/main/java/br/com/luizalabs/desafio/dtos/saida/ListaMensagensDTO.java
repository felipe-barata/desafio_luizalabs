package br.com.luizalabs.desafio.dtos.saida;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListaMensagensDTO implements Serializable {

  private int idMensagem;

  private int status;

  private String descricaoStatus;

}
