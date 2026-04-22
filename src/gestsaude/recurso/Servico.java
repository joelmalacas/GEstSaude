package gestsaude.recurso;

import poo.util.Validator;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Representa um serviço. Deve ter o id, uma descrição, a sala. Deve ter uma
 * lista das senhas que ainda terão de ser atendidas.
 */
public class Servico {

    private String id;
    private String descricao;
    private Queue<Senha> fila;

    public Servico(String id, String descricao) {
        this.id = Validator.requireNonBlank(id);
        this.descricao = Validator.requireNonBlank(descricao);
        this.fila = new LinkedList<>();
    }

    /**
     * Retorna a próxima senha a ser atendida por este serviço
     * 
     * @return a próxima senha a ser atendida por este serviço, null se não tiver
     *         mais senhas
     */
    public Senha getProximaSenha() {
        return fila.peek();
    }

    /**
     * o utente não responde à chamada? A sua senha passa para o fim da lista.
     */
    public void saltaProximaSenha() {
        if (!fila.isEmpty()) {
            Senha s = fila.poll();
            fila.add(s);
        }
    }

    /**
     * a senha termina a consulta neste serviço
     * 
     * @param s a senha que terminou o serviço
     */
    public void terminaConsulta(Senha s) {
        if (fila.remove(s))
            s.terminaAtendimento();
    }

    /**
     * Retorna as senhas que estão em lista de espera para serem atendidas neste
     * serviço
     * 
     * @return as senhas que estão em lista de espera para serem atendidas
     */
    public Collection<Senha> getEmEspera() {
        return Collections.unmodifiableCollection(fila);
    }

    /*Método para adicionar senha*/
    public void adicionaSenha(Senha s) {
        fila.add(s);
    }
}
