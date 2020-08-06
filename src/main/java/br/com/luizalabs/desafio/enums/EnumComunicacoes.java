package br.com.luizalabs.desafio.enums;

import lombok.AllArgsConstructor;

import java.util.stream.Stream;

@AllArgsConstructor
public enum EnumComunicacoes {

  EMAIL(1, "e-mail"),
  SMS(2, "SMS"),
  PUSH(3, "Push"),
  WHATSAPP(4, "whatsapp");

  private int tipo;
  private String descricao;

  public int getTipo() {
    return tipo;
  }

  public String getDescricao() {
    return descricao;
  }

  public static EnumComunicacoes get(int valor) {
    return Stream.of(values()).filter(e -> e.getTipo() == valor).findFirst().orElse(null);
  }
}
