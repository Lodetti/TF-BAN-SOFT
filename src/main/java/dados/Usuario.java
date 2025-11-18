package dados;

public class Usuario {
    private int id_usuario;
    private String nome;
    private String email;
    private String senha;
    private String descricao;

    public Usuario(int id_usuario, String nome, String email, String senha, String descricao){
        this.id_usuario = id_usuario;
        this.email = email;
        this.nome = nome;
        this.senha = senha;
        this.descricao = descricao;
    }
    
    public Usuario(int id_usuario, String nome, String email, String senha){
        this.id_usuario = id_usuario;
        this.email = email;
        this.nome = nome;
        this.senha = senha;
        this.descricao = "";
    }

    public Usuario(){

    }

    public String getNome() {
        return nome;
    }
    public int getId_usuario() {
        return id_usuario;
    }
    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return id_usuario + " - " + nome + " - " + descricao;
    }

    
}
