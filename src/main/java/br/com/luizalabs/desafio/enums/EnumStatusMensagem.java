package br.com.luizalabs.desafio.enums;

import lombok.AllArgsConstructor;

import java.util.stream.Stream;

@AllArgsConstructor
public enum EnumStatusMensagem {

  PENDENTE(1, "Pendente"),
  ENTREGUE(2, "Entregue"),
  CANCELADO(3, "Cancelado"),
  ERRO(4, "Erro"),
  ;

  private int status;
  private String descricao;

  public int getStatus() {
    return status;
  }

  public String getDescricao() {
    return descricao;
  }

  public static EnumStatusMensagem get(int valor) {
    return Stream.of(values()).filter(e -> e.getStatus() == valor).findFirst().orElse(null);
  }
}
