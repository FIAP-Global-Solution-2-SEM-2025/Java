package com.br.fiap.skillsfast.domain.model;

import com.br.fiap.skillsfast.domain.exceptions.ValidacaoDominioException;

public class Empresa {

    private Long id;
    private String nome;
    private String cnpj;
    private String descricao;
    private String website;
    private String setor;
    private String tamanho;
    private String logo;
    private Boolean ativo;
    private Long versao;


    public Empresa() {
        this.ativo = true;
        this.versao = 0L;
    }

    public Empresa(String nome, String cnpj, String descricao) {
        this();
        setNome(nome);
        setCnpj(cnpj);
        setDescricao(descricao);
    }

    public Empresa(Long id, String nome, String cnpj, Boolean ativo) {
        this();
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.ativo = ativo;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoDominioException("Nome da empresa é obrigatório");
        }
        this.nome = nome.trim();
    }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            throw new ValidacaoDominioException("CNPJ é obrigatório");
        }
        String cnpjLimpo = cnpj.replaceAll("\\D", "");

        if (cnpjLimpo.length() != 14) {
            throw new ValidacaoDominioException("CNPJ deve ter 14 dígitos");
        }
        this.cnpj = cnpjLimpo;
    }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) {
        if (descricao != null && descricao.length() > 500) {
            throw new ValidacaoDominioException("Descrição deve ter no máximo 500 caracteres");
        }
        this.descricao = descricao;
    }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getSetor() { return setor; }
    public void setSetor(String setor) { this.setor = setor; }

    public String getTamanho() { return tamanho; }
    public void setTamanho(String tamanho) {
        if (tamanho != null && !tamanho.equals("PEQUENA") && !tamanho.equals("MEDIA") && !tamanho.equals("GRANDE")) {
            throw new ValidacaoDominioException("Tamanho deve ser PEQUENA, MEDIA ou GRANDE");
        }
        this.tamanho = tamanho;
    }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public Boolean isAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public Long getVersao() { return versao; }
    public void setVersao(Long versao) { this.versao = versao == null ? 0L : versao; }

    public void incrementarVersao() {
        this.versao = versao == null ? 1L : versao + 1;
    }
}