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
	private String numero;
	private LocalDateTime entrada;
	private LocalDateTime atendimento;
	private Consulta consulta;
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

		calculaHoraAtendimento();

		consulta.setSenha(this);

		if (!horaValida(atendimento)) {
			gest.terminaConsulta(consulta);
			throw new IllegalArgumentException("Hora de atendimento inválida, consulta anulada");
		}
	}

	private boolean podeEmitirSenha() {
		LocalDateTime agora = entrada;
		LocalDateTime horaConsulta = consulta.getDataHora();
		long diffMinutos = Duration.between(horaConsulta, agora).toMinutes();

		return diffMinutos >= -180 && diffMinutos <= 120;
	}

	private void calculaHoraAtendimento() {
		LocalDateTime horaConsulta = consulta.getDataHora();
		long atrasoMinutos = Duration.between(horaConsulta, entrada).toMinutes();

		if (atrasoMinutos <= 0) {
			atendimento = horaConsulta;
		} else {
			atendimento = entrada.plusMinutes(45);
		}
	}

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
		// TODO FEITO implementar este método
		if (index >= servicos.size())
			return null;
		return servicos.get(index);
	}

	/**
	 * Terminou o atendimento na consulta ou serviço atual. Se ainda tem serviços
	 * por visitar, deve passar para o próximo
	 */
	public void terminaAtendimento() {
		// TODO FEITO implementar este método
		index++;


		Servico proximo = servicoAtual();
		if (proximo == null)
			gest.terminaConsulta(consulta);
		else
			proximo.adicionaSenha(this);
	}

	public void adicionaServico(Servico s) {
		if (!servicos.contains(s))
			servicos.add(s);
	}

	public void rejeitaUtente() {
		atendimento = atendimento.plusMinutes(15);
		if (!horaValida(atendimento)) {
			gest.terminaConsulta(consulta);
		}
	}

	public void saltaUtente() {
		Servico atual = servicoAtual();
		if (atual != null) {
			atual.saltaProximaSenha();
		}
	}

	public String getNumero() { return numero; }
	public LocalDateTime getEntrada() { return entrada; }
	public LocalDateTime getAtendimento() { return atendimento; }
	public Consulta getConsulta() { return consulta; }

	public String toString() {
		return numero;
	}
}
