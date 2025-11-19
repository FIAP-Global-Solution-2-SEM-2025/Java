package com.br.fiap.skillsfast.domain.repository;

import com.br.fiap.skillsfast.domain.model.Vaga;
import java.util.List;
import java.util.Optional;

public interface VagaRepository {
    Vaga salvar(Vaga vaga);
    Optional<Vaga> buscarPorId(Long id);
    List<Vaga> buscarTodas();
    List<Vaga> buscarAtivas();
    List<Vaga> buscarPorEmpresa(Long empresaId);
    void atualizar(Vaga vaga);
    void desativar(Long id, Long versao);
    void reativar(Long id, Long versao);
}