package com.br.fiap.skillsfast.domain.repository;

import com.br.fiap.skillsfast.domain.model.Empresa;
import java.util.List;
import java.util.Optional;

public interface EmpresaRepository {
    Empresa salvar(Empresa empresa);
    Optional<Empresa> buscarPorId(Long id);
    Optional<Empresa> buscarPorCnpj(String cnpj);
    List<Empresa> buscarTodas();
    void atualizar(Empresa empresa);
    void desativar(Long id, Long versao);
    void reativar(Long id, Long versao);
    List<Empresa> buscarPorSetor(String setor);
    List<Empresa> buscarPorTamanho(String tamanho);
}