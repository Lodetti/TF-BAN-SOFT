package apresentacao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

import dados.Conversa;
import dados.Mensagem;
import dados.Midia;
import dados.Post;
import dados.Usuario;
import excecoes.DeleteException;
import excecoes.InsertException;
import excecoes.SelectException;
import negocio.Sistema;

// TODO: Mensagem de erro para usuário não encontrado ao excluir
// TODO: Mensagem de erro para usuário já cadastrado ao cadastrar


public class Main {
    private static Scanner scan = new Scanner(System.in);

    private static Sistema sistema;

    public static void main(String[] args) {

    try {
        sistema = new Sistema("Groudon19!");

        System.out.println("Escolha a oopcao:");
        int opcao = -1;
        do {
            opcao = menuInicial(scan);
            switch (opcao) {
                case 1:
                    int opcaoLogin = -1;
                    do {
                        opcaoLogin = menuLogin(scan); 
                        switch (opcaoLogin) {
                            case 1:
                                System.out.println("Entrar");
                                Usuario usuario = login(sistema);
                                if(usuario.getEmail() == null){
                                    System.out.println("Usuario nao encontrado");
                                    break;
                                }
                                int id_usuario = usuario.getId_usuario();
                                System.out.println("Login de " + usuario.getNome() + " feito com sucesso");
                                int opcaoPrincipal = -1;
                                do{
                                    opcaoPrincipal = menuPrincipal(scan);
                                    switch(opcaoPrincipal){
                                        case 1:
                                            int opcaoFeed = -1;
                                            do{
                                                opcaoFeed = feed(scan);
                                                switch(opcaoFeed){
                                                    case 1:
                                                        mostraPosts();
                                                        break;
                                                    case 2:
                                                        curtePost(id_usuario);
                                                        break;
                                                    case 3:
                                                        comentaPost(id_usuario);
                                                        break;
                                                    case 4:
                                                        segueUsuario(id_usuario);
                                                        break;
                                                    case 0: 
                                                        System.out.println("Voltando ao menu principal...");
                                                        break; 
                                                    default:
                                                        System.out.println("Opção inválida! Tente novamente.");
                                                        break;
                                                }
                                            }while(opcaoFeed != 0);
                                            break;
                                        case 2:
                                            int opcaoPerfil = -1;
                                            do{
                                                opcaoPerfil = perfil(scan);
                                                switch(opcaoPerfil){
                                                    case 1:
                                                        mostraPostsUsuario(id_usuario);
                                                        break;
                                                    case 2:
                                                        publicaPostUsuario(sistema, id_usuario);
                                                        break;
                                                    case 3:
                                                        mostraPostsUsuario(id_usuario);
                                                        deletePost(); // arriscado
                                                        break;
                                                    case 0: 
                                                        System.out.println("Voltando ao menu principal...");
                                                        break; 
                                                    default:
                                                        System.out.println("Opção inválida! Tente novamente.");
                                                        break;
                                                }
                                            }while(opcaoPerfil != 0);
                                            break;
                                        case 3:
                                            int opcaoChat = -1;
                                            do{
                                                opcaoChat = chat(scan);
                                                switch (opcaoChat) {
                                                    case 1:
                                                        mostraConversasUsuario(id_usuario);
                                                        break;
                                                    case 2:
                                                        mostraConversasUsuario(id_usuario);
                                                        System.out.println("Digite o ID da conversa: ");
                                                        int id_conversa = Integer.parseInt(scan.nextLine());
                                                        if(sistema.checkParticipation(id_conversa, id_usuario) == false){
                                                            System.out.println("Conversa inexistente");
                                                            break;
                                                        }
                                                        int opcaoAcessaConversa = -1;
                                                        do{
                                                            opcaoAcessaConversa = acessaConversa(scan, id_conversa);
                                                            switch (opcaoAcessaConversa) {
                                                                case 1:
                                                                    mostraConteudoDaConversa(id_conversa);
                                                                    break;
                                                                case 2:
                                                                    mandaMensagem(id_usuario, id_conversa);
                                                                    break;
                                                                case 3:
                                                                    mostraMensagensUsuario(id_conversa, id_usuario);
                                                                    removeMensagem();
                                                                    break;
                                                                case 4:
                                                                    adicionarUsuarioNaConversa(id_conversa);
                                                                    break;
                                                                case 0: 
                                                                    System.out.println("Voltando ao menu principal...");
                                                                    break; 
                                                                default:
                                                                    System.out.println("Opção inválida! Tente novamente.");
                                                                    break;
                                                            }
                                                        }while(opcaoAcessaConversa != 0);

                                                        break;
                                                    case 3:
                                                        criaConversaUsuario(id_usuario);
                                                        break;
                                                    case 4:
                                                        mostraConversasUsuario(id_usuario);
                                                        removeConversa();
                                                        break;
                                                    default:
                                                        break;
                                                }
                                            }while(opcaoChat != 0);
                                            break;
                                        case 4:
                                            deleteUsuario(id_usuario);
                                            break;
                                        case 0: 
                                            System.out.println("Voltando ao menu principal...");
                                            break; 
                                        default:
                                            System.out.println("Opção inválida! Tente novamente.");
                                            break; 
                                    }
                                }while(opcaoPrincipal != 0 && opcaoPrincipal != 4);
                                break; 
                            case 2: 
                                System.out.println("Cadastrar");
                                cadastraUsuario(sistema);
                                System.out.println("Usuario cadastrado com sucesso");
                                break; 
                            case 0: 
                                System.out.println("Voltando ao menu principal...");
                                break; 
                            default:
                                System.out.println("Opção inválida! Tente novamente.");
                                break; 
                        }
                    } while (opcaoLogin != 0); 
                    break;
                case 2: 
                    int opcaoAdmin = -1;
                    do {
                        opcaoAdmin = menuAdmin(scan); 
                        switch (opcaoAdmin) {
                            case 1: 
                                int opcaoUser = -1;
                                do {
                                    opcaoUser = menuUsuario(scan); 
                                    switch (opcaoUser) {
                                        case 1:
                                            System.out.println("Cadastrar Usuário");
                                            cadastraUsuario(sistema);
                                            break;
                                        case 2:
                                            System.out.println("Mostrar Usuários");
                                            mostraUsuarios();
                                            break;
                                        case 3:
                                            System.out.println("Excluir Usuário");
                                            deleteUsuarioAdmin();
                                            break;
                                        case 4:
                                            System.out.println("Mostra seguidores do Usuario mais seguido");
                                            subconsulta();
                                            break;
                                        case 0:
                                            System.out.println("Voltando ao menu de Administrador..."); 
                                            break; 
                                        default:
                                            System.out.println("Opção inválida! Tente novamente.");
                                    }
                                } while (opcaoUser != 0); 
                                break; 
                            case 2: 
                                int opcaoPost = -1;
                                do {
                                    opcaoPost = menuPost(scan);
                                    switch (opcaoPost) {
                                        case 1:
                                            System.out.println("Publicar Post");
                                            publicaPost(sistema);
                                            break;
                                        case 2:
                                            System.out.println("Mostrar Posts");
                                            mostraPosts();
                                            break;
                                        case 3:
                                            System.out.println("Excluir Post");
                                            mostraPosts();
                                            deletePost();
                                            break;
                                        case 0:
                                            System.out.println("Voltando ao menu de Administrador...");
                                            break;
                                        default:
                                            System.out.println("Opção inválida! Tente novamente.");
                                    }
                                } while (opcaoPost != 0);
                                break;
                            case 3: // Menu Mídia
                                int opcaoMidia = -1;
                                do {
                                    opcaoMidia = menuMidia(scan);
                                    switch (opcaoMidia) {
                                        case 1:
                                            System.out.println("Publica Mídia");
                                            publicaMidia(sistema);
                                            break;
                                        case 2:
                                            System.out.println("Mostrar Mídias");
                                            mostraMidias();
                                            break;
                                        case 3:
                                            System.out.println("Excluir Mídia");
                                            deleteMidia();
                                            break;
                                        case 0:
                                            System.out.println("Voltando ao menu de Administrador...");
                                            break;
                                        default:
                                            System.out.println("Opção inválida! Tente novamente.");
                                    }
                                } while (opcaoMidia != 0);
                                break;
                            case 4: // Menu Mensagem
                                int opcaoMensagem = -1;
                                do {
                                    opcaoMensagem = menuMensagem(scan);
                                    switch (opcaoMensagem) {
                                        case 1:
                                            System.out.println("Mandar Mensagem");
                                            mandaMensagemAdmin();
                                            break;
                                        case 2:
                                            System.out.println("Mostrar Mensagens");
                                            mostraMensagens();
                                            break;
                                        case 3:
                                            System.out.println("Excluir Mensagem");
                                            mostraMensagens();
                                            removeMensagem();
                                            break;
                                        case 0:
                                            System.out.println("Voltando ao menu de Administrador...");
                                            break;
                                        default:
                                            System.out.println("Opção inválida! Tente novamente.");
                                    }
                                } while (opcaoMensagem != 0);
                                break;
                            case 5: // Menu Conversa
                                int opcaoConversa = -1;
                                do {
                                    opcaoConversa = menuConversa(scan);
                                    switch (opcaoConversa) {
                                        case 1:
                                            System.out.println("Cadastrar Conversa");
                                            criaConversa();
                                            break;
                                        case 2:
                                            System.out.println("Mostrar Conversas");
                                            mostraConversas();
                                            break;
                                        case 3:
                                            System.out.println("Excluir Conversa");
                                            mostraConversas();
                                            removeConversa();
                                            break;
                                        case 0:
                                            System.out.println("Voltando ao menu de Administrador...");
                                            break;
                                        default:
                                            System.out.println("Opção inválida! Tente novamente.");
                                    }
                                } while (opcaoConversa != 0);
                                break;
                            case 0: 
                                System.out.println("Voltando ao menu principal...");
                                break;
                            default:
                                System.out.println("Opção inválida! Tente novamente.");
                        }
                    } while (opcaoAdmin != 0); 
                    break; 
                case 0:
                    System.out.println("Saindo do programa...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0); 
        System.out.println("Programa encerrado.");
    } catch (SQLException | ClassNotFoundException | InsertException | DeleteException | SelectException e) {
        e.printStackTrace();
    }
}

    public static int menuInicial(Scanner scan){
        System.out.println("-------- Inicio --------");
        System.out.println("1 - Usuario");
        System.out.println("2 - Admin");
        System.out.println("0 - Sair do Programa");
        System.out.print("Sua opção: ");
        return Integer.parseInt(scan.nextLine());
    }

    public static int menuAdmin(Scanner scan){
        System.out.println("-------- Admin --------");
        System.out.println("1 - Usuario");
        System.out.println("2 - Post");
        System.out.println("3 - Midia");
        System.out.println("4 - Mensagem");
        System.out.println("5 - Conversa");
        System.out.println("0 - Voltar");
        System.out.print("Sua opção: ");
        return Integer.parseInt(scan.nextLine());
    }

    public static int menuLogin(Scanner scan){
        System.out.println("-------- Login --------");
        System.out.println("1 - Entrar");
        System.out.println("2 - Cadastrar");
        System.out.println("0 - Voltar");
        System.out.print("Sua opção: ");
        return Integer.parseInt(scan.nextLine());
    }

    public static int menuPrincipal(Scanner scan){
        System.out.println("-------- Menu Principal --------");
        System.out.println("1 - Feed");
        System.out.println("2 - Perfil");
        System.out.println("3 - Chat");
        System.out.println("4 - Excluir Conta");
        System.out.println("0 - Sair");
        System.out.print("Sua opção: ");
        return Integer.parseInt(scan.nextLine());
    }

    public static int feed(Scanner scan){
        System.out.println("-------- Feed ---------");
        System.out.println("1 - Ver Posts");
        System.out.println("2 - Curtir");
        System.out.println("3 - Comentar");
        System.out.println("4 - Seguir");
        System.out.println("0 - Voltar");
        System.out.print("Sua opção: ");
        return Integer.parseInt(scan.nextLine());
    }

    public static int perfil(Scanner scan){
        System.out.println("-------- Perfil --------");
        System.out.println("1 - Mostrar seus Posts");
        System.out.println("2 - Publicar um Post");
        System.out.println("3 - Excluir um Post");
        System.out.println("0 - Voltar");
        System.out.print("Sua opção: ");
        return Integer.parseInt(scan.nextLine());
    }

    public static int chat(Scanner scan){
        System.out.println("-------- Chat --------");
        System.out.println("1 - Mostrar Conversas");
        System.out.println("2 - Acessar Conversa");
        System.out.println("3 - Nova Conversa");
        System.out.println("4 - Remover Conversa");
        System.out.println("0 - Voltar");
        System.out.print("Sua opção: ");
        return Integer.parseInt(scan.nextLine());
    }

    public static int acessaConversa(Scanner scan, int conversa){
        System.out.println("------------------------");
        System.out.println("1 - Mostrar conversa");
        System.out.println("2 - Mandar Mensagem");
        System.out.println("3 - Excluir Mensagem");
        System.out.println("4 - Adicionar Usuario numa conversa");
        System.out.println("0 - Voltar");
        System.out.print("Sua opção: ");
        return Integer.parseInt(scan.nextLine());
    }

    public static int menuUsuario(Scanner scan){
        System.out.println("-------- Usuario --------");
        System.out.println("1 - Cadastrar Usuario");
        System.out.println("2 - Mostrar Usuarios");
        System.out.println("3 - Excluir Usuario");
        System.out.println("4 - Mostrar seguidores do usuario mais seguido");
        System.out.println("0 - Voltar ao Menu Principal");
        System.out.print("Sua opção: ");
        return Integer.parseInt(scan.nextLine());
    }

    public static int menuPost(Scanner scan){
        System.out.println("-------- Post --------");
        System.out.println("1 - Publicar Post");
        System.out.println("2 - Mostrar Posts");
        System.out.println("3 - Excluir Post");
        System.out.println("0 - Voltar ao Menu Principal");
        System.out.print("Sua opção: ");
        return Integer.parseInt(scan.nextLine());
    }

    public static int menuMidia(Scanner scan){
        System.out.println("-------- Midia --------");
        System.out.println("1 - Publica Midia");
        System.out.println("2 - Mostrar Midias");
        System.out.println("3 - Excluir Midia"); //estranho
        System.out.println("0 - Voltar ao Menu Principal");
        System.out.print("Sua opção: ");
        return Integer.parseInt(scan.nextLine());
    }

    public static int menuMensagem(Scanner scan){
        System.out.println("-------- Mensagem --------");
        System.out.println("1 - Cadastrar Mensagem");
        System.out.println("2 - Mostrar Mensagens");
        System.out.println("3 - Excluir Mensagem");
        System.out.println("0 - Voltar ao Menu Principal");
        System.out.print("Sua opção: ");
        return Integer.parseInt(scan.nextLine());
    }   

    public static int menuConversa(Scanner scan){
        System.out.println("-------- Conversa --------");
        System.out.println("1 - Cadastrar Conversa");
        System.out.println("2 - Mostrar Conversas");
        System.out.println("3 - Excluir Conversa");
        System.out.println("0 - Voltar ao Menu Principal");
        System.out.print("Sua opção: ");
        return Integer.parseInt(scan.nextLine());
    }

    public static Usuario login(Sistema sistema) throws SQLException, ClassNotFoundException, SelectException {
        try{
            System.out.println("Digite seu e-mail: ");
            String email = scan.nextLine();
            System.out.println("Digite sua senha: ");
            String senha = scan.nextLine();

            return sistema.login(email, senha);
        }catch(SQLException e){
            throw new SelectException("Erro no login");
        }
    }

    public static void cadastraUsuario(Sistema sistema) throws SQLException, ClassNotFoundException, InsertException {
        Usuario usuario = new Usuario();
        System.out.println("Digite o nome do usuário:");
        String nome = scan.nextLine();
        System.out.println("Digite o email do usuário:");
        String email = scan.nextLine(); 
        System.out.println("Digite a senha do usuário:");
        String senha = scan.nextLine();
        System.out.println("Digite a descrição do usuário (opcional):");
        String descricao = scan.nextLine();

        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha); 
        usuario.setDescricao(descricao);

        sistema.insereUsuario(usuario);
    }

    public static void mostraUsuarios() throws SQLException, ClassNotFoundException, SelectException{
        System.out.println("Id - Nome - Descrição");
        for (Usuario usuario : sistema.mostraUsuarios()) {
            System.out.println(usuario);
        }
    }

    public static void subconsulta() throws SQLException, ClassNotFoundException, SelectException{
        System.out.println("Id - Nome - Descrição");
        for (Usuario usuario : sistema.subconsulta()) {
            System.out.println(usuario);
        }
    }

    public static void segueUsuario(int id_seguidor)throws SQLException, ClassNotFoundException, InsertException, SelectException {
        int id_seguido = 0;
        mostraUsuarios();
        System.out.println("Digite o ID do usuario que quer seguir:");
        id_seguido = Integer.parseInt(scan.nextLine());
        if(id_seguidor == id_seguido){
            throw new InsertException("Erro: Impossivel seguir a propria conta");
        }

        sistema.segueUsuario(id_seguidor, id_seguido);
    }

    public static void curtePost(int id_usuario) throws SQLException, ClassNotFoundException, InsertException, SelectException {
        int id_post = 0;

        mostraPosts();
        System.out.println("Digite o ID do post que quer curtir:");
        id_post = Integer.parseInt(scan.nextLine());
        
        sistema.curtePost(id_post, id_usuario);
    }

    public static void deleteUsuarioAdmin()throws SQLException, ClassNotFoundException, DeleteException, SelectException {
        try{
            mostraUsuarios();
            System.out.println("Digite o ID do usuário a ser excluído:");
            int id = Integer.parseInt(scan.nextLine());
            deleteUsuario(id);
        }catch(SQLException e){
            throw new DeleteException("Erro ao deletar usuario");
        }
    }    

    public static void deleteUsuario(int id_usuario) throws SQLException, ClassNotFoundException, DeleteException, SelectException {
         try{
            for(int i : sistema.removePostUsuario(id_usuario)){
                
                sistema.removePost(i);
            }
            sistema.removeUsuario(id_usuario);
         }catch(SQLException e){
            throw new DeleteException("Erro ao excluir seu usuario");
         }
    }

    public static void publicaPost(Sistema sistema) throws SQLException, ClassNotFoundException, InsertException, SelectException {
        Post post = new Post();
        mostraUsuarios();
        System.out.println("Digite o ID do usuário que está publicando:");
        int id_usuario = Integer.parseInt(scan.nextLine());
        mostraMidias();
        System.out.println("Digite o ID da midia:");
        int id_midia = Integer.parseInt(scan.nextLine());
        List<Midia> aux = sistema.mostraMidias();
        int cont = 0;
        for(Midia midia : aux){
            if(id_midia == midia.getId_midia()){
                break;
            }
            else{
                cont++;
                if(cont == aux.size()){
                    throw new InsertException("Midia nao encontrada");
                }
            }
        }
        System.out.println("Digite a legenda do post (opcional):");
        String legenda = scan.nextLine();

        post.setId_usuario(id_usuario);
        post.setData_hora(new Timestamp(System.currentTimeMillis()));
        post.setLegenda(legenda);

        sistema.inserePost(post);
        sistema.possuir(id_midia);
    }

    public static void publicaPostUsuario(Sistema sistema, int id_usuario) throws SQLException, ClassNotFoundException, SelectException, InsertException {
        Post post = new Post();
        System.out.println("Deseja Carregar uma nova midia? (1 - Sim | 2 - Nao)");
        if(Integer.parseInt(scan.nextLine()) == 1){
            publicaMidia(sistema);
        }
        mostraMidias();
        System.out.println("Digite o ID da mídia: ");
        int id_midia = Integer.parseInt(scan.nextLine());
        List<Midia> aux = sistema.mostraMidias();
        int cont = 0;
        for(Midia midia : aux){
            if(id_midia == midia.getId_midia()){
                break;
            }
            else{
                cont++;
                if(cont == aux.size()){
                    throw new InsertException("Midia nao encontrada");
                }
            }
        }
        System.out.println("Digite a legenda do post (opcional): ");
        String legenda = scan.nextLine();

        post.setId_usuario(id_usuario);
        post.setData_hora(new Timestamp(System.currentTimeMillis()));
        post.setLegenda(legenda);

        sistema.inserePost(post);
        sistema.possuir(id_midia);
    }

    public static void deletePost() throws SQLException, ClassNotFoundException, DeleteException, SelectException {
        System.out.println("Digite o ID do post a ser excluído:");
        int id = Integer.parseInt(scan.nextLine());
        sistema.removePost(id);
    }

    public static void mostraPosts() throws SQLException, ClassNotFoundException, SelectException {
        System.out.println("Id - Usuario - Data/Hora - Legenda");
        for (Post post : sistema.mostraPosts()) {
            System.out.println(post);
        }
    }

    public static void mostraPostsUsuario(int id_usuario) throws SQLException, ClassNotFoundException, SelectException {
        System.out.println("Id - Usuario - Data/Hora - Legenda");
        for (Post post : sistema.buscaPostsUsuario(id_usuario)){
            System.out.println(post);
        }
    }

    public static void publicaMidia(Sistema sistema) throws SQLException, ClassNotFoundException, InsertException, SelectException {
        Midia midia = new Midia();
        System.out.println("Digite o tamanho da mídia:");
        int tamanho = Integer.parseInt(scan.nextLine());
        System.out.println("Digite o tipo da mídia (1 para imagem, 2 para vídeo):");
        int tipo = Integer.parseInt(scan.nextLine());
        int duracao = 0;
        if(tipo == 2) {
            System.out.println("Digite a duração da mídia (em segundos, opcional):");
            String duracaoInput = scan.nextLine();
            if (!duracaoInput.isEmpty()) {
                duracao = Integer.parseInt(duracaoInput);
            }
        }
        midia.setTamanho(tamanho);
        midia.setTipo(tipo);
        midia.setDuracao(duracao);

        sistema.insereMidia(midia);
    }

    public static void deleteMidia() throws SQLException, ClassNotFoundException, DeleteException, SelectException {
        mostraMidias();
        System.out.println("Digite o ID da mídia a ser excluída:");
        int id = Integer.parseInt(scan.nextLine());
        sistema.removeMidia(id);
    }

    public static void mostraMidias() throws SQLException, ClassNotFoundException, SelectException {
        System.out.println("Id - Tamanho - Tipo - (Duração)");
        for (Midia midia : sistema.mostraMidias()) {
            System.out.println(midia);
        }
    }

    public static void mandaMensagem(int id_usuario, int id_conversa) throws SQLException, ClassNotFoundException, InsertException, SelectException {
        try {    
            Mensagem mensagem = new Mensagem();

            if(sistema.checkParticipation(id_conversa, id_usuario) == false){
                throw new InsertException("Usuario fora da conversa");
            }

            int id_post = 0;
            int id_midia = 0;
            mostraPosts();
            System.out.println("Digite o ID do post relacionado (opcional):");
            String id_post_input = scan.nextLine();
            if (!id_post_input.isEmpty()) {
                id_post = Integer.parseInt(id_post_input);
            }

            if(id_post == 0){
                mostraMidias();
                System.out.println("Digite o ID da mídia relacionada (opcional):");
                String id_midia_input = scan.nextLine();
                if (!id_midia_input.isEmpty()) {
                    id_midia = Integer.parseInt(id_midia_input);
                }
            }

            String texto = "";
            System.out.println("Digite o texto da mensagem:");
            texto = scan.nextLine();

            if(id_post == 0 && id_midia == 0 && texto.isEmpty()){
                throw new InsertException("Mensagem sem nenhum parametro");
            }

            Timestamp data_hora = new Timestamp(System.currentTimeMillis());

            mensagem.setId_usuario(id_usuario);
            mensagem.setId_post(id_post);
            mensagem.setId_midia(id_midia);
            mensagem.setTexto(texto);
            mensagem.setData_hora(data_hora);
            mensagem.setEntregue(true);
            mensagem.setVisualizado(false);

            sistema.insereMensagem(mensagem);
            sistema.recebeMensagem(id_conversa); // recebe a mensagem mais recente
        }catch(SQLException e){
            throw new InsertException("Erro ao enviar mensagem");
        }
    }

    public static void mandaMensagemAdmin() throws SQLException, ClassNotFoundException, InsertException, SelectException {
        try {    
            Mensagem mensagem = new Mensagem();
            mostraUsuarios();
            System.out.println("Digite o ID do usuário que está enviando a mensagem:");
            int id_usuario = Integer.parseInt(scan.nextLine());

            mostraConversas();
            System.out.println("Digite o ID da conversa:");
            int id_conversa = Integer.parseInt(scan.nextLine());

            if(sistema.checkParticipation(id_conversa, id_usuario) == false){
                throw new InsertException("Usuario nao esta na conversa");
            }

            int id_post = 0;
            int id_midia = 0;
            mostraPosts();
            System.out.println("Digite o ID do post relacionado (opcional):");
            String id_post_input = scan.nextLine();
            if (!id_post_input.isEmpty()) {
                id_post = Integer.parseInt(id_post_input);
            }

            if(id_post == 0){
                mostraMidias();
                System.out.println("Digite o ID da mídia relacionada (opcional):");
                String id_midia_input = scan.nextLine();
                if (!id_midia_input.isEmpty()) {
                    id_midia = Integer.parseInt(id_midia_input);
                }
            }

            String texto = "";
            System.out.println("Digite o texto da mensagem:");
            texto = scan.nextLine();

            if(id_post == 0 && id_midia == 0 && texto.isEmpty()){
                throw new InsertException("Mensagem sem nenhum parametro");
            }

            Timestamp data_hora = new Timestamp(System.currentTimeMillis());

            mensagem.setId_usuario(id_usuario);
            mensagem.setId_post(id_post);
            mensagem.setId_midia(id_midia);
            mensagem.setTexto(texto);
            mensagem.setData_hora(data_hora);
            mensagem.setEntregue(true);
            mensagem.setVisualizado(false);

            sistema.insereMensagem(mensagem);
            sistema.recebeMensagem(id_conversa); // recebe a mensagem mais recente
        }catch(SQLException e){
            throw new InsertException("Erro ao enviar mensagem");
        }
    }

    public static void removeMensagem() throws SQLException, ClassNotFoundException, DeleteException, SelectException {
        System.out.println("Digite o ID da mensagem a ser excluída:");
        int id_mensagem = Integer.parseInt(scan.nextLine());
        sistema.removeMensagem(id_mensagem);
    }

    public static void mostraMensagens() throws SQLException, ClassNotFoundException, SelectException {
        System.out.println("Id - Usuário - Texto - Data/Hora - Post - Midia");
        for (Mensagem mensagem : sistema.mostraMensagens()) {
            System.out.println(mensagem);
        }
    }

    public static void criaConversa() throws SQLException, ClassNotFoundException, InsertException, SelectException {
        String nomeConversa = "";
        do{
            System.out.println("Digite o nome da conversa:");
            nomeConversa = scan.nextLine();
        }while(nomeConversa.isEmpty());

        sistema.insereConversa(nomeConversa);
    }

    public static void criaConversaUsuario(int id_usuario) throws SQLException, ClassNotFoundException, InsertException, SelectException {
        criaConversa();
        sistema.insereConversaUsuario(id_usuario);
    }

    public static void removeConversa() throws SQLException, ClassNotFoundException, DeleteException, SelectException {
        System.out.println("Digite o ID da conversa a ser excluída:");
        int id_conversa = Integer.parseInt(scan.nextLine());
        for(int i : sistema.mensagensConversa(id_conversa)){
            sistema.removeMensagem(i);
        }
        sistema.removeConversa(id_conversa);
    }

    public static void mostraConversas() throws SQLException, ClassNotFoundException, SelectException {
        System.out.println("Id - Nome");
        for (Conversa conversa : sistema.mostraConversas()) {
            System.out.println(conversa);
        }
    }

    public static void mostraConversasUsuario(int id_usuario) throws SQLException, ClassNotFoundException, SelectException {
        System.out.println("Id - Nome");
        for (Conversa conversa : sistema.mostraConversasUsuario(id_usuario)) {
            System.out.println(conversa);
        }
    }

    public static void mostraConteudoDaConversa(int id_conversa) throws SQLException, ClassNotFoundException, SelectException{
        
        for(Mensagem m : sistema.mostraConteudoDaConversa(id_conversa)){
            System.out.println(m);
        }
    }

    public static void mostraMensagensUsuario(int id_conversa, int id_usuario) throws SQLException, ClassNotFoundException, SelectException{
        
        for(Mensagem m : sistema.userMessages(id_conversa, id_usuario)){
            System.out.println(m);
        }
    }

    public static void adicionarUsuarioNaConversa(int id_conversa) throws SQLException, ClassNotFoundException, InsertException, SelectException {
        mostraUsuarios();
        System.out.println("Digite o ID do usuário que quer adicionar:");
        int id_usuario = Integer.parseInt(scan.nextLine());

        sistema.participarConversa(id_conversa, id_usuario);
    }

    public static void comentaPost(int id_usuario) throws SQLException, ClassNotFoundException, InsertException, SelectException{
        mostraPosts();
        System.out.println("Digite o id do post:");
        int id_post = Integer.parseInt(scan.nextLine());
        System.out.println("Conteudo do comentario:");
        String texto = scan.nextLine();
        if(texto.isEmpty()){
            throw new InsertException("Comentário vazio inválido");
        }
        Timestamp data_hora = new Timestamp(System.currentTimeMillis());

        sistema.comentar(id_post, id_usuario, texto, data_hora);

    }

    

}
