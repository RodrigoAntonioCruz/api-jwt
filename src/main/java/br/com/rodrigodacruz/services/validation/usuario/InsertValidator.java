package br.com.rodrigodacruz.services.validation.usuario;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import br.com.rodrigodacruz.domain.Usuario;
import br.com.rodrigodacruz.domain.enums.TipoUsuario;
import br.com.rodrigodacruz.dto.usuario.CadastrarUsuarioDTO;
import br.com.rodrigodacruz.repositories.UsuarioRepository;
import br.com.rodrigodacruz.resources.exception.FieldMessage;

public class InsertValidator implements ConstraintValidator<Insert, CadastrarUsuarioDTO> {

	@Autowired
	private UsuarioRepository repo;

	@Override
	public void initialize(Insert ann) {
	}

	@Override
	public boolean isValid(CadastrarUsuarioDTO objDto, ConstraintValidatorContext context) {

		List<FieldMessage> list = new ArrayList<>();

		if (objDto.getTipo().equals(TipoUsuario.PESSOAFISICA.getCod()) && !Cpf_Cnpj.isValidCPF(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}

		if (objDto.getTipo().equals(TipoUsuario.PESSOAJURIDICA.getCod()) && !Cpf_Cnpj.isValidCNPJ(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
		}

		Usuario aux = repo.findByEmail(objDto.getEmail());
		if (aux != null) {
			list.add(new FieldMessage("email", "Email já existente"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
				   .addConstraintViolation();
		}
		return list.isEmpty();
	}
}
