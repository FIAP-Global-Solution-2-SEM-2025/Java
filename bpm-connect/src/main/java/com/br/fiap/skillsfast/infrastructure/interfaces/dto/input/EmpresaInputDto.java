package com.br.fiap.skillsfast.infrastructure.interfaces.dto.input;

public class EmpresaInputDto {
    private String nome;
    private String cnpj;
    private String descricao;
    private String website;
    private String setor;
    private String tamanho;
    private String logo;

    // Construtores
    public EmpresaInputDto() {}

    public EmpresaInputDto(String nome, String cnpj, String descricao) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.descricao = descricao;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getSetor() { return setor; }
    public void setSetor(String setor) { this.setor = setor; }

    public String getTamanho() { return tamanho; }
    public void setTamanho(String tamanho) { this.tamanho = tamanho; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
}