create table if not exists mensagem(
  id              BIGINT NOT NULL AUTO_INCREMENT,
  data_hora_envio datetime not null,
  destinatario    varchar(200),
  mensagem        longtext,
  status          int not null,
PRIMARY KEY(id)
)ENGINE = InnoDB;

create table if not exists comunicacoes_mensagem(
  tipo        int NOT NULL,
  id_mensagem BIGINT NOT NULL,
  PRIMARY KEY(tipo, id_mensagem)
)ENGINE = InnoDB;