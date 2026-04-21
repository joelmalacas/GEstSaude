package gestsaude.arranque;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import javax.swing.Timer;

import gestsaude.menu.MaquinaEntrada;
import gestsaude.menu.MenuEspecialidade;
import gestsaude.menu.MenuSecretaria;
import gestsaude.menu.MenuServico;
import gestsaude.menu.RelogioDialog;
import gestsaude.recurso.*;
import poo.util.RelogioSimulado;

/**
 * Classe responsavel pelo arranque do sistema.
 * Tem um método para criar a configuração de teste
 */
public class Arranque {
	/**
	 * Cria a configuração inicial do sistema
	 * 
	 * @return um GEstSaude já completamente configurado
	 */
	public static GEstSaude getGEstSaude() {
		GEstSaude g = new GEstSaude();

		// colocar o relógio simulado nas 8:00
		RelogioSimulado.getRelogioSimulado().setTempoAtual(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0)));

		// TODO criar os utentes
		Utente u1 = new Utente("Joel", 12345);
		Utente u2 = new Utente("Ana", 67890);

		g.adicionaUtente(u1);
		g.adicionaUtente(u2);

		// TODO criar as especialidades
		Especialidade oncologia = new Especialidade("Oto1","Otorrino - Dr Narize");
		Especialidade oftalmologia = new Especialidade("Ofta1","Oftalmologia - Dra Íris Tapada");
		Especialidade cardio = new Especialidade("Card1","Cardiologia - Dr Paul Sassão");

		g.adicionaEspecialidade(oncologia);
		g.adicionaEspecialidade(cardio);

		// TODO criar os serviços
		Servico s1 = new Servico("Radiologia");
		Servico s2 = new Servico("Enfermagem");

		g.adicionaServico(s1);
		g.adicionaServico(s2);


		//TODO criar as consultas
		Consulta consulta = new Consulta(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 10)), u1, oncologia);
		Consulta consulta1 = new Consulta(LocalDateTime.of(LocalDate.now(), LocalTime.of(12,50)), u2, cardio);

		g.adicionaConsulta(consulta);
		g.adicionaConsulta(consulta1);

		return g;
	}

	/**
	 * arranque do sistema
	 */
	public static void main(String[] args) {
		// criar o GEstSaude
		GEstSaude gs = getGEstSaude();

		// Definir o tempo por segundo no relógio simulado
		RelogioSimulado relogio = RelogioSimulado.getRelogioSimulado();
		relogio.setTicksPorSegundo(1); // alterar este valor se quiserem que o tempo passe mais rápido

		// criar o menu da secretaria, neste caso irá ter apenas um
		MenuSecretaria secretaria = new MenuSecretaria(new Point(230, 10), "Secretaria", gs);
		secretaria.setVisible(true);

		// criar a janela do relógio
		RelogioDialog relogioDlg = new RelogioDialog(secretaria, new Point(10, 10));
		relogioDlg.setVisible(true);

		// criar uma máquina de entrada
		MaquinaEntrada me1 = new MaquinaEntrada(secretaria, new Point(10, 150), "Entrada 1", gs);
		me1.setVisible(true);

		// criar todos os menus de serviço e de especialidades
		// TODO ver as especialidades e os serviços do sistema
		Collection<Especialidade> especiais = List.of();
		Collection<Servico> servicos = List.of();
		int idx = 0;
		int totalJanelas = 0;
		MenuEspecialidade menusEsp[] = new MenuEspecialidade[especiais.size()];
		for (Especialidade e : especiais) {
			menusEsp[idx] = new MenuEspecialidade(secretaria,
					new Point(10 + (totalJanelas % 5) * 200, 270 + (totalJanelas / 5) * 180), e, gs);
			menusEsp[idx].setVisible(true);
			idx++;
			totalJanelas++;
		}
		MenuServico menusServ[] = new MenuServico[servicos.size()];
		idx = 0;
		for (Servico s : servicos) {
			menusServ[idx] = new MenuServico(secretaria,
					new Point(10 + (totalJanelas % 5) * 200, 270 + (totalJanelas / 5) * 180), s);
			menusServ[idx].setVisible(true);
			idx++;
			totalJanelas++;
		}

		// criar um temporizador que vai atualizando as várias janelas
		// do menu de serviços, de 5 em 5 segundos (5000 milisegundos)
		Timer t = new Timer(5000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (MenuEspecialidade me : menusEsp)
					me.atualizarInfo();
				for (MenuServico ms : menusServ)
					ms.atualizarInfo();

				secretaria.atualizar();
			}
		});
		t.start();
	}
}
