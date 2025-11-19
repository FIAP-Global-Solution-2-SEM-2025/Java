package com.br.fiap.skillsfast.infrastructure.interfaces.dto.input;

public class CandidaturaInputDto {
    private Long usuarioId;
    private Long vagaId;

    // Construtores
    public CandidaturaInputDto() {}

    public CandidaturaInputDto(Long usuarioId, Long vagaId) {
        this.usuarioId = usuarioId;
        this.vagaId = vagaId;
    }

    // Getters e Setters
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getVagaId() { return vagaId; }
    public void setVagaId(Long vagaId) { this.vagaId = vagaId; }
}