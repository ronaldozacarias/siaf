package ufc.quixada.npi.afastamento.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

@Entity
public class Documento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	
	private String extensao;
	
	@Type(type="org.hibernate.type.BinaryType") 
	private byte[] arquivo;
	
	@ManyToOne
	private Reserva afastamento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}
	
	public String getExtensao() {
		return extensao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}

	public Reserva getAfastamento() {
		return afastamento;
	}

	public void setAfastamento(Reserva afastamento) {
		this.afastamento = afastamento;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Documento) {
			Documento other = (Documento) obj;
			if (other != null && other.getId() != null && this.id != null && other.getId().equals(this.id)) {
				return true;
			}
		}
		return false;
	}
	
}

