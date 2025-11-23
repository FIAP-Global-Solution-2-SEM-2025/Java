package com.br.fiap.skillsfast.application.service;

import com.br.fiap.skillsfast.domain.exceptions.EntidadeNaoLocalizada;
import com.br.fiap.skillsfast.domain.exceptions.ValidacaoDominioException;
import com.br.fiap.skillsfast.domain.model.Vaga;
import com.br.fiap.skillsfast.domain.repository.VagaRepository;
import com.br.fiap.skillsfast.domain.service.VagaService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class VagaServiceImpl implements VagaService {

    @Inject
    VagaRepository vagaRepository;

    @Override
    public Vaga criarVaga(Vaga vaga) throws ValidacaoDominioException, EntidadeNaoLocalizada {
        return vagaRepository.salvar(vaga);
    }

    @Override
    public Vaga buscarVagaPorId(Long id) throws EntidadeNaoLocalizada {
        return vagaRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoLocalizada("Vaga não encontrada com ID: " + id));
    }

    @Override
    public List<Vaga> buscarTodasVagas() {
        return vagaRepository.buscarTodas();
    }

    @Override
    public List<Vaga> buscarVagasPorEmpresa(Long empresaId) throws EntidadeNaoLocalizada {
        List<Vaga> vagas = vagaRepository.buscarPorEmpresa(empresaId);
        if (vagas.isEmpty()) {
            throw new EntidadeNaoLocalizada("Nenhuma vaga encontrada para a empresa com ID: " + empresaId);
        }
        return vagas;
    }

    @Override
    public List<Vaga> buscarVagasAtivas() {
        return vagaRepository.buscarAtivas();
    }

    @Override
    public List<Vaga> buscarVagasPorFiltro(String tipo, String nivel, String localizacao, String titulo) {
        List<Vaga> todasVagas = vagaRepository.buscarAtivas();

        return todasVagas.stream()
                .filter(vaga -> tipo == null || tipo.equalsIgnoreCase(vaga.getTipo()))
                .filter(vaga -> nivel == null || nivel.equalsIgnoreCase(vaga.getNivel()))
                .filter(vaga -> localizacao == null || vaga.getLocalizacao().toLowerCase().contains(localizacao.toLowerCase()))
                .filter(vaga -> titulo == null || vaga.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Vaga atualizarVaga(Long id, Vaga vaga) throws EntidadeNaoLocalizada, ValidacaoDominioException {
        Vaga vagaExistente = buscarVagaPorId(id);

        // Atualiza apenas campos permitidos
        vagaExistente.setTitulo(vaga.getTitulo());
        vagaExistente.setDescricao(vaga.getDescricao());
        vagaExistente.setTipo(vaga.getTipo());
        vagaExistente.setNivel(vaga.getNivel());
        vagaExistente.setLocalizacao(vaga.getLocalizacao());
        vagaExistente.setSalario(vaga.getSalario());
        vagaExistente.setRequisitos(vaga.getRequisitos());
        vagaExistente.setEmpresaNome(vaga.getEmpresaNome());

        vagaRepository.atualizar(vagaExistente);
        return vagaExistente;
    }

    @Override
    public void desativarVaga(Long id, Long versao) throws EntidadeNaoLocalizada {
        Vaga vaga = buscarVagaPorId(id);
        vagaRepository.desativar(id, versao);
    }

    @Override
    public void reativarVaga(Long id, Long versao) throws EntidadeNaoLocalizada {
        Vaga vaga = buscarVagaPorId(id);
        vagaRepository.reativar(id, versao);
    }

    @Override
    public List<Vaga> buscarVagasPorTitulo(String titulo) {
        return vagaRepository.buscarAtivas().stream()
                .filter(vaga -> vaga.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public void adicionarRequisitoAVaga(Long vagaId, String requisito) throws EntidadeNaoLocalizada, ValidacaoDominioException {
        Vaga vaga = buscarVagaPorId(vagaId);
        vaga.adicionarRequisito(requisito);
        vagaRepository.atualizar(vaga);
    }

    @Override
    public List<Vaga> buscarVagasRecentes() {
        return vagaRepository.buscarAtivas().stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<Vaga> buscarVagasPorNomeEmpresa(String nomeEmpresa) {
        if (nomeEmpresa == null || nomeEmpresa.trim().isEmpty()) {
            return buscarVagasAtivas();
        }

        return vagaRepository.buscarAtivas().stream()
                .filter(vaga -> vaga.getEmpresaNome() != null &&
                        vaga.getEmpresaNome().toLowerCase().contains(nomeEmpresa.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Vaga> buscarVagasPorTituloENivel(String titulo, String nivel) {
        return List.of();
    }

    @Override
    public List<Vaga> buscarVagasAvancado(String empresa, String titulo, String nivel) {
        return List.of();
    }

    @Override
    public Integer contarCandidaturasPorVaga(Long vagaId) {
        return 0;
    }
}