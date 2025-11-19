package com.br.fiap.skillsfast.domain.service;

import com.br.fiap.skillsfast.domain.exceptions.EntidadeNaoLocalizada;
import com.br.fiap.skillsfast.domain.exceptions.ValidacaoDominioException;
import com.br.fiap.skillsfast.domain.model.Candidatura;

import java.util.List;

public interface CandidaturaService {
    Candidatura candidatarAVaga(Long usuarioId, Long vagaId) throws ValidacaoDominioException, EntidadeNaoLocalizada;
    Candidatura buscarCandidaturaPorId(Long id) throws EntidadeNaoLocalizada;
    List<Candidatura> buscarCandidaturasPorUsuario(Long usuarioId) throws EntidadeNaoLocalizada;
    List<Candidatura> buscarCandidaturasPorVaga(Long vagaId) throws EntidadeNaoLocalizada;
    Candidatura atualizarStatusCandidatura(Long id, String status, String observacao) throws EntidadeNaoLocalizada, ValidacaoDominioException;
    void cancelarCandidatura(Long id, Long versao) throws EntidadeNaoLocalizada;
    boolean usuarioJaCandidatou(Long usuarioId, Long vagaId);
    List<Candidatura> listarTodasCandidaturas();
    Candidatura aprovarCandidatura(Long id, String observacao) throws EntidadeNaoLocalizada;
    Candidatura rejeitarCandidatura(Long id, String observacao) throws EntidadeNaoLocalizada;
    List<Candidatura> buscarCandidaturasPorEmpresa(Long empresaId);
}