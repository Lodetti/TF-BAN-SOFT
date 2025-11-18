package dados;

public class Midia {
    private int id_midia;
    private int tamanho;
    private int tipo;
    private int duracao;

    public Midia(int id_midia, int tamanho, int tipo, int duracao) {
        this.id_midia = id_midia;
        this.tamanho = tamanho;
        this.tipo = tipo;
        this.duracao = duracao;
    }

    public Midia(int id_midia, int tamanho, int tipo) {
        this.id_midia = id_midia;
        this.tamanho = tamanho;
        this.tipo = tipo;
    }

    public Midia() {
    }

    public int getId_midia() {
        return id_midia;
    }
    public void setId_midia(int id_midia) {
        this.id_midia = id_midia;
    }
    public int getTamanho() {
        return tamanho;
    }
    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }
    public int getTipo() {
        return tipo;
    }
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    public int getDuracao() {
        return duracao;
    }
    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }
    
    @Override
    public String toString() {
        if(tipo == 2){
            return id_midia + " - " + tamanho + " - Video - " + duracao/60 + "min e " + duracao%60 + "seg";
        }else {
            return id_midia + " - " + tamanho + " - Imagem";
        }
    }

    
}
