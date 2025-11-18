package negocio;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import dados.Conversa;
import dados.Mensagem;
import dados.Midia;
import dados.Post;
import dados.Usuario;
import excecoes.DeleteException;
import excecoes.InsertException;
import excecoes.SelectException;
import persistencia.Conexao;
import persistencia.ConversaDAO;
import persistencia.MensagemDAO;
import persistencia.MidiaDAO;
import persistencia.PostDAO;
import persistencia.UsuarioDAO;

public class Sistema {
    private UsuarioDAO usuarioDAO;
    private PostDAO postDAO;
    private MidiaDAO midiaDAO;
    private MensagemDAO mensagemDAO;
    private ConversaDAO conversaDAO;


    public Sistema(String senha) throws ClassNotFoundException, SQLException {
        Conexao.setSenha(senha);
        usuarioDAO = UsuarioDAO.getInstance();
        postDAO = PostDAO.getInstance();
        midiaDAO = MidiaDAO.getInstance();
        mensagemDAO = MensagemDAO.getInstance();
        conversaDAO = ConversaDAO.getInstance();
    }

    public void insereUsuario(Usuario usuario) throws InsertException, SQLException, ClassNotFoundException {
        usuarioDAO.insert(usuario);
    }

    public void removeUsuario(int id) throws SQLException, ClassNotFoundException, DeleteException {
        usuarioDAO.delete(id);
    }

    public List<Usuario> mostraUsuarios() throws SQLException, ClassNotFoundException, SelectException {
        return usuarioDAO.show();
    }

    public void segueUsuario(int id_seguidor, int id_seguido) throws SQLException, ClassNotFoundException, InsertException{
        usuarioDAO.follow(id_seguidor, id_seguido);
    }

    public void curtePost(int id_post, int id_usuario) throws SQLException, ClassNotFoundException, InsertException {
        usuarioDAO.like(id_post, id_usuario);
    }
    
    public void comentar(int id_post, int id_usuario, String texto, Timestamp dataHora) throws SQLException, ClassNotFoundException, InsertException{
        postDAO.comment(id_post, id_usuario, texto, dataHora);
    }

    public Usuario login(String email, String senha) throws SQLException, ClassNotFoundException, SelectException {
        return usuarioDAO.login(email, senha);
    }

    public List<Usuario> subconsulta()throws SQLException, ClassNotFoundException, SelectException{
        return usuarioDAO.mostFollowedUsersFollowers();
    }

    public void inserePost(Post post) throws SQLException, ClassNotFoundException, InsertException {
        postDAO.insert(post);
    }

    public void removePost(int id) throws SQLException, ClassNotFoundException, DeleteException {
        postDAO.delete(id);
    }

    public List<Integer> removePostUsuario(int id_usuario) throws SQLException, ClassNotFoundException, SelectException {
        return postDAO.removePostsUsuario(id_usuario);
    }

    public List<Post> mostraPosts() throws SQLException, ClassNotFoundException, SelectException {
        return postDAO.show();
    }

    public List<Post> buscaPostsUsuario(int id_usuario) throws SQLException, ClassNotFoundException, SelectException {
        return postDAO.showPostsUsuario(id_usuario);
    }

    public void insereMidia(Midia midia) throws SQLException, ClassNotFoundException, InsertException {
        midiaDAO.insert(midia);
    }

    public void removeMidia(int id) throws SQLException, ClassNotFoundException, DeleteException {
        midiaDAO.delete(id);
    }

    public List<Midia> mostraMidias() throws SQLException, ClassNotFoundException, SelectException {
        return midiaDAO.show();
    }

    public void insereMensagem(Mensagem mensagem) throws SQLException, ClassNotFoundException, InsertException {
        mensagemDAO.insert(mensagem);
    }

    public void removeMensagem(int id) throws SQLException, ClassNotFoundException, DeleteException {
        mensagemDAO.delete(id);
    }
    
    public List<Mensagem> mostraMensagens() throws SQLException, ClassNotFoundException, SelectException {
        return mensagemDAO.show();
    }

    public void recebeMensagem(int id_conversa) throws SQLException, ClassNotFoundException, InsertException {
        mensagemDAO.receive(id_conversa);
    }

    public void insereConversa(String nomeConversa) throws SQLException, ClassNotFoundException, InsertException {
        conversaDAO.inserirConversa(nomeConversa);
    }

    public void insereConversaUsuario(int id_usuario) throws SQLException, ClassNotFoundException, InsertException, SelectException {
        conversaDAO.criaConversaUsuario(id_usuario);
    }

    public void removeConversa(int id) throws SQLException, ClassNotFoundException, DeleteException {
        conversaDAO.deleteConversa(id);
    }

    public List<Integer> mensagensConversa(int id_conversa) throws SQLException, ClassNotFoundException, SelectException{
        return conversaDAO.todasMensagens(id_conversa);
    }

    public List<Conversa> mostraConversas() throws SQLException, ClassNotFoundException, SelectException {
        return conversaDAO.show();
    }

    public List<Conversa> mostraConversasUsuario(int id_usuario) throws SQLException, ClassNotFoundException, SelectException {
        return conversaDAO.showConversasUsuario(id_usuario);
    }

    public List<Mensagem> mostraConteudoDaConversa(int id_conversa) throws SQLException, ClassNotFoundException, SelectException {
        return conversaDAO.showConversa(id_conversa);
    }

    public List<Mensagem> userMessages(int id_conversa, int id_usuario) throws SQLException, ClassNotFoundException, SelectException {
        return conversaDAO.mensagensUsuario(id_conversa, id_usuario);
    }

    public void participarConversa(int id_conversa, int id_usuario) throws SQLException, ClassNotFoundException, InsertException {
        conversaDAO.participa(id_conversa, id_usuario);
    }

    public boolean checkParticipation(int id_conversa, int id_usuario) throws SQLException, ClassNotFoundException, SelectException {
        return conversaDAO.verificaParticipacao(id_conversa, id_usuario);
    }


    public void possuir(int id_midia) throws SQLException, ClassNotFoundException, InsertException{
        postDAO.possui(id_midia);
    }

}
