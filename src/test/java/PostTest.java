import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import dados.Post;
import java.sql.Timestamp;

public class PostTest {

    @Test
    public void testConstrutorCompleto() {
        Timestamp ts = Timestamp.valueOf("2024-06-01 12:00:00");
        Post post = new Post(1, 2, ts, "Legenda teste");
        assertEquals(1, post.getId_post());
        assertEquals(2, post.getId_usuario());
        assertEquals(ts, post.getData_hora());
        assertEquals("Legenda teste", post.getLegenda());
    }

    @Test
    public void testConstrutorSemLegenda() {
        Timestamp ts = Timestamp.valueOf("2024-06-01 13:00:00");
        Post post = new Post(2, 3, ts);
        assertEquals(2, post.getId_post());
        assertEquals(3, post.getId_usuario());
        assertEquals(ts, post.getData_hora());
        assertNull(post.getLegenda());
    }

    @Test
    public void testSetAndGetId_post() {
        Post post = new Post();
        post.setId_post(10);
        assertEquals(10, post.getId_post());
    }

    @Test
    public void testSetAndGetId_usuario() {
        Post post = new Post();
        post.setId_usuario(20);
        assertEquals(20, post.getId_usuario());
    }

    @Test
    public void testSetAndGetData_hora() {
        Post post = new Post();
        Timestamp ts = Timestamp.valueOf("2024-06-02 14:00:00");
        post.setData_hora(ts);
        assertEquals(ts, post.getData_hora());
    }

    @Test
    public void testSetAndGetLegenda() {
        Post post = new Post();
        post.setLegenda("Nova legenda");
        assertEquals("Nova legenda", post.getLegenda());
    }

    @Test
    public void testToString() {
        Timestamp ts = Timestamp.valueOf("2024-06-03 15:00:00");
        Post post = new Post(5, 6, ts, "Teste legenda");
        String expected = "5 - 6 - " + ts + " - Teste legenda";
        assertEquals(expected, post.toString());
    }

    @Test
    public void testEqualsMesmoObjeto() {
        Timestamp ts = Timestamp.valueOf("2024-06-04 16:00:00");
        Post post = new Post(7, 8, ts, "Legenda");
        assertEquals(post, post);
    }

    @Test
    public void testEqualsObjetosDiferentes() {
        Timestamp ts = Timestamp.valueOf("2024-06-06 18:00:00");
        Post p1 = new Post(11, 12, ts, "Legenda 1");
        Post p2 = new Post(12, 12, ts, "Legenda 2");
        assertNotEquals(p1, p2);
    }
}
