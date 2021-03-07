package br.com.rodrigodacruz.resources;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import br.com.rodrigodacruz.domain.Usuario;
import br.com.rodrigodacruz.dto.usuario.CredenciaisUsuarioDTO;
import br.com.rodrigodacruz.security.JWTAuthorization;
import br.com.rodrigodacruz.security.JWTAuthorizationService;
import br.com.rodrigodacruz.security.JWTUtil;
import br.com.rodrigodacruz.services.UsuarioService;
import br.com.rodrigodacruz.services.jobs.Jobs;

@RestController
@RequestMapping(value = "/autenticacao")
public class AutenticacaoResource {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired 
	private Jobs jobs;
	
	@RequestMapping(value = "/redefinir/token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		JWTAuthorizationService user = JWTAuthorization.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/recuperar/usuario/{email}", method = RequestMethod.GET)
	public Usuario recuperarUsuario(@PathVariable("email") String email) {
		return jobs.jobEmailUsuario(email);
	}
	
	@RequestMapping(value = "/redefinir/senha/{token}", method = RequestMethod.POST)
	public ResponseEntity<Void> redefinirSenha(@PathVariable("token") String token, @RequestBody CredenciaisUsuarioDTO objetoDto) {
		usuarioService.redefinirSenhaUsuario(token, objetoDto.getSenha());
		return ResponseEntity.noContent().build();
	}
	
	
	/*
	 * @RequestMapping(value = "/redefinir/senha/{token}", method =
	 * RequestMethod.GET) public Usuario redefinirSenha(@PathVariable("token")
	 * String token) { return usuarioService.redefinirSenhaUsuario(token); }
	 */
}
