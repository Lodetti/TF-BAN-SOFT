package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import dados.Midia;
import excecoes.DeleteException;
import excecoes.InsertException;
import excecoes.SelectException;

public class MidiaDAO {
    private static MidiaDAO instance = null;

    private PreparedStatement insert;
    private PreparedStatement delete;
    private PreparedStatement show;

    private MidiaDAO() throws ClassNotFoundException, SQLException {
        Connection conexao = Conexao.getConexao();
        insert = conexao.prepareStatement("INSERT INTO midia (tamanho, tipo, duracao) VALUES (?, ?, ?)");
        delete = conexao.prepareStatement("DELETE FROM midia WHERE id_midia = ?");
        show = conexao.prepareStatement("SELECT * FROM midia");
    }

    public static MidiaDAO getInstance() throws ClassNotFoundException, SQLException {
        if (instance == null) {
            instance = new MidiaDAO();
        }
        return instance;
    }

    public void insert(Midia midia) throws SQLException, ClassNotFoundException, InsertException {
        try {
            if (insert == null) {
                new MidiaDAO();
            }
            insert.setInt(1, midia.getTamanho());
            if (midia.getTipo() == 2){
                insert.setBoolean(2, true);
                insert.setInt(3, midia.getDuracao());
            } else {
                insert.setBoolean(2, false);
                insert.setNull(3, java.sql.Types.INTEGER);
            }
            
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new InsertException("Erro ao inserir mídia");
        }
    }

    public void delete(int id) throws SQLException, ClassNotFoundException, DeleteException {
        try {
            if (delete == null) {
                new MidiaDAO();
            }
            delete.setInt(1, id);
            delete.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteException("Erro ao deletar mídia");
        }
    }

    public List<Midia> show() throws SQLException, ClassNotFoundException, SelectException {
        try {
            if (show == null) {
                new MidiaDAO();
            }
            show.executeQuery();
            ResultSet rs = show.getResultSet();
            List<Midia> midias = new LinkedList<Midia>();
            while (rs.next()) {
                Midia midia = new Midia();
                midia.setId_midia(rs.getInt("id_midia"));
                midia.setTamanho(rs.getInt("tamanho"));
                if(rs.getBoolean("tipo")) {
                    midia.setTipo(2); // Assume 2 pra video
                    midia.setDuracao(rs.getInt("duracao"));
                } else {
                    midia.setTipo(1); // Assume 1 pra imagem
                    midia.setDuracao(0);
                }
                midias.add(midia);
            }
            return midias;
        } catch (SQLException e) {
            throw new SelectException("Erro ao mostrar mídias");
        }
    }
    
}
