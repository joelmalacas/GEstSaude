package Teste;

import gestsaude.recurso.*;
import poo.util.RelogioSimulado;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class teste {
    public static void main(String[] args) {
        GEstSaude g = new GEstSaude();

        // colocar o relógio simulado nas 8:00
        RelogioSimulado.getRelogioSimulado().setTempoAtual(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0)));

        // TODO criar os utentes
        Utente u1 = new Utente("Joel", 12345);
        Utente u2 = new Utente("Gonçalo", 67890);

        g.adicionaUtente(u1);
        g.adicionaUtente(u2);

        // TODO criar as especialidades
        Especialidade oncologia = new Especialidade("Oto1","Otorrino");
        Especialidade cardio = new Especialidade("Car1","Cardiologia");

        g.adicionaEspecialidade(oncologia);
        g.adicionaEspecialidade(cardio);

        // TODO criar os serviços
        //Servico s1 = new Servico("Radiologia");
        //Servico s2 = new Servico("Enfermagem");

        //g.adicionaServico(s1);
        //g.adicionaServico(s2);


        //TODO criar as consultas
        Consulta consulta = new Consulta(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 10)), u1, oncologia);
        Consulta consulta1 = new Consulta(LocalDateTime.of(LocalDate.now(), LocalTime.of(12,50)), u2, cardio);

        g.adicionaConsulta(consulta);
        g.adicionaConsulta(consulta1);

        // =====================
        // EMISSÃO DE SENHAS (válidas) | TESTES PASSADOS
        // =====================

        //g.emiteSenha(consulta, LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 5)), LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 10)));
        g.emiteSenha(consulta1, LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 25)), LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 35)));

        // =====================
        // TESTE: atraso (válido < 2h)
        // =====================
        g.emiteSenha(consulta, LocalDateTime.of(LocalDate.now(), LocalTime.of(10,0)), LocalDateTime.of(LocalDate.now(), LocalTime.of(10,0))); // 1h atraso

        //Verificar estado da consulta com metodo validarConsulta
        int resultado = g.validarConsulta(consulta);
        System.out.println("Validar Consulta: " + resultado); // imprime o número

        // =====================
        // Consulta INVÁLIDA: TESTE PASSADO
        // =====================
        // Consulta às 8:10, entrada às 10:11 = 2h01 de atraso → INVÁLIDO
        //g.emiteSenha(consulta, LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 11)), LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 11)));

        for (int i = 0; i < g.getConsultas().size(); i++) {
            Consulta c = g.getConsultas().get(i);

            System.out.println("+----------------+----------------------+");
            System.out.println("| Campo          | Valor                |");
            System.out.println("+----------------+----------------------+");
            System.out.printf("| %-14s | %-20s |%n", "Nome Utente", c.getUtente().getNome());
            System.out.printf("| %-14s | %-20s |%n", "SNS Utente", c.getUtente().getSns());
            System.out.printf("| %-14s | %-20s |%n", "ID Esp", c.getEspecialidade().getID());
            System.out.printf("| %-14s | %-20s |%n", "Especialidade", c.getEspecialidade().getNome());
            System.out.printf("| %-14s | %-20s |%n", "Validada", c.estaValidada());
            System.out.printf("| %-14s | %-20s |%n", "Data Hora", c.getDataHora());
            System.out.printf("| %-14s | %-20s |%n", "Senha", c.getSenha());
            System.out.println("+----------------+----------------------+");
        }
    }
}
