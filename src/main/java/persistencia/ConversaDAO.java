package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dados.Conversa;
import dados.Mensagem;
import excecoes.DeleteException;
import excecoes.InsertException;
import excecoes.SelectException;

public class ConversaDAO {
    private static ConversaDAO instancia = null;  
    
    private PreparedStatement insert;
    private PreparedStatement delete;
    private PreparedStatement show;
    private PreparedStatement participa;
    private PreparedStatement showConversa;
    private PreparedStatement allMessages;
    private PreparedStatement showConversaUsuario;
    private PreparedStatement latestConversa;
    private PreparedStatement verificaParticipa;
    private PreparedStatement userMessages;


    private ConversaDAO() throws ClassNotFoundException, SQLException {
        
        Connection conexao = Conexao.getConexao();
        insert = conexao.prepareStatement("INSERT INTO conversa (nome_conversa) VALUES (?)");
        delete = conexao.prepareStatement("DELETE FROM participa WHERE id_conversa = ?;" + "DELETE FROM conversa WHERE id_conversa = ?");
        show = conexao.prepareStatement("SELECT * FROM conversa");
        showConversa = conexao.prepareStatement("SELECT m.* FROM recebe r, mensagem m WHERE ? = r.id_conversa AND m.id_mensagem = r.id_mensagem ORDER BY m.data_hora;");
        participa = conexao.prepareStatement("INSERT INTO participa (id_conversa, id_usuario) VALUES (?, ?)");
        allMessages = conexao.prepareStatement("SELECT id_mensagem FROM recebe WHERE id_conversa = ?;");
        showConversaUsuario = conexao.prepareStatement("SELECT c.* FROM conversa c, participa p WHERE p.id_usuario = ? AND c.id_conversa = p.id_conversa;");
        latestConversa = conexao.prepareStatement("INSERT INTO participa (id_conversa, id_usuario) VALUES ((SELECT MAX(id_conversa) FROM conversa), ?)");
        verificaParticipa = conexao.prepareStatement("SELECT EXISTS (SELECT 1 FROM participa WHERE id_conversa = ? AND id_usuario = ?) AS tupla_exist;");
        userMessages = conexao.prepareStatement("SELECT m.* FROM recebe r JOIN mensagem m ON r.id_mensagem = m.id_mensagem WHERE r.id_conversa = ? AND m.id_usuario = ?;");
    }

    public static ConversaDAO getInstance() throws ClassNotFoundException, SQLException {
        if (instancia == null) {
            instancia = new ConversaDAO();
        }
        return instancia;
    }

    public void inserirConversa(String nomeConversa) throws SQLException, ClassNotFoundException, InsertException {
        try {
            if (insert == null) {
                new ConversaDAO();
            }
            insert.setString(1, nomeConversa);
            insert.executeUpdate();

        } catch (SQLException e) {
            throw new InsertException("Erro ao inserir conversa: " + e.getMessage());
        }
    }

    public void deleteConversa(int idConversa) throws SQLException, ClassNotFoundException, DeleteException {
        try {
            if (delete == null) {
                new ConversaDAO();
            }
            delete.setInt(1, idConversa);
            delete.setInt(2, idConversa);
            delete.executeUpdate();

        } catch (SQLException e) {
            throw new DeleteException("Erro ao deletar conversa: " + e.getMessage());
        }
    }

    public List<Integer> todasMensagens(int id_conversa) throws SQLException, ClassNotFoundException, SelectException{
        try{
            if(allMessages == null){
                new ConversaDAO();
            }
            allMessages.setInt(1, id_conversa);
            allMessages.executeQuery();
            List<Integer> mensagens = new ArrayList<Integer>();
            ResultSet rs = allMessages.getResultSet();
            while(rs.next()){
                mensagens.add(rs.getInt("id_mensagem"));
            }
            return mensagens; 
        }catch(SQLException e){
            throw new SelectException("Erro ao deletar mensagens de uma conversa");
        }
    }

    public List<Conversa> show() throws SQLException, ClassNotFoundException, SelectException {
        List<Conversa> conversas = new ArrayList<>();
        try {
            if (show == null) {
                new ConversaDAO();
            }
            ResultSet rs = show.executeQuery();
            while (rs.next()) {
                Conversa conversa = new Conversa();
                int id = rs.getInt("id_conversa");
                String nome = rs.getString("nome_conversa");
                conversa.setId_conversa(id);
                conversa.setNome_conversa(nome);
                conversas.add(conversa);
            }
            return conversas;
        } catch (SQLException e) {
            throw new SelectException("Erro ao mostrar conversas: " + e.getMessage());
        }
    }

