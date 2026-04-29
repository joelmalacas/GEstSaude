package gestsaude.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import gestsaude.recurso.Consulta;
import poo.util.Validator;

/**
 * Class utilitária que define um conjunto base de operações com listas de
 * consultas. Usaremos Lambdas, quando as aprendermos.
 */
public class Consultas {

	/**
	 * retorna uma lista apenas com as consultas que estão entre as datas definidas
	 * 
	 * @param cs  consultas a filtrar
	 * @param ini data inicial a considerar (inclusive)
	 * @param fim data final a considerar (inclusive)
	 * @return uma lista com as consultas que estão entre as datas definidas
	 */

	public static List<Consulta> getConsultaEntreDatas(Collection<Consulta> cs, LocalDateTime ini, LocalDateTime fim) {
		// TODO FEITO implementar este método
		List<Consulta> res = new ArrayList<>();

		for (Consulta c : cs) {
			LocalDateTime data = c.getDataHora();

			if (!data.isBefore(ini) && !data.isAfter(fim))
				res.add(c);
		}
		return Collections.unmodifiableList(res);
	}

	/**
	 * Retorna uma lista apenas com as consultas marcadas num dado dia
	 * 
	 * @param cs  consultas a filtrar
	 * @param dia dia a usar
	 * @return uma lista com as consultas marcadas para dia
	 */
	public static List<Consulta> getConsultasDoDia(Collection<Consulta> cs, LocalDate dia) {
		// TODO FEITO implementar este método
		List<Consulta> res = new ArrayList<>();

		for (Consulta c : cs) {
			LocalDate data = c.getDataHora().toLocalDate();

			if (data.equals(dia))
				res.add(c);
		}
		return Collections.unmodifiableList(res);
	}

	/**
	 * Retorna uma lista com as consultas após uma dada data
	 * 
	 * @param cs lista de consulta a filtrar
	 * @param t  tempo a partir do qual se deve considerar as consultas (inclusive)
	 * @return uma lista com as consultas após a data t
	 */
	public static List<Consulta> getConsultasApos(List<Consulta> cs, LocalDateTime t) {
		// TODO FEITO implementar este método
		List<Consulta> res = new ArrayList<>();

		for (Consulta c : cs) {
			if (!c.getDataHora().isBefore(t))
				res.add(c);
		}
		return Collections.unmodifiableList(res);
	}


	public static boolean Verifica10Especialidade(Collection<Consulta> cs, Consulta nova) {
		for (Consulta consultas : cs) {

			if (consultas.getEspecialidade().equals(nova.getEspecialidade())){
				long DiffMinutos = Math.abs(Duration.between(consultas.getDataHora(), nova.getDataHora()).toMinutes());
				if (DiffMinutos < 10)
					return true;
			}
		}

		return false;
	}

	public static boolean Verifica3HorasUtente(Collection<Consulta> cs, Consulta nova) {
		for (Consulta consultas : cs) {
			if (consultas.getUtente().equals(nova.getUtente())) {
				long DiffMinutos = Math.abs(Duration.between(consultas.getDataHora(), nova.getDataHora()).toMinutes());
				if (DiffMinutos < 180)
					return true;
			}

		}

		return false;
	}
}
