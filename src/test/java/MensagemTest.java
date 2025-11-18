import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import dados.Mensagem;
import java.sql.Timestamp;

public class MensagemTest {

    @Test
    public void testConstrutorCompleto() {
        Timestamp ts = Timestamp.valueOf("2024-06-10 10:00:00");
        Mensagem msg = new Mensagem(1, ts, "Olá", 2, 3, 4, true, false);
        assertEquals(1, msg.getId_mensagem());
        assertEquals(ts, msg.getData_hora());
        assertEquals("Olá", msg.getTexto());
        assertEquals(2, msg.getId_usuario());
        assertEquals(3, msg.getId_post());
        assertEquals(4, msg.getId_midia());
        assertTrue(msg.isEntregue());
        assertFalse(msg.isVisualizado());
    }

    @Test
    public void testConstrutorVazio() {
        Mensagem msg = new Mensagem();
        assertNotNull(msg);
    }

    @Test
    public void testSetAndGetId_mensagem() {
        Mensagem msg = new Mensagem();
        msg.setId_mensagem(10);
        assertEquals(10, msg.getId_mensagem());
    }

    @Test
    public void testSetAndGetData_hora() {
        Mensagem msg = new Mensagem();
        Timestamp ts = Timestamp.valueOf("2024-06-11 11:00:00");
        msg.setData_hora(ts);
        assertEquals(ts, msg.getData_hora());
    }

    @Test
    public void testSetAndGetTexto() {
        Mensagem msg = new Mensagem();
        msg.setTexto("Teste texto");
        assertEquals("Teste texto", msg.getTexto());
    }

    @Test
    public void testSetAndGetId_usuario() {
        Mensagem msg = new Mensagem();
        msg.setId_usuario(5);
        assertEquals(5, msg.getId_usuario());
    }

    @Test
    public void testSetAndGetId_post() {
        Mensagem msg = new Mensagem();
        msg.setId_post(6);
        assertEquals(6, msg.getId_post());
    }

    @Test
    public void testSetAndGetId_midia() {
        Mensagem msg = new Mensagem();
        msg.setId_midia(7);
        assertEquals(7, msg.getId_midia());
    }

    @Test
    public void testSetAndIsEntregue() {
        Mensagem msg = new Mensagem();
        msg.setEntregue(true);
        assertTrue(msg.isEntregue());
        msg.setEntregue(false);
        assertFalse(msg.isEntregue());
    }

    @Test
    public void testSetAndIsVisualizado() {
        Mensagem msg = new Mensagem();
        msg.setVisualizado(true);
        assertTrue(msg.isVisualizado());
        msg.setVisualizado(false);
        assertFalse(msg.isVisualizado());
    }

    @Test
    public void testToStringSemPostEMidia() {
        Timestamp ts = Timestamp.valueOf("2024-06-12 12:00:00");
        Mensagem msg = new Mensagem(1, ts, "Oi", 2, 0, 0, false, false);
        String expected = "1 - 2 - Oi - " + ts;
        assertEquals(expected, msg.toString());
    }

    @Test
    public void testToStringComPostEMidia() {
        Timestamp ts = Timestamp.valueOf("2024-06-13 13:00:00");
        Mensagem msg = new Mensagem(2, ts, "Mensagem", 3, 4, 5, true, true);
        String expected = "2 - 3 - Mensagem - " + ts + " - 4 - 5";
        assertEquals(expected, msg.toString());
    }

    @Test
    public void testEqualsMesmoObjeto() {
        Mensagem msg = new Mensagem();
        assertEquals(msg, msg);
    }
    
    @Test
    public void testEqualsObjetosDiferentes() {
        Mensagem m1 = new Mensagem();
        m1.setId_mensagem(1);
        Mensagem m2 = new Mensagem();
        m2.setId_mensagem(2);
        assertNotEquals(m1, m2);
    }
}
