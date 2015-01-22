package ufc.quixada.npi.afastamento.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Professor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message="Obrigatório")
	private String nome;
	
	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private boolean habilitado;

	@ManyToMany
	@JoinTable(name = "papel_usuario", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "papel_id"))
	private List<Papel> papeis;

	@Size(min = 11, message="Minino 11 dígitos")
	private String cpf;
	
	@Size(min = 7, message="Minino 7 dígitos")
	private String siape;

	@NotEmpty(message="Obrigatório")
	@Email(message="E-mail Inválido")
	private String email;
	
	@NotNull(message = "Obrigatório")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dataNascimento;
	
	@NotNull(message = "Ano obrigatório")
	private Integer anoAdmissao;
	
	@NotNull(message = "Semestre Obrigatório")
	@Min(message = "Semestre inválido", value = 1)
	@Max(message = "Semestre inválido", value = 2)
	private Integer semestreAdmissao;
	
	@OneToMany(mappedBy = "professor", cascade = CascadeType.REMOVE)
	private List<Reserva> reservas;

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}

	public List<Papel> getPapeis() {
		return papeis;
	}

	public void setPapeis(List<Papel> papeis) {
		this.papeis = papeis;
	}

	public void addPapel(Papel papel) {
		this.papeis.add(papel);
	}

	public String getSiape() {
		return siape;
	}

	public void setSiape(String siape) {
		this.siape = siape;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Integer getAnoAdmissao() {
		return anoAdmissao;
	}

	public void setAnoAdmissao(Integer anoAdmissao) {
		this.anoAdmissao = anoAdmissao;
	}

	public Integer getSemestreAdmissao() {
		return semestreAdmissao;
	}

	public void setSemestreAdmissao(Integer semestreAdmissao) {
		this.semestreAdmissao = semestreAdmissao;
	}

	public List<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Professor other = (Professor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	

}
