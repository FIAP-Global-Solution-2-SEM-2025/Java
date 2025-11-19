package com.br.fiap.skillsfast.infrastructure.persistence;

import com.br.fiap.skillsfast.domain.model.Vaga;
import com.br.fiap.skillsfast.domain.repository.VagaRepository;
import com.br.fiap.skillsfast.infrastructure.exceptions.InfraestruturaException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JdbcVagaRepository implements VagaRepository {

    @Inject
    DataSource dataSource;

    @Override
    public Vaga salvar(Vaga vaga) {
        String sql = """
            INSERT INTO VAGA (ID, TITULO, DESCRICAO, TIPO, NIVEL, LOCALIZACAO, SALARIO, REQUISITOS, EMPRESA_ID, EMPRESA_NOME)
            VALUES (VAGA_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID"})) {

            stmt.setString(1, vaga.getTitulo());
            stmt.setString(2, vaga.getDescricao());
            stmt.setString(3, vaga.getTipo());
            stmt.setString(4, vaga.getNivel());
            stmt.setString(5, vaga.getLocalizacao());

            if (vaga.getSalario() != null) {
                stmt.setBigDecimal(6, vaga.getSalario());
            } else {
                stmt.setNull(6, java.sql.Types.DECIMAL);
            }

            if (vaga.getRequisitos() != null && !vaga.getRequisitos().isEmpty()) {
                stmt.setString(7, String.join(",", vaga.getRequisitos()));
            } else {
                stmt.setString(7, "");
            }

            stmt.setLong(8, vaga.getEmpresaId());
            stmt.setString(9, vaga.getEmpresaNome());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    vaga.setId(rs.getLong(1));
                }
            }

            return vaga;
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao salvar vaga", e);
        }
    }

    @Override
    public Optional<Vaga> buscarPorId(Long id) {
        String sql = "SELECT * FROM VAGA WHERE ID = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapearVaga(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar vaga por ID", e);
        }
    }

    @Override
    public List<Vaga> buscarTodas() {
        String sql = "SELECT * FROM VAGA ORDER BY DATA_PUBLICACAO DESC";
        List<Vaga> vagas = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vagas.add(mapearVaga(rs));
            }
            return vagas;
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar todas as vagas", e);
        }
    }

    @Override
    public List<Vaga> buscarAtivas() {
        String sql = "SELECT * FROM VAGA WHERE ATIVA = 1 ORDER BY DATA_PUBLICACAO DESC";
        List<Vaga> vagas = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vagas.add(mapearVaga(rs));
            }
            return vagas;
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar vagas ativas", e);
        }
    }

    @Override
    public List<Vaga> buscarPorEmpresa(Long empresaId) {
        String sql = "SELECT * FROM VAGA WHERE EMPRESA_ID = ? ORDER BY DATA_PUBLICACAO DESC";
        List<Vaga> vagas = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, empresaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                vagas.add(mapearVaga(rs));
            }
            return vagas;
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar vagas por empresa", e);
        }
    }

    @Override
    public void atualizar(Vaga vaga) {
        String sql = """
            UPDATE VAGA SET TITULO = ?, DESCRICAO = ?, TIPO = ?, NIVEL = ?, LOCALIZACAO = ?, 
            SALARIO = ?, REQUISITOS = ?, EMPRESA_NOME = ?, DATA_ATUALIZACAO = CURRENT_TIMESTAMP, VERSAO = VERSAO + 1
            WHERE ID = ? AND VERSAO = ?
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vaga.getTitulo());
            stmt.setString(2, vaga.getDescricao());
            stmt.setString(3, vaga.getTipo());
            stmt.setString(4, vaga.getNivel());
            stmt.setString(5, vaga.getLocalizacao());

            if (vaga.getSalario() != null) {
                stmt.setBigDecimal(6, vaga.getSalario());
            } else {
                stmt.setNull(6, java.sql.Types.DECIMAL);
            }

            if (vaga.getRequisitos() != null && !vaga.getRequisitos().isEmpty()) {
                stmt.setString(7, String.join(",", vaga.getRequisitos()));
            } else {
                stmt.setString(7, "");
            }

            stmt.setString(8, vaga.getEmpresaNome());
            stmt.setLong(9, vaga.getId());
            stmt.setLong(10, vaga.getVersao());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new InfraestruturaException("Conflito de versão ao atualizar vaga");
            }

            // Incrementa versão no objeto
            vaga.setVersao(vaga.getVersao() + 1);
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao atualizar vaga", e);
        }
    }

    @Override
    public void desativar(Long id, Long versao) {
        String sql = "UPDATE VAGA SET ATIVA = 0, DATA_ATUALIZACAO = CURRENT_TIMESTAMP, VERSAO = VERSAO + 1 WHERE ID = ? AND VERSAO = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.setLong(2, versao);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new InfraestruturaException("Conflito de versão ao desativar vaga");
            }
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao desativar vaga", e);
        }
    }

    @Override
    public void reativar(Long id, Long versao) {
        String sql = "UPDATE VAGA SET ATIVA = 1, DATA_ATUALIZACAO = CURRENT_TIMESTAMP, VERSAO = VERSAO + 1 WHERE ID = ? AND VERSAO = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.setLong(2, versao);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new InfraestruturaException("Conflito de versão ao reativar vaga");
            }
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao reativar vaga", e);
        }
    }

    private Vaga mapearVaga(ResultSet rs) throws SQLException {
        Vaga vaga = new Vaga();
        vaga.setId(rs.getLong("ID"));
        vaga.setTitulo(rs.getString("TITULO"));
        vaga.setDescricao(rs.getString("DESCRICAO"));
        vaga.setTipo(rs.getString("TIPO"));
        vaga.setNivel(rs.getString("NIVEL"));
        vaga.setLocalizacao(rs.getString("LOCALIZACAO"));
        vaga.setSalario(rs.getBigDecimal("SALARIO"));

        // Converter string de requisitos para lista
        String requisitosStr = rs.getString("REQUISITOS");
        if (requisitosStr != null && !requisitosStr.trim().isEmpty()) {
            vaga.setRequisitos(List.of(requisitosStr.split(",")));
        } else {
            vaga.setRequisitos(new ArrayList<>());
        }

        vaga.setEmpresaId(rs.getLong("EMPRESA_ID"));
        vaga.setEmpresaNome(rs.getString("EMPRESA_NOME"));
        vaga.setAtiva(rs.getInt("ATIVA") == 1);

        Timestamp dataPublicacao = rs.getTimestamp("DATA_PUBLICACAO");
        if (dataPublicacao != null) {
            vaga.setDataPublicacao(dataPublicacao.toLocalDateTime());
        }

        vaga.setVersao(rs.getLong("VERSAO"));

        return vaga;
    }
}