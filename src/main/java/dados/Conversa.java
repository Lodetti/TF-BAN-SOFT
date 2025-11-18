package dados;

public class Conversa {
    private int id_conversa;
    private String nome_conversa;

    public Conversa(int id_conversa, String nome_conversa){
        this.id_conversa = id_conversa;
        this.nome_conversa = nome_conversa;
    }

    public Conversa(){}

    public int getId_conversa() {
        return id_conversa;
    }
    public void setId_conversa(int id_conversa) {
        this.id_conversa = id_conversa;
    }
    public String getNome_conversa() {
        return nome_conversa;
    }
    public void setNome_conversa(String nome_conversa) {
        this.nome_conversa = nome_conversa;
    }
    
    @Override
    public String toString() {
        return id_conversa + " - " + nome_conversa;
    }

    
}
