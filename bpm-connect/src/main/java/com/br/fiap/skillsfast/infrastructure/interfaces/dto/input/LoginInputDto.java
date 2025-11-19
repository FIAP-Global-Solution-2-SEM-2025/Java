package com.br.fiap.skillsfast.infrastructure.interfaces.dto.input;

public class LoginInputDto {
    private String email;
    private String senha;

    // Construtores
    public LoginInputDto() {}

    public LoginInputDto(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}