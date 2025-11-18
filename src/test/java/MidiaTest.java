import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import dados.Midia;

public class MidiaTest {

    @Test
    public void testConstrutorCompleto() {
        Midia midia = new Midia(1, 500, 2, 125);
        assertEquals(1, midia.getId_midia());
        assertEquals(500, midia.getTamanho());
        assertEquals(2, midia.getTipo());
        assertEquals(125, midia.getDuracao());
    }

    @Test
    public void testConstrutorSemDuracao() {
        Midia midia = new Midia(2, 300, 1);
        assertEquals(2, midia.getId_midia());
        assertEquals(300, midia.getTamanho());
        assertEquals(1, midia.getTipo());
        assertEquals(0, midia.getDuracao());
    }

    @Test
    public void testSetAndGetId_midia() {
        Midia midia = new Midia();
        midia.setId_midia(10);
        assertEquals(10, midia.getId_midia());
    }

    @Test
    public void testSetAndGetTamanho() {
        Midia midia = new Midia();
        midia.setTamanho(1024);
        assertEquals(1024, midia.getTamanho());
    }

    @Test
    public void testSetAndGetTipo() {
        Midia midia = new Midia();
        midia.setTipo(2);
        assertEquals(2, midia.getTipo());
    }

    @Test
    public void testSetAndGetDuracao() {
        Midia midia = new Midia();
        midia.setDuracao(90);
        assertEquals(90, midia.getDuracao());
    }

    @Test
    public void testToStringImagem() {
        Midia midia = new Midia(3, 200, 1);
        String expected = "3 - 200 - Imagem";
        assertEquals(expected, midia.toString());
    }

    @Test
    public void testToStringVideo() {
        Midia midia = new Midia(4, 800, 2, 125);
        String expected = "4 - 800 - Video - 2min e 5seg";
        assertEquals(expected, midia.toString());
    }

    @Test
    public void testEqualsMesmoObjeto() {
        Midia midia = new Midia(5, 100, 1);
        assertEquals(midia, midia);
    }

    @Test
    public void testEqualsObjetosDiferentes() {
        Midia m1 = new Midia(7, 100, 1);
        Midia m2 = new Midia(8, 100, 1);
        assertNotEquals(m1, m2);
    }
}
