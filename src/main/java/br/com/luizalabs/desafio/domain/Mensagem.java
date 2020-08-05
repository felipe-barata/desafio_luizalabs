package br.com.luizalabs.desafio.domain;

import br.com.luizalabs.desafio.converters.EnumStatusMapping;
import br.com.luizalabs.desafio.enums.EnumStatusMensagem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mensagem")
public class Mensagem {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "data_hora_envio", columnDefinition = "datetime")
  private LocalDateTime dataHoraEnvio;

  private String destinatario;

  @Column(columnDefinition = "longtext")
  private String mensagem;

  @Convert(converter = EnumStatusMapping.class)
  private EnumStatusMensagem status;

}
