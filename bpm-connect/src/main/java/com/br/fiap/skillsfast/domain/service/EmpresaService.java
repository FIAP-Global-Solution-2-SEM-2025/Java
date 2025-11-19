package com.br.fiap.skillsfast.domain.service;

import com.br.fiap.skillsfast.domain.exceptions.EntidadeNaoLocalizada;
import com.br.fiap.skillsfast.domain.exceptions.ValidacaoDominioException;
import com.br.fiap.skillsfast.domain.model.Empresa;

import java.util.List;

public interface EmpresaService {
    Empresa criarEmpresa(Empresa empresa) throws ValidacaoDominioException;
    Empresa buscarEmpresaPorId(Long id) throws EntidadeNaoLocalizada;
    Empresa buscarEmpresaPorCnpj(String cnpj) throws EntidadeNaoLocalizada;
    Empresa atualizarEmpresa(Long id, Empresa empresa) throws EntidadeNaoLocalizada, ValidacaoDominioException;
    List<Empresa> listarTodasEmpresas();
    void desativarEmpresa(Long id, Long versao) throws EntidadeNaoLocalizada;
    void reativarEmpresa(Long id, Long versao) throws EntidadeNaoLocalizada;
    List<Empresa> buscarEmpresasPorSetor(String setor);
    List<Empresa> buscarEmpresasPorTamanho(String tamanho);
}