package br.dev.pedropareschi.cursomc.repositories;

import br.dev.pedropareschi.cursomc.domain.Cliente;
import br.dev.pedropareschi.cursomc.domain.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    @Transactional(readOnly = true)
    Page<Pedido> findByCliente(Cliente cliente, Pageable pageRequest);

}
