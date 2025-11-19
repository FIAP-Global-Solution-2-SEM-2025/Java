package com.br.fiap.skillsfast.infrastructure.interfaces.dto.input;

import java.math.BigDecimal;
import java.util.List;

public class VagaInputDto {
    private String titulo;
    private String descricao;
    private String tipo;
    private String nivel;
    private String localizacao;
    private BigDecimal salario;
    private List<String> requisitos;
    private Long empresaId;

    // Construtores
    public VagaInputDto() {}

    public VagaInputDto(String titulo, String descricao, String tipo, String nivel, String localizacao, Long empresaId) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.nivel = nivel;
        this.localizacao = localizacao;
        this.empresaId = empresaId;
    }

    // Getters e Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }

    public List<String> getRequisitos() { return requisitos; }
    public void setRequisitos(List<String> requisitos) { this.requisitos = requisitos; }

    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }
}