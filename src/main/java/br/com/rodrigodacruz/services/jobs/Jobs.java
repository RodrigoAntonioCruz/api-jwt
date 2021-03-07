package br.com.rodrigodacruz.services.jobs;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import br.com.rodrigodacruz.domain.Tarefa;
import br.com.rodrigodacruz.domain.Usuario;
import br.com.rodrigodacruz.repositories.TarefaRepository;
import br.com.rodrigodacruz.repositories.UsuarioRepository;
import br.com.rodrigodacruz.security.JWTUtil;
import br.com.rodrigodacruz.services.exceptions.ObjectNotFoundException;

@Service
public class Jobs {
	
	@Autowired
	private JWTUtil tokenResetPassword;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private TarefaRepository tarefaRepository;
	
	@Value("${hostname}")
	private String hostname;
	
	public Usuario jobEmailUsuario(String email) {
		Usuario objeto = usuarioRepository.findByEmail(email);
		if (objeto == null) {
			throw new ObjectNotFoundException("Usuário não encontrado! Email: " + email);
		}else {
			Integer tipo = 1;
			String token = tokenResetPassword.generateTokenResetPassword(email);
			String url =  hostname + "/autenticacao/redefinir/senha/" + token;
			String usuario = objeto.getNome();
			
			Tarefa tarefa = new Tarefa(null, tipo, url, email, usuario);
			tarefaRepository.saveAll(Arrays.asList(tarefa));
			
			return null;
		}
	}
}
