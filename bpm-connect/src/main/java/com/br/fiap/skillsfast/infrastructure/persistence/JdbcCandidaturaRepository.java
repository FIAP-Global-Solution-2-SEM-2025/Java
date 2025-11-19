package com.br.fiap.skillsfast.infrastructure.persistence;

import com.br.fiap.skillsfast.domain.model.Candidatura;
import com.br.fiap.skillsfast.domain.repository.CandidaturaRepository;
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

public class JdbcCandidaturaRepository implements CandidaturaRepository {

    @Inject
    DataSource dataSource;

    @Override
    public Candidatura salvar(Candidatura candidatura) {
        String sql = """
            INSERT INTO CANDIDATURA (ID, USUARIO_ID, VAGA_ID, STATUS, OBSERVACAO)
            VALUES (CANDIDATURA_SEQ.NEXTVAL, ?, ?, ?, ?)
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID"})) {

            stmt.setLong(1, candidatura.getUsuarioId());
            stmt.setLong(2, candidatura.getVagaId());
            stmt.setString(3, candidatura.getStatus());
            stmt.setString(4, candidatura.getObservacao());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    candidatura.setId(rs.getLong(1));
                }
            }

            return candidatura;
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao salvar candidatura", e);
        }
    }

    @Override
    public Optional<Candidatura> buscarPorId(Long id) {
        String sql = "SELECT * FROM CANDIDATURA WHERE ID = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapearCandidatura(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar candidatura por ID", e);
        }
    }

    @Override
    public List<Candidatura> buscarTodas() {
        String sql = "SELECT * FROM CANDIDATURA ORDER BY DATA_CANDIDATURA DESC";
        List<Candidatura> candidaturas = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                candidaturas.add(mapearCandidatura(rs));
            }
            return candidaturas;
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar todas as candidaturas", e);
        }
    }

    @Override
    public List<Candidatura> buscarPorUsuario(Long usuarioId) {
        String sql = """
            SELECT c.* FROM CANDIDATURA c 
            WHERE c.USUARIO_ID = ? 
            ORDER BY c.DATA_CANDIDATURA DESC
            """;
        List<Candidatura> candidaturas = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                candidaturas.add(mapearCandidatura(rs));
            }
            return candidaturas;
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar candidaturas por usuário", e);
        }
    }

    @Override
    public List<Candidatura> buscarPorVaga(Long vagaId) {
        String sql = """
            SELECT c.* FROM CANDIDATURA c 
            WHERE c.VAGA_ID = ? 
            ORDER BY c.DATA_CANDIDATURA DESC
            """;
        List<Candidatura> candidaturas = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, vagaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                candidaturas.add(mapearCandidatura(rs));
            }
            return candidaturas;
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar candidaturas por vaga", e);
        }
    }

    @Override
    public List<Candidatura> buscarPorEmpresa(Long empresaId) {
        String sql = """
            SELECT c.* FROM CANDIDATURA c
            JOIN VAGA v ON c.VAGA_ID = v.ID
            WHERE v.EMPRESA_ID = ?
            ORDER BY c.DATA_CANDIDATURA DESC
            """;
        List<Candidatura> candidaturas = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, empresaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                candidaturas.add(mapearCandidatura(rs));
            }
            return candidaturas;
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar candidaturas por empresa", e);
        }
    }

    @Override
    public void atualizar(Candidatura candidatura) {
        String sql = """
            UPDATE CANDIDATURA SET STATUS = ?, OBSERVACAO = ?, 
            DATA_ATUALIZACAO = CURRENT_TIMESTAMP, VERSAO = VERSAO + 1
            WHERE ID = ? AND VERSAO = ?
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, candidatura.getStatus());
            stmt.setString(2, candidatura.getObservacao());
            stmt.setLong(3, candidatura.getId());
            stmt.setLong(4, candidatura.getVersao());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new InfraestruturaException("Conflito de versão ao atualizar candidatura");
            }

            candidatura.setVersao(candidatura.getVersao() + 1);
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao atualizar candidatura", e);
        }
    }

    @Override
    public boolean usuarioJaCandidatou(Long usuarioId, Long vagaId) {
        String sql = "SELECT COUNT(*) FROM CANDIDATURA WHERE USUARIO_ID = ? AND VAGA_ID = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, usuarioId);
            stmt.setLong(2, vagaId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao verificar candidatura", e);
        }
    }

    private Candidatura mapearCandidatura(ResultSet rs) throws SQLException {
        Candidatura candidatura = new Candidatura();
        candidatura.setId(rs.getLong("ID"));
        candidatura.setUsuarioId(rs.getLong("USUARIO_ID"));
        candidatura.setVagaId(rs.getLong("VAGA_ID"));
        candidatura.setStatus(rs.getString("STATUS"));

        Timestamp dataCandidatura = rs.getTimestamp("DATA_CANDIDATURA");
        if (dataCandidatura != null) {
            candidatura.setDataCandidatura(dataCandidatura.toLocalDateTime());
        }

        candidatura.setObservacao(rs.getString("OBSERVACAO"));
        candidatura.setVersao(rs.getLong("VERSAO"));

        return candidatura;
    }
}