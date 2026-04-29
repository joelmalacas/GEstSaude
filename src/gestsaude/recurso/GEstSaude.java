package gestsaude.recurso;

import poo.util.RelogioSimulado;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Representa o sistema. Deve armazenar todas as consulta, utentes,
 * especialidades, serviços e senhas
 */
public class GEstSaude {

	// constantes para erros e situações que podem ocorrer para usar no retorno de
	// alguns métodos
	public static final int CONSULTA_ACEITE = 0;
	public static final int CONSULTA_JA_VALIDADA = CONSULTA_ACEITE + 1;
	public static final int UTENTE_SEM_CONSULTA_HOJE = CONSULTA_JA_VALIDADA + 1;
	public static final int UTENTE_SEM_CONSULTA_PROXIMA = UTENTE_SEM_CONSULTA_HOJE + 1;
	public static final int UTENTE_DEMASIADO_ATRASADO = UTENTE_SEM_CONSULTA_PROXIMA + 1;
	public static final int UTENTE_ATRASADO_FORAHORAS = UTENTE_DEMASIADO_ATRASADO + 1;
	public static final int UTENTE_ATRASADO_ADIADO = UTENTE_ATRASADO_FORAHORAS + 1;
	public static final int SERVICO_SEM_CONSULTA = UTENTE_ATRASADO_ADIADO + 1;
	public static final int DATA_JA_PASSOU = SERVICO_SEM_CONSULTA + 1;
	public static final int UTENTE_JA_TEM_CONSULTA = DATA_JA_PASSOU + 1;
	public static final int ESPECIALISTA_JA_TEM_CONSULTA = UTENTE_JA_TEM_CONSULTA + 1;
	public static final int ALTERACAO_INVALIDA = ESPECIALISTA_JA_TEM_CONSULTA + 1;


	private List<Utente> utentes = new ArrayList<>();
	private List<Especialidade> especialidades = new ArrayList<>();
	private List<Servico> servicos = new ArrayList<>();
	private List<Consulta> consultas = new ArrayList<>();
	private List<Senha> senhas = new ArrayList<>();

	private static GEstSaude instance;

	public GEstSaude() {}

	public static GEstSaude getInstance() {
		if (instance == null) {
			instance = new GEstSaude();
		}
		return instance;
	}


	public void adicionaUtente(Utente u) {
		utentes.add(u);
	}

	public Collection<Utente> getUtentes() {
		return Collections.unmodifiableCollection(utentes);
	}


	public void adicionaEspecialidade(Especialidade e) {
		especialidades.add(e);
	}

	public Collection<Especialidade> getEspecialidades() {
		return Collections.unmodifiableCollection(especialidades);
	}

	public Collection<String> getIdsEspecialidades() {
		List<String> ids = new ArrayList<>();
		for (Especialidade e : especialidades)
			ids.add(e.getID());
		return Collections.unmodifiableList(ids);
	}


	public void adicionaServico(Servico s) {
		servicos.add(s);
	}

	public Servico getServicoPorNome(String nome) {
		for (Servico s : servicos) {
			if (s.getID().equals(nome))
				return s;
		}
		return null;
	}

	public List<Servico> getServicos() {
		return Collections.unmodifiableList(servicos);
	}


	public void adicionaConsulta(Consulta c) {
		consultas.add(c);
		c.getUtente().addConsulta(c);
		c.getEspecialidade().getConsultas().add(c);
	}

	public List<Consulta> getConsultas() {
		return Collections.unmodifiableList(consultas);
	}

	public Collection<Senha> getSenhas() {
		return Collections.unmodifiableCollection(senhas);
	}


	private int contadorSenhas = 0;

	private String geraNumeroSenha() {
		contadorSenhas++;
		return "A" + contadorSenhas;
	}

	public Senha criaSenha(LocalDateTime entrada, LocalDateTime atendimento, Consulta consulta) {
		String numero = geraNumeroSenha();
		Senha s = new Senha(this, numero, entrada, atendimento, consulta);
		senhas.add(s);
		consulta.getEspecialidade().getSenhas().add(s);
		return s;
	}

