package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dados.Post;
import excecoes.DeleteException;
import excecoes.InsertException;
import excecoes.SelectException;

public class PostDAO {
    private static PostDAO instance = null;

    private PreparedStatement insert;
    private PreparedStatement delete;
    private PreparedStatement show;
    private PreparedStatement comment;
    private PreparedStatement possui;
    private PreparedStatement postsIdUsuario;
    private PreparedStatement postsUsuario;

    private PostDAO() throws ClassNotFoundException, SQLException {
        Connection conexao = Conexao.getConexao();
        insert = conexao.prepareStatement("INSERT INTO post (id_usuario, data_hora, legenda) VALUES (?, ?, ?)");
        delete = conexao.prepareStatement("UPDATE mensagem\n" +
                                          "SET id_post = NULL,\n" +
                                          "\ttexto = 'Post excluido " +
                                          "' || texto\n" +
                                          "WHERE id_post = ?;\n" +
                                          "DELETE FROM curte WHERE id_post = ?;\n" +
                                          "DELETE FROM comenta WHERE id_post = ?;\n" +
                                          "DELETE FROM possui WHERE id_post = ?;\n" +
                                          "DELETE FROM post WHERE id_post = ?;");
        show = conexao.prepareStatement("SELECT * FROM post");
        comment = conexao.prepareStatement("INSERT INTO comenta (id_post, id_usuario, texto, dataHora) VALUES (?, ?, ?, ?)");
        possui = conexao.prepareStatement("INSERT INTO possui (id_post, id_midia) VALUES ((SELECT MAX(id_post) FROM post), ?)");
        postsIdUsuario = conexao.prepareStatement("SELECT id_post FROM post WHERE id_usuario = ?");
        postsUsuario = conexao.prepareStatement("SELECT * FROM post WHERE id_usuario = ?");

    }

    public static PostDAO getInstance() throws ClassNotFoundException, SQLException {
        if (instance == null) {
            instance = new PostDAO();
        }
        return instance;
    }

    public void insert(Post post) throws SQLException, ClassNotFoundException, InsertException {
        try{
            if (insert == null) {
                new PostDAO();
            }
            insert.setInt(1, post.getId_usuario());
            insert.setTimestamp(2, post.getData_hora());
            if (post.getLegenda() == null || post.getLegenda().isEmpty()) {
                insert.setNull(3, java.sql.Types.VARCHAR);
            } else {
                insert.setString(3, post.getLegenda());
            }
            insert.executeUpdate();
        }catch (SQLException e) {
            throw new InsertException("Erro ao publicar o post");
        }
    }

    public void delete(int id) throws SQLException, ClassNotFoundException, DeleteException {
        try {
            if (delete == null) {
                new PostDAO();
            }
            delete.setInt(1, id);
            delete.setInt(2, id);
            delete.setInt(3, id);
            delete.setInt(4, id);
            delete.setInt(5, id);
            delete.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteException("Erro ao deletar o post");
        }
    }

    public List<Post> show() throws SQLException, ClassNotFoundException, SelectException {
        try {
            if (show == null) {
                new PostDAO();
            }
            show.executeQuery();
            List<Post> posts = new LinkedList<Post>();
            ResultSet rs = show.getResultSet();
            while (rs.next()) {
                Post post = new Post();
                post.setId_post(rs.getInt("id_post"));
                post.setId_usuario(rs.getInt("id_usuario"));
                post.setData_hora(rs.getTimestamp("data_hora"));
                if(rs.getString("legenda") != null) {
                    post.setLegenda(rs.getString("legenda"));
                } else {
                    post.setLegenda("[null]");
                }
                posts.add(post);
            }
            return posts;
        } catch (SQLException e) {
            throw new SQLException("Erro ao mostrar os posts");
        }
    }

    public void comment(int id_post, int id_usuario, String texto, Timestamp dataHora) throws SQLException, ClassNotFoundException, InsertException {
        try {
            if(comment == null){
                new PostDAO();
            }

            comment.setInt(1, id_post);
            comment.setInt(2, id_usuario);
            comment.setString(3, texto);
            comment.setTimestamp(4, dataHora);

            comment.executeUpdate();

        } catch (SQLException e) {
            throw new InsertException("Erro ao comentar");
        }
    }

    public void possui(int id_midia) throws SQLException, ClassNotFoundException, InsertException{
        try {
            if(possui == null){
                new PostDAO();
            }
            possui.setInt(1, id_midia);

            possui.executeUpdate();

        } catch (SQLException e) {
            throw new InsertException("Erro ao inserir midia no post");
        }
    }

    public List<Integer> removePostsUsuario(int id_usuario)throws SQLException, ClassNotFoundException, SelectException{
        try{
            if(postsIdUsuario == null){
                new PostDAO();
            }
            postsIdUsuario.setInt(1, id_usuario);
            postsIdUsuario.executeQuery();
            List<Integer> posts = new ArrayList<Integer>();
            ResultSet rs = postsIdUsuario.getResultSet();
            while(rs.next()){
                posts.add(rs.getInt("id_post"));
            }
            
            return posts;

        }catch (SQLException e){
            throw new SelectException("Erro ao remover posts do usuario");
        }
    }

    public List<Post> showPostsUsuario(int id_usuario) throws SQLException, ClassNotFoundException, SelectException {
        try{
            if(postsUsuario == null){
                new PostDAO();
            }
            postsUsuario.setInt(1, id_usuario);
            postsUsuario.executeQuery();
            List<Post> posts = new LinkedList<Post>();
            ResultSet rs = postsUsuario.getResultSet();
            while(rs.next()){
                Post post = new Post();
                post.setId_post(rs.getInt("id_post"));
                post.setId_usuario(rs.getInt("id_usuario"));
                post.setData_hora(rs.getTimestamp("data_hora"));
                if(rs.getString("legenda") != null) {
                    post.setLegenda(rs.getString("legenda"));
                } else {
                    post.setLegenda("[null]");
                }
                posts.add(post);
            }
            return posts;
        }catch(SQLException e){
            throw new SelectException("Erro ao buscar posts do usuario");
        }
    }

}
