package com.br.fiap.skillsfast.infrastructure.interfaces.mappers;

import com.br.fiap.skillsfast.domain.model.Empresa;
import com.br.fiap.skillsfast.infrastructure.interfaces.dto.input.EmpresaInputDto;
import com.br.fiap.skillsfast.infrastructure.interfaces.dto.output.EmpresaOutputDto;

public class EmpresaMapper {

    public static Empresa toDomain(EmpresaInputDto dto) {
        Empresa empresa = new Empresa();
        empresa.setNome(dto.getNome());
        empresa.setCnpj(dto.getCnpj());
        empresa.setDescricao(dto.getDescricao());
        empresa.setWebsite(dto.getWebsite());
        empresa.setSetor(dto.getSetor());
        empresa.setTamanho(dto.getTamanho());
        empresa.setLogo(dto.getLogo());
        return empresa;
    }

    public static EmpresaOutputDto toOutputDto(Empresa empresa) {
        EmpresaOutputDto dto = new EmpresaOutputDto();
        dto.setId(empresa.getId());
        dto.setNome(empresa.getNome());
        dto.setCnpj(empresa.getCnpj());
        dto.setDescricao(empresa.getDescricao());
        dto.setWebsite(empresa.getWebsite());
        dto.setSetor(empresa.getSetor());
        dto.setTamanho(empresa.getTamanho());
        dto.setLogo(empresa.getLogo());
        dto.setAtivo(empresa.isAtivo());
        return dto;
    }
}