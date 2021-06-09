package br.dev.pedropareschi.cursomc.resources;

import br.dev.pedropareschi.cursomc.domain.Cidade;
import br.dev.pedropareschi.cursomc.dto.CidadeDTO;
import br.dev.pedropareschi.cursomc.services.CidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CidadeResource {

    @Autowired
    public CidadeService service;

    @RequestMapping(method = RequestMethod.GET, value = "/estados/{estado_id}/cidades")
    public ResponseEntity<List<CidadeDTO>> findByEstado(@PathVariable Integer estado_id){
        List<Cidade> list = service.findByEstado(estado_id);
        List<CidadeDTO> dtoList = list.stream().map(CidadeDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtoList);
    }
}
