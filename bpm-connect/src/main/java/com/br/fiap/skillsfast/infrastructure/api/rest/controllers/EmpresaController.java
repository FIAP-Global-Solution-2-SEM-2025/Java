package com.br.fiap.skillsfast.infrastructure.api.rest.controllers;

import com.br.fiap.skillsfast.domain.exceptions.EntidadeNaoLocalizada;
import com.br.fiap.skillsfast.domain.exceptions.ValidacaoDominioException;
import com.br.fiap.skillsfast.domain.model.Empresa;
import com.br.fiap.skillsfast.domain.service.EmpresaService;
import com.br.fiap.skillsfast.infrastructure.interfaces.dto.input.EmpresaInputDto;
import com.br.fiap.skillsfast.infrastructure.interfaces.dto.output.EmpresaOutputDto;
import com.br.fiap.skillsfast.infrastructure.interfaces.mappers.EmpresaMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/empresas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class EmpresaController {

    @Inject
    EmpresaService empresaService;

    @POST
    public Response criarEmpresa(EmpresaInputDto empresaInput) {
        try {
            System.out.println("[DEBUG] Recebendo criação de empresa: " + empresaInput.getNome());

            // Adicionar debug para ver os dados recebidos
            System.out.println("CNPJ: " + empresaInput.getCnpj());
            System.out.println("Descrição: " + empresaInput.getDescricao());
            System.out.println("Setor: " + empresaInput.getSetor());
            System.out.println("Tamanho: " + empresaInput.getTamanho());

            Empresa empresa = EmpresaMapper.toDomain(empresaInput);
            System.out.println("[DEBUG] Empresa mapeada: " + empresa.getNome());

            Empresa empresaCriada = empresaService.criarEmpresa(empresa);
            System.out.println("[DEBUG] Empresa criada com ID: " + empresaCriada.getId());

            EmpresaOutputDto output = EmpresaMapper.toOutputDto(empresaCriada);
            return Response.status(Response.Status.CREATED).entity(output).build();

        } catch (ValidacaoDominioException e) {
            System.err.println("[ERRO VALIDAÇÃO] " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            System.err.println("[ERRO INTERNO] " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarEmpresaPorId(@PathParam("id") Long id) {
        try {
            Empresa empresa = empresaService.buscarEmpresaPorId(id);
            EmpresaOutputDto output = EmpresaMapper.toOutputDto(empresa);
            return Response.ok(output).build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @GET
    @Path("/cnpj/{cnpj}")
    public Response buscarEmpresaPorCnpj(@PathParam("cnpj") String cnpj) {
        try {
            Empresa empresa = empresaService.buscarEmpresaPorCnpj(cnpj);
            EmpresaOutputDto output = EmpresaMapper.toOutputDto(empresa);
            return Response.ok(output).build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizarEmpresa(@PathParam("id") Long id, EmpresaInputDto empresaInput) {
        try {
            Empresa empresa = EmpresaMapper.toDomain(empresaInput);
            Empresa empresaAtualizada = empresaService.atualizarEmpresa(id, empresa);
            EmpresaOutputDto output = EmpresaMapper.toOutputDto(empresaAtualizada);
            return Response.ok(output).build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ValidacaoDominioException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @GET
    public Response listarTodasEmpresas() {
        try {
            List<Empresa> empresas = empresaService.listarTodasEmpresas();
            List<EmpresaOutputDto> output = empresas.stream()
                    .map(EmpresaMapper::toOutputDto)
                    .collect(Collectors.toList());
            return Response.ok(output).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @PATCH
    @Path("/{id}/desativar")
    public Response desativarEmpresa(@PathParam("id") Long id, @QueryParam("versao") Long versao) {
        try {
            empresaService.desativarEmpresa(id, versao);
            return Response.ok("Empresa desativada com sucesso").build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @PATCH
    @Path("/{id}/reativar")
    public Response reativarEmpresa(@PathParam("id") Long id, @QueryParam("versao") Long versao) {
        try {
            empresaService.reativarEmpresa(id, versao);
            return Response.ok("Empresa reativada com sucesso").build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @GET
    @Path("/setor/{setor}")
    public Response buscarEmpresasPorSetor(@PathParam("setor") String setor) {
        try {
            List<Empresa> empresas = empresaService.buscarEmpresasPorSetor(setor);
            List<EmpresaOutputDto> output = empresas.stream()
                    .map(EmpresaMapper::toOutputDto)
                    .collect(Collectors.toList());
            return Response.ok(output).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @GET
    @Path("/tamanho/{tamanho}")
    public Response buscarEmpresasPorTamanho(@PathParam("tamanho") String tamanho) {
        try {
            List<Empresa> empresas = empresaService.buscarEmpresasPorTamanho(tamanho);
            List<EmpresaOutputDto> output = empresas.stream()
                    .map(EmpresaMapper::toOutputDto)
                    .collect(Collectors.toList());
            return Response.ok(output).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }
}