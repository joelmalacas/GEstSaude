package gestsaude.recurso;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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

	private static final LocalTime ABERTURA = LocalTime.of(8, 10);
	private static final LocalTime FECHO = LocalTime.of(19, 50);

	public Senha(GEstSaude gest, String numero, LocalDateTime entrada, LocalDateTime atendimento, Consulta consulta) {
		this.gest = gest;
		this.numero = numero;
		this.entrada = entrada;
		this.atendimento = atendimento;
		this.consulta = consulta;
		this.servicos = new ArrayList<>();

		if (!podeEmitirSenha()) {
			throw new IllegalArgumentException("Não pode emitir senha");
		}

		//Calcula a hora prevista de atendimento
		calculaHoraAtendimento();

		//Associa esta senha à consulta
		consulta.setSenha(this);

		//Se hora prevista estiver fora do horário de atendimento, anula a consulta
		if (!horaValida(atendimento)) {
			gest.terminaConsulta(consulta);
			throw new IllegalArgumentException("Hora de atendimento inválida, consulta anulada");
		}
	}

	/*
	 * Verifica se o utente pode retirar a senha
	 * - Consulta nas próximas 3h ou anteriores 2h
	 * - Atraso <= 2 horas
	 */
	private boolean podeEmitirSenha() {
		LocalDateTime agora = entrada;
		LocalDateTime horaConsulta = consulta.getDataHora();
		long diffMinutos = Duration.between(horaConsulta, agora).toMinutes();
		// negativo = chegou antes, positivo = chegou atrasado
		return diffMinutos >= -180 && diffMinutos <= 120;
	}

	/*
	* Calcular a hora prevista de atendimento
	* - Se atrasado < 2h -> atendimento = hora da consulta
	* - Se atrasado > 0 --> atendimento = entrada + 45 minutos
	*/
	private void calculaHoraAtendimento() {
		LocalDateTime horaConsulta = consulta.getDataHora();
		long atrasoMinutos = Duration.between(horaConsulta, entrada).toMinutes();

		if (atrasoMinutos <= 0) {
			atendimento = horaConsulta;
		} else {
			//Chegou atrasado, mas <= 2h
			atendimento = entrada.plusMinutes(45);
		}
	}

	/*Verifica se a hora está dentro do horário de atendimento */
	private boolean horaValida(LocalDateTime hora) {
		LocalTime t = hora.toLocalTime();
		return !t.isBefore(ABERTURA) && !t.isAfter(FECHO);
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
		Servico proximo = servicoAtual();
		if (proximo == null)
			gest.terminaConsulta(consulta);
		else
			proximo.adicionaSenha(this);
	}

	/*
	* Adiciona um serviço à lista de serviços (evita duplicação)
	*/
	public void adicionaServico(Servico s) {
		if (!servicos.contains(s))
			servicos.add(s);
	}

	/**
	 * Rejeita utente
	 * Adiciona atraso de 15 minutos à hora prevista de atendimento
	 * Se hora prevista ficar fora do horário, descarta senha e anula consulta
	 */
	public void rejeitaUtente() {
		atendimento = atendimento.plusMinutes(15);
		if (!horaValida(atendimento)) {
			gest.terminaConsulta(consulta);
		}
	}

	/**
	 * Salta utente na fila do serviço atual
	 * Move a senha para o final da fila
	 */
	public void saltaUtente() {
		Servico atual = servicoAtual();
		if (atual != null) {
			atual.saltaProximaSenha();
		}
	}

	// getters
	public String getNumero() { return numero; }
	public LocalDateTime getEntrada() { return entrada; }
	public LocalDateTime getAtendimento() { return atendimento; }
	public Consulta getConsulta() { return consulta; }

	//toString
	public String toString() {
		return numero;
	}
}
