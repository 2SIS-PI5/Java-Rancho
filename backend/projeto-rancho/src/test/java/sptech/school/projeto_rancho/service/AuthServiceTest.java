package sptech.school.projeto_rancho.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.projeto_rancho.dto.auth.CadastroRequest;
import sptech.school.projeto_rancho.dto.auth.LoginResponse;
import sptech.school.projeto_rancho.model.Usuario;
import sptech.school.projeto_rancho.repository.UsuarioRepository;
import sptech.school.projeto_rancho.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService — Testes Unitários")
class AuthServiceTest {

    @Mock UsuarioRepository usuarioRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtUtil jwtUtil;

    @InjectMocks AuthService service;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Admin");
        usuario.setEmail("admin@rancho.com");
        usuario.setSenha("$2a$encoded");
        usuario.setRole("GESTOR");
    }

    // ── login ──────────────────────────────────────

    @Test
    @DisplayName("login() retorna LoginResponse com token quando credenciais corretas")
    void login_credenciaisCorretas_retornaToken() {
        when(usuarioRepository.findByEmail("admin@rancho.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("123456", "$2a$encoded")).thenReturn(true);
        when(jwtUtil.gerarToken("admin@rancho.com", 1, "GESTOR")).thenReturn("jwt.token.aqui");

        LoginResponse response = service.login("admin@rancho.com", "123456");

        assertThat(response.getToken()).isEqualTo("jwt.token.aqui");
        assertThat(response.getUser().getEmail()).isEqualTo("admin@rancho.com");
        assertThat(response.getUser().getRole()).isEqualTo("GESTOR");
    }

    @Test
    @DisplayName("login() lança exceção quando usuário não existe")
    void login_usuarioNaoEncontrado_lancaExcecao() {
        when(usuarioRepository.findByEmail("inexistente@rancho.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.login("inexistente@rancho.com", "qualquer"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("não encontrado");
    }

    @Test
    @DisplayName("login() lança exceção quando senha incorreta")
    void login_senhaIncorreta_lancaExcecao() {
        when(usuarioRepository.findByEmail("admin@rancho.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("errada", "$2a$encoded")).thenReturn(false);

        assertThatThrownBy(() -> service.login("admin@rancho.com", "errada"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Senha incorreta");
    }

    // ── cadastrar ──────────────────────────────────

    @Test
    @DisplayName("cadastrar() salva novo usuário com role GESTOR por padrão")
    void cadastrar_emailNovo_salvaComGestor() {
        CadastroRequest req = new CadastroRequest();
        req.setNome("Novo Gestor");
        req.setEmail("novo@rancho.com");
        req.setSenha("senha123");
        req.setRole(null); // role não informada → deve usar "GESTOR"

        when(usuarioRepository.existsByEmail("novo@rancho.com")).thenReturn(false);
        when(passwordEncoder.encode("senha123")).thenReturn("$2a$encoded_novo");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario result = service.cadastrar(req);

        assertThat(result.getRole()).isEqualTo("GESTOR");
        assertThat(result.getEmail()).isEqualTo("novo@rancho.com");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("cadastrar() respeita role informada no request")
    void cadastrar_comRole_salvaRoleEspecificada() {
        CadastroRequest req = new CadastroRequest();
        req.setNome("Admin Master");
        req.setEmail("master@rancho.com");
        req.setSenha("senha123");
        req.setRole("ADMIN");

        when(usuarioRepository.existsByEmail("master@rancho.com")).thenReturn(false);
        when(passwordEncoder.encode("senha123")).thenReturn("$2a$encoded");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario result = service.cadastrar(req);

        assertThat(result.getRole()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("cadastrar() lança exceção quando e-mail já existe")
    void cadastrar_emailDuplicado_lancaExcecao() {
        CadastroRequest req = new CadastroRequest();
        req.setEmail("admin@rancho.com");
        req.setSenha("qualquer");

        when(usuarioRepository.existsByEmail("admin@rancho.com")).thenReturn(true);

        assertThatThrownBy(() -> service.cadastrar(req))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("já cadastrado");

        verify(usuarioRepository, never()).save(any());
    }

    // ── enviarCodigoRecuperacao ────────────────────

    @Test
    @DisplayName("enviarCodigoRecuperacao() lança exceção quando e-mail não existe")
    void enviarCodigo_emailInexistente_lancaExcecao() {
        when(usuarioRepository.existsByEmail("nao@existe.com")).thenReturn(false);

        assertThatThrownBy(() -> service.enviarCodigoRecuperacao("nao@existe.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("não encontrado");
    }

    @Test
    @DisplayName("enviarCodigoRecuperacao() não lança exceção quando e-mail existe")
    void enviarCodigo_emailExistente_executaSemExcecao() {
        when(usuarioRepository.existsByEmail("admin@rancho.com")).thenReturn(true);

        assertThatCode(() -> service.enviarCodigoRecuperacao("admin@rancho.com"))
                .doesNotThrowAnyException();
    }

    // ── verificarCodigo ────────────────────────────

    @Test
    @DisplayName("verificarCodigo() lança exceção quando nenhum código foi enviado")
    void verificarCodigo_semCodigoEnviado_lancaExcecao() {
        assertThatThrownBy(() -> service.verificarCodigo("sem@codigo.com", "123456"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Nenhum código");
    }

    @Test
    @DisplayName("verificarCodigo() lança exceção quando código é inválido")
    void verificarCodigo_codigoErrado_lancaExcecao() {
        // Envia código primeiro
        when(usuarioRepository.existsByEmail("admin@rancho.com")).thenReturn(true);
        service.enviarCodigoRecuperacao("admin@rancho.com");

        // Tenta verificar com código errado
        assertThatThrownBy(() -> service.verificarCodigo("admin@rancho.com", "000000"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Código inválido");
    }

    // ── redefinirSenha ─────────────────────────────

    @Test
    @DisplayName("redefinirSenha() atualiza a senha do usuário")
    void redefinirSenha_usuarioExistente_atualizaSenha() {
        when(usuarioRepository.findByEmail("admin@rancho.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("novaSenha123")).thenReturn("$2a$nova");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        assertThatCode(() -> service.redefinirSenha("admin@rancho.com", "novaSenha123"))
                .doesNotThrowAnyException();

        verify(usuarioRepository).save(any());
    }

    @Test
    @DisplayName("redefinirSenha() lança exceção quando usuário não existe")
    void redefinirSenha_usuarioNaoEncontrado_lancaExcecao() {
        when(usuarioRepository.findByEmail("nao@existe.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.redefinirSenha("nao@existe.com", "qualquer"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("não encontrado");
    }
}
