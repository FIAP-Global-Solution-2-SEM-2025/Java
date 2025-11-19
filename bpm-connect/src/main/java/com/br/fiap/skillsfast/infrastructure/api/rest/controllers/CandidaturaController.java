package com.br.fiap.skillsfast.infrastructure.api.rest.controllers;

import com.br.fiap.skillsfast.domain.exceptions.EntidadeNaoLocalizada;
import com.br.fiap.skillsfast.domain.exceptions.ValidacaoDominioException;
import com.br.fiap.skillsfast.domain.model.Candidatura;
import com.br.fiap.skillsfast.domain.service.CandidaturaService;
import com.br.fiap.skillsfast.infrastructure.interfaces.dto.input.CandidaturaInputDto;
import com.br.fiap.skillsfast.infrastructure.interfaces.dto.output.CandidaturaOutputDto;
import com.br.fiap.skillsfast.infrastructure.interfaces.mappers.CandidaturaMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/candidaturas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class CandidaturaController {

    @Inject
    CandidaturaService candidaturaService;

    @GET
    public Response listarTodasCandidaturas() {
        try {
            List<Candidatura> candidaturas = candidaturaService.listarTodasCandidaturas();
            List<CandidaturaOutputDto> output = candidaturas.stream()
                    .map(CandidaturaMapper::toOutputDto)
                    .collect(Collectors.toList());
            return Response.ok(output).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @POST
    public Response candidatarAVaga(CandidaturaInputDto candidaturaInput) {
        try {
            Candidatura candidatura = CandidaturaMapper.toDomain(candidaturaInput);
            Candidatura candidaturaCriada = candidaturaService.candidatarAVaga(candidaturaInput.getUsuarioId(), candidaturaInput.getVagaId());
            CandidaturaOutputDto output = CandidaturaMapper.toOutputDto(candidaturaCriada);
            return Response.status(Response.Status.CREATED).entity(output).build();
        } catch (ValidacaoDominioException | EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarCandidaturaPorId(@PathParam("id") Long id) {
        try {
            Candidatura candidatura = candidaturaService.buscarCandidaturaPorId(id);
            CandidaturaOutputDto output = CandidaturaMapper.toOutputDto(candidatura);
            return Response.ok(output).build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @GET
    @Path("/usuario/{usuarioId}")
    public Response buscarCandidaturasPorUsuario(@PathParam("usuarioId") Long usuarioId) {
        try {
            List<Candidatura> candidaturas = candidaturaService.buscarCandidaturasPorUsuario(usuarioId);
            List<CandidaturaOutputDto> output = candidaturas.stream()
                    .map(CandidaturaMapper::toOutputDto)
                    .collect(Collectors.toList());
            return Response.ok(output).build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @GET
    @Path("/vaga/{vagaId}")
    public Response buscarCandidaturasPorVaga(@PathParam("vagaId") Long vagaId) {
        try {
            List<Candidatura> candidaturas = candidaturaService.buscarCandidaturasPorVaga(vagaId);
            List<CandidaturaOutputDto> output = candidaturas.stream()
                    .map(CandidaturaMapper::toOutputDto)
                    .collect(Collectors.toList());
            return Response.ok(output).build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @GET
    @Path("/empresa/{empresaId}")
    public Response buscarCandidaturasPorEmpresa(@PathParam("empresaId") Long empresaId) {
        try {
            List<Candidatura> candidaturas = candidaturaService.buscarCandidaturasPorEmpresa(empresaId);
            List<CandidaturaOutputDto> output = candidaturas.stream()
                    .map(CandidaturaMapper::toOutputDto)
                    .collect(Collectors.toList());
            return Response.ok(output).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @PATCH
    @Path("/{id}/status")
    public Response atualizarStatusCandidatura(
            @PathParam("id") Long id,
            @QueryParam("status") String status,
            @QueryParam("observacao") String observacao) {
        try {
            Candidatura candidatura = candidaturaService.atualizarStatusCandidatura(id, status, observacao);
            CandidaturaOutputDto output = CandidaturaMapper.toOutputDto(candidatura);
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
    @Path("/{id}/aprovar")
    public Response aprovarCandidatura(
            @PathParam("id") Long id,
            @QueryParam("observacao") String observacao) {
        try {
            Candidatura candidatura = candidaturaService.aprovarCandidatura(id, observacao);
            CandidaturaOutputDto output = CandidaturaMapper.toOutputDto(candidatura);
            return Response.ok(output).build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @PATCH
    @Path("/{id}/rejeitar")
    public Response rejeitarCandidatura(
            @PathParam("id") Long id,
            @QueryParam("observacao") String observacao) {
        try {
            Candidatura candidatura = candidaturaService.rejeitarCandidatura(id, observacao);
            CandidaturaOutputDto output = CandidaturaMapper.toOutputDto(candidatura);
            return Response.ok(output).build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @PATCH
    @Path("/{id}/cancelar")
    public Response cancelarCandidatura(@PathParam("id") Long id, @QueryParam("versao") Long versao) {
        try {
            candidaturaService.cancelarCandidatura(id, versao);
            return Response.ok("Candidatura cancelada com sucesso").build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ValidacaoDominioException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @GET
    @Path("/verificar")
    public Response verificarCandidatura(
            @QueryParam("usuarioId") Long usuarioId,
            @QueryParam("vagaId") Long vagaId) {
        try {
            boolean jaCandidatou = candidaturaService.usuarioJaCandidatou(usuarioId, vagaId);
            return Response.ok(jaCandidatou).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }
}