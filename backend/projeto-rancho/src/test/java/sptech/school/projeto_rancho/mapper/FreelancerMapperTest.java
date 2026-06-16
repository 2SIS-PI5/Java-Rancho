package sptech.school.projeto_rancho.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sptech.school.projeto_rancho.dto.FreelancerDTO;
import sptech.school.projeto_rancho.model.Freelancer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("FreelancerMapper — Testes Unitários")
class FreelancerMapperTest {

    private final FreelancerMapper mapper = new FreelancerMapper();

    // ── toDTO ──────────────────────────────────────

    @Test
    @DisplayName("toDTO() mapeia todos os campos da entidade para o DTO")
    void toDTO_entidadeCompleta_mapeiaCorretamente() {
        Freelancer f = new Freelancer();
        f.setId(1L);
        f.setNome("João Silva");
        f.setCpf("111.222.333-44");
        f.setTelefone("(11) 9999-9999");
        f.setEmail("joao@email.com");
        f.setEspecialidade("Garçom");
        f.setValorHora(BigDecimal.valueOf(25.0));
        f.setStatus("ativo");
        f.setObservacoes("Pontual");
        f.setCep("09834-203");
        f.setLogradouro("Rua A");
        f.setNumero("100");
        f.setComplemento("Apto 1");
        f.setBairro("Centro");
        f.setCidade("Santo André");
        f.setEstado("SP");
        f.setLatitude(-23.5);
        f.setLongitude(-46.8);
        f.setDistanciaKm(5.0);
        f.setPixChave("joao@pix.com");
        f.setCriadoEm(LocalDateTime.of(2025, 1, 1, 10, 0));

        FreelancerDTO dto = mapper.toDTO(f);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNome()).isEqualTo("João Silva");
        assertThat(dto.getCpf()).isEqualTo("111.222.333-44");
        assertThat(dto.getTelefone()).isEqualTo("(11) 9999-9999");
        assertThat(dto.getEmail()).isEqualTo("joao@email.com");
        assertThat(dto.getEspecialidade()).isEqualTo("Garçom");
        assertThat(dto.getValorHora()).isEqualByComparingTo(BigDecimal.valueOf(25.0));
        assertThat(dto.getStatus()).isEqualTo("ativo");
        assertThat(dto.getObservacoes()).isEqualTo("Pontual");
        assertThat(dto.getCep()).isEqualTo("09834-203");
        assertThat(dto.getLogradouro()).isEqualTo("Rua A");
        assertThat(dto.getNumero()).isEqualTo("100");
        assertThat(dto.getComplemento()).isEqualTo("Apto 1");
        assertThat(dto.getBairro()).isEqualTo("Centro");
        assertThat(dto.getCidade()).isEqualTo("Santo André");
        assertThat(dto.getEstado()).isEqualTo("SP");
        assertThat(dto.getLatitude()).isEqualTo(-23.5);
        assertThat(dto.getLongitude()).isEqualTo(-46.8);
        assertThat(dto.getDistanciaKm()).isEqualTo(5.0);
        assertThat(dto.getPixChave()).isEqualTo("joao@pix.com");
    }

    @Test
    @DisplayName("toDTO() retorna null quando entidade é null")
    void toDTO_entidadeNull_retornaNull() {
        assertThat(mapper.toDTO(null)).isNull();
    }

    // ── toEntity ───────────────────────────────────

    @Test
    @DisplayName("toEntity() mapeia todos os campos do DTO para a entidade")
    void toEntity_dtoCompleto_mapeiaCorretamente() {
        FreelancerDTO dto = new FreelancerDTO();
        dto.setNome("Maria Souza");
        dto.setCpf("222.333.444-55");
        dto.setTelefone("(11) 8888-8888");
        dto.setEmail("maria@email.com");
        dto.setEspecialidade("Cozinheira");
        dto.setValorHora(BigDecimal.valueOf(30.0));
        dto.setStatus("ativo");
        dto.setObservacoes("Experiência em buffet");
        dto.setCep("09000-000");
        dto.setCidade("São Paulo");
        dto.setEstado("SP");
        dto.setPixChave("maria@pix.com");

        Freelancer f = mapper.toEntity(dto);

        assertThat(f.getNome()).isEqualTo("Maria Souza");
        assertThat(f.getCpf()).isEqualTo("222.333.444-55");
        assertThat(f.getEmail()).isEqualTo("maria@email.com");
        assertThat(f.getEspecialidade()).isEqualTo("Cozinheira");
        assertThat(f.getStatus()).isEqualTo("ativo");
        assertThat(f.getCidade()).isEqualTo("São Paulo");
        assertThat(f.getPixChave()).isEqualTo("maria@pix.com");
        assertThat(f.getCriadoEm()).isNotNull();
    }

    @Test
    @DisplayName("toEntity() usa 'ativo' como status padrão quando não informado")
    void toEntity_semStatus_usaAtivoPorPadrao() {
        FreelancerDTO dto = new FreelancerDTO();
        dto.setNome("Pedro");
        dto.setStatus(null);

        Freelancer f = mapper.toEntity(dto);

        assertThat(f.getStatus()).isEqualTo("ativo");
    }

    @Test
    @DisplayName("toEntity() retorna null quando DTO é null")
    void toEntity_dtoNull_retornaNull() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    // ── atualizarEntidade ──────────────────────────

    @Test
    @DisplayName("atualizarEntidade() sobrescreve os campos da entidade com dados do DTO")
    void atualizarEntidade_atualizaCampos() {
        Freelancer f = new Freelancer();
        f.setNome("Nome Antigo");
        f.setStatus("inativo");

        FreelancerDTO dto = new FreelancerDTO();
        dto.setNome("Nome Novo");
        dto.setStatus("ativo");
        dto.setEmail("novo@email.com");
        dto.setEspecialidade("Bartender");
        dto.setCidade("Guarulhos");

        mapper.atualizarEntidade(f, dto);

        assertThat(f.getNome()).isEqualTo("Nome Novo");
        assertThat(f.getStatus()).isEqualTo("ativo");
        assertThat(f.getEmail()).isEqualTo("novo@email.com");
        assertThat(f.getEspecialidade()).isEqualTo("Bartender");
        assertThat(f.getCidade()).isEqualTo("Guarulhos");
    }

    @Test
    @DisplayName("atualizarEntidade() não altera status quando DTO.status é null")
    void atualizarEntidade_statusNull_mantemStatusAtual() {
        Freelancer f = new Freelancer();
        f.setStatus("ativo");

        FreelancerDTO dto = new FreelancerDTO();
        dto.setStatus(null);

        mapper.atualizarEntidade(f, dto);

        assertThat(f.getStatus()).isEqualTo("ativo");
    }

    @Test
    @DisplayName("atualizarEntidade() não altera pixChave quando DTO.pixChave é null")
    void atualizarEntidade_pixChaveNull_mantemPixChaveAtual() {
        Freelancer f = new Freelancer();
        f.setPixChave("chave.antiga@pix.com");

        FreelancerDTO dto = new FreelancerDTO();
        dto.setPixChave(null);

        mapper.atualizarEntidade(f, dto);

        assertThat(f.getPixChave()).isEqualTo("chave.antiga@pix.com");
    }
}
