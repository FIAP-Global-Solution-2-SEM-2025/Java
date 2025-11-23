package com.br.fiap.skillsfast.infrastructure.persistence;

import com.br.fiap.skillsfast.domain.model.Vaga;
import com.br.fiap.skillsfast.domain.repository.VagaRepository;
import com.br.fiap.skillsfast.infrastructure.exceptions.InfraestruturaException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@ApplicationScoped
public class JdbcVagaRepository implements VagaRepository {

    @Inject
    DataSource dataSource;

    @Override
    public Vaga salvar(Vaga vaga) {
        System.out.println("=== [DEBUG] INICIANDO salvar() VAGA ===");
        System.out.println("[DEBUG] Dados da vaga: " + vaga.getTitulo() + ", Empresa: " + vaga.getEmpresaNome());

        String sql = """
            INSERT INTO VAGA (ID, TITULO, DESCRICAO, TIPO, NIVEL, LOCALIZACAO, SALARIO, REQUISITOS, EMPRESA_ID, EMPRESA_NOME)
            VALUES (VAGA_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID"})) {

            System.out.println("[DEBUG] Preparando statement...");

            stmt.setString(1, vaga.getTitulo());
            stmt.setString(2, vaga.getDescricao());
            stmt.setString(3, vaga.getTipo());
            stmt.setString(4, vaga.getNivel());
            stmt.setString(5, vaga.getLocalizacao());

            // CORREÇÃO: usar setBigDecimal() para compatibilidade com a entidade Vaga
            if (vaga.getSalario() != null) {
                stmt.setBigDecimal(6, vaga.getSalario());
                System.out.println("[DEBUG] Salario: " + vaga.getSalario());
            } else {
                stmt.setNull(6, java.sql.Types.DECIMAL);
                System.out.println("[DEBUG] Salario: NULL");
            }

            if (vaga.getRequisitos() != null && !vaga.getRequisitos().isEmpty()) {
                String requisitosStr = String.join(",", vaga.getRequisitos());
                stmt.setString(7, requisitosStr);
                System.out.println("[DEBUG] Requisitos: " + requisitosStr);
            } else {
                stmt.setString(7, "");
                System.out.println("[DEBUG] Requisitos: VAZIO");
            }

            stmt.setLong(8, vaga.getEmpresaId());
            stmt.setString(9, vaga.getEmpresaNome());

            System.out.println("[DEBUG] EmpresaId: " + vaga.getEmpresaId());
            System.out.println("[DEBUG] EmpresaNome: " + vaga.getEmpresaNome());

            System.out.println("[DEBUG] Executando INSERT...");
            int linhasAfetadas = stmt.executeUpdate();
            System.out.println("[DEBUG] Linhas afetadas: " + linhasAfetadas);

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long idGerado = rs.getLong(1);
                    vaga.setId(idGerado);
                    System.out.println("[DEBUG] ID gerado: " + idGerado);
                } else {
                    System.out.println("[DEBUG] Nenhum ID gerado!");
                }
            }

            System.out.println("=== [DEBUG] Vaga salva com sucesso ===");
            return vaga;

        } catch (SQLException e) {
            System.err.println("[DEBUG] SQLException no salvar: " + e.getMessage());
            System.err.println("[DEBUG] SQL State: " + e.getSQLState());
            System.err.println("[DEBUG] Error Code: " + e.getErrorCode());
            e.printStackTrace();
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
        System.out.println("=== [DEBUG] INICIANDO buscarTodas() ===");
        String sql = "SELECT * FROM VAGA ORDER BY DATA_PUBLICACAO DESC";
        List<Vaga> vagas = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("[DEBUG] Conexão e query executada com sucesso");

            int count = 0;
            while (rs.next()) {
                count++;
                Long id = rs.getLong("ID");
                System.out.println("[DEBUG] Processando vaga " + count + ", ID: " + id);

                // Debug de cada campo
                System.out.println("   TITULO: " + rs.getString("TITULO"));
                System.out.println("   DESCRICAO: " + (rs.getString("DESCRICAO") != null ? "PRESENTE" : "NULL"));
                System.out.println("   TIPO: " + rs.getString("TIPO"));
                System.out.println("   NIVEL: " + rs.getString("NIVEL"));
                System.out.println("   LOCALIZACAO: " + rs.getString("LOCALIZACAO"));
                System.out.println("   EMPRESA_ID: " + rs.getLong("EMPRESA_ID"));
                System.out.println("   EMPRESA_NOME: " + rs.getString("EMPRESA_NOME"));

                try {
                    Vaga vaga = mapearVaga(rs);
                    vagas.add(vaga);
                    System.out.println("   Vaga mapeada com sucesso");
                } catch (Exception e) {
                    System.err.println("   ERRO no mapeamento da vaga ID " + id);
                    e.printStackTrace();
                    throw e;
                }
            }

            System.out.println("=== [DEBUG] buscarTodas() finalizado. Total: " + count + " vagas ===");
            return vagas;

        } catch (SQLException e) {
            System.err.println("[DEBUG] SQLException no buscarTodas: " + e.getMessage());
            e.printStackTrace();
            throw new InfraestruturaException("Erro ao buscar todas as vagas", e);
        } catch (Exception e) {
            System.err.println("[DEBUG] Exception genérica no buscarTodas: " + e.getMessage());
            e.printStackTrace();
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

            // CORREÇÃO: usar setBigDecimal() para compatibilidade com a entidade Vaga
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
        System.out.println("   [DEBUG] Iniciando mapearVaga");

        Vaga vaga = new Vaga();

        try {
            vaga.setId(rs.getLong("ID"));
            System.out.println("     ID: " + vaga.getId());

            vaga.setTitulo(rs.getString("TITULO"));
            System.out.println("     TITULO: " + vaga.getTitulo());

            vaga.setDescricao(rs.getString("DESCRICAO"));
            System.out.println("     DESCRICAO: " + (vaga.getDescricao() != null ? "PRESENTE" : "NULL"));

            vaga.setTipo(rs.getString("TIPO"));
            System.out.println("     TIPO: " + vaga.getTipo());

            vaga.setNivel(rs.getString("NIVEL"));
            System.out.println("     NIVEL: " + vaga.getNivel());

            vaga.setLocalizacao(rs.getString("LOCALIZACAO"));
            System.out.println("     LOCALIZACAO: " + vaga.getLocalizacao());

            // CORREÇÃO: usar getBigDecimal() para compatibilidade com a entidade Vaga
            BigDecimal salario = rs.getBigDecimal("SALARIO");
            if (!rs.wasNull()) {
                vaga.setSalario(salario);
                System.out.println("     SALARIO: " + vaga.getSalario());
            } else {
                vaga.setSalario(null);
                System.out.println("     SALARIO: NULL");
            }

            // Requisitos
            String requisitosStr = rs.getString("REQUISITOS");
            if (requisitosStr != null && !requisitosStr.trim().isEmpty()) {
                vaga.setRequisitos(List.of(requisitosStr.split(",")));
                System.out.println("     REQUISITOS: " + vaga.getRequisitos());
            } else {
                vaga.setRequisitos(new ArrayList<>());
                System.out.println("     REQUISITOS: VAZIO");
            }

            vaga.setEmpresaId(rs.getLong("EMPRESA_ID"));
            System.out.println("     EMPRESA_ID: " + vaga.getEmpresaId());

            vaga.setEmpresaNome(rs.getString("EMPRESA_NOME"));
            System.out.println("     EMPRESA_NOME: " + vaga.getEmpresaNome());

            vaga.setAtiva(rs.getInt("ATIVA") == 1);
            System.out.println("     ATIVA: " + vaga.isAtiva());

            Timestamp dataPublicacao = rs.getTimestamp("DATA_PUBLICACAO");
            if (dataPublicacao != null) {
                vaga.setDataPublicacao(dataPublicacao.toLocalDateTime());
                System.out.println("     DATA_PUBLICACAO: " + vaga.getDataPublicacao());
            } else {
                System.out.println("     DATA_PUBLICACAO: NULL");
            }

            vaga.setVersao(rs.getLong("VERSAO"));
            System.out.println("     VERSAO: " + vaga.getVersao());

            System.out.println("   [DEBUG] mapearVaga finalizado com sucesso");
            return vaga;

        } catch (Exception e) {
            System.err.println("   ERRO no mapearVaga: " + e.getMessage());
            throw e;
        }
    }
}