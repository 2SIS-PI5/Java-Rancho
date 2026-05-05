package sptech.school.projeto_rancho.service;

import org.springframework.stereotype.Service;
import sptech.school.projeto_rancho.dto.funcionario.FuncionarioRequestDTO;
import sptech.school.projeto_rancho.dto.funcionario.FuncionarioUpdateDTO;
import sptech.school.projeto_rancho.exception.AreaNaoEncontadaException;
import sptech.school.projeto_rancho.exception.FuncionarioNaoEncontradoException;
import sptech.school.projeto_rancho.mapper.FuncionarioMapper;
import sptech.school.projeto_rancho.model.Area;
import sptech.school.projeto_rancho.model.Funcionario;
import sptech.school.projeto_rancho.repository.AreaRepository;
import sptech.school.projeto_rancho.repository.FuncionarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final AreaRepository areaRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository, AreaRepository areaRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.areaRepository = areaRepository;
    }

    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    public Funcionario buscarPorId(Integer id) {
        return funcionarioRepository.findById(id).orElseThrow(FuncionarioNaoEncontradoException::new);
    }

    public Funcionario criar(FuncionarioRequestDTO dto) {
        Area area = areaRepository.findById(dto.getIdArea()).orElse(null);
        if (area == null) {
            throw new AreaNaoEncontadaException();
        }


        Funcionario funcionario = FuncionarioMapper.toEntity(dto, area);
        return funcionarioRepository.save(funcionario);
    }

    public Funcionario atualizar(Integer id, FuncionarioUpdateDTO dto) {
        Funcionario funcionario = funcionarioRepository.findById(id).orElse(null);
        if (funcionario == null) {
            throw new FuncionarioNaoEncontradoException();
        }

        // Atualizar apenas os campos fornecidos (não nulos)
        if (dto.getNome() != null) {
            funcionario.setNome(dto.getNome());
        }

        if (dto.getNumeroCompleto() != null) {
            funcionario.setDdd(dto.getNumeroCompleto().substring(0, 2));
            funcionario.setNumero(dto.getNumeroCompleto().substring(2));
        }

        if (dto.getCep() != null) {
            funcionario.setCep(dto.getCep());
        }

        if (dto.getPixChave() != null) {
            funcionario.setPixChave(dto.getPixChave());
        }

        if (dto.getIdArea() != null) {
            Area area = areaRepository.findById(dto.getIdArea()).orElse(null);
            if (area == null) {
                throw new AreaNaoEncontadaException();
            }
            funcionario.setArea(area);
        }

        if (dto.getValorDiaria() != null) {
            funcionario.setValorDiaria(dto.getValorDiaria());
        }

        if (dto.getCustoTransporte() != null) {
            funcionario.setCustoTransporte(dto.getCustoTransporte());
        }

        if (dto.getDistancia() != null) {
            funcionario.setDistancia(dto.getDistancia());
        }

        if (dto.getDataCadastro() != null) {
            funcionario.setDataCadastro(dto.getDataCadastro());
        }

        return funcionarioRepository.save(funcionario);
    }

    public void deletar(Integer id) {
        if (!funcionarioRepository.existsById(id)){
            throw new FuncionarioNaoEncontradoException();
        }
        funcionarioRepository.deleteById(id);
    }

    public List<Funcionario> buscarPorNome(String nome) {
        List<Funcionario> funcionarios = funcionarioRepository.findByNomeContainingIgnoreCase(nome);
        if (funcionarios.isEmpty()){
            throw new FuncionarioNaoEncontradoException();
        }
        return funcionarios;
    }

    public List<Funcionario> buscarPorArea(Integer id) {
        if (!funcionarioRepository.existsByAreaId(id)){
            throw new FuncionarioNaoEncontradoException();
        }
        return funcionarioRepository.findByAreaId(id);
    }
}