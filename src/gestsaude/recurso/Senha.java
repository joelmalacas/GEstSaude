package gestsaude.recurso;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Representa uma Senha. Deve ter um número (letra e número, por exemplo A1, A2,
 * A12), a hora de entrada e a hora prevista de atendimento e qual a consulta
 * associada.
 */
public class Senha {

	private GEstSaude gest; // deve ter ligação ao sistema
	private String numero;             // ex: "A1", "A2"
	private LocalDateTime entrada;     // hora de entrada
	private LocalDateTime atendimento; // hora prevista de atendimento
	private Consulta consulta;         // consulta associada
	private List<Servico> servicos;
	private int index;

	public Senha(GEstSaude gest, String numero, LocalDateTime entrada, LocalDateTime atendimento, Consulta consulta) {
		this.gest = gest;
		this.numero = numero;
		this.entrada = entrada;
		this.atendimento = atendimento;
		this.consulta = consulta;
		this.servicos = new LinkedList<>();
	}

	/**
	 * Indica qual o próximo serviço que tem de visitar
	 * 
	 * @return o serviço que tem de visitar ou null se não tiver serviços a visitar
	 */
	public Servico servicoAtual() {
		// Se o index for maior ou igual ao tamanho da lista, não tem mais serviços
		if (index >= servicos.size())
			return null;
		return servicos.get(index); //Retorna o serviço atual pela posição do index
	}

	/**
	 * Terminou o atendimento na consulta ou serviço atual. Se ainda tem serviços
	 * por visitar, deve passar para o próximo
	 */
	public void terminaAtendimento() {
		index++;

		//Se já não tem mais serviços, avisa o sistema que a consulta terminou
		if (servicoAtual() == null)
			gest.terminaConsulta(consulta);
	}

	// getters
	public String getNumero() { return numero; }
	public LocalDateTime getEntrada() { return entrada; }
	public LocalDateTime getAtendimento() { return atendimento; }
	public Consulta getConsulta() { return consulta; }
}
