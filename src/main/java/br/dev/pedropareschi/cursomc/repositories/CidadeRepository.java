package br.dev.pedropareschi.cursomc.repositories;

import br.dev.pedropareschi.cursomc.domain.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer> {

    @Transactional(readOnly = true)
    List<Cidade> findByEstadoId(Integer estadoId);
}
