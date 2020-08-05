package br.com.luizalabs.desafio.domain.chaves;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComunicacaoMensagemPK implements Serializable {

  private Integer tipo;

  @Column(name = "id_mensagem")
  private Long idMensagem;

}
