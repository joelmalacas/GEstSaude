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

		// TODO FEITO criar os utentes
		Utente u1 = new Utente("Raquel Marques Soares", 121);
		Utente u2 = new Utente("Daniel Mendes Rodrigues", 122);
		Utente u3 = new Utente("Zeferino Dias Torres", 123);
		Utente u4 = new Utente("Anabela Dias Santos", 124);
		Utente u5 = new Utente("Felizberto Desgraçado", 125);
		Utente u6 = new Utente("Antonina Martins Pires", 126);
		Utente u7 = new Utente("Camaleão das Neves Freitas", 127);
		Utente u8 = new Utente("João Pais Pereira", 128);
		Utente u9 = new Utente("Carlos Freitas Lobo", 129);
		Utente u10 = new Utente("Daniel Mendes Rodrigues", 130);
		Utente u11 = new Utente("Dália Ribeiro Sanches", 120);

		g.adicionaUtente(u1);
		g.adicionaUtente(u2);
		g.adicionaUtente(u3);
		g.adicionaUtente(u4);
		g.adicionaUtente(u5);
		g.adicionaUtente(u6);
		g.adicionaUtente(u7);
		g.adicionaUtente(u8);
		g.adicionaUtente(u9);
		g.adicionaUtente(u10);
		g.adicionaUtente(u11);

		// TODO FEITO criar as especialidades
		Especialidade otorrino = new Especialidade("Oto1","Otorrino - Dr Narize");
		Especialidade oftalmologia = new Especialidade("Ofta1","Oftalmologia - Dra Íris Tapada");
		Especialidade pediatria2 = new Especialidade("Ped2","Pediatria - Dr B. B. Zinho");
		Especialidade pediatria = new Especialidade("Ped1","Pediatria - Dra P. Quena");
		Especialidade dermatologia = new Especialidade("Derm1","Dermatologia - Dra V. Ruga");
		Especialidade pneumologia = new Especialidade("Pneu1","Pneumologia - Dr Paul Mão");
		Especialidade ortopedia2 = new Especialidade("Orto2","Ortopedia - Dr Entorse");
		Especialidade cardiologia = new Especialidade("Card1","Cardiologia - Dr Paul Sassão");
		Especialidade ortopedia = new Especialidade("Orto1","Ortopedia - Dr Ossos");
		Especialidade otorrino2 = new Especialidade("Oto2","Otorrino - Dra Odete Otite");

		g.adicionaEspecialidade(otorrino);
		g.adicionaEspecialidade(oftalmologia);
		g.adicionaEspecialidade(pediatria2);
		g.adicionaEspecialidade(pediatria);
		g.adicionaEspecialidade(dermatologia);
		g.adicionaEspecialidade(pneumologia);
		g.adicionaEspecialidade(ortopedia2);
		g.adicionaEspecialidade(cardiologia);
		g.adicionaEspecialidade(ortopedia);
		g.adicionaEspecialidade(otorrino2);

		// TODO FEITO criar os serviços
		Servico s1 = new Servico("Scopia", "Lab. Endos");
		Servico s2 = new Servico("NeuLab", "Lab. Neurologia");
		Servico s3 = new Servico("Rad", "Sala RX");
		Servico s4 = new Servico("Audio", "Lab. Som");
		Servico s5 = new Servico("Enf", "Enfermaria");

		g.adicionaServico(s1);
		g.adicionaServico(s2);
		g.adicionaServico(s3);
		g.adicionaServico(s4);
		g.adicionaServico(s5);


		//TODO FEITO criar as consultas
		Consulta c1 = new Consulta(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 10)), u2, ortopedia);
		Consulta c2 = new Consulta(LocalDateTime.of(LocalDate.now(), LocalTime.of(8,10)), u11, pediatria);
		Consulta c3 = new Consulta(LocalDateTime.of(LocalDate.now(), LocalTime.of(8,10)), u1, pediatria2);
		Consulta c4 = new Consulta(LocalDateTime.of(LocalDate.now(), LocalTime.of(8,20)), u5, dermatologia);
		Consulta c5 = new Consulta(LocalDateTime.of(LocalDate.now(), LocalTime.of(8,30)), u6, pediatria);
		Consulta c6 = new Consulta(LocalDateTime.of(LocalDate.now(), LocalTime.of(8,40)), u7, pediatria);
		Consulta c7 = new Consulta(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(8,10)), u7, pediatria);
		Consulta c8 = new Consulta(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(8,20)), u3, pediatria);

		g.adicionaConsulta(c1);
		g.adicionaConsulta(c2);
		g.adicionaConsulta(c3);
		g.adicionaConsulta(c4);
		g.adicionaConsulta(c5);
		g.adicionaConsulta(c6);
		g.adicionaConsulta(c7);
		g.adicionaConsulta(c8);

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
