package sptech.school.projeto_rancho.mapper;


import sptech.school.projeto_rancho.dto.funcionario.FuncionarioRequestDTO;
import sptech.school.projeto_rancho.dto.funcionario.FuncionarioResponseDto;
import sptech.school.projeto_rancho.model.Area;
import sptech.school.projeto_rancho.model.Funcionario;

import java.time.LocalDate;

public class FuncionarioMapper {

    public static Funcionario toEntity(FuncionarioRequestDTO dto, Area area) {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(dto.getNome());
        funcionario.setDdd(dto.getNumeroCompleto().substring(0, 2));
        funcionario.setNumero(dto.getNumeroCompleto().substring(2));
        funcionario.setCep(dto.getCep());
        funcionario.setPixChave(dto.getPixChave());
        funcionario.setArea(area);
        funcionario.setValorDiaria(dto.getValorDiaria());
        funcionario.setCustoTransporte(dto.getCustoTransporte() != null ? dto.getCustoTransporte() : false);
        funcionario.setDistancia(dto.getDistancia());
        funcionario.setDataCadastro(LocalDate.now());
        return funcionario;
    }

    public static FuncionarioResponseDto toDto(Funcionario funcionario) {
        FuncionarioResponseDto dto = new FuncionarioResponseDto();
        dto.setNome(funcionario.getNome());
        dto.setNumeroCompleto(funcionario.getDdd() + funcionario.getNumero());
        dto.setCep(funcionario.getCep());
        dto.setPixChave(funcionario.getPixChave());
        dto.setValorDiaria(funcionario.getValorDiaria());
        dto.setCustoTransporte(funcionario.getCustoTransporte());
        dto.setDistancia(funcionario.getDistancia());
        dto.setDataCadastro(funcionario.getDataCadastro());

        if (funcionario.getArea() != null) {
            dto.setNomeArea(funcionario.getArea().getNome());
        }

        return dto;
    }
}