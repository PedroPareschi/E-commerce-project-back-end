package br.dev.pedropareschi.cursomc.services;

import br.dev.pedropareschi.cursomc.domain.Cidade;
import br.dev.pedropareschi.cursomc.domain.Cliente;
import br.dev.pedropareschi.cursomc.domain.Endereco;
import br.dev.pedropareschi.cursomc.domain.enums.Perfil;
import br.dev.pedropareschi.cursomc.domain.enums.TipoCliente;
import br.dev.pedropareschi.cursomc.dto.ClienteDTO;
import br.dev.pedropareschi.cursomc.dto.ClienteNewDTO;
import br.dev.pedropareschi.cursomc.repositories.ClienteRepository;
import br.dev.pedropareschi.cursomc.repositories.EnderecoRepository;
import br.dev.pedropareschi.cursomc.security.UserSS;
import br.dev.pedropareschi.cursomc.services.exceptions.AuthorizationException;
import br.dev.pedropareschi.cursomc.services.exceptions.DataIntegrityException;
import br.dev.pedropareschi.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repo;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private BCryptPasswordEncoder pe;

    @Autowired
    private GoogleService googleService;

    @Autowired
    private ImageService imageService;

    @Value("${img.prefix.client.profile}")
    private String prefix;

    @Value("${img.profile.size}")
    private Integer size;

    public Cliente find(Integer id){
        UserSS user = UserService.authenticated();
        if(user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())){
            throw new AuthorizationException("Acesso negado");
        }
        Optional<Cliente> obj = repo.findById(id);
        return obj.orElseThrow(()->  new ObjectNotFoundException("Objeto não encontrado! Id: " + id +
        ", Tipo: " + Cliente.class.getName()));
    }
    @Transactional
    public Cliente insert(Cliente obj){
        obj.setId(null);
        obj = repo.save(obj);
        enderecoRepository.saveAll(obj.getEnderecos());
        return obj;
    }

    public Cliente update(Cliente obj){
        Cliente newObj = find(obj.getId());
        updateData(newObj, obj);
       return repo.save(newObj);
    }

    public void delete(Integer id){
        find(id);
        try {
            repo.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionados");
        }
    }

    public Cliente findByEmail(String email) {
        UserSS user = UserService.authenticated();
        if (user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
            throw new AuthorizationException("Acesso negado");
        }

        Cliente obj = repo.findByEmail(email);
        if (obj == null) {
            throw new ObjectNotFoundException(
                    "Objeto não encontrado! Id: " + user.getId() + ", Tipo: " + Cliente.class.getName());
        }
        return obj;
    }


    public List<Cliente> findAll() {
        return repo.findAll();
    }

    public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return repo.findAll(pageRequest);
    }

    public Cliente fromDTO(ClienteDTO objDto){
        return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
    }

    private void updateData(Cliente newObj, Cliente obj){
        newObj.setNome(obj.getNome());
        newObj.setEmail(obj.getEmail());
    }

    public Cliente fromDTO(ClienteNewDTO objDto){
        Cliente cli = new Cliente(null,
                objDto.getNome(),
                objDto.getEmail(),
                objDto.getCpfOuCnpj(),
                TipoCliente.toEnum(objDto.getTipo()),
                pe.encode(objDto.getSenha())
                );

        Cidade cidade = new Cidade(objDto.getCidadeId(), null, null);

        Endereco end = new Endereco(null,
                objDto.getLogradouro(),
                objDto.getNumero(),
                objDto.getComplemento(),
                objDto.getBairro(),
                objDto.getCep(),
                cli,
                cidade);

        cli.getEnderecos().add(end);
        cli.getTelefones().add(objDto.getTelefone1());
        if(objDto.getTelefone2() != null){
            cli.getTelefones().add(objDto.getTelefone2());
        } if(objDto.getTelefone3() != null){
            cli.getTelefones().add(objDto.getTelefone3());
        }
        return cli;
    }

    public URI uploadProfilePicture(MultipartFile multipartFile){
        UserSS user = UserService.authenticated();
        if(user == null){
            throw new AuthorizationException("Acesso negado");
        }
        BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
        jpgImage = imageService.cropSquare(jpgImage);
        jpgImage = imageService.resize(jpgImage, size);
        String fileName = prefix + user.getId() + ".jpg";

        return googleService.uploadFile(imageService.getInputStream(jpgImage, "jpg"),
                fileName, "image");
    }
}
