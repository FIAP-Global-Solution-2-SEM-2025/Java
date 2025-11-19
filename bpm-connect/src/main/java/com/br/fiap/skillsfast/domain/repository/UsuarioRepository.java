package com.br.fiap.skillsfast.domain.repository;

import com.br.fiap.skillsfast.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorEmail(String email);
    List<Usuario> buscarTodos();
    void editar(Usuario usuario);
    void desativar(Long id, Long versao);
    void reativar(Long id, Long versao);
}