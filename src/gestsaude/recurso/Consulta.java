package gestsaude.recurso;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Representa uma Consulta. Deve ter o dia e hora da marcação, o utente e a
 * especialidade.
 */
public class Consulta {

	/**
	 * Indica se a consulta já está validada
	 * 
	 * @return true, se a consulta já está validada
	 */

	private LocalDateTime dataHora;
	private Utente utente;
	private Especialidade especialidade;
	private Senha senha;
	private Servico servico;

	public Consulta(LocalDateTime dataHora, Utente utente, Especialidade especialidade) {
		verificaHorasConsulta(Objects.requireNonNull(dataHora));

		this.dataHora = Objects.requireNonNull(dataHora);
		this.utente = Objects.requireNonNull(utente);
		this.especialidade = Objects.requireNonNull(especialidade);
	}

	public boolean estaValidada() {
		return senha != null;
	}

	public void setDataHora(LocalDateTime dataHora) {
		if (estaValidada())
			throw new IllegalArgumentException("A consulta já está validada");

		verificaHorasConsulta(Objects.requireNonNull(dataHora));
		this.dataHora = Objects.requireNonNull(dataHora);
	}

	public void setSenha(Senha senha) {
		if (estaValidada())
			throw new IllegalArgumentException("A consulta já está validada");
		this.senha = Objects.requireNonNull(senha);
	}

	private void verificaHorasConsulta(LocalDateTime dataHora) {
		//Método para verificar a hora de marcação da consulta (8:10 ou depois) || (19:50 ou antes)
		if (dataHora.toLocalTime().isBefore(LocalTime.of(8, 10)) || dataHora.toLocalTime().isAfter(LocalTime.of(19, 50)))
			throw new IllegalArgumentException("A consulta deve ser marcada entre as 8:10 e as 19:50");
	}

	//GETTER's
	public LocalDateTime getDataHora() { return dataHora; }
	public Utente getUtente() { return utente; }
	public Especialidade getEspecialidade() { return especialidade; }
	public Servico getServico() { return servico; }
	public Senha getSenha() {return senha;}
}
