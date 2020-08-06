package br.com.luizalabs.desafio.domain;

import br.com.luizalabs.desafio.domain.chaves.ComunicacaoMensagemPK;
import br.com.luizalabs.desafio.enums.EnumComunicacoes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comunicacoes_mensagem")
public class ComunicacaoMensagem {

  @EmbeddedId
  private ComunicacaoMensagemPK chave;

  public EnumComunicacoes getTipo() {
    return chave.getTipo();
  }

  public Long getIdMensagem() {
    return chave.getIdMensagem();
  }
}
