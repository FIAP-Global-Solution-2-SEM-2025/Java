package com.br.fiap.skillsfast.infrastructure.persistence;

import com.br.fiap.skillsfast.domain.model.Empresa;
import com.br.fiap.skillsfast.domain.repository.EmpresaRepository;
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

public class JdbcEmpresaRepository implements EmpresaRepository {

    @Inject
    DataSource dataSource;

    @Override
    public Empresa salvar(Empresa empresa) {
        String sql = """
            INSERT INTO EMPRESA (ID, NOME, CNPJ, DESCRICAO, WEBSITE, SETOR, TAMANHO, LOGO)
            VALUES (EMPRESA_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID"})) {

            stmt.setString(1, empresa.getNome());
            stmt.setString(2, empresa.getCnpj());
            stmt.setString(3, empresa.getDescricao());
            stmt.setString(4, empresa.getWebsite());
            stmt.setString(5, empresa.getSetor());
            stmt.setString(6, empresa.getTamanho());
            stmt.setString(7, empresa.getLogo());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    empresa.setId(rs.getLong(1));
                }
            }

            return empresa;
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao salvar empresa", e);
        }
    }

    @Override
    public Optional<Empresa> buscarPorId(Long id) {
        String sql = "SELECT * FROM EMPRESA WHERE ID = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapearEmpresa(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar empresa por ID", e);
        }
    }

    @Override
    public Optional<Empresa> buscarPorCnpj(String cnpj) {
        String sql = "SELECT * FROM EMPRESA WHERE CNPJ = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cnpj);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapearEmpresa(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar empresa por CNPJ", e);
        }
    }

    @Override
    public List<Empresa> buscarTodas() {
        String sql = "SELECT * FROM EMPRESA ORDER BY DATA_CRIACAO DESC";
        List<Empresa> empresas = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                empresas.add(mapearEmpresa(rs));
            }
            return empresas;
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar todas as empresas", e);
        }
    }

    @Override
    public void atualizar(Empresa empresa) {
        String sql = """
            UPDATE EMPRESA SET NOME = ?, DESCRICAO = ?, WEBSITE = ?, SETOR = ?, 
            TAMANHO = ?, LOGO = ?, DATA_ATUALIZACAO = CURRENT_TIMESTAMP, VERSAO = VERSAO + 1
            WHERE ID = ? AND VERSAO = ?
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, empresa.getNome());
            stmt.setString(2, empresa.getDescricao());
            stmt.setString(3, empresa.getWebsite());
            stmt.setString(4, empresa.getSetor());
            stmt.setString(5, empresa.getTamanho());
            stmt.setString(6, empresa.getLogo());
            stmt.setLong(7, empresa.getId());
            stmt.setLong(8, empresa.getVersao());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new InfraestruturaException("Conflito de versão ao atualizar empresa");
            }

            empresa.setVersao(empresa.getVersao() + 1);
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao atualizar empresa", e);
        }
    }

    @Override
    public void desativar(Long id, Long versao) {
        String sql = "UPDATE EMPRESA SET ATIVO = 0, DATA_ATUALIZACAO = CURRENT_TIMESTAMP, VERSAO = VERSAO + 1 WHERE ID = ? AND VERSAO = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.setLong(2, versao);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new InfraestruturaException("Conflito de versão ao desativar empresa");
            }
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao desativar empresa", e);
        }
    }

    @Override
    public void reativar(Long id, Long versao) {
        String sql = "UPDATE EMPRESA SET ATIVO = 1, DATA_ATUALIZACAO = CURRENT_TIMESTAMP, VERSAO = VERSAO + 1 WHERE ID = ? AND VERSAO = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.setLong(2, versao);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new InfraestruturaException("Conflito de versão ao reativar empresa");
            }
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao reativar empresa", e);
        }
    }

    @Override
    public List<Empresa> buscarPorSetor(String setor) {
        String sql = "SELECT * FROM EMPRESA WHERE SETOR LIKE ? AND ATIVO = 1 ORDER BY NOME";
        List<Empresa> empresas = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + setor + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                empresas.add(mapearEmpresa(rs));
            }
            return empresas;
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar empresas por setor", e);
        }
    }

    @Override
    public List<Empresa> buscarPorTamanho(String tamanho) {
        String sql = "SELECT * FROM EMPRESA WHERE TAMANHO = ? AND ATIVO = 1 ORDER BY NOME";
        List<Empresa> empresas = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tamanho);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                empresas.add(mapearEmpresa(rs));
            }
            return empresas;
        } catch (SQLException e) {
            throw new InfraestruturaException("Erro ao buscar empresas por tamanho", e);
        }
    }

    private Empresa mapearEmpresa(ResultSet rs) throws SQLException {
        Empresa empresa = new Empresa();
        empresa.setId(rs.getLong("ID"));
        empresa.setNome(rs.getString("NOME"));
        empresa.setCnpj(rs.getString("CNPJ"));
        empresa.setDescricao(rs.getString("DESCRICAO"));
        empresa.setWebsite(rs.getString("WEBSITE"));
        empresa.setSetor(rs.getString("SETOR"));
        empresa.setTamanho(rs.getString("TAMANHO"));
        empresa.setLogo(rs.getString("LOGO"));
        empresa.setAtivo(rs.getInt("ATIVO") == 1);
        empresa.setVersao(rs.getLong("VERSAO"));

        return empresa;
    }
}