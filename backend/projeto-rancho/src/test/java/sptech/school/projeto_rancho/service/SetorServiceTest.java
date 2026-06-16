package sptech.school.projeto_rancho.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.projeto_rancho.dto.SetorDTO;
import sptech.school.projeto_rancho.exception.RecursoNaoEncontradoException;
import sptech.school.projeto_rancho.mapper.SetorMapper;
import sptech.school.projeto_rancho.model.Setor;
import sptech.school.projeto_rancho.repository.SetorRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SetorService — Testes Unitários")
class SetorServiceTest {

    @Mock SetorRepository repo;
    @Mock SetorMapper mapper;

    @InjectMocks SetorService service;

    private Setor setor;
    private SetorDTO dto;

    @BeforeEach
    void setUp() {
        setor = new Setor();
        setor.setId(1);
        setor.setNome("Cozinha");

        dto = new SetorDTO();
        dto.setId(1);
        dto.setNome("Cozinha");
    }

    @Test
    @DisplayName("listar() retorna todos os setores mapeados")
    void listar_retornaTodos() {
        when(repo.findAll()).thenReturn(List.of(setor));
        when(mapper.toDTO(setor)).thenReturn(dto);

        List<SetorDTO> result = service.listar();

        assertThat(result).hasSize(1).contains(dto);
    }

    @Test
    @DisplayName("listar() retorna lista vazia quando não há setores")
    void listar_semSetores_retornaVazio() {
        when(repo.findAll()).thenReturn(List.of());

        assertThat(service.listar()).isEmpty();
    }

    @Test
    @DisplayName("buscarPorId() retorna DTO quando encontrado")
    void buscarPorId_encontrado_retornaDTO() {
        when(repo.findById(1)).thenReturn(Optional.of(setor));
        when(mapper.toDTO(setor)).thenReturn(dto);

        assertThat(service.buscarPorId(1)).isPresent().contains(dto);
    }

    @Test
    @DisplayName("buscarPorId() retorna vazio quando não encontrado")
    void buscarPorId_naoEncontrado_retornaVazio() {
        when(repo.findById(99)).thenReturn(Optional.empty());

        assertThat(service.buscarPorId(99)).isEmpty();
    }

    @Test
    @DisplayName("criar() salva e retorna DTO")
    void criar_dadosValidos_salvaSucesso() {
        when(mapper.toEntity(dto)).thenReturn(setor);
        when(repo.save(setor)).thenReturn(setor);
        when(mapper.toDTO(setor)).thenReturn(dto);

        SetorDTO result = service.criar(dto);

        assertThat(result).isEqualTo(dto);
        verify(repo).save(setor);
    }

    @Test
    @DisplayName("atualizar() encontra setor, aplica mudanças e salva")
    void atualizar_encontrado_atualizaSucesso() {
        when(repo.findById(1)).thenReturn(Optional.of(setor));
        when(repo.save(setor)).thenReturn(setor);
        when(mapper.toDTO(setor)).thenReturn(dto);

        SetorDTO result = service.atualizar(1, dto);

        assertThat(result).isEqualTo(dto);
        verify(mapper).atualizarEntidade(setor, dto);
        verify(repo).save(setor);
    }

    @Test
    @DisplayName("atualizar() lança exceção quando setor não existe")
    void atualizar_naoEncontrado_lancaExcecao() {
        when(repo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.atualizar(99, dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("excluir() chama deleteById quando ID existe")
    void excluir_encontrado_deleta() {
        when(repo.existsById(1)).thenReturn(true);

        service.excluir(1);

        verify(repo).deleteById(1);
    }

    @Test
    @DisplayName("excluir() lança exceção quando ID não existe")
    void excluir_naoEncontrado_lancaExcecao() {
        when(repo.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> service.excluir(99))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("99");

        verify(repo, never()).deleteById(any());
    }
}
