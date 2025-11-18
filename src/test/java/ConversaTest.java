import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import dados.Conversa;

public class ConversaTest {

    @Test
    public void testConstrutorCompleto() {
        Conversa conversa = new Conversa(1, "Grupo da Família");
        assertEquals(1, conversa.getId_conversa());
        assertEquals("Grupo da Família", conversa.getNome_conversa());
    }

    @Test
    public void testConstrutorVazio() {
        Conversa conversa = new Conversa();
        assertNotNull(conversa);
    }

    @Test
    public void testSetAndGetId_conversa() {
        Conversa conversa = new Conversa();
        conversa.setId_conversa(10);
        assertEquals(10, conversa.getId_conversa());
    }

    @Test
    public void testSetAndGetNome_conversa() {
        Conversa conversa = new Conversa();
        conversa.setNome_conversa("Amigos");
        assertEquals("Amigos", conversa.getNome_conversa());
    }

    @Test
    public void testToString() {
        Conversa conversa = new Conversa(2, "Trabalho");
        String expected = "2 - Trabalho";
        assertEquals(expected, conversa.toString());
    }

    @Test
    public void testEqualsMesmoObjeto() {
        Conversa conversa = new Conversa(3, "Teste");
        assertEquals(conversa, conversa);
    }

    @Test
    public void testEqualsObjetosDiferentes() {
        Conversa c1 = new Conversa(5, "A");
        Conversa c2 = new Conversa(6, "A");
        assertNotEquals(c1, c2);
    }
}
