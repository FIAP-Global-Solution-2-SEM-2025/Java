package com.br.fiap.skillsfast.infrastructure.interfaces.dto.output;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class VagaOutputDto {
    private Long id;
    private String titulo;
    private String empresa;
    private String localizacao;
    private String tipo;
    private String nivel;
    private BigDecimal salario;
    private String descricao;
    private List<String> requisitos;
    private LocalDateTime dataPublicacao;
    private Integer numeroCandidatos;
    private Boolean ativa;

    // Construtores
    public VagaOutputDto() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<String> getRequisitos() { return requisitos; }
    public void setRequisitos(List<String> requisitos) { this.requisitos = requisitos; }

    public LocalDateTime getDataPublicacao() { return dataPublicacao; }
    public void setDataPublicacao(LocalDateTime dataPublicacao) { this.dataPublicacao = dataPublicacao; }

    public Integer getNumeroCandidatos() { return numeroCandidatos; }
    public void setNumeroCandidatos(Integer numeroCandidatos) { this.numeroCandidatos = numeroCandidatos; }

    public Boolean getAtiva() { return ativa; }
    public void setAtiva(Boolean ativa) { this.ativa = ativa; }
}