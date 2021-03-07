package br.com.rodrigodacruz.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Data;

@Data
@Entity
public class Tarefa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer tipo;
	private String url;
	private String email;
	private String usuario;
	
    @CreationTimestamp
    private LocalDateTime dt_cadastro;
 
    @UpdateTimestamp
    private LocalDateTime dt_atualizacao;
    
    public Tarefa() {
    	
    }
    
	public Tarefa(Integer id, Integer tipo, String url, String email, String usuario) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.url = url;
		this.email = email;
		this.usuario = usuario;
	}

}
