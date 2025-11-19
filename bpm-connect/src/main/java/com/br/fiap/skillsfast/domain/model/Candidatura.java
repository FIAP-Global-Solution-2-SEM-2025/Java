package com.br.fiap.skillsfast.domain.model;

import com.br.fiap.skillsfast.domain.exceptions.ValidacaoDominioException;

import java.time.LocalDateTime;
import java.util.Objects;

public class Candidatura {

    private Long id;
    private Long usuarioId;
    private Long vagaId;
    private String status;
    private LocalDateTime dataCandidatura;
    private String observacao;
    private Long versao;


    public Candidatura() {
        this.status = "PENDENTE";
        this.dataCandidatura = LocalDateTime.now();
        this.versao = 0L;
    }

    public Candidatura(Long usuarioId, Long vagaId) {
        this();
        setUsuarioId(usuarioId);
        setVagaId(vagaId);
    }

    public Candidatura(Long id, Long usuarioId, Long vagaId, String status) {
        this();
        this.id = id;
        this.usuarioId = usuarioId;
        this.vagaId = vagaId;
        this.status = status;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) {
        if (usuarioId == null || usuarioId <= 0) {
            throw new ValidacaoDominioException("ID do usuário é obrigatório");
        }
        this.usuarioId = usuarioId;
    }

    public Long getVagaId() { return vagaId; }
    public void setVagaId(Long vagaId) {
        if (vagaId == null || vagaId <= 0) {
            throw new ValidacaoDominioException("ID da vaga é obrigatório");
        }
        this.vagaId = vagaId;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        if (status == null || (!status.equals("PENDENTE") && !status.equals("APROVADA") &&
                !status.equals("REJEITADA") && !status.equals("CANCELADA"))) {
            throw new ValidacaoDominioException("Status deve ser PENDENTE, APROVADA, REJEITADA ou CANCELADA");
        }
        this.status = status;
    }

    public LocalDateTime getDataCandidatura() { return dataCandidatura; }
    public void setDataCandidatura(LocalDateTime dataCandidatura) { this.dataCandidatura = dataCandidatura; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) {
        if (observacao != null && observacao.length() > 500) {
            throw new ValidacaoDominioException("Observação deve ter no máximo 500 caracteres");
        }
        this.observacao = observacao;
    }

    public Long getVersao() { return versao; }
    public void setVersao(Long versao) { this.versao = Objects.requireNonNullElse(versao, 0L); }

    public void incrementarVersao() {
        this.versao = versao == null ? 1L : versao + 1;
    }

    // Métodos de negócio
    public void aprovar() {
        setStatus("APROVADA");
    }

    public void rejeitar() {
        setStatus("REJEITADA");
    }

    public void cancelar() {
        setStatus("CANCELADA");
    }

    public boolean isPendente() {
        return "PENDENTE".equals(status);
    }
}