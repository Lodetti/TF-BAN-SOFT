import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dados.Usuario;

public class UsuarioTest {
    
    @Test
    public void testCriarUsuarioValido_ComDescricao() {
        Usuario usuario = new Usuario(1, "Gerson", "gerson@gmail.com", "fgneT3", "Saindo do flamengo");

        assertEquals(1, usuario.getId_usuario());
        assertEquals("Gerson", usuario.getNome());
        assertEquals("gerson@gmail.com", usuario.getEmail());
        assertEquals("fgneT3", usuario.getSenha());
        assertEquals("Saindo do flamengo", usuario.getDescricao());
    }

    @Test
    public void testCriarUsuarioValido_SemDescricao() {
        Usuario usuario = new Usuario(1, "Gerson", "gerson@gmail.com", "fgneT3");

        assertEquals(1, usuario.getId_usuario());
        assertEquals("Gerson", usuario.getNome());
        assertEquals("gerson@gmail.com", usuario.getEmail());
        assertEquals("fgneT3", usuario.getSenha());
        assertEquals("", usuario.getDescricao());
    }

    @Test
    public void testSetNomeUsuario(){
        Usuario u = new Usuario();
        u.setNome("Joao");
        assertEquals("Joao", u.getNome());
    }

    @Test
    public void testSetAndGetIdUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId_usuario(42);
        assertEquals(42, usuario.getId_usuario());
    }

    @Test
    public void testSetAndGetDescricao() {
        Usuario usuario = new Usuario();
        usuario.setDescricao("Nova descricao");
        assertEquals("Nova descricao", usuario.getDescricao());
    }

    @Test
    public void testSetAndGetEmail() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        assertEquals("teste@email.com", usuario.getEmail());
    }

    @Test
    public void testSetAndGetSenha() {
        Usuario usuario = new Usuario();
        usuario.setSenha("senha123");
        assertEquals("senha123", usuario.getSenha());
    }

    @Test
    public void testToString() {
        Usuario usuario = new Usuario(10, "Maria", "maria@email.com", "senha", "Descricao legal");
        String expected = "10 - Maria - Descricao legal";
        assertEquals(expected, usuario.toString());
    }

    @Test
    public void testEqualsMesmoObjeto() {
        Usuario usuario = new Usuario(1, "Joao", "joao@email.com", "senha", "desc");
        assertEquals(usuario, usuario);
    }

    @Test
    public void testEqualsObjetosDiferentes() {
        Usuario u1 = new Usuario(1, "Joao", "joao@email.com", "senha", "desc");
        Usuario u2 = new Usuario(2, "Maria", "maria@email.com", "senha", "desc");
        assertEquals(false, u1.equals(u2));
    }
}