    public List<Mensagem> showConversa(int id_conversa) throws SQLException, ClassNotFoundException, SelectException {
        try{
            if(showConversa == null){
                new ConversaDAO();
            }

            showConversa.setInt(1, id_conversa);
            ResultSet rs = showConversa.executeQuery();

            List<Mensagem> mensagens = new LinkedList<Mensagem>();

            while(rs.next()){
                Mensagem mensagem = new Mensagem();
                mensagem.setId_mensagem(rs.getInt("id_mensagem"));
                mensagem.setData_hora(rs.getTimestamp("data_hora"));

                if(rs.getString("texto") != null) {
                    mensagem.setTexto(rs.getString("texto"));
                } else {
                    mensagem.setTexto("[null]");
                }

                mensagem.setId_usuario(rs.getInt("id_usuario"));

                if(rs.wasNull()) {
                    mensagem.setId_post(0);
                } else {
                    mensagem.setId_post(rs.getInt("id_post"));
                }

                if(rs.wasNull()) {
                    mensagem.setId_midia(0);
                } else {
                    mensagem.setId_midia(rs.getInt("id_midia"));
                }

                mensagem.setEntregue(rs.getBoolean("entregue"));
                mensagem.setVisualizado(rs.getBoolean("visualizado"));
                mensagens.add(mensagem);
            }
            return mensagens;
        }catch(SQLException e){
            throw new SelectException("Erro ao mostrar a conversa");
        }
    }

    public List<Mensagem> mensagensUsuario(int id_conversa, int id_usuario) throws SQLException, ClassNotFoundException, SelectException {
        try{
            if(userMessages == null){
                new ConversaDAO();
            }

            userMessages.setInt(1, id_conversa);
            userMessages.setInt(2, id_usuario);
            ResultSet rs = userMessages.executeQuery();

            List<Mensagem> mensagens = new LinkedList<Mensagem>();

            while(rs.next()){
                Mensagem mensagem = new Mensagem();
                mensagem.setId_mensagem(rs.getInt("id_mensagem"));
                mensagem.setData_hora(rs.getTimestamp("data_hora"));

                if(rs.getString("texto") != null) {
                    mensagem.setTexto(rs.getString("texto"));
                } else {
                    mensagem.setTexto("[null]");
                }

                mensagem.setId_usuario(rs.getInt("id_usuario"));

                if(rs.wasNull()) {
                    mensagem.setId_post(0);
                } else {
                    mensagem.setId_post(rs.getInt("id_post"));
                }

                if(rs.wasNull()) {
                    mensagem.setId_midia(0);
                } else {
                    mensagem.setId_midia(rs.getInt("id_midia"));
                }

                mensagem.setEntregue(rs.getBoolean("entregue"));
                mensagem.setVisualizado(rs.getBoolean("visualizado"));
                mensagens.add(mensagem);
            }
            return mensagens;
        }catch(SQLException e){
            throw new SelectException("Erro ao buscar mensagens do usuario");
        }
    }

    public List<Conversa> showConversasUsuario(int id_usuario) throws SQLException, ClassNotFoundException, SelectException {
        List<Conversa> conversas = new LinkedList<Conversa>();
        try {
            if (showConversaUsuario == null) {
                new ConversaDAO();
            }
            showConversaUsuario.setInt(1, id_usuario);
            ResultSet rs = showConversaUsuario.executeQuery();
            while (rs.next()) {
                Conversa conversa = new Conversa();
                int id = rs.getInt("id_conversa");
                String nome = rs.getString("nome_conversa");
                conversa.setId_conversa(id);
                conversa.setNome_conversa(nome);
                conversas.add(conversa);
            }
            return conversas;
        } catch (SQLException e) {
            throw new SelectException("Erro ao buscar conversas do usuario: " + e.getMessage());
        }
    }

    public void participa(int id_conversa, int id_usuario) throws SQLException, ClassNotFoundException, InsertException {
        try {
            if(participa == null){
                new ConversaDAO();
            }
            participa.setInt(1, id_conversa);
            participa.setInt(2, id_usuario);

            participa.executeUpdate();
        } catch (SQLException e) {
            throw new InsertException("Erro ao participar na conversa");
        }
    }

    public boolean verificaParticipacao(int id_conversa, int id_usuario) throws SQLException, ClassNotFoundException, SelectException {
        try {
            if(verificaParticipa == null){
                new ConversaDAO();
            }
            verificaParticipa.setInt(1, id_conversa);
            verificaParticipa.setInt(2, id_usuario);
            
            ResultSet rs = verificaParticipa.executeQuery();
            boolean bool = false;
            if(rs.next()){
                bool = rs.getBoolean("tupla_exist");
            }
            return bool;
        } catch (SQLException e) {
            throw new SelectException("Erro ao verificar participacao");
        }
    }

    public void criaConversaUsuario(int id_usuario) throws SQLException, ClassNotFoundException, SelectException, InsertException {
        try {
            if(latestConversa == null){
                new ConversaDAO();
            }
            latestConversa.setInt(1, id_usuario);
            latestConversa.executeUpdate();
        } catch (SQLException e) {
            throw new InsertException("Erro ao usuario criar conversa");
        }
    }
}