	/**
	 * Deve retornar qual a primeira consulta do dia de um utente
	 *
	 * @param u o utente de quem ver a primeira consulta do dia
	 * @return a primeira consulta do dia do utente
	 */
	public Consulta primeiraConsultaDia(Utente u) {
		// TODO FEITO implementar este método
		if (u.getConsultas().isEmpty()) {
			return null;
		}
		return u.getConsultas().getFirst();
	}

	/**
	 * Valida a consulta. Verifica se a consulta pode ser concretizada e, se sim,
	 * emite a senha respetiva.
	 *
	 * @param c a consulta a verificar
	 * @return o resultado deve ser um dos seguintes:
	 *         <li>
	 *         CONSULTA_ACEITE se a consulta pode ser realizada
	 *         <li>UTENTE_SEM_CONSULTA_PROXIMA se o utente não tem qualquer consulta
	 *         nas próximas 3 horas
	 *         <li>UTENTE_DEMASIADO_ATRASADO se o utente tinha a consulta há mais de
	 *         2 horas
	 *         <li>UTENTE_ATRASADO_ADIADO se o utente chegou atrasado e isso levou a
	 *         que a hora da consulta fosse adiada
	 *         <li>UTENTE_ATRASADO_FORAHORAS o utente atrasou-se e devido a isso a
	 *         hora de atendimento já fica fora do horário
	 */
	public int validarConsulta(Consulta c) {
		// TODO FEITO validar a consulta
		// TODO FEITO emitir a senha
		LocalDateTime agora = RelogioSimulado.getRelogioSimulado().getTempoAtual();
		LocalDateTime dataConsulta = c.getDataHora();

		if (c.estaValidada())
			return CONSULTA_JA_VALIDADA;

		if (dataConsulta.isAfter(agora.plusHours(3)))
			return UTENTE_SEM_CONSULTA_PROXIMA;

		if (agora.isAfter(dataConsulta)) {

			if (agora.isAfter(dataConsulta.plusHours(2)))
				return UTENTE_DEMASIADO_ATRASADO;

			if (agora.toLocalTime().isAfter(LocalTime.of(20, 0)))
				return UTENTE_ATRASADO_FORAHORAS;

			c.setDataHora(agora.plusMinutes(45));
			criaSenha(agora, agora.plusMinutes(45), c);
			return UTENTE_ATRASADO_ADIADO;
		}

		criaSenha(agora, dataConsulta, c);
		return CONSULTA_ACEITE;
	}

	/**
	 * Emite a senha para a consulta. Se a consulta já está validada, retorna a
	 * senha previamente emitida.
	 *
	 * @param c           a consulta a qual a senha ficará associada
	 * @param entrada     a data de entrada no sistema
	 * @param atendimento a data prevista de atendimento (pode ser diferente da data
	 *                    da consulta)
	 * @return a senha criada
	 */
	public Senha emiteSenha(Consulta c, LocalDateTime entrada, LocalDateTime atendimento) {
		// TODO FEITO implementar este método
		if (c.estaValidada())
			return c.getSenha();
		return criaSenha(entrada, atendimento, c);
	}

	/**
	 * Indicação ao sistema de que a consulta terminou. Deve eliminar a consulta e a
	 * senha respetiva
	 *
	 * @param c a consulta terminada
	 */
	public void terminaConsulta(Consulta c) {
		// TODO FEITO implementar este método
		consultas.remove(c);
		senhas.remove(c.getSenha());
		c.getUtente().removeConsulta(c);
		c.getEspecialidade().terminaConsulta(c.getSenha());
	}

	/**
	 * Verica se a consulta pode ser adicionada ao sistema
	 *
	 * @param c a consulta a testar
	 * @return o resultado deve ser um dos seguintes:
	 *         <li>CONSULTA_ACEITE se a consulta puder ser adicionada ao sistema
	 *         <li>DATA_JA_PASSOU se a data for numa altura anterior à data atual
	 *         <li>UTENTE_JA_TEM_CONSULTA se o utente já tem uma consulta no horário
	 *         indicado
	 *         <li>ESPECIALISTA_JA_TEM_CONSULTA se o especialista já tiver outra
	 *         consulta na mesma data
	 */
	public int podeAceitarConsulta(Consulta c) {
		// TODO FEITO implementar este método
		LocalDateTime agora = RelogioSimulado.getRelogioSimulado().getTempoAtual();

		if (c.getDataHora().isBefore(agora))
			return DATA_JA_PASSOU;

		for (Consulta cs : consultas) {
			if (cs.getUtente().equals(c.getUtente())) {
				long DiffMinutos = Math.abs(Duration.between(cs.getDataHora(), c.getDataHora()).toMinutes());
				if (DiffMinutos < 180)
					return UTENTE_JA_TEM_CONSULTA;
			}

			if (cs.getEspecialidade().equals(c.getEspecialidade())){
				long DiffMinutos = Math.abs(Duration.between(cs.getDataHora(), c.getDataHora()).toMinutes());
				if (DiffMinutos < 10)
					return ESPECIALISTA_JA_TEM_CONSULTA;
			}
		}

		return CONSULTA_ACEITE;
	}

