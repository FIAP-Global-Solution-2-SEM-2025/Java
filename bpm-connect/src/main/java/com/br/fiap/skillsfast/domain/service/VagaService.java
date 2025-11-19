package com.br.fiap.skillsfast.domain.service;

import com.br.fiap.skillsfast.domain.exceptions.EntidadeNaoLocalizada;
import com.br.fiap.skillsfast.domain.exceptions.ValidacaoDominioException;
import com.br.fiap.skillsfast.domain.model.Vaga;

import java.util.List;

public interface VagaService {
    Vaga criarVaga(Vaga vaga) throws ValidacaoDominioException, EntidadeNaoLocalizada;
    Vaga buscarVagaPorId(Long id) throws EntidadeNaoLocalizada;
    List<Vaga> buscarVagasPorEmpresa(Long empresaId) throws EntidadeNaoLocalizada;
    List<Vaga> buscarVagasAtivas();
    List<Vaga> buscarVagasPorFiltro(String tipo, String nivel, String localizacao, String titulo);
    Vaga atualizarVaga(Long id, Vaga vaga) throws EntidadeNaoLocalizada, ValidacaoDominioException;
    void desativarVaga(Long id, Long versao) throws EntidadeNaoLocalizada;
    void reativarVaga(Long id, Long versao) throws EntidadeNaoLocalizada;
    List<Vaga> buscarVagasPorTitulo(String titulo);
    void adicionarRequisitoAVaga(Long vagaId, String requisito) throws EntidadeNaoLocalizada, ValidacaoDominioException;
    List<Vaga> buscarVagasRecentes();
    Integer contarCandidaturasPorVaga(Long vagaId);
}