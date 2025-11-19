package com.br.fiap.skillsfast.infrastructure.interfaces.mappers;

import com.br.fiap.skillsfast.domain.model.Usuario;
import com.br.fiap.skillsfast.infrastructure.interfaces.dto.input.UsuarioInputDto;
import com.br.fiap.skillsfast.infrastructure.interfaces.dto.output.UsuarioOutputDto;

public class UsuarioMapper {

    public static Usuario toDomain(UsuarioInputDto dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setTipo(dto.getTipo());
        usuario.setTelefone(dto.getTelefone());
        usuario.setLocalizacao(dto.getLocalizacao());
        usuario.setFotoPerfil(dto.getFotoPerfil());
        usuario.setCurriculo(dto.getCurriculo());
        usuario.setExperiencia(dto.getExperiencia());
        usuario.setHabilidades(dto.getHabilidades());
        return usuario;
    }

    public static UsuarioOutputDto toOutputDto(Usuario usuario) {
        UsuarioOutputDto dto = new UsuarioOutputDto();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setTipo(usuario.getTipo());
        dto.setTelefone(usuario.getTelefone());
        dto.setLocalizacao(usuario.getLocalizacao());
        dto.setFotoPerfil(usuario.getFotoPerfil());
        dto.setAtivo(usuario.isAtivo());
        dto.setDataCriacao(usuario.getDataCriacao());
        dto.setCurriculo(usuario.getCurriculo());
        dto.setExperiencia(usuario.getExperiencia());
        dto.setHabilidades(usuario.getHabilidades());
        return dto;
    }
}