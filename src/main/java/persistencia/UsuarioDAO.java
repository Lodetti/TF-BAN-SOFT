package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import dados.Usuario;
import excecoes.DeleteException;
import excecoes.InsertException;
import excecoes.SelectException;

public class UsuarioDAO {
    private static UsuarioDAO instance = null;

    private PreparedStatement insert;
    private PreparedStatement delete;
    private PreparedStatement show;
    private PreparedStatement follow;
    private PreparedStatement like;
    private PreparedStatement checkLogin;
    private PreparedStatement mostfollowedusersfollowers;

    private UsuarioDAO() throws SQLException, ClassNotFoundException{
        Connection conexao = Conexao.getConexao();
        insert = conexao.prepareStatement("INSERT INTO usuario (nome, senha, descricao, email) VALUES (?, ?, ?, ?)");
        delete = conexao.prepareStatement("UPDATE mensagem\n" +
                                          "SET id_usuario = NULL\n" +
                                          "WHERE id_usuario = ?;\n" +
                                          "DELETE FROM segue WHERE id_seguidor = ?;\n" +
                                          "DELETE FROM segue WHERE id_seguido = ?;\n" +
                                          "DELETE FROM curte WHERE id_usuario = ?;\n" +
                                          "DELETE FROM comenta WHERE id_usuario = ?;\n" +
                                          "DELETE FROM participa WHERE id_usuario = ?;\n" +
                                          "DELETE FROM usuario WHERE id_usuario = ?;");
        show = conexao.prepareStatement("SELECT * FROM usuario");
        follow = conexao.prepareStatement("INSERT INTO segue (id_seguidor, id_seguido) VALUES (?,?)");
        like = conexao.prepareStatement("INSERT INTO curte (id_post, id_usuario) VALUES (?, ?)");
        mostfollowedusersfollowers = conexao.prepareStatement("SELECT * FROM usuario WHERE id_usuario = ANY (SELECT s1.id_seguidor FROM segue s1 WHERE s1.id_seguido = (SELECT s2.id_seguido FROM segue s2 GROUP BY s2.id_seguido ORDER BY COUNT (s2.id_seguidor) DESC LIMIT 1));");
        checkLogin = conexao.prepareStatement("SELECT * FROM usuario WHERE email = ? AND senha = ?;");
    }

    public static UsuarioDAO getInstance() throws ClassNotFoundException, SQLException {
        if(instance == null){
            instance = new UsuarioDAO();
        }
        return instance;
    }

    public void insert(Usuario usuario) throws InsertException, SQLException, ClassNotFoundException {
        try{
            if (insert == null) {
                new UsuarioDAO();
            }
            insert.setString(1, usuario.getNome());
            insert.setString(2, usuario.getSenha());
            if(usuario.getDescricao().isEmpty()){
                insert.setNull(3, java.sql.Types.VARCHAR);
            } else {
                insert.setString(3, usuario.getDescricao());
            }
            insert.setString(4, usuario.getEmail());
            insert.executeUpdate();
        }catch(SQLException e){
            throw new InsertException("Erro ao fazer login do usuario");
        }
    }
    
    public void delete(int id) throws DeleteException, SQLException, ClassNotFoundException {
        try {
            if (delete == null) {
                new UsuarioDAO();
            }
            delete.setInt(1, id);
            delete.setInt(2, id);
            delete.setInt(3, id);
            delete.setInt(4, id);
            delete.setInt(5, id);
            delete.setInt(6, id);
            delete.setInt(7, id);
            delete.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteException("Usuário não pode ser removido");
        }
    }

    public List<Usuario> show() throws SQLException, ClassNotFoundException, SelectException {
        try{
            List<Usuario> usuarios = new LinkedList<Usuario>();

            if (show == null) {
                new UsuarioDAO();
            }
            ResultSet rs = show.executeQuery();
            while(rs.next()){
                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                if(rs.getString("descricao") != null){
                    usuario.setDescricao(rs.getString("descricao"));
                } else {
                    usuario.setDescricao("[null]");
                }
                usuarios.add(usuario);
            }
            return usuarios;
        }catch(SQLException e){
            throw new SelectException("Erro ao buscar usuarios");
        }
    }

    public void follow(int id_seguidor, int id_seguido) throws SQLException, ClassNotFoundException, InsertException{
        try{
            if (follow == null){
                new UsuarioDAO();
            }

            follow.setInt(1, id_seguidor);
            follow.setInt(2, id_seguido);

            follow.executeUpdate();
        }catch(SQLException e){
            throw new InsertException("Erro ao seguir");
        }
    }

    public void like(int id_post, int id_usuario) throws SQLException, ClassNotFoundException, InsertException {
        try{
            if(like == null){
                new UsuarioDAO();
            }
            like.setInt(1, id_post);
            like.setInt(2, id_usuario);

            like.executeUpdate();
        }catch(SQLException e){
            throw new InsertException("Erro ao curtir");
        }
    }

    public Usuario login(String email, String senha) throws SQLException, ClassNotFoundException, SelectException {
        try{
            if(checkLogin == null){
                new UsuarioDAO();
            }
            checkLogin.setString(1, email);
            checkLogin.setString(2, senha);

            checkLogin.executeQuery();

            Usuario usuario = new Usuario();
            ResultSet rs = checkLogin.getResultSet();
            while(rs.next()){
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                if(rs.getString("descricao") != null){
                    usuario.setDescricao(rs.getString("descricao"));
                } else {
                    usuario.setDescricao("[null]");
                }
            }
            return usuario;
        }catch(SQLException e){
            throw new SelectException("Erro ao fazer login no BD");
        }
    }

    public List<Usuario> mostFollowedUsersFollowers() throws SQLException, ClassNotFoundException, SelectException{
        try{
            if(mostfollowedusersfollowers == null){
                new UsuarioDAO();
            }

            ResultSet rs = mostfollowedusersfollowers.executeQuery();
            List<Usuario> usuarios = new LinkedList<Usuario>();

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                if(rs.getString("descricao") != null){
                    usuario.setDescricao(rs.getString("descricao"));
                } else {
                    usuario.setDescricao("[null]");
                }
                usuarios.add(usuario);
            }
            return usuarios;

        }catch(SQLException e){
            throw new SelectException("Erro ao buscar seguidores");
        }
    }

    
}
