package br.com.rodrigodacruz.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.rodrigodacruz.domain.Endereco;
import br.com.rodrigodacruz.domain.Usuario;
import br.com.rodrigodacruz.domain.enums.Perfil;
import br.com.rodrigodacruz.domain.enums.TipoUsuario;
import br.com.rodrigodacruz.dto.usuario.CadastrarUsuarioDTO;
import br.com.rodrigodacruz.dto.usuario.EditarUsuarioDTO;
import br.com.rodrigodacruz.repositories.UsuarioRepository;
import br.com.rodrigodacruz.repositories.EnderecoRepository;
import br.com.rodrigodacruz.security.JWTAuthorizationService;
import br.com.rodrigodacruz.security.JWTUtil;
import br.com.rodrigodacruz.security.JWTAuthorization;
import br.com.rodrigodacruz.services.exceptions.AuthorizationException;
import br.com.rodrigodacruz.services.exceptions.DataIntegrityException;
import br.com.rodrigodacruz.services.exceptions.ObjectNotFoundException;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private BCryptPasswordEncoder senhaEncoder;
	
	@Autowired
	private JWTUtil tokenResetPassword;

	public Usuario find(Integer id) {

		JWTAuthorizationService usuario = JWTAuthorization.authenticated();
		if (usuario == null || !usuario.hasRole(Perfil.ADMIN) && !id.equals(usuario.getId())) {
			throw new AuthorizationException("Acesso negado");
		}

		Optional<Usuario> objeto = usuarioRepository.findById(id);
		return objeto.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Usuario.class.getName()));
	}

	@Transactional
	public Usuario insert(Usuario objeto) {
		objeto.setId(null);
		objeto = usuarioRepository.save(objeto);
		enderecoRepository.saveAll(objeto.getEnderecos());
		return objeto;
	}

	public Usuario update(Usuario objeto) {
		Usuario novoObjeto = find(objeto.getId());
		updateData(novoObjeto, objeto);
		return usuarioRepository.save(novoObjeto);
	}

	public void delete(Integer id) {
		find(id);
		try {
			usuarioRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Usuário não excluído!");
		}
	}

	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}

	public Usuario findByEmail(String email) {
		JWTAuthorizationService usuario = JWTAuthorization.authenticated();
		if (usuario == null || !usuario.hasRole(Perfil.ADMIN) && !email.equals(usuario.getUsername())) {
			throw new AuthorizationException("Acesso negado");
		}

		Usuario objeto = usuarioRepository.findByEmail(email);
		if (objeto == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + usuario.getId() + ", Tipo: " + Usuario.class.getName());
		}
		return objeto;
	}

	public Page<Usuario> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return usuarioRepository.findAll(pageRequest);
	}

	public Usuario fromDTO(EditarUsuarioDTO objetoDto) {
		return new Usuario(objetoDto.getId(), objetoDto.getNome(), objetoDto.getEmail(), null, null, null);
	}

	public Usuario fromDTO(CadastrarUsuarioDTO objetoDto) {
		Usuario usuario = new Usuario(null, objetoDto.getNome(), objetoDto.getEmail(), objetoDto.getCpfOuCnpj(),
				TipoUsuario.toEnum(objetoDto.getTipo()), senhaEncoder.encode(objetoDto.getSenha()));

		Endereco end = new Endereco(null, objetoDto.getCep(), objetoDto.getLogradouro(), objetoDto.getNumero(),
				objetoDto.getComplemento(), objetoDto.getBairro(), objetoDto.getCidade(), objetoDto.getEstado(),
				usuario);

		usuario.getEnderecos().add(end);
		usuario.getTelefones().add(objetoDto.getTelefone1());

		if (objetoDto.getTelefone2() != null) {
			usuario.getTelefones().add(objetoDto.getTelefone2());
		}

		if (objetoDto.getTelefone3() != null) {
			usuario.getTelefones().add(objetoDto.getTelefone3());
		}

		return usuario;
	}

	private void updateData(Usuario novoObjeto, Usuario objeto) {
		novoObjeto.setNome(objeto.getNome());
		novoObjeto.setEmail(objeto.getEmail());
	}

	public Usuario redefinirSenhaUsuario(String token, String senha) {
		String email = tokenResetPassword.getUsername(token);
		
		Usuario usuario = usuarioRepository.findByEmail(email);
		
		if (usuario != null) {
			usuario.setSenha(senhaEncoder.encode(senha));
			usuarioRepository.save(usuario);	
		}else {
		    throw new AuthorizationException("Token inválido: Solicite novamente a redefinição de senha!");
		}
		return null;
	}
}
