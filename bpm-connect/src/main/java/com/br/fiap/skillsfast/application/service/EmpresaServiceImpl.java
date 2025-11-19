package com.br.fiap.skillsfast.application.service;

import com.br.fiap.skillsfast.domain.exceptions.EntidadeNaoLocalizada;
import com.br.fiap.skillsfast.domain.exceptions.ValidacaoDominioException;
import com.br.fiap.skillsfast.domain.model.Empresa;
import com.br.fiap.skillsfast.domain.repository.EmpresaRepository;
import com.br.fiap.skillsfast.domain.service.EmpresaService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class EmpresaServiceImpl implements EmpresaService {

    @Inject
    EmpresaRepository empresaRepository;

    @Override
    public Empresa criarEmpresa(Empresa empresa) throws ValidacaoDominioException {
        verificarCnpjExistente(empresa.getCnpj());
        return empresaRepository.salvar(empresa);
    }

    @Override
    public Empresa buscarEmpresaPorId(Long id) throws EntidadeNaoLocalizada {
        return empresaRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoLocalizada("Empresa não encontrada com ID: " + id));
    }

    @Override
    public Empresa buscarEmpresaPorCnpj(String cnpj) throws EntidadeNaoLocalizada {
        return empresaRepository.buscarPorCnpj(cnpj)
                .orElseThrow(() -> new EntidadeNaoLocalizada("Empresa não encontrada com CNPJ: " + cnpj));
    }

    @Override
    public Empresa atualizarEmpresa(Long id, Empresa empresa) throws EntidadeNaoLocalizada, ValidacaoDominioException {
        Empresa empresaExistente = buscarEmpresaPorId(id);

        if (!empresaExistente.getCnpj().equals(empresa.getCnpj())) {
            verificarCnpjExistente(empresa.getCnpj());
        }
        empresaExistente.setNome(empresa.getNome());
        empresaExistente.setCnpj(empresa.getCnpj());
        empresaExistente.setDescricao(empresa.getDescricao());
        empresaExistente.setWebsite(empresa.getWebsite());
        empresaExistente.setSetor(empresa.getSetor());
        empresaExistente.setTamanho(empresa.getTamanho());
        empresaExistente.setLogo(empresa.getLogo());

        empresaRepository.atualizar(empresaExistente);
        return empresaExistente;
    }

    @Override
    public List<Empresa> listarTodasEmpresas() {
        return empresaRepository.buscarTodas();
    }

    @Override
    public void desativarEmpresa(Long id, Long versao) throws EntidadeNaoLocalizada {
        Empresa empresa = buscarEmpresaPorId(id);
        empresaRepository.desativar(id, versao);
    }

    @Override
    public void reativarEmpresa(Long id, Long versao) throws EntidadeNaoLocalizada {
        Empresa empresa = buscarEmpresaPorId(id);
        empresaRepository.reativar(id, versao);
    }

    @Override
    public List<Empresa> buscarEmpresasPorSetor(String setor) {
        if (setor == null || setor.trim().isEmpty()) {
            return empresaRepository.buscarTodas().stream()
                    .filter(Empresa::isAtivo)
                    .toList();
        }
        return empresaRepository.buscarPorSetor(setor);
    }

    @Override
    public List<Empresa> buscarEmpresasPorTamanho(String tamanho) {
        if (tamanho == null || tamanho.trim().isEmpty()) {
            return empresaRepository.buscarTodas().stream()
                    .filter(Empresa::isAtivo)
                    .toList();
        }
        return empresaRepository.buscarPorTamanho(tamanho);
    }

    private void verificarCnpjExistente(String cnpj) throws ValidacaoDominioException {
        if (empresaRepository.buscarPorCnpj(cnpj).isPresent()) {
            throw new ValidacaoDominioException("Já existe uma empresa cadastrada com este CNPJ");
        }
    }
}