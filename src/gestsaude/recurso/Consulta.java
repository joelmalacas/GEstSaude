package gestsaude.recurso;

import java.time.LocalDateTime;
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

	public Consulta(LocalDateTime dataHora, Utente utente, Especialidade especialidade) {
		this.dataHora = Objects.requireNonNull(dataHora);
		this.utente = Objects.requireNonNull(utente);
		this.especialidade = Objects.requireNonNull(especialidade);
	}

	public boolean estaValidada() {
		// TODO implementar este método
		return senha != null;
	}

	//GETTER's
	public LocalDateTime getDataHora() { return dataHora; }
	public Utente getUtente() { return utente; }
	public Especialidade getEspecialidade() { return especialidade; }
}
