package com.br.fiap.skillsfast.domain.service;

import com.br.fiap.skillsfast.domain.exceptions.EntidadeNaoLocalizada;
import com.br.fiap.skillsfast.domain.exceptions.ValidacaoDominioException;
import com.br.fiap.skillsfast.domain.model.Usuario;

import java.util.List;

public interface UsuarioService {
    Usuario criarUsuario(Usuario usuario) throws ValidacaoDominioException;
    Usuario buscarUsuarioPorId(Long id) throws EntidadeNaoLocalizada;
    Usuario buscarUsuarioPorEmail(String email) throws EntidadeNaoLocalizada;
    Usuario atualizarUsuario(Long id, Usuario usuario) throws EntidadeNaoLocalizada, ValidacaoDominioException;
    List<Usuario> listarTodosUsuarios();
    void desativarUsuario(Long id, Long versao) throws EntidadeNaoLocalizada;
    void reativarUsuario(Long id, Long versao) throws EntidadeNaoLocalizada;
    boolean validarCredenciais(String email, String senha);
    void alterarSenha(Long id, String senhaAtual, String novaSenha) throws EntidadeNaoLocalizada, ValidacaoDominioException;
}