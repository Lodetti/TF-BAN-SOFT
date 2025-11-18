import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import apresentacao.Main;
import dados.Mensagem;
import dados.Midia;
import dados.Post;
import dados.Usuario;
import excecoes.InsertException;
import negocio.Sistema;

public class MainTest {

    @Mock
    private Sistema mockSistema;

    @Mock
    private Scanner mockScanner;

    @InjectMocks
    private Main main;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        try {
            Field sistemaField = Main.class.getDeclaredField("sistema");
            sistemaField.setAccessible(true);
            sistemaField.set(null, mockSistema);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Field scannerField = Main.class.getDeclaredField("scan");
            scannerField.setAccessible(true);
            scannerField.set(null, mockScanner);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSegueUsuario() throws Exception {
        int id_seguidor = 1;
        int id_seguido = 2;

        when(mockScanner.nextLine()).thenReturn(String.valueOf(id_seguido));

        when(mockSistema.mostraUsuarios()).thenReturn(new ArrayList<Usuario>());

        Main.segueUsuario(id_seguidor);

        verify(mockScanner, times(1)).nextLine();
        verify(mockSistema, times(1)).mostraUsuarios();
        verify(mockSistema, times(1)).segueUsuario(id_seguidor, id_seguido);
    }

    @Test
    public void testSegueUsuarioSegueEleMesmo() throws Exception {
        int id_seguidor = 1;
        int id_seguido = 1;

        when(mockScanner.nextLine()).thenReturn(String.valueOf(id_seguido));

        when(mockSistema.mostraUsuarios()).thenReturn(new ArrayList<Usuario>());

        assertThrows(InsertException.class, () -> Main.segueUsuario(id_seguidor));

        verify(mockSistema, times(1)).mostraUsuarios();
        verify(mockSistema, times(0)).segueUsuario(anyInt(), anyInt());
    }

    @Test
    public void testComentaPost() throws Exception {
        int id_usuario = 1;
        int id_post = 10;
        String comentario = "Comentário de teste";

        when(mockSistema.mostraPosts()).thenReturn(new ArrayList<>());

        when(mockScanner.nextLine()).thenReturn(String.valueOf(id_post)).thenReturn(comentario);

        Main.comentaPost(id_usuario);

        verify(mockSistema, times(1)).mostraPosts();
        verify(mockScanner, times(2)).nextLine();
        verify(mockSistema, times(1)).comentar(eq(id_post),eq(id_usuario),eq(comentario),any(Timestamp.class)
        );
    }

    @Test
    public void testComentaPost_ComentarioVazio() throws Exception {
        int id_usuario = 1;
        int id_post = 10;
        String comentario = "";

        when(mockSistema.mostraPosts()).thenReturn(new ArrayList<>());

        when(mockScanner.nextLine()).thenReturn(String.valueOf(id_post)).thenReturn(comentario);

        assertThrows(InsertException.class ,() -> Main.comentaPost(id_usuario));

        verify(mockSistema, times(1)).mostraPosts();
        verify(mockScanner, times(2)).nextLine();
        verify(mockSistema, times(0)).comentar(anyInt(),anyInt(),anyString(),any(Timestamp.class));
    }

    @Test 
    public void testPublicaPost() throws Exception {
        int id_midia = 2;
        Post post = new Post(3, 1, null, "Legenda teste");

        List<Midia> midias = List.of(
                new Midia(1, 15, 1),
                new Midia(2, 2048, 2, 20),
                new Midia(3, 256, 2, 2)
            ); 

        when(mockSistema.mostraMidias()).thenReturn(midias);

        when(mockSistema.mostraUsuarios()).thenReturn(new ArrayList<>());

        when(mockScanner.nextLine()).thenReturn(String.valueOf(post.getId_usuario())).thenReturn(String.valueOf(id_midia)).thenReturn(post.getLegenda());

        Main.publicaPost(mockSistema);
        
        verify(mockSistema, times(1)).mostraUsuarios();
        verify(mockSistema, times(2)).mostraMidias();
        verify(mockScanner, times(3)).nextLine();
        verify(mockSistema, times(1)).inserePost(argThat(value -> 
                value.getId_usuario() == post.getId_usuario() &&
                value.getLegenda().equals(post.getLegenda())
        ));
        verify(mockSistema, times(1)).possuir(id_midia);
    }

    @Test
    public void testPublicaPost_MidiaInvalida() throws Exception {
        int id_midia = 72;
        Post post = new Post(3, 1, null, "Legenda teste");

        List<Midia> midias = List.of(
                new Midia(1, 15, 1),
                new Midia(2, 2048, 2, 20),
                new Midia(3, 256, 2, 2)
            ); 

        when(mockSistema.mostraMidias()).thenReturn(midias);

        when(mockSistema.mostraUsuarios()).thenReturn(new ArrayList<>());

        when(mockScanner.nextLine()).thenReturn(String.valueOf(post.getId_usuario())).thenReturn(String.valueOf(id_midia)).thenReturn(post.getLegenda());

        assertThrows(InsertException.class, () -> Main.publicaPost(mockSistema));

        verify(mockSistema, times(1)).mostraUsuarios();
        verify(mockSistema, times(2)).mostraMidias();
        verify(mockScanner, times(2)).nextLine();
        verify(mockSistema, times(0)).inserePost(argThat(value -> 
                value.getId_usuario() == post.getId_usuario() &&
                value.getLegenda().equals(post.getLegenda())
        ));
        verify(mockSistema, times(0)).possuir(id_midia);
    }

    @Test 
    public void testPublicaPostUsuario() throws Exception {
        int id_midia = 2;
        int id_usuario = 1;
        Post post = new Post(3, id_usuario, null, "Legenda teste");

        List<Midia> midias = List.of(
                new Midia(1, 15, 1),
                new Midia(2, 2048, 2, 20),
                new Midia(3, 256, 2, 2)
            ); 


        when(mockSistema.mostraMidias()).thenReturn(midias);

        when(mockScanner.nextLine()).thenReturn(String.valueOf(2)).thenReturn(String.valueOf(id_midia)).thenReturn(post.getLegenda());

        Main.publicaPostUsuario(mockSistema, id_usuario);

        verify(mockSistema, times(2)).mostraMidias();
        verify(mockScanner, times(3)).nextLine();
        verify(mockSistema, times(1)).inserePost(argThat(value -> 
                value.getId_usuario() == post.getId_usuario() &&
                value.getLegenda().equals(post.getLegenda())
        ));
        verify(mockSistema, times(1)).possuir(id_midia);
    }

    @Test 
    public void testPublicaPostUsuario_ComMidiaInvalida() throws Exception {
        int id_midia = 5;
        int id_usuario = 1;
        Post post = new Post(3, id_usuario, null, "Legenda teste");

        List<Midia> midias = List.of(
                new Midia(1, 15, 1),
                new Midia(2, 2048, 2, 20),
                new Midia(3, 256, 2, 2)
            ); 


        when(mockSistema.mostraMidias()).thenReturn(midias);

        when(mockScanner.nextLine()).thenReturn(String.valueOf(2)).thenReturn(String.valueOf(id_midia)).thenReturn(post.getLegenda());

        assertThrows(InsertException.class, () -> Main.publicaPostUsuario(mockSistema, id_usuario));

        verify(mockSistema, times(2)).mostraMidias();
        verify(mockScanner, times(2)).nextLine();
        verify(mockSistema, times(0)).inserePost(argThat(value -> 
                value.getId_usuario() == post.getId_usuario() &&
                value.getLegenda().equals(post.getLegenda())
        ));
        verify(mockSistema, times(0)).possuir(id_midia);
    }

    @Test
    public void testMandaMensagem() throws Exception {
        int id_usuario = 2;
        int id_conversa = 1;
        Mensagem mensagem = new Mensagem(1, null, "", id_usuario, 0, 4, true, false);

        when(mockSistema.checkParticipation(id_conversa, id_usuario)).thenReturn(true);
        when(mockSistema.mostraPosts()).thenReturn(new ArrayList<>());
        when(mockSistema.mostraMidias()).thenReturn(new ArrayList<>());
        when(mockScanner.nextLine())
            .thenReturn("") // id_post vazio
            .thenReturn(String.valueOf(mensagem.getId_midia()))
            .thenReturn(mensagem.getTexto());

        Main.mandaMensagem(id_usuario, id_conversa);

        verify(mockSistema, times(1)).checkParticipation(id_conversa, id_usuario);
        verify(mockSistema, times(1)).mostraPosts();
        verify(mockSistema, times(1)).mostraMidias();
        verify(mockScanner, times(3)).nextLine();
        verify(mockSistema, times(1)).insereMensagem(argThat(m -> 
            m.getId_usuario() == mensagem.getId_usuario() &&
            m.getId_post() == mensagem.getId_post() &&
            m.getId_midia() == mensagem.getId_midia() &&
            m.getTexto().equals(mensagem.getTexto())
        ));
        verify(mockSistema, times(1)).recebeMensagem(id_conversa);
    }

    @Test
    public void testMandaMensagem_UsuarioForaDaConversa() throws Exception {
        int id_usuario = 2;
        int id_conversa = 1;
        Mensagem mensagem = new Mensagem(1, null, "", id_usuario, 0, 4, true, false);

        when(mockSistema.checkParticipation(id_conversa, id_usuario)).thenReturn(false);
        when(mockSistema.mostraPosts()).thenReturn(new ArrayList<>());
        when(mockSistema.mostraMidias()).thenReturn(new ArrayList<>());
        when(mockScanner.nextLine())
            .thenReturn("") // id_post vazio
            .thenReturn(String.valueOf(mensagem.getId_midia()))
            .thenReturn(mensagem.getTexto());

        assertThrows(InsertException.class, () -> Main.mandaMensagem(id_usuario, id_conversa));

        verify(mockSistema, times(1)).checkParticipation(id_conversa, id_usuario);
        verify(mockSistema, times(0)).mostraPosts();
        verify(mockSistema, times(0)).mostraMidias();
        verify(mockScanner, times(0)).nextLine();
        verify(mockSistema, times(0)).insereMensagem(argThat(m -> 
            m.getId_usuario() == mensagem.getId_usuario() &&
            m.getId_post() == mensagem.getId_post() &&
            m.getId_midia() == mensagem.getId_midia() &&
            m.getTexto().equals(mensagem.getTexto())
        ));
        verify(mockSistema, times(0)).recebeMensagem(id_conversa);
    }

    @Test
    public void testMandaMensagem_SemParametros() throws Exception {
        int id_usuario = 2;
        int id_conversa = 1;
        Mensagem mensagem = new Mensagem(1, null, "", id_usuario, 0, 4, true, false);

        when(mockSistema.checkParticipation(id_conversa, id_usuario)).thenReturn(true);
        when(mockSistema.mostraPosts()).thenReturn(new ArrayList<>());
        when(mockSistema.mostraMidias()).thenReturn(new ArrayList<>());
        when(mockScanner.nextLine())
            .thenReturn("") // id_post vazio
            .thenReturn("") // id_midia vazio
            .thenReturn(mensagem.getTexto());

        assertThrows(InsertException.class, () -> Main.mandaMensagem(id_usuario, id_conversa));

        verify(mockSistema, times(1)).checkParticipation(id_conversa, id_usuario);
        verify(mockSistema, times(1)).mostraPosts();
        verify(mockSistema, times(1)).mostraMidias();
        verify(mockScanner, times(3)).nextLine();
        verify(mockSistema, times(0)).insereMensagem(argThat(m -> 
            m.getId_usuario() == mensagem.getId_usuario() &&
            m.getId_post() == mensagem.getId_post() &&
            m.getId_midia() == mensagem.getId_midia() &&
            m.getTexto().equals(mensagem.getTexto())
        ));
        verify(mockSistema, times(0)).recebeMensagem(id_conversa);
    }

    @Test
    public void testMandaMensagemAdmin() throws Exception {
        int id_usuario = 2;
        int id_conversa = 1;
        Mensagem mensagem = new Mensagem(1, null, "", id_usuario, 1, 0, true, false);

        when(mockSistema.mostraUsuarios()).thenReturn(new ArrayList<>());
        when(mockSistema.mostraConversas()).thenReturn(new ArrayList<>());
        when(mockSistema.checkParticipation(id_conversa, id_usuario)).thenReturn(true);
        when(mockSistema.mostraPosts()).thenReturn(new ArrayList<>());
        when(mockSistema.mostraMidias()).thenReturn(new ArrayList<>());
        when(mockScanner.nextLine())
            .thenReturn(String.valueOf(id_usuario))
            .thenReturn(String.valueOf(id_conversa))
            .thenReturn(String.valueOf(mensagem.getId_post()))
            .thenReturn("") // id_midia vazio
            .thenReturn(mensagem.getTexto());

        Main.mandaMensagemAdmin();

        verify(mockSistema, times(1)).mostraUsuarios();
        verify(mockSistema, times(1)).mostraConversas();
        verify(mockSistema, times(1)).checkParticipation(id_conversa, id_usuario);
        verify(mockSistema, times(1)).mostraPosts();
        verify(mockSistema, times(0)).mostraMidias();
        verify(mockScanner, times(4)).nextLine();
        verify(mockSistema, times(1)).insereMensagem(argThat(m -> 
            m.getId_usuario() == mensagem.getId_usuario() &&
            m.getId_post() == mensagem.getId_post() &&
            m.getId_midia() == mensagem.getId_midia() &&
            m.getTexto().equals(mensagem.getTexto())
        ));
        verify(mockSistema, times(1)).recebeMensagem(id_conversa);
    }

    @Test
    public void testMandaMensagemAdmin_UsuarioNaoParticipa() throws Exception {
        int id_usuario = 2;
        int id_conversa = 1;
        Mensagem mensagem = new Mensagem(1, null, "", id_usuario, 1, 0, true, false);

        when(mockSistema.mostraUsuarios()).thenReturn(new ArrayList<>());
        when(mockSistema.mostraConversas()).thenReturn(new ArrayList<>());
        when(mockSistema.checkParticipation(id_conversa, id_usuario)).thenReturn(false);
        when(mockSistema.mostraPosts()).thenReturn(new ArrayList<>());
        when(mockSistema.mostraMidias()).thenReturn(new ArrayList<>());
        when(mockScanner.nextLine())
            .thenReturn(String.valueOf(id_usuario))
            .thenReturn(String.valueOf(id_conversa))
            .thenReturn(String.valueOf(mensagem.getId_post()))
            .thenReturn("") // id_midia vazio
            .thenReturn(mensagem.getTexto());

        assertThrows(InsertException.class, () -> Main.mandaMensagemAdmin());

        verify(mockSistema, times(1)).mostraUsuarios();
        verify(mockSistema, times(1)).mostraConversas();
        verify(mockSistema, times(1)).checkParticipation(id_conversa, id_usuario);
        verify(mockSistema, times(0)).mostraPosts();
        verify(mockSistema, times(0)).mostraMidias();
        verify(mockScanner, times(2)).nextLine();
        verify(mockSistema, times(0)).insereMensagem(argThat(m -> 
            m.getId_usuario() == mensagem.getId_usuario() &&
            m.getId_post() == mensagem.getId_post() &&
            m.getId_midia() == mensagem.getId_midia() &&
            m.getTexto().equals(mensagem.getTexto())
        ));
        verify(mockSistema, times(0)).recebeMensagem(id_conversa);
    }

    @Test
    public void testMandaMensagemAdmin_SemParametros() throws Exception {
        int id_usuario = 2;
        int id_conversa = 1;
        Mensagem mensagem = new Mensagem(1, null, "", id_usuario, 1, 0, true, false);

        when(mockSistema.mostraUsuarios()).thenReturn(new ArrayList<>());
        when(mockSistema.mostraConversas()).thenReturn(new ArrayList<>());
        when(mockSistema.checkParticipation(id_conversa, id_usuario)).thenReturn(true);
        when(mockSistema.mostraPosts()).thenReturn(new ArrayList<>());
        when(mockSistema.mostraMidias()).thenReturn(new ArrayList<>());
        when(mockScanner.nextLine())
            .thenReturn(String.valueOf(id_usuario))
            .thenReturn(String.valueOf(id_conversa))
            .thenReturn("")
            .thenReturn("") // id_midia vazio
            .thenReturn(mensagem.getTexto());

        assertThrows(InsertException.class, () -> Main.mandaMensagemAdmin());

        verify(mockSistema, times(1)).mostraUsuarios();
        verify(mockSistema, times(1)).mostraConversas();
        verify(mockSistema, times(1)).checkParticipation(id_conversa, id_usuario);
        verify(mockSistema, times(1)).mostraPosts();
        verify(mockSistema, times(1)).mostraMidias();
        verify(mockScanner, times(5)).nextLine();
        verify(mockSistema, times(0)).insereMensagem(argThat(m -> 
            m.getId_usuario() == mensagem.getId_usuario() &&
            m.getId_post() == mensagem.getId_post() &&
            m.getId_midia() == mensagem.getId_midia() &&
            m.getTexto().equals(mensagem.getTexto())
        ));
        verify(mockSistema, times(0)).recebeMensagem(id_conversa);
    }
    

}