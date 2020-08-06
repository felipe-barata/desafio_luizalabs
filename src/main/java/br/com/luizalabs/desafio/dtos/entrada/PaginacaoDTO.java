package br.com.luizalabs.desafio.dtos.entrada;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginacaoDTO implements Serializable {

  private int pagina;

  private int qtdDeRegistros;

  private String ordem;
}
