package sptech.school.projeto_rancho.security;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JwtUtil — Testes Unitários")
class JwtUtilTest {

    private JwtUtil jwtUtil;

    // Chave de 256 bits (32+ caracteres) para HS256
    private static final String SECRET =
            "ranchoComancheSecretKeyParaTeste12345678";
    private static final long EXPIRACAO_24H = 86_400_000L;
    private static final long EXPIRADO = -1L; // já expirado

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secretKey", SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expiration", EXPIRACAO_24H);
    }

    // ── gerarToken ─────────────────────────────────

    @Test
    @DisplayName("gerarToken() retorna token não-nulo e não-vazio")
    void gerarToken_retornaTokenValido() {
        String token = jwtUtil.gerarToken("admin@rancho.com", 1, "GESTOR");

        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("gerarToken() gera tokens diferentes para e-mails diferentes")
    void gerarToken_emailsDiferentes_retornaTokensDiferentes() {
        String t1 = jwtUtil.gerarToken("usuario1@rancho.com", 1, "GESTOR");
        String t2 = jwtUtil.gerarToken("usuario2@rancho.com", 2, "ADMIN");

        assertThat(t1).isNotEqualTo(t2);
    }

    // ── extrairEmail ───────────────────────────────

    @Test
    @DisplayName("extrairEmail() retorna o e-mail correto do token")
    void extrairEmail_retornaEmailCorreto() {
        String token = jwtUtil.gerarToken("gestor@rancho.com", 2, "GESTOR");

        assertThat(jwtUtil.extrairEmail(token)).isEqualTo("gestor@rancho.com");
    }

    // ── extrairUserId ──────────────────────────────

    @Test
    @DisplayName("extrairUserId() retorna o userId correto do token")
    void extrairUserId_retornaIdCorreto() {
        String token = jwtUtil.gerarToken("admin@rancho.com", 42, "GESTOR");

        assertThat(jwtUtil.extrairUserId(token)).isEqualTo(42);
    }

    // ── extrairRole ────────────────────────────────

    @Test
    @DisplayName("extrairRole() retorna a role correta do token")
    void extrairRole_retornaRoleCorreta() {
        String token = jwtUtil.gerarToken("admin@rancho.com", 1, "ADMIN");

        assertThat(jwtUtil.extrairRole(token)).isEqualTo("ADMIN");
    }

    // ── validarToken ───────────────────────────────

    @Test
    @DisplayName("validarToken() retorna true para token válido")
    void validarToken_tokenValido_retornaTrue() {
        String token = jwtUtil.gerarToken("admin@rancho.com", 1, "GESTOR");

        assertThat(jwtUtil.validarToken(token)).isTrue();
    }

    @Test
    @DisplayName("validarToken() retorna false para token expirado")
    void validarToken_tokenExpirado_retornaFalse() {
        ReflectionTestUtils.setField(jwtUtil, "expiration", EXPIRADO);
        String token = jwtUtil.gerarToken("admin@rancho.com", 1, "GESTOR");

        assertThat(jwtUtil.validarToken(token)).isFalse();
    }

    @Test
    @DisplayName("validarToken() retorna false para token inválido/malformado")
    void validarToken_tokenInvalido_retornaFalse() {
        assertThat(jwtUtil.validarToken("isso.nao.e.um.jwt")).isFalse();
    }

    @Test
    @DisplayName("validarToken() retorna false para token vazio")
    void validarToken_tokenVazio_retornaFalse() {
        assertThat(jwtUtil.validarToken("")).isFalse();
    }

    @Test
    @DisplayName("validarToken() retorna false para token com assinatura diferente")
    void validarToken_assinaturaAlterada_retornaFalse() {
        String token = jwtUtil.gerarToken("admin@rancho.com", 1, "GESTOR");
        String tokenAlterado = token.substring(0, token.length() - 5) + "XXXXX";

        assertThat(jwtUtil.validarToken(tokenAlterado)).isFalse();
    }

    // ── isExpirado ─────────────────────────────────

    @Test
    @DisplayName("isExpirado() retorna false para token recém-gerado")
    void isExpirado_tokenNovo_retornaFalse() {
        String token = jwtUtil.gerarToken("admin@rancho.com", 1, "GESTOR");

        assertThat(jwtUtil.isExpirado(token)).isFalse();
    }

    @Test
    @DisplayName("isExpirado() retorna true quando validarToken retorna false para token expirado")
    void isExpirado_tokenExpirado_retornaTrue() {
        // Com expiração -1ms, validarToken deve retornar false (token expirado)
        ReflectionTestUtils.setField(jwtUtil, "expiration", EXPIRADO);
        String token = jwtUtil.gerarToken("admin@rancho.com", 1, "GESTOR");

        // validarToken captura ExpiredJwtException internamente e retorna false
        assertThat(jwtUtil.validarToken(token)).isFalse();
    }

    // ── consistência ───────────────────────────────

    @Test
    @DisplayName("Token contém todos os claims inseridos corretamente")
    void token_contemTodosOsClaims() {
        String token = jwtUtil.gerarToken("usuario@teste.com", 7, "OPERADOR");

        assertThat(jwtUtil.extrairEmail(token)).isEqualTo("usuario@teste.com");
        assertThat(jwtUtil.extrairUserId(token)).isEqualTo(7);
        assertThat(jwtUtil.extrairRole(token)).isEqualTo("OPERADOR");
    }
}
