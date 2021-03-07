package br.com.rodrigodacruz.jobs;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import br.com.rodrigodacruz.domain.Tarefa;
import br.com.rodrigodacruz.repositories.TarefaRepository;
import br.com.rodrigodacruz.services.email.EmailSend;

@Component
public class TarefaAgendada {
	
	@Autowired
	private EmailSend email;
	
	@Autowired
	private TarefaRepository tarefaRepository;
	
	private static final long tempoExecucao = 60000;
	
	private static final String zoneBR = "America/Sao_Paulo";
	 
	@Scheduled(fixedDelay = tempoExecucao, zone = zoneBR)
	public void executarTarefasAgendadas() {

	    List<Tarefa> tarefas = tarefaRepository.findAll();

		for(Tarefa tarefa : tarefas){
			
			if(tarefa.getTipo() == 1) {
			  email.redefinirSenha(tarefa.getId(), tarefa.getUsuario(), tarefa.getEmail(), tarefa.getUrl());
			}
			else if (tarefa.getTipo() == 2) {
				System.out.println("Tarefa tipo 2 executada!");
			}
			else {
				System.out.println("Nenhuma tarefa a ser executada!");
			}
		}	
	}
}
