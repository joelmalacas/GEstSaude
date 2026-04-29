package gestsaude.recurso;

import poo.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa um Utente. Deve ter um nome, o número de SNS e armazenar as
 * consultas que tem marcadas no futuro.
 */
public class Utente {
    private String nome;
    private int sns;
    private List<Consulta> consultas;

    public Utente(String nome, int sns) {
        this.nome = Validator.requireNonBlank(nome);
        this.sns = Validator.requirePositive(sns);
        this.consultas = new ArrayList<>();
    }

    public void addConsulta(Consulta c) {
        this.consultas.add(c);
    }

    public void removeConsulta(Consulta c) {
        this.consultas.remove(c);
    }

    public String getNome() { return nome; }
    public int getSns() { return sns; }
    public List<Consulta> getConsultas() { return Collections.unmodifiableList(consultas); }
}
