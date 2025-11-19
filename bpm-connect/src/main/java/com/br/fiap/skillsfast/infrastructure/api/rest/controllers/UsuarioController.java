package com.br.fiap.skillsfast.infrastructure.api.rest.controllers;

import com.br.fiap.skillsfast.domain.exceptions.EntidadeNaoLocalizada;
import com.br.fiap.skillsfast.domain.exceptions.ValidacaoDominioException;
import com.br.fiap.skillsfast.domain.model.Usuario;
import com.br.fiap.skillsfast.domain.service.UsuarioService;
import com.br.fiap.skillsfast.infrastructure.interfaces.dto.input.LoginInputDto;
import com.br.fiap.skillsfast.infrastructure.interfaces.dto.input.UsuarioInputDto;
import com.br.fiap.skillsfast.infrastructure.interfaces.dto.output.LoginOutputDto;
import com.br.fiap.skillsfast.infrastructure.interfaces.dto.output.UsuarioOutputDto;
import com.br.fiap.skillsfast.infrastructure.interfaces.mappers.UsuarioMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class UsuarioController {

    @Inject
    UsuarioService usuarioService;

    @POST
    public Response criarUsuario(UsuarioInputDto usuarioInput) {
        try {
            Usuario usuario = UsuarioMapper.toDomain(usuarioInput);
            Usuario usuarioCriado = usuarioService.criarUsuario(usuario);
            UsuarioOutputDto output = UsuarioMapper.toOutputDto(usuarioCriado);
            return Response.status(Response.Status.CREATED).entity(output).build();
        } catch (ValidacaoDominioException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarUsuarioPorId(@PathParam("id") Long id) {
        try {
            Usuario usuario = usuarioService.buscarUsuarioPorId(id);
            UsuarioOutputDto output = UsuarioMapper.toOutputDto(usuario);
            return Response.ok(output).build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @GET
    @Path("/email/{email}")
    public Response buscarUsuarioPorEmail(@PathParam("email") String email) {
        try {
            Usuario usuario = usuarioService.buscarUsuarioPorEmail(email);
            UsuarioOutputDto output = UsuarioMapper.toOutputDto(usuario);
            return Response.ok(output).build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizarUsuario(@PathParam("id") Long id, UsuarioInputDto usuarioInput) {
        try {
            Usuario usuario = UsuarioMapper.toDomain(usuarioInput);
            Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuario);
            UsuarioOutputDto output = UsuarioMapper.toOutputDto(usuarioAtualizado);
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
    public Response listarTodosUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
            List<UsuarioOutputDto> output = usuarios.stream()
                    .map(UsuarioMapper::toOutputDto)
                    .collect(Collectors.toList());
            return Response.ok(output).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @PATCH
    @Path("/{id}/desativar")
    public Response desativarUsuario(@PathParam("id") Long id, @QueryParam("versao") Long versao) {
        try {
            usuarioService.desativarUsuario(id, versao);
            return Response.ok("Usuário desativado com sucesso").build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @PATCH
    @Path("/{id}/reativar")
    public Response reativarUsuario(@PathParam("id") Long id, @QueryParam("versao") Long versao) {
        try {
            usuarioService.reativarUsuario(id, versao);
            return Response.ok("Usuário reativado com sucesso").build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @POST
    @Path("/login")
    public Response login(LoginInputDto loginInput) {
        try {
            boolean credenciaisValidas = usuarioService.validarCredenciais(loginInput.getEmail(), loginInput.getSenha());
            if (credenciaisValidas) {
                Usuario usuario = usuarioService.buscarUsuarioPorEmail(loginInput.getEmail());
                LoginOutputDto output = new LoginOutputDto(true, "Login realizado com sucesso", usuario.getId(), usuario.getTipo());
                return Response.ok(output).build();
            } else {
                LoginOutputDto output = new LoginOutputDto(false, "Credenciais inválidas");
                return Response.status(Response.Status.UNAUTHORIZED).entity(output).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }

    @PATCH
    @Path("/{id}/senha")
    public Response alterarSenha(
            @PathParam("id") Long id,
            @QueryParam("senhaAtual") String senhaAtual,
            @QueryParam("novaSenha") String novaSenha) {
        try {
            usuarioService.alterarSenha(id, senhaAtual, novaSenha);
            return Response.ok("Senha alterada com sucesso").build();
        } catch (EntidadeNaoLocalizada e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ValidacaoDominioException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno do servidor").build();
        }
    }
}