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
		List<Consulta> res = new ArrayList<>();

		for (Consulta c : cs) {
			LocalDate data = c.getDataHora().toLocalDate(); //Fica só a data

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
		List<Consulta> res = new ArrayList<>();

		for (Consulta c : cs) {
			if (!c.getDataHora().isBefore(t)) //Igual ou depois a "t"
				res.add(c);
		}
		return Collections.unmodifiableList(res);
	}

	/*
	* Método que verifica intervalo de 10 minutos na mesma especialidade
	*/
	public static boolean Verifica10Especialidade(Collection<Consulta> cs, Consulta nova) {
		for (Consulta c : cs) {
			if (c == nova) continue;
			if (!c.getEspecialidade().equals(nova.getEspecialidade())) continue;
			long minutos = Math.abs(Duration.between(c.getDataHora(), nova.getDataHora()).toMinutes());

			if (minutos < 10)
				return true;
		}

		return false;
	}

	/*
	 * Método que verifica que o utente não pode ter mais
	 *    que 1 consulta em menos de 3 horas
	 */
	public static boolean Verifica3HorasUtente(Collection<Consulta> cs, Consulta nova) {
		for (Consulta c : cs) {
			//Verifica se as duas referências apontam para o mesmo objeto
			if (c == nova) continue;

			//Mesmo Utente
			if (!c.getUtente().equals(nova.getUtente())) continue;

			//Verifica se é o mesmo dia
			if (!c.getDataHora().toLocalDate().equals(nova.getDataHora().toLocalDate())) continue;

			//Calcular o intervalo de tempo (horas) entre as duas datas
			long horas = Math.abs(Duration.between(c.getDataHora(), nova.getDataHora()).toHours());

			if (horas < 3)
				return true;
		}

		return false;
	}
}
