package gestsaude.recurso;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import poo.tempo.HorarioDiario;
import poo.util.Validator;

/**
 * Representa uma Especialidade. Deve ter uma lista das consultas marcadas e uma
 * lista com as senhas a serem atendidas no dia atual.
 */
public class Especialidade {

	//Constante inicial do ID_ESPECIALIDADE

	// para simplificar, cada serviço vai ter sempre o mesmo horário
	private HorarioDiario horario = new HorarioDiario(LocalTime.of(8, 10), LocalTime.of(19, 50));
	private String id,nome;
	private List<Especialidade> especialidades = new ArrayList<>();
	private List<Consulta> consultas; //Todas as consultas marcadas
	private List<Senha> senhas; //Senhas a atender hoje

	public Especialidade(String id,String nome) {
		this.id = Validator.requireNonBlankTrimmed(id);
		this.nome = Validator.requireNonBlank(nome);
		this.consultas = new ArrayList<>();
		this.senhas = new ArrayList<>();
	}

	/**
	 * Retorna qual a próxima senha em espera
	 *
	 * @return a próxima senha em espera
	 */
	public Senha getProximaSenha() {
		if (senhas.isEmpty())
			return null;
		return senhas.get(0);
	}

	/**
	 * O utente não responde à chamada? Coloca a senha/consulta para 15 minutos mais
	 * tarde e passa ao próximo utente.
	 */
	public void rejeitaProximaSenha() {
		if (!senhas.isEmpty()) {
			Senha s = senhas.remove(0); // retira do início
			senhas.add(s);              // coloca no fim
		}
	}

	/**
	 * Terminou a consulta da senha
	 *
	 * @param s a senha cuja consulta terminou
	 */
	public void terminaConsulta(Senha s) {
		senhas.remove(s);
		consultas.remove(s.getConsulta());
	}

	/**
	 * Retorna as senhas que estão em lista de espera para serem atendidas nesta
	 * especialidade
	 *
	 * @return as senhas que estão em lista de espera para serem atendidas
	 */
	public Collection<Senha> getEmEspera() {
		return new ArrayList<>(senhas);
	}

	//GETTER's
	public String getNome() { return nome; }
	public List<Consulta> getConsultas() { return consultas; }
	public List<Senha> getSenhas() { return senhas; }
	public String getID() { return id; }
}