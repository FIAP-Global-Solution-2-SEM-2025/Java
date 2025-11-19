package com.br.fiap.skillsfast.infrastructure.interfaces.mappers;

import com.br.fiap.skillsfast.domain.model.Vaga;
import com.br.fiap.skillsfast.infrastructure.interfaces.dto.input.VagaInputDto;
import com.br.fiap.skillsfast.infrastructure.interfaces.dto.output.VagaOutputDto;

public class VagaMapper {

    public static Vaga toDomain(VagaInputDto dto) {
        Vaga vaga = new Vaga();
        vaga.setTitulo(dto.getTitulo());
        vaga.setDescricao(dto.getDescricao());
        vaga.setTipo(dto.getTipo());
        vaga.setNivel(dto.getNivel());
        vaga.setLocalizacao(dto.getLocalizacao());
        vaga.setSalario(dto.getSalario());
        vaga.setRequisitos(dto.getRequisitos());
        vaga.setEmpresaId(dto.getEmpresaId());
        return vaga;
    }

    public static VagaOutputDto toOutputDto(Vaga vaga) {
        VagaOutputDto dto = new VagaOutputDto();
        dto.setId(vaga.getId());
        dto.setTitulo(vaga.getTitulo());
        dto.setEmpresa(vaga.getEmpresaNome());
        dto.setLocalizacao(vaga.getLocalizacao());
        dto.setTipo(vaga.getTipo());
        dto.setNivel(vaga.getNivel());
        dto.setSalario(vaga.getSalario());
        dto.setDescricao(vaga.getDescricao());
        dto.setRequisitos(vaga.getRequisitos());
        dto.setDataPublicacao(vaga.getDataPublicacao());
        dto.setAtiva(vaga.isAtiva());
        return dto;
    }
}