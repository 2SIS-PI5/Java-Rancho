package sptech.school.projeto_rancho.service;

import sptech.school.projeto_rancho.dto.auth.CadastroRequest;
import sptech.school.projeto_rancho.dto.auth.LoginResponse;
import sptech.school.projeto_rancho.dto.auth.UserDTO;
import sptech.school.projeto_rancho.model.Usuario;
import sptech.school.projeto_rancho.repository.UsuarioRepository;
import sptech.school.projeto_rancho.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    private final Map<String, String[]> codigosRecuperacao = new HashMap<>();

    public LoginResponse login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new RuntimeException("Senha incorreta.");
        }

        String token = jwtUtil.gerarToken(
            usuario.getEmail(),
            usuario.getId(),
            usuario.getRole()
        );

        UserDTO userDTO = new UserDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getRole()
        );

        return new LoginResponse(token, userDTO);
    }

    public Usuario cadastrar(CadastroRequest req) {
        if (usuarioRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado no sistema.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(req.getNome());
        usuario.setEmail(req.getEmail());
        usuario.setSenha(passwordEncoder.encode(req.getSenha()));
        usuario.setRole(req.getRole() != null ? req.getRole() : "GESTOR");

        return usuarioRepository.save(usuario);
    }

    public void enviarCodigoRecuperacao(String email) {
        if (!usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("E-mail não encontrado no sistema.");
        }

        String codigo = String.format("%06d", new Random().nextInt(999999));
        String expiracao = LocalDateTime.now().plusMinutes(15).toString();

        codigosRecuperacao.put(email, new String[]{codigo, expiracao});

        System.out.println("========================================");
        System.out.println("CÓDIGO DE RECUPERAÇÃO PARA: " + email);
        System.out.println("CÓDIGO: " + codigo);
        System.out.println("Expira em: " + expiracao);
        System.out.println("========================================");
    }

    public void verificarCodigo(String email, String codigo) {
        String[] dados = codigosRecuperacao.get(email);
        if (dados == null) {
            throw new RuntimeException("Nenhum código enviado para este e-mail.");
        }

        String codigoSalvo  = dados[0];
        String expiracao    = dados[1];

        if (LocalDateTime.now().isAfter(LocalDateTime.parse(expiracao))) {
            codigosRecuperacao.remove(email);
            throw new RuntimeException("Código expirado. Solicite um novo.");
        }

        if (!codigoSalvo.equals(codigo)) {
            throw new RuntimeException("Código inválido.");
        }
    }

    public void redefinirSenha(String email, String novaSenha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);

        codigosRecuperacao.remove(email);
    }
}
