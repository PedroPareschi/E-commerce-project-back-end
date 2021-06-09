package br.dev.pedropareschi.cursomc.resources;

import br.dev.pedropareschi.cursomc.domain.Estado;
import br.dev.pedropareschi.cursomc.dto.EstadoDTO;
import br.dev.pedropareschi.cursomc.services.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {

    @Autowired
    private EstadoService service;
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<EstadoDTO>> findAll() {
        List<Estado> list = service.findAll();
        List<EstadoDTO> dtoList = list.stream().map(EstadoDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtoList);
    }
}
