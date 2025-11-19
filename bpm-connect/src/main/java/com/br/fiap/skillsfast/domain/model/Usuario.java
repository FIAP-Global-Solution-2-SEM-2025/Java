package com.br.fiap.skillsfast.domain.model;

import com.br.fiap.skillsfast.domain.exceptions.ValidacaoDominioException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Usuario {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String tipo;
    private String telefone;
    private String localizacao;
    private String fotoPerfil;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private Long versao;
    private String curriculo;
    private String experiencia;
    private List<String> habilidades;


    public Usuario() {
        this.habilidades = new ArrayList<>();
        this.ativo = true;
        this.dataCriacao = LocalDateTime.now();
        this.versao = 0L;
    }

    public Usuario(String nome, String email, String senha, String tipo) {
        this();
        setNome(nome);
        setEmail(email);
        setSenha(senha);
        setTipo(tipo);
    }

    public Usuario(Long id, String nome, String email, String tipo, Boolean ativo) {
        this();
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
        this.ativo = ativo;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoDominioException("Nome é obrigatório");
        }
        if (nome.trim().length() < 2) {
            throw new ValidacaoDominioException("Nome deve ter pelo menos 2 caracteres");
        }
        this.nome = nome.trim();
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidacaoDominioException("Email é obrigatório");
        }
        final String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(regex)) {
            throw new ValidacaoDominioException("Email inválido");
        }
        this.email = email.trim();
    }

    public String getSenha() { return senha; }
    public void setSenha(String senha) {
        if (senha == null || senha.trim().isEmpty()) {
            throw new ValidacaoDominioException("Senha é obrigatória");
        }
        if (senha.length() < 6) {
            throw new ValidacaoDominioException("Senha deve ter pelo menos 6 caracteres");
        }
        this.senha = senha;
    }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) {
        if (tipo == null || (!tipo.equals("CANDIDATO") && !tipo.equals("EMPRESA"))) {
            throw new ValidacaoDominioException("Tipo deve ser CANDIDATO ou EMPRESA");
        }
        this.tipo = tipo;
    }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) {
        if (telefone != null && !telefone.trim().isEmpty()) {
            final String regex = "^\\(\\d{2}\\)\\d{4,5}-\\d{4}$";
            if (!telefone.matches(regex)) {
                throw new ValidacaoDominioException("Telefone inválido, utilize o formato (DD) XXXXX-XXXX");
            }
        }
        this.telefone = telefone;
    }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }

    public Boolean isAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public Long getVersao() { return versao; }
    public void setVersao(Long versao) { this.versao = Objects.requireNonNullElse(versao, 0L); }

    public String getCurriculo() { return curriculo; }
    public void setCurriculo(String curriculo) { this.curriculo = curriculo; }

    public String getExperiencia() { return experiencia; }
    public void setExperiencia(String experiencia) { this.experiencia = experiencia; }

    public List<String> getHabilidades() { return habilidades; }
    public void setHabilidades(List<String> habilidades) {
        this.habilidades = habilidades != null ? new ArrayList<>(habilidades) : new ArrayList<>();
    }

    public void incrementarVersao() {
        this.versao = versao == null ? 1L : versao + 1;
    }
}