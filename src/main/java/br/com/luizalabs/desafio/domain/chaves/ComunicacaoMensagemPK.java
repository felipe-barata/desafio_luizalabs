package br.com.luizalabs.desafio.domain.chaves;

import br.com.luizalabs.desafio.converters.EnumComunicacoesMapping;
import br.com.luizalabs.desafio.enums.EnumComunicacoes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComunicacaoMensagemPK implements Serializable {

  @Convert(converter = EnumComunicacoesMapping.class)
  private EnumComunicacoes tipo;

  @Column(name = "id_mensagem")
  private Long idMensagem;

}
