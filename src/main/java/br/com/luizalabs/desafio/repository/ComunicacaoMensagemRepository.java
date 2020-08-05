package br.com.luizalabs.desafio.repository;

import br.com.luizalabs.desafio.domain.ComunicacaoMensagem;
import br.com.luizalabs.desafio.domain.chaves.ComunicacaoMensagemPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComunicacaoMensagemRepository extends JpaRepository<ComunicacaoMensagem, ComunicacaoMensagemPK> {
}
