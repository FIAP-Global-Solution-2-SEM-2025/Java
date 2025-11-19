package com.br.fiap.skillsfast.infrastructure.interfaces.dto.output;

public class LoginOutputDto {
    private boolean sucesso;
    private String mensagem;
    private Long usuarioId;
    private String tipoUsuario;

    // Construtores
    public LoginOutputDto() {}

    public LoginOutputDto(boolean sucesso, String mensagem) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
    }

    public LoginOutputDto(boolean sucesso, String mensagem, Long usuarioId, String tipoUsuario) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.usuarioId = usuarioId;
        this.tipoUsuario = tipoUsuario;
    }

    // Getters e Setters
    public boolean isSucesso() { return sucesso; }
    public void setSucesso(boolean sucesso) { this.sucesso = sucesso; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
}