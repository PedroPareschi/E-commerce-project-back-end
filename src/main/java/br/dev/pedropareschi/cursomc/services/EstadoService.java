package br.dev.pedropareschi.cursomc.services;

import br.dev.pedropareschi.cursomc.domain.Estado;
import br.dev.pedropareschi.cursomc.repositories.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoService {
    @Autowired
    public EstadoRepository repository;

    public List<Estado> findAll(){
        return repository.findAllByOrderByNome();
    }
}
