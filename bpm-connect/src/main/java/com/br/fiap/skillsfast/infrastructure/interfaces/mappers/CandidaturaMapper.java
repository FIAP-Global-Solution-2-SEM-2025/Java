package com.br.fiap.skillsfast.infrastructure.interfaces.mappers;

import com.br.fiap.skillsfast.domain.model.Candidatura;
import com.br.fiap.skillsfast.infrastructure.interfaces.dto.input.CandidaturaInputDto;
import com.br.fiap.skillsfast.infrastructure.interfaces.dto.output.CandidaturaOutputDto;

public class CandidaturaMapper {

    public static Candidatura toDomain(CandidaturaInputDto dto) {
        return new Candidatura(dto.getUsuarioId(), dto.getVagaId());
    }

    public static CandidaturaOutputDto toOutputDto(Candidatura candidatura) {
        CandidaturaOutputDto dto = new CandidaturaOutputDto();
        dto.setId(candidatura.getId());
        dto.setVagaId(candidatura.getVagaId());
        dto.setUsuarioId(candidatura.getUsuarioId());
        dto.setStatus(candidatura.getStatus());
        dto.setDataCandidatura(candidatura.getDataCandidatura());
        dto.setObservacao(candidatura.getObservacao());
        return dto;
    }
}