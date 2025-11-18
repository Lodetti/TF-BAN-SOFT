import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import dados.Conversa;
import dados.Mensagem;
import dados.Midia;
import dados.Post;
import dados.Usuario;
import excecoes.DeleteException;
import excecoes.InsertException;
import excecoes.SelectException;
import negocio.Sistema;
import persistencia.Conexao;
import persistencia.ConversaDAO;
import persistencia.MensagemDAO;
import persistencia.MidiaDAO;
import persistencia.PostDAO;
import persistencia.UsuarioDAO;

public class SistemaTest {

    private UsuarioDAO usuarioDAOMock;
    private PostDAO postDAOMock;
    private MidiaDAO midiaDAOMock;
    private MensagemDAO mensagemDAOMock;
    private ConversaDAO conversaDAOMock;

    @BeforeEach
    public void setup() {
        usuarioDAOMock = mock(UsuarioDAO.class);
        postDAOMock = mock(PostDAO.class);
        midiaDAOMock = mock(MidiaDAO.class);
        mensagemDAOMock = mock(MensagemDAO.class);
        conversaDAOMock = mock(ConversaDAO.class);
    }

    private void withMockedDAOs(TestLogic testLogic) throws Exception {
        try (MockedStatic<Conexao> conexaoMockStatic = Mockito.mockStatic(Conexao.class);
             MockedStatic<UsuarioDAO> usuarioDAOMockStatic = Mockito.mockStatic(UsuarioDAO.class);
             MockedStatic<PostDAO> postDAOMockStatic = Mockito.mockStatic(PostDAO.class);
             MockedStatic<MidiaDAO> midiaDAOMockStatic = Mockito.mockStatic(MidiaDAO.class);
             MockedStatic<MensagemDAO> mensagemDAOMockStatic = Mockito.mockStatic(MensagemDAO.class);
             MockedStatic<ConversaDAO> conversaDAOMockStatic = Mockito.mockStatic(ConversaDAO.class)) {

            // Configura o comportamento dos métodos estáticos getInstance()
            usuarioDAOMockStatic.when(UsuarioDAO::getInstance).thenReturn(usuarioDAOMock);
            postDAOMockStatic.when(PostDAO::getInstance).thenReturn(postDAOMock);
            midiaDAOMockStatic.when(MidiaDAO::getInstance).thenReturn(midiaDAOMock);
            mensagemDAOMockStatic.when(MensagemDAO::getInstance).thenReturn(mensagemDAOMock);
            conversaDAOMockStatic.when(ConversaDAO::getInstance).thenReturn(conversaDAOMock);

            // Mockar o retorno de Conexao.getConexao() se for usado por algum DAO
            // para evitar NullPointerException dentro dos DAOs mockados.
            conexaoMockStatic.when(Conexao::getConexao).thenReturn(mock(java.sql.Connection.class));

            // Executa a lógica de teste passada como argumento
            testLogic.run();
        }
    }

    @FunctionalInterface
    private interface TestLogic {
        void run() throws Exception;
    }

