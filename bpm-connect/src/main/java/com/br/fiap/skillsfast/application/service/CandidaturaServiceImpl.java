package com.br.fiap.skillsfast.application.service;

import com.br.fiap.skillsfast.domain.exceptions.EntidadeNaoLocalizada;
import com.br.fiap.skillsfast.domain.exceptions.ValidacaoDominioException;
import com.br.fiap.skillsfast.domain.model.Candidatura;
import com.br.fiap.skillsfast.domain.model.Usuario;
import com.br.fiap.skillsfast.domain.model.Vaga;
import com.br.fiap.skillsfast.domain.repository.CandidaturaRepository;
import com.br.fiap.skillsfast.domain.service.CandidaturaService;
import com.br.fiap.skillsfast.domain.service.UsuarioService;
import com.br.fiap.skillsfast.domain.service.VagaService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CandidaturaServiceImpl implements CandidaturaService {

    @Inject
    CandidaturaRepository candidaturaRepository;

    @Inject
    UsuarioService usuarioService;

    @Inject
    VagaService vagaService;

    @Override
    public Candidatura candidatarAVaga(Long usuarioId, Long vagaId) throws ValidacaoDominioException, EntidadeNaoLocalizada {
        Usuario usuario = usuarioService.buscarUsuarioPorId(usuarioId);
        Vaga vaga = vagaService.buscarVagaPorId(vagaId);

        if (!vaga.isAtiva()) {
            throw new ValidacaoDominioException("Não é possível candidatar-se a uma vaga inativa");
        }

        if (usuarioJaCandidatou(usuarioId, vagaId)) {
            throw new ValidacaoDominioException("Usuário já se candidatou a esta vaga");
        }

        Candidatura candidatura = new Candidatura(usuarioId, vagaId);
        return candidaturaRepository.salvar(candidatura);
    }

    @Override
    public Candidatura buscarCandidaturaPorId(Long id) throws EntidadeNaoLocalizada {
        return candidaturaRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoLocalizada("Candidatura não encontrada com ID: " + id));
    }

    @Override
    public List<Candidatura> buscarCandidaturasPorUsuario(Long usuarioId) throws EntidadeNaoLocalizada {
        usuarioService.buscarUsuarioPorId(usuarioId);
        return candidaturaRepository.buscarPorUsuario(usuarioId);
    }

    @Override
    public List<Candidatura> buscarCandidaturasPorVaga(Long vagaId) throws EntidadeNaoLocalizada {
        vagaService.buscarVagaPorId(vagaId);
        return candidaturaRepository.buscarPorVaga(vagaId);
    }

    @Override
    public Candidatura atualizarStatusCandidatura(Long id, String status, String observacao) throws EntidadeNaoLocalizada, ValidacaoDominioException {
        Candidatura candidatura = buscarCandidaturaPorId(id);

        candidatura.setStatus(status);
        candidatura.setObservacao(observacao);

        candidaturaRepository.atualizar(candidatura);
        return candidatura;
    }

    @Override
    public void cancelarCandidatura(Long id, Long versao) throws EntidadeNaoLocalizada {
        Candidatura candidatura = buscarCandidaturaPorId(id);

        if (!candidatura.isPendente()) {
            throw new ValidacaoDominioException("Só é possível cancelar candidaturas pendentes");
        }

        candidatura.cancelar();
        candidaturaRepository.atualizar(candidatura);
    }

    @Override
    public boolean usuarioJaCandidatou(Long usuarioId, Long vagaId) {
        return candidaturaRepository.usuarioJaCandidatou(usuarioId, vagaId);
    }

    @Override
    public List<Candidatura> listarTodasCandidaturas() {
        return candidaturaRepository.buscarTodas();
    }

    @Override
    public Candidatura aprovarCandidatura(Long id, String observacao) throws EntidadeNaoLocalizada {
        Candidatura candidatura = buscarCandidaturaPorId(id);
        candidatura.aprovar();
        candidatura.setObservacao(observacao);

        candidaturaRepository.atualizar(candidatura);
        return candidatura;
    }

    @Override
    public Candidatura rejeitarCandidatura(Long id, String observacao) throws EntidadeNaoLocalizada {
        Candidatura candidatura = buscarCandidaturaPorId(id);
        candidatura.rejeitar();
        candidatura.setObservacao(observacao);

        candidaturaRepository.atualizar(candidatura);
        return candidatura;
    }

    @Override
    public List<Candidatura> buscarCandidaturasPorEmpresa(Long empresaId) {
        // Verifica se empresa existe através do serviço de vaga
        try {
            vagaService.buscarVagasPorEmpresa(empresaId); // Isso valida se a empresa existe
            return candidaturaRepository.buscarPorEmpresa(empresaId);
        } catch (EntidadeNaoLocalizada e) {
            // Se não encontrar vagas, retorna lista vazia
            return List.of();
        }
    }
}