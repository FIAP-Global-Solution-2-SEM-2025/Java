package com.br.fiap.skillsfast.domain.repository;

import com.br.fiap.skillsfast.domain.model.Candidatura;
import java.util.List;
import java.util.Optional;

public interface CandidaturaRepository {
    Candidatura salvar(Candidatura candidatura);
    Optional<Candidatura> buscarPorId(Long id);
    List<Candidatura> buscarTodas();
    List<Candidatura> buscarPorUsuario(Long usuarioId);
    List<Candidatura> buscarPorVaga(Long vagaId);
    List<Candidatura> buscarPorEmpresa(Long empresaId);
    void atualizar(Candidatura candidatura);
    boolean usuarioJaCandidatou(Long usuarioId, Long vagaId);
}