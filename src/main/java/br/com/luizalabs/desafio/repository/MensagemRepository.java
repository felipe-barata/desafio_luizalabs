package br.com.luizalabs.desafio.repository;

import br.com.luizalabs.desafio.domain.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
}
