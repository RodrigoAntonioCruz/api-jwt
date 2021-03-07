package br.com.rodrigodacruz.services.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import java.util.HashMap;
import java.util.Map;
import br.com.rodrigodacruz.repositories.TarefaRepository;

public class EmailSend {

    @Autowired
    EmailSenderService senderService;
    
	@Autowired
	private TarefaRepository tarefaRepository;
    
    @Value("${spring.mail.username}")
    private String remetente;
    
    private static final Logger LOG = LoggerFactory.getLogger(EmailSend.class);

    public void redefinirSenha(Integer id, String usuario, String email, String url) {
        Map<String, Object> properties = new HashMap<String, Object>();
        
        properties.put("nome", usuario);
        properties.put("url", url);

        EmailConfigTamplate mail = EmailConfigTamplate.builder()
            .from(remetente)
            .to(email)
            .subject("Redefinição de senha")
            .htmlTemplate(new EmailConfigTamplate
            .HtmlTemplate("reset-senha", properties))
            .build();

        try {
            senderService.sendEmail(mail);
            LOG.info("Email enviado com sucesso!");
            tarefaRepository.deleteById(id);
        } catch (Exception e) {
            LOG.info("Erro ao enviar email.");
        }

    }
}
