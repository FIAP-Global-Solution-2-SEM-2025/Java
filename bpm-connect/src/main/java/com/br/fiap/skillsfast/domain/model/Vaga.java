package com.br.fiap.skillsfast.domain.model;

import com.br.fiap.skillsfast.domain.exceptions.ValidacaoDominioException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vaga {

    private Long id;
    private String titulo;
    private String descricao;
    private String tipo;
    private String nivel;
    private String localizacao;
    private BigDecimal salario;
    private List<String> requisitos;
    private Long empresaId;
    private String empresaNome;
    private Boolean ativa;
    private LocalDateTime dataPublicacao;
    private Long versao;


    public Vaga() {
        this.requisitos = new ArrayList<>();
        this.ativa = true;
        this.dataPublicacao = LocalDateTime.now();
        this.versao = 0L;
    }

    public Vaga(String titulo, String descricao, String tipo, String nivel, String localizacao, Long empresaId) {
        this();
        setTitulo(titulo);
        setDescricao(descricao);
        setTipo(tipo);
        setNivel(nivel);
        setLocalizacao(localizacao);
        setEmpresaId(empresaId);
    }

    public Vaga(Long id, String titulo, String localizacao, String tipo, Boolean ativa) {
        this();
        this.id = id;
        this.titulo = titulo;
        this.localizacao = localizacao;
        this.tipo = tipo;
        this.ativa = ativa;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new ValidacaoDominioException("Título da vaga é obrigatório");
        }
        if (titulo.trim().length() < 5) {
            throw new ValidacaoDominioException("Título deve ter pelo menos 5 caracteres");
        }
        this.titulo = titulo.trim();
    }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new ValidacaoDominioException("Descrição da vaga é obrigatória");
        }
        if (descricao.trim().length() < 20) {
            throw new ValidacaoDominioException("Descrição deve ter pelo menos 20 caracteres");
        }
        this.descricao = descricao.trim();
    }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) {
        if (tipo == null || (!tipo.equals("CLT") && !tipo.equals("PJ") &&
                !tipo.equals("FREELANCE") && !tipo.equals("ESTAGIO"))) {
            throw new ValidacaoDominioException("Tipo deve ser CLT, PJ, FREELANCE ou ESTAGIO");
        }
        this.tipo = tipo;
    }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) {
        if (nivel == null || (!nivel.equals("JUNIOR") && !nivel.equals("PLENO") && !nivel.equals("SENIOR"))) {
            throw new ValidacaoDominioException("Nível deve ser JUNIOR, PLENO ou SENIOR");
        }
        this.nivel = nivel;
    }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) {
        if (localizacao == null || localizacao.trim().isEmpty()) {
            throw new ValidacaoDominioException("Localização é obrigatória");
        }
        this.localizacao = localizacao.trim();
    }

    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) {
        if (salario != null && salario.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidacaoDominioException("Salário não pode ser negativo");
        }
        this.salario = salario;
    }

    public List<String> getRequisitos() { return requisitos; }
    public void setRequisitos(List<String> requisitos) {
        this.requisitos = requisitos != null ? new ArrayList<>(requisitos) : new ArrayList<>();
    }

    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) {
        if (empresaId == null || empresaId <= 0) {
            throw new ValidacaoDominioException("ID da empresa é obrigatório");
        }
        this.empresaId = empresaId;
    }

    public String getEmpresaNome() { return empresaNome; }
    public void setEmpresaNome(String empresaNome) { this.empresaNome = empresaNome; }

    public Boolean isAtiva() { return ativa; }
    public void setAtiva(Boolean ativa) { this.ativa = ativa; }

    public LocalDateTime getDataPublicacao() { return dataPublicacao; }
    public void setDataPublicacao(LocalDateTime dataPublicacao) { this.dataPublicacao = dataPublicacao; }

    public Long getVersao() { return versao; }
    public void setVersao(Long versao) { this.versao = Objects.requireNonNullElse(versao, 0L); }

    public void incrementarVersao() {
        this.versao = versao == null ? 1L : versao + 1;
    }

    // Métodos de negócio
    public void adicionarRequisito(String requisito) {
        if (requisito == null || requisito.trim().isEmpty()) {
            throw new ValidacaoDominioException("Requisito não pode ser vazio");
        }
        if (this.requisitos == null) {
            this.requisitos = new ArrayList<>();
        }
        this.requisitos.add(requisito.trim());
    }
}