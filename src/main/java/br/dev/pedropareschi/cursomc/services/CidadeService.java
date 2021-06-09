package br.dev.pedropareschi.cursomc.services;

import br.dev.pedropareschi.cursomc.domain.Cidade;
import br.dev.pedropareschi.cursomc.domain.Estado;
import br.dev.pedropareschi.cursomc.repositories.CidadeRepository;
import br.dev.pedropareschi.cursomc.repositories.EstadoRepository;
import br.dev.pedropareschi.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CidadeService {
    @Autowired
    public CidadeRepository repository;

    @Autowired
    public EstadoRepository estadoRepository;

    public List<Cidade> findByEstado(Integer estadoId){
        Estado estado = estadoRepository.findById(estadoId).orElse(null);
        if(estado == null){
            throw new ObjectNotFoundException("Estado com id inexistente");
        }
        return repository.findByEstadoId(estadoId);
    }
}
