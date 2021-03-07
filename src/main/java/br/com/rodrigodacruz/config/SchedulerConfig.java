package br.com.rodrigodacruz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@EnableScheduling
public class SchedulerConfig {
	@Bean
	public void tarefasAgendadas() {
		
	}
}
