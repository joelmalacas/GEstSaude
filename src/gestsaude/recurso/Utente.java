package gestsaude.recurso;

import poo.util.Validator;

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
    }

    public void addConsulta(Consulta c) {
        this.consultas.add(c);
    }

    public void removeConsulta(Consulta c) {
        this.consultas.remove(c);
    }

    //GETTER's
    public String getNome() { return nome; }
    public int getSns() { return sns; }
    public List<Consulta> getConsultas() { return consultas; }
}
