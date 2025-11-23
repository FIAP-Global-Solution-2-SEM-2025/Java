package com.br.fiap.skillsfast.infrastructure.api.rest.controllers;

import com.br.fiap.skillsfast.domain.exceptions.EntidadeNaoLocalizada;
import com.br.fiap.skillsfast.domain.exceptions.ValidacaoDominioException;
import com.br.fiap.skillsfast.domain.model.Vaga;
import com.br.fiap.skillsfast.domain.service.VagaService;
import com.br.fiap.skillsfast.infrastructure.interfaces.dto.input.VagaInputDto;
import com.br.fiap.skillsfast.infrastructure.interfaces.dto.output.VagaOutputDto;
import com.br.fiap.skillsfast.infrastructure.interfaces.mappers.VagaMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/vagas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class VagaController {

    @Inject
    VagaService vagaService;

    //@POST
    //public Response criarVaga(VagaInputDto vagaInput) {
    //    try {
    //        Vaga vaga = VagaMapper.toDomain(vagaInput);
    //        Vaga vagaCriada = vagaService.criarVaga(vaga);
    //        VagaOutputDto output = VagaMapper.toOutputDto(vagaCriada);
    //        return Response.status(Response.Status.CREATED).entity(output).build();
    //    } catch (ValidacaoDominioException | EntidadeNaoLocalizada e) {
    //        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    //    } catch (Exception e) {
    //        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
    //    }
    //}

    @GET
    @Path("/{id}")
    public Response buscarVagaPorId(@PathParam("id") Long id) {
        try {
            Vaga vaga = vagaService.buscarVagaPorId(id);
            VagaOutputDto output = VagaMapper.toOutputDto(vaga);
            return Response.ok(output).build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @GET
    @Path("/empresa/{empresaId}")
    public Response buscarVagasPorEmpresa(@PathParam("empresaId") Long empresaId) {
        try {
            List<Vaga> vagas = vagaService.buscarVagasPorEmpresa(empresaId);
            List<VagaOutputDto> output = vagas.stream()
                    .map(VagaMapper::toOutputDto)
                    .collect(Collectors.toList());
            return Response.ok(output).build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @GET
    @Path("/ativas")
    public Response buscarVagasAtivas() {
        try {
            List<Vaga> vagas = vagaService.buscarVagasAtivas();
            List<VagaOutputDto> output = vagas.stream()
                    .map(VagaMapper::toOutputDto)
                    .collect(Collectors.toList());
            return Response.ok(output).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @GET
    @Path("/recentes")
    public Response buscarVagasRecentes() {
        try {
            List<Vaga> vagas = vagaService.buscarVagasRecentes();
            List<VagaOutputDto> output = vagas.stream()
                    .map(VagaMapper::toOutputDto)
                    .collect(Collectors.toList());
            return Response.ok(output).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @GET
    @Path("/filtrar")
    public Response buscarVagasPorFiltro(
            @QueryParam("tipo") String tipo,
            @QueryParam("nivel") String nivel,
            @QueryParam("localizacao") String localizacao,
            @QueryParam("titulo") String titulo) {
        try {
            List<Vaga> vagas = vagaService.buscarVagasPorFiltro(tipo, nivel, localizacao, titulo);
            List<VagaOutputDto> output = vagas.stream()
                    .map(VagaMapper::toOutputDto)
                    .collect(Collectors.toList());
            return Response.ok(output).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizarVaga(@PathParam("id") Long id, VagaInputDto vagaInput) {
        try {
            Vaga vaga = VagaMapper.toDomain(vagaInput);
            Vaga vagaAtualizada = vagaService.atualizarVaga(id, vaga);
            VagaOutputDto output = VagaMapper.toOutputDto(vagaAtualizada);
            return Response.ok(output).build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ValidacaoDominioException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @PATCH
    @Path("/{id}/desativar")
    public Response desativarVaga(@PathParam("id") Long id, @QueryParam("versao") Long versao) {
        try {
            vagaService.desativarVaga(id, versao);
            return Response.ok("Vaga desativada com sucesso").build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @PATCH
    @Path("/{id}/reativar")
    public Response reativarVaga(@PathParam("id") Long id, @QueryParam("versao") Long versao) {
        try {
            vagaService.reativarVaga(id, versao);
            return Response.ok("Vaga reativada com sucesso").build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @POST
    @Path("/{id}/requisitos")
    public Response adicionarRequisito(@PathParam("id") Long id, @QueryParam("requisito") String requisito) {
        try {
            vagaService.adicionarRequisitoAVaga(id, requisito);
            return Response.ok("Requisito adicionado com sucesso").build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ValidacaoDominioException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @GET
    @Path("/{id}/candidaturas/count")
    public Response contarCandidaturasPorVaga(@PathParam("id") Long id) {
        try {
            Integer count = vagaService.contarCandidaturasPorVaga(id);
            return Response.ok(count).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @POST
    public Response criarVaga(VagaInputDto vagaInput) {
        try {
            System.out.println("=== [CONTROLLER] RECEBENDO VAGA ===");
            System.out.println("[CONTROLLER] Titulo: " + vagaInput.getTitulo());
            System.out.println("[CONTROLLER] EmpresaId: " + vagaInput.getEmpresaId());
            System.out.println("[CONTROLLER] EmpresaNome: " + vagaInput.getEmpresaNome());
            System.out.println("[CONTROLLER] Descricao: " + vagaInput.getDescricao());

            Vaga vaga = VagaMapper.toDomain(vagaInput);
            System.out.println("[CONTROLLER] Vaga mapeada: " + vaga.getTitulo());

            Vaga vagaCriada = vagaService.criarVaga(vaga);
            VagaOutputDto output = VagaMapper.toOutputDto(vagaCriada);
            return Response.status(Response.Status.CREATED).entity(output).build();

        } catch (ValidacaoDominioException | EntidadeNaoLocalizada e) {
            System.err.println("[CONTROLLER] ERRO DE VALIDAÇÃO: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            System.err.println("[CONTROLLER] ERRO INTERNO: " + e.getMessage());
            e.printStackTrace(); // ← ISSO VAI MOSTRAR A STACK TRACE REAL
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno do servidor: " + e.getMessage())
                    .build();
        }
    }

}