	/**
	 * Verica se a consulta pode ser alterada
	 *
	 * @param c a consulta com as alterações
	 * @return o resultado deve ser um dos seguintes:
	 *         <li>CONSULTA_ACEITE se a consulta puder ser adicionada ao sistema
	 *         <li>DATA_JA_PASSOU se a data for numa altura anterior à data atual
	 *         <li>UTENTE_JA_TEM_CONSULTA se o utente já tem uma consulta no horário
	 *         indicado
	 *         <li>ESPECIALISTA_JA_TEM_CONSULTA se o especialista já tiver outra
	 *         consulta na mesma data
	 *         <li>ALTERACAO_INVALIDA se mudou a especialidade ou o utente na
	 *         consulta
	 */
	public int podeAlterarConsulta(Consulta antiga, Consulta nova) {
		// TODO FEITO implementar este método
		LocalDateTime agora = RelogioSimulado.getRelogioSimulado().getTempoAtual();

		if (!antiga.getUtente().equals(nova.getUtente()) || !antiga.getEspecialidade().equals(nova.getEspecialidade()))
			return ALTERACAO_INVALIDA;

		if (nova.getDataHora().isBefore(agora))
			return DATA_JA_PASSOU;

		for (Consulta existente : consultas) {
			if (existente == antiga) continue;

			if (existente.getUtente().equals(nova.getUtente())) {
				long diff = Math.abs(Duration.between(existente.getDataHora(), nova.getDataHora()).toMinutes());
				if (diff < 180)
					return UTENTE_JA_TEM_CONSULTA;
			}

			if (existente.getEspecialidade().equals(nova.getEspecialidade())){
				long diff = Math.abs(Duration.between(existente.getDataHora(), nova.getDataHora()).toMinutes());
				if (diff < 10)
					return ESPECIALISTA_JA_TEM_CONSULTA;
			}
		}

		return CONSULTA_ACEITE;
	}

	/**
	 * altera uma consulta por outra
	 *
	 * @param antiga a consulta que será alterada
	 * @param nova   a consulta já com as alterações
	 * @return o resultado deve ser um dos seguintes:
	 *         <li>CONSULTA_ACEITE se a consulta puder ser adicionada ao sistema
	 *         <li>DATA_JA_PASSOU se a data for numa altura anterior à data atual
	 *         <li>UTENTE_JA_TEM_CONSULTA se o utente já tem uma consulta no horário
	 *         indicado
	 *         <li>ESPECIALISTA_JA_TEM_CONSULTA se o especialista já tiver outra
	 *         consulta na mesma data
	 *         <li>ALTERACAO_INVALIDA se mudou a especialidade ou o utente na
	 *         consulta
	 */
	public int alteraConsulta(Consulta antiga, Consulta nova) {
		// TODO FEITO implementar este método
		int res = podeAlterarConsulta(antiga, nova);
		if (res != CONSULTA_ACEITE) return res;

		int index = consultas.indexOf(antiga);
		consultas.set(index, nova);

		return CONSULTA_ACEITE;
	}

	public void apagarConsulta(Consulta c) {
		consultas.remove(c);
		c.getUtente().removeConsulta(c);
		c.getEspecialidade().getConsultas().remove(c);
	}

	public Utente getUtentePorSns(int sns) {
		for (Utente u : utentes) {
			if (Objects.equals(u.getSns(), sns)) {
				return u;
			}
		}
		return null;
	}

	public Especialidade getEspecialidadePorId(String id) {
		for (Especialidade esp : especialidades) {
			if (Objects.equals(esp.getID(), id)) {
				return esp;
			}
		}

		return null;
	}
}
