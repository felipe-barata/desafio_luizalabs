package br.com.luizalabs.desafio.converters;

import br.com.luizalabs.desafio.enums.EnumStatusMensagem;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class EnumStatusMapping implements AttributeConverter<EnumStatusMensagem, Integer> {

  @Override
  public Integer convertToDatabaseColumn(EnumStatusMensagem enumInterface) {
    if (enumInterface == null) {
      return null;
    }
    return enumInterface.getStatus();
  }

  @Override
  public EnumStatusMensagem convertToEntityAttribute(Integer integer) {
    if (integer == null || integer == 0) {
      return null;
    }

    return EnumStatusMensagem.get(integer);
  }
}
