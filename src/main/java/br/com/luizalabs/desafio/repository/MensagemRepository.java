package br.com.luizalabs.desafio.repository;

import br.com.luizalabs.desafio.domain.Mensagem;
import br.com.luizalabs.desafio.projections.ListaMensagensProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {

  @Query(value = "select id idMensagem, status from mensagem", nativeQuery = true)
  Page<ListaMensagensProjection> consultaStatusMensagens(PageRequest pageRequest);

}
