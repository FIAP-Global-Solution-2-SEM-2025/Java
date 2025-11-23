package com.br.fiap.skillsfast.infrastructure.persistence;

import com.br.fiap.skillsfast.domain.model.Usuario;
import com.br.fiap.skillsfast.domain.repository.UsuarioRepository;
import com.br.fiap.skillsfast.infrastructure.exceptions.InfraestruturaException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JdbcUsuarioRepository implements UsuarioRepository {

    @Inject
    DataSource dataSource;

    @Override
    public Usuario salvar(Usuario usuario) {
        String sql = """
            INSERT INTO USUARIO (ID, NOME, EMAIL, SENHA, TIPO, TELEFONE, LOCALIZACAO, 
            FOTO_PERFIL, CURRICULO, EXPERIENCIA, HABILIDADES)
            VALUES (USUARIO_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID"})) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getTipo());
            stmt.setString(5, usuario.getTelefone());
            stmt.setString(6, usuario.getLocalizacao());
            stmt.setString(7, usuario.getFotoPerfil());
            stmt.setString(8, usuario.getCurriculo());
            stmt.setString(9, usuario.getExperiencia());

            if (usuario.getHabilidades() != null && !usuario.getHabilidades().isEmpty()) {
                stmt.setString(10, String.join(",", usuario.getHabilidades()));
            } else {
                stmt.setString(10, "");
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getLong(1));
                }
            }

            return usuario;
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao salvar usuário", e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        String sql = "SELECT * FROM USUARIO WHERE ID = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapearUsuario(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar usuário por ID", e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        String sql = "SELECT * FROM USUARIO WHERE EMAIL = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapearUsuario(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar usuário por email", e);
        }
    }

    @Override
    public List<Usuario> buscarTodos() {
        String sql = "SELECT * FROM USUARIO ORDER BY DATA_CRIACAO DESC";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
            return usuarios;
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar todos os usuários", e);
        }
    }

    // CORREÇÃO: Mudei de 'editar' para 'atualizar' para bater com a interface
    @Override
    public void atualizar(Usuario usuario) {
        String sql = """
            UPDATE USUARIO SET NOME = ?, EMAIL = ?, TELEFONE = ?, LOCALIZACAO = ?, 
            FOTO_PERFIL = ?, CURRICULO = ?, EXPERIENCIA = ?, HABILIDADES = ?, 
            VERSAO = VERSAO + 1 WHERE ID = ? AND VERSAO = ?
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getTelefone());
            stmt.setString(4, usuario.getLocalizacao());
            stmt.setString(5, usuario.getFotoPerfil());
            stmt.setString(6, usuario.getCurriculo());
            stmt.setString(7, usuario.getExperiencia());

            if (usuario.getHabilidades() != null && !usuario.getHabilidades().isEmpty()) {
                stmt.setString(8, String.join(",", usuario.getHabilidades()));
            } else {
                stmt.setString(8, "");
            }

            stmt.setLong(9, usuario.getId());
            stmt.setLong(10, usuario.getVersao());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new InfraestruturaException("Conflito de versão ao atualizar usuário");
            }

            usuario.setVersao(usuario.getVersao() + 1);
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao atualizar usuário", e);
        }
    }

    @Override
    public void desativar(Long id, Long versao) {
        String sql = "UPDATE USUARIO SET ATIVO = 0, VERSAO = VERSAO + 1 WHERE ID = ? AND VERSAO = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.setLong(2, versao);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new InfraestruturaException("Conflito de versão ao desativar usuário");
            }
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao desativar usuário", e);
        }
    }

    @Override
    public void reativar(Long id, Long versao) {
        String sql = "UPDATE USUARIO SET ATIVO = 1, VERSAO = VERSAO + 1 WHERE ID = ? AND VERSAO = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.setLong(2, versao);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new InfraestruturaException("Conflito de versão ao reativar usuário");
            }
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao reativar usuário", e);
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("ID"));
        usuario.setNome(rs.getString("NOME"));
        usuario.setEmail(rs.getString("EMAIL"));
        usuario.setSenha(rs.getString("SENHA"));
        usuario.setTipo(rs.getString("TIPO"));
        usuario.setTelefone(rs.getString("TELEFONE"));
        usuario.setLocalizacao(rs.getString("LOCALIZACAO"));
        usuario.setFotoPerfil(rs.getString("FOTO_PERFIL"));
        usuario.setAtivo(rs.getInt("ATIVO") == 1);

        Timestamp dataCriacao = rs.getTimestamp("DATA_CRIACAO");
        if (dataCriacao != null) {
            usuario.setDataCriacao(dataCriacao.toLocalDateTime());
        }

        usuario.setVersao(rs.getLong("VERSAO"));
        usuario.setCurriculo(rs.getString("CURRICULO"));
        usuario.setExperiencia(rs.getString("EXPERIENCIA"));

        // Converter string de habilidades para lista
        String habilidadesStr = rs.getString("HABILIDADES");
        if (habilidadesStr != null && !habilidadesStr.trim().isEmpty()) {
            usuario.setHabilidades(List.of(habilidadesStr.split(",")));
        } else {
            usuario.setHabilidades(new ArrayList<>());
        }

        return usuario;
    }
}