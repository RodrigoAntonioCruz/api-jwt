package br.com.rodrigodacruz.dto.usuario;

import java.io.Serializable;
import lombok.Data;

@Data
public class CredenciaisUsuarioDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String senha;
}
