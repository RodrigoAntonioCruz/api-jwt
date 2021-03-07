package br.com.rodrigodacruz.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.rodrigodacruz.domain.Usuario;
import br.com.rodrigodacruz.dto.usuario.CadastrarUsuarioDTO;
import br.com.rodrigodacruz.dto.usuario.EditarUsuarioDTO;
import br.com.rodrigodacruz.services.UsuarioService;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioResource {

	@Autowired
	private UsuarioService service;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Usuario> find(@PathVariable Integer id) {
		Usuario objeto = service.find(id);
		return ResponseEntity.ok().body(objeto);
	}

	@RequestMapping(value = "/email", method = RequestMethod.GET)
	public ResponseEntity<Usuario> find(@RequestParam(value = "value") String email) {
		Usuario objeto = service.findByEmail(email);
		return ResponseEntity.ok().body(objeto);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody CadastrarUsuarioDTO objetoDto) {
		Usuario objeto = service.fromDTO(objetoDto);
		objeto = service.insert(objeto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objeto.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody EditarUsuarioDTO objetoDto, @PathVariable Integer id) {
		Usuario objeto = service.fromDTO(objetoDto);
		objeto.setId(id);
		objeto = service.update(objeto);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<EditarUsuarioDTO>> findAll() {
		List<Usuario> lista = service.findAll();
		List<EditarUsuarioDTO> listaDto = lista.stream().map(objeto -> new EditarUsuarioDTO(objeto)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaDto);
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<EditarUsuarioDTO>> findPage(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		Page<Usuario> lista = service.findPage(page, linesPerPage, orderBy, direction);
		Page<EditarUsuarioDTO> listaDto = lista.map(objeto -> new EditarUsuarioDTO(objeto));
		return ResponseEntity.ok().body(listaDto);
	}

}
