package br.com.luizalabs.desafio.converters;

import br.com.luizalabs.desafio.enums.EnumComunicacoes;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class EnumComunicacoesMapping implements AttributeConverter<EnumComunicacoes, Integer> {

  @Override
  public Integer convertToDatabaseColumn(EnumComunicacoes enumInterface) {
    if (enumInterface == null) {
      return null;
    }
    return enumInterface.getTipo();
  }

  @Override
  public EnumComunicacoes convertToEntityAttribute(Integer integer) {
    if (integer == null || integer == 0) {
      return null;
    }

    return EnumComunicacoes.get(integer);
  }
}
