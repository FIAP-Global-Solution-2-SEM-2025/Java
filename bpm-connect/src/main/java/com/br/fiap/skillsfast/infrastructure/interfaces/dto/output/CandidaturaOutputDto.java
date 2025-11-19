package com.br.fiap.skillsfast.infrastructure.interfaces.dto.output;

import java.time.LocalDateTime;

public class CandidaturaOutputDto {
    private Long id;
    private Long vagaId;
    private String vagaTitulo;
    private String empresaNome;
    private Long usuarioId;
    private String usuarioNome;
    private String status;
    private LocalDateTime dataCandidatura;
    private String observacao;

    // Construtores
    public CandidaturaOutputDto() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getVagaId() { return vagaId; }
    public void setVagaId(Long vagaId) { this.vagaId = vagaId; }

    public String getVagaTitulo() { return vagaTitulo; }
    public void setVagaTitulo(String vagaTitulo) { this.vagaTitulo = vagaTitulo; }

    public String getEmpresaNome() { return empresaNome; }
    public void setEmpresaNome(String empresaNome) { this.empresaNome = empresaNome; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNome() { return usuarioNome; }
    public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataCandidatura() { return dataCandidatura; }
    public void setDataCandidatura(LocalDateTime dataCandidatura) { this.dataCandidatura = dataCandidatura; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}