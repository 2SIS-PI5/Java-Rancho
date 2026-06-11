package sptech.school.projeto_rancho.service;

import sptech.school.projeto_rancho.dto.SetorDTO;
import sptech.school.projeto_rancho.exception.RecursoNaoEncontradoException;
import sptech.school.projeto_rancho.mapper.SetorMapper;
import sptech.school.projeto_rancho.model.Setor;
import sptech.school.projeto_rancho.repository.SetorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SetorService {

    @Autowired
    private SetorRepository repo;

    @Autowired
    private SetorMapper mapper;

    public List<SetorDTO> listar() {
        return repo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public Optional<SetorDTO> buscarPorId(Integer id) {
        return repo.findById(id).map(mapper::toDTO);
    }

    public SetorDTO criar(SetorDTO dto) {
        Setor s = mapper.toEntity(dto);
        return mapper.toDTO(repo.save(s));
    }

    public SetorDTO atualizar(Integer id, SetorDTO dto) {
        Setor s = repo.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Setor não encontrado. ID: " + id));
        mapper.atualizarEntidade(s, dto);
        return mapper.toDTO(repo.save(s));
    }

    public void excluir(Integer id) {
        if (!repo.existsById(id)) {
            throw new RecursoNaoEncontradoException("Setor não encontrado. ID: " + id);
        }
        repo.deleteById(id);
    }
}
