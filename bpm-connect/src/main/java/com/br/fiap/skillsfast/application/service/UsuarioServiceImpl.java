package com.br.fiap.skillsfast.application.service;

import com.br.fiap.skillsfast.domain.exceptions.EntidadeNaoLocalizada;
import com.br.fiap.skillsfast.domain.exceptions.ValidacaoDominioException;
import com.br.fiap.skillsfast.domain.model.Usuario;
import com.br.fiap.skillsfast.domain.repository.UsuarioRepository;
import com.br.fiap.skillsfast.domain.service.UsuarioService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Override
    public Usuario criarUsuario(Usuario usuario) throws ValidacaoDominioException {
        verificarEmailExistente(usuario.getEmail());
        return usuarioRepository.salvar(usuario);
    }

    @Override
    public Usuario buscarUsuarioPorId(Long id) throws EntidadeNaoLocalizada {
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoLocalizada("Usuário não encontrado com ID: " + id));
    }

    @Override
    public Usuario buscarUsuarioPorEmail(String email) throws EntidadeNaoLocalizada {
        return usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new EntidadeNaoLocalizada("Usuário não encontrado com email: " + email));
    }

    @Override
    public Usuario atualizarUsuario(Long id, Usuario usuario) throws EntidadeNaoLocalizada, ValidacaoDominioException {
        Usuario usuarioExistente = buscarUsuarioPorId(id);

        // Verifica se o email foi alterado e se já existe
        if (!usuarioExistente.getEmail().equals(usuario.getEmail())) {
            verificarEmailExistente(usuario.getEmail());
        }

        // Atualiza apenas campos permitidos
        usuarioExistente.setNome(usuario.getNome());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setTelefone(usuario.getTelefone());
        usuarioExistente.setLocalizacao(usuario.getLocalizacao());
        usuarioExistente.setFotoPerfil(usuario.getFotoPerfil());
        usuarioExistente.setCurriculo(usuario.getCurriculo());
        usuarioExistente.setExperiencia(usuario.getExperiencia());
        usuarioExistente.setHabilidades(usuario.getHabilidades());

        usuarioRepository.atualizar(usuarioExistente);
        return usuarioExistente;
    }

    @Override
    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.buscarTodos();
    }

    @Override
    public void desativarUsuario(Long id, Long versao) throws EntidadeNaoLocalizada {
        Usuario usuario = buscarUsuarioPorId(id);
        usuarioRepository.desativar(id, versao);
    }

    @Override
    public void reativarUsuario(Long id, Long versao) throws EntidadeNaoLocalizada {
        Usuario usuario = buscarUsuarioPorId(id);
        usuarioRepository.reativar(id, versao);
    }

    @Override
    public boolean validarCredenciais(String email, String senha) {
        try {
            Usuario usuario = buscarUsuarioPorEmail(email);
            return usuario.getSenha().equals(senha) && usuario.isAtivo();
        } catch (EntidadeNaoLocalizada e) {
            return false;
        }
    }

    @Override
    public void alterarSenha(Long id, String senhaAtual, String novaSenha) throws EntidadeNaoLocalizada, ValidacaoDominioException {
        Usuario usuario = buscarUsuarioPorId(id);

        // Verifica senha atual
        if (!usuario.getSenha().equals(senhaAtual)) {
            throw new ValidacaoDominioException("Senha atual incorreta");
        }

        // Valida nova senha
        if (novaSenha == null || novaSenha.trim().isEmpty() || novaSenha.length() < 6) {
            throw new ValidacaoDominioException("Nova senha deve ter pelo menos 6 caracteres");
        }

        usuario.setSenha(novaSenha);

        usuarioRepository.atualizar(usuario);
    }

    private void verificarEmailExistente(String email) throws ValidacaoDominioException {
        if (usuarioRepository.buscarPorEmail(email).isPresent()) {
            throw new ValidacaoDominioException("Já existe um usuário cadastrado com este email");
        }
    }
}