    @Test
    public void testConstrutorSistema() throws Exception {
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            assertNotNull(s);
        });
    }

    @Test
    public void testInsereUsuario() throws Exception {
        Usuario usuario = new Usuario(1, "Teste", "teste@email.com", "1234");
        withMockedDAOs(() -> {
            Sistema s= new Sistema("senha");
            doNothing().when(usuarioDAOMock).insert(usuario);

            assertDoesNotThrow(() -> s.insereUsuario(usuario));
            verify(usuarioDAOMock, times(1)).insert(usuario);
        });
    }

    @Test
    public void testInsereUsuarioThrowsInsertException() throws Exception {
        Usuario usuario = new Usuario(1, "Teste", "teste@email.com", "1234", "Teste descricao");
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            doThrow(new InsertException("Erro")).when(usuarioDAOMock).insert(usuario);

            assertThrows(InsertException.class, () -> s.insereUsuario(usuario));
        });
    }

    @Test
    public void testRemoveUsuario() throws Exception {
        int id = 1;
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            doNothing().when(usuarioDAOMock).delete(id);

            assertDoesNotThrow(() -> s.removeUsuario(id));
            verify(usuarioDAOMock, times(1)).delete(id);
        });
    }

    @Test
    public void testRemoveUsuarioThrowsDeleteException() throws Exception {
        Usuario usuario = new Usuario(1, "Teste", "teste@email.com", "1234", "Teste descricao");
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            doThrow(new DeleteException("Erro")).when(usuarioDAOMock).delete(usuario.getId_usuario());

            assertThrows(DeleteException.class, () -> s.removeUsuario(usuario.getId_usuario()));
        });
    }

    @Test
    public void testMostraUsuarios() throws Exception {
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            List<Usuario> usuariosMock = List.of(
                new Usuario(1, "A", "a@email.com", "123"),
                new Usuario(2, "B", "b@email.com", "456")
            );
            when(usuarioDAOMock.show()).thenReturn(usuariosMock);

            List<Usuario> resultado = s.mostraUsuarios();

            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            assertEquals("A", resultado.get(0).getNome());
            verify(usuarioDAOMock, times(1)).show();
        });
    }

    @Test
    public void testMostraUsuariosThrowsSelectException() throws Exception {
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            doThrow(new SelectException("Erro")).when(usuarioDAOMock).show();

            assertThrows(SelectException.class, () -> s.mostraUsuarios());
        });
    }

    @Test
    public void testSegueUsuario() throws Exception{
        int id_seguidor = 1, id_seguido = 2;
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            doNothing().when(usuarioDAOMock).follow(id_seguidor, id_seguido);

            assertDoesNotThrow(() -> s.segueUsuario(id_seguidor, id_seguido));
            verify(usuarioDAOMock, times(1)).follow(id_seguidor, id_seguido);
        });
    }

    @Test
    public void testCurtePost() throws Exception{
        int id_post = 1, id_usuario = 2;
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            doNothing().when(usuarioDAOMock).like(id_post, id_usuario);

            assertDoesNotThrow(() -> s.curtePost(id_post, id_usuario));
            verify(usuarioDAOMock, times(1)).like(id_post, id_usuario);
        });
    }

    @Test
    public void testComentaPost() throws Exception{
        int id_post = 1, id_usuario = 2;
        Timestamp ts = Timestamp.valueOf("2024-06-04 16:00:00");
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            doNothing().when(postDAOMock).comment(id_post, id_usuario, "bla", ts);

            assertDoesNotThrow(() -> s.comentar(id_post, id_usuario, "bla", ts));
            verify(postDAOMock, times(1)).comment(id_post, id_usuario, "bla", ts);
        });
    }

    @Test 
    public void testLogin() throws Exception{
        String email = "b@email.com", senha = "456";
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            Usuario usuarioMock = new Usuario(2, "B", "b@email.com", "456");
               
            when(usuarioDAOMock.login(email, senha)).thenReturn(usuarioMock);

            Usuario resultado = s.login(email, senha);

            assertNotNull(resultado);
            assertEquals(email, resultado.getEmail());
            assertEquals(senha, resultado.getSenha());
            verify(usuarioDAOMock, times(1)).login(email, senha);
        });
    }

    @Test
    public void testSubconsulta() throws Exception {
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            List<Usuario> usuariosMock = List.of(
                new Usuario(1, "A", "a@email.com", "123"),
                new Usuario(2, "B", "b@email.com", "456")
            );
            when(usuarioDAOMock.mostFollowedUsersFollowers()).thenReturn(usuariosMock);

            List<Usuario> resultado = s.subconsulta();

            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            assertEquals("A", resultado.get(0).getNome());
            verify(usuarioDAOMock, times(1)).mostFollowedUsersFollowers();
        });
    }

    @Test
    public void testInserePost() throws Exception {
        Timestamp ts = Timestamp.valueOf("2024-06-04 16:00:32");
        Post post = new Post(1, 42, ts, "trintaequatro");
        withMockedDAOs(() -> {
            Sistema s= new Sistema("senha");
            doNothing().when(postDAOMock).insert(post);

            assertDoesNotThrow(() -> s.inserePost(post));
            verify(postDAOMock, times(1)).insert(post);
        });
    }

    @Test
    public void testRemovePost() throws Exception {
        int id = 3;
        withMockedDAOs(() -> {
            Sistema s= new Sistema("senha");
            doNothing().when(postDAOMock).delete(id);

            assertDoesNotThrow(() -> s.removePost(id));
            verify(postDAOMock, times(1)).delete(id);
        });
    }

    @Test
    public void testRemovePostUsuario() throws Exception{
        int id_usuario = 4;
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            List<Integer> lista = List.of(1, 2);

            when(postDAOMock.removePostsUsuario(id_usuario)).thenReturn(lista);

            List<Integer> resultado = s.removePostUsuario(id_usuario);

            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            assertEquals(1, resultado.get(0));
            verify(postDAOMock, times(1)).removePostsUsuario(id_usuario);
        });
    }

    @Test
    public void testMostraPosts() throws Exception {
        Timestamp ts1 = Timestamp.valueOf("2024-06-04 16:14:32");
        Timestamp ts2 = Timestamp.valueOf("2024-06-04 17:00:32");
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            List<Post> postsMock = List.of(
                new Post(2, 5, ts1, "123"),
                new Post(4, 7, ts2, "seila")
            );
            when(postDAOMock.show()).thenReturn(postsMock);

            List<Post> resultado = s.mostraPosts();

            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            assertEquals(4, resultado.get(1).getId_post());
            verify(postDAOMock, times(1)).show();
        });
    }

    @Test
    public void testMostraPostsUsuario() throws Exception {
        Timestamp ts1 = Timestamp.valueOf("2024-06-04 16:14:32");
        Timestamp ts2 = Timestamp.valueOf("2024-06-04 17:00:32");
        int id_usuario = 4;
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            List<Post> postsMock = List.of(
                new Post(2, 4, ts1, "123"),
                new Post(4, 4, ts2, "seila"));
            when(postDAOMock.showPostsUsuario(id_usuario)).thenReturn(postsMock);

            List<Post> resultado = s.buscaPostsUsuario(id_usuario);

            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            assertEquals(id_usuario, resultado.get(1).getId_post());
            verify(postDAOMock, times(1)).showPostsUsuario(id_usuario);
        });
    }

    @Test
    public void testInsereMidia() throws Exception {
        Midia midia = new Midia(3, 1024, 2, 10);
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            doNothing().when(midiaDAOMock).insert(midia);

            assertDoesNotThrow(() -> s.insereMidia(midia));
            verify(midiaDAOMock, times(1)).insert(midia);
        });
    }

    @Test
    public void testRemoveMidia() throws Exception {
        int id = 8;
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            doNothing().when(midiaDAOMock).delete(id);

            assertDoesNotThrow(() -> s.removeMidia(id));
            verify(midiaDAOMock, times(1)).delete(id);
        });
    }

    @Test
    public void testMostraMidias() throws Exception {
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            List<Midia> midiasMock = List.of(
                new Midia(1, 15, 1),
                new Midia(2, 2048, 2, 20),
                new Midia(3, 256, 2, 2)
            );
            when(midiaDAOMock.show()).thenReturn(midiasMock);

            List<Midia> resultado = s.mostraMidias();

            assertNotNull(resultado);
            assertEquals(3, resultado.size());
            assertEquals(2, resultado.get(1).getId_midia());
            verify(midiaDAOMock, times(1)).show();
        });
    }

    @Test
    public void testInsereMensagem() throws Exception {
        Timestamp ts = Timestamp.valueOf("2024-06-04 18:00:32");
        Mensagem mensagem = new Mensagem(6, ts, "Olha isso", 3, 0, 3, false, false);
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            doNothing().when(mensagemDAOMock).insert(mensagem);

            assertDoesNotThrow(() -> s.insereMensagem(mensagem));
            verify(mensagemDAOMock, times(1)).insert(mensagem);
        });
    }

    @Test
    public void removeMensagem() throws Exception {
        int id = 17;
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            doNothing().when(mensagemDAOMock).delete(id);

            assertDoesNotThrow(() -> s.removeMensagem(id));
            verify(mensagemDAOMock, times(1)).delete(id);
        });
    }

    @Test
    public void testMostraMensagem() throws Exception {
        Timestamp ts1 = Timestamp.valueOf("2024-06-04 19:00:32");
        Timestamp ts2 = Timestamp.valueOf("2024-06-04 20:00:32");
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            List<Mensagem> mensagensMock = List.of(
                new Mensagem(1, ts1, "Que linda a lua em sergipe", 4, 4, 0, true, true),
                new Mensagem(2, ts2, "caraca", 7, 0, 0, true, false)
            );
            when(mensagemDAOMock.show()).thenReturn(mensagensMock);

            List<Mensagem> resultado = s.mostraMensagens();

            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            assertEquals(2, resultado.get(1).getId_mensagem());
            verify(mensagemDAOMock, times(1)).show();
        });
    }

    @Test
    public void testRecebeMensagem() throws Exception {
        int id_conversa = 3;
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            doNothing().when(mensagemDAOMock).receive(id_conversa);

            assertDoesNotThrow(() -> s.recebeMensagem(id_conversa));
            verify(mensagemDAOMock, times(1)).receive(id_conversa);
        });
    }

    @Test
    public void testInsereConversa() throws Exception {
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            doNothing().when(conversaDAOMock).inserirConversa("coffe breaks udesc-cct");

            assertDoesNotThrow(() -> s.insereConversa("coffe breaks udesc-cct"));
            verify(conversaDAOMock, times(1)).inserirConversa("coffe breaks udesc-cct");
        });
    }

    @Test 
    public void testInserirConversasUsuario() throws Exception {
        int id_usuario = 11;
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            doNothing().when(conversaDAOMock).criaConversaUsuario(id_usuario);

            assertDoesNotThrow(() -> s.insereConversaUsuario(id_usuario));
            verify(conversaDAOMock, times(1)).criaConversaUsuario(id_usuario);
        });
    }

    @Test 
    public void testRemoveConversa() throws Exception {
        int id = 17;
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            doNothing().when(conversaDAOMock).deleteConversa(id);

            assertDoesNotThrow(() -> s.removeConversa(id));
            verify(conversaDAOMock, times(1)).deleteConversa(id);
        });
    }

    @Test
    public void testMensagensConversa() throws Exception {
        int id_conversa = 7;
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            List<Integer> mensagensIdMock = List.of(1, 2);

            when(conversaDAOMock.todasMensagens(id_conversa)).thenReturn(mensagensIdMock);

            List<Integer> resultado = s.mensagensConversa(id_conversa);

            assertNotNull(resultado);
            assertEquals(mensagensIdMock, resultado);
            assertEquals(2, resultado.size());
            assertEquals(1, resultado.get(0));
            verify(conversaDAOMock, times(1)).todasMensagens(id_conversa);
        });
    }

    @Test
    public void testMostraConversa() throws Exception {
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            List<Conversa> conversasMock = List.of(
                new Conversa(1, "trafico de animais exoticos"),
                new Conversa(2, "remedios clandestinos")
            );
            when(conversaDAOMock.show()).thenReturn(conversasMock);

            List<Conversa> resultado = s.mostraConversas();

            assertNotNull(resultado);
            assertEquals(conversasMock, resultado);
            assertEquals(2, resultado.size());
            assertEquals(2, resultado.get(1).getId_conversa());
            verify(conversaDAOMock, times(1)).show();
        });
    }

    @Test
    public void testMostrarConversasUsuario() throws Exception {
        int id_usuario = 2;
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            List<Conversa> conversasMock = List.of(
                new Conversa(1, "CoC - udesc_cct"),
                new Conversa(2, "CR - udesc_cct")
            );
            when(conversaDAOMock.showConversasUsuario(id_usuario)).thenReturn(conversasMock);

            List<Conversa> resultado = s.mostraConversasUsuario(id_usuario);

            assertNotNull(resultado);
            assertEquals(conversasMock, resultado);
            assertEquals(2, resultado.size());
            assertEquals(2, resultado.get(1).getId_conversa());
            verify(conversaDAOMock, times(1)).showConversasUsuario(id_usuario);
        });
    }

    @Test
    public void testMostraConteudoDaConversa() throws Exception {
        int id_conversa = 1;
        Timestamp ts1 = Timestamp.valueOf("2024-06-04 21:00:32");
        Timestamp ts2 = Timestamp.valueOf("2024-06-04 22:00:32");
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            List<Mensagem> mensagensMock =  List.of(
                new Mensagem(6, ts1, "Tu viu esse loko", 5, 8, 0, true, true),
                new Mensagem(8, ts2, "ele tá é certo", 9, 0, 0, true, true)
            );
            when(conversaDAOMock.showConversa(id_conversa)).thenReturn(mensagensMock);

            List<Mensagem> resultado = s.mostraConteudoDaConversa(id_conversa);

            assertNotNull(resultado);
            assertEquals(mensagensMock, resultado);
            assertEquals(2, resultado.size());
            assertEquals(8, resultado.get(1).getId_mensagem());
            verify(conversaDAOMock, times(1)).showConversa(id_conversa);
        });
    }

    @Test
    public void testUserMessages() throws Exception {
        int id_conversa = 4, id_usuario = 2;
        Timestamp ts1 = Timestamp.valueOf("2024-06-04 23:00:32");
        Timestamp ts2 = Timestamp.valueOf("2024-06-05 00:00:00");
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            List<Mensagem> mensagensMock = List.of(
                new Mensagem(10, ts1, "Mano tu n pode ta falando serio", 5, 0, 0, true, true),
                new Mensagem(11, ts2,"e se eu estiver?", 9, 0, 0, true, true)
            );
            when(conversaDAOMock.mensagensUsuario(id_conversa, id_usuario)).thenReturn(mensagensMock);

            List<Mensagem> resultado = s.userMessages(id_conversa, id_usuario);

            assertNotNull(resultado);
            assertEquals(mensagensMock, resultado);
            assertEquals(2, resultado.size());
            assertEquals(11, resultado.get(1).getId_mensagem());
            verify(conversaDAOMock, times(1)).mensagensUsuario(id_conversa, id_usuario);
        });
    }

    @Test
    public void testParticipaConversa() throws Exception {
        int id_conversa = 1, id_usuario = 1;
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            doNothing().when(conversaDAOMock).participa(id_conversa, id_usuario);

            assertDoesNotThrow(() -> s.participarConversa(id_conversa, id_usuario));
            verify(conversaDAOMock, times(1)).participa(id_conversa, id_usuario);
        });
    }

    @Test
    public void testCheckParticipation() throws Exception {
        int id_conversa = 65, id_usuario = 10;
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            Boolean boolMock = true;
            when(conversaDAOMock.verificaParticipacao(id_conversa, id_usuario)).thenReturn(boolMock);

            Boolean resultado = s.checkParticipation(id_conversa, id_usuario);

            assertNotNull(resultado);
            assertEquals(boolMock, resultado);
            verify(conversaDAOMock, times(1)).verificaParticipacao(id_conversa, id_usuario);
        });
    }

    @Test
    public void testPossuir() throws Exception {
        int id_midia = 3;
        withMockedDAOs(() -> {
            Sistema s = new Sistema("senha");
            doNothing().when(postDAOMock).possui(id_midia);

            assertDoesNotThrow(() -> s.possuir(id_midia));
            verify(postDAOMock, times(1)).possui(id_midia);
        });
    }
}
