package com.br.fiap.skillsfast.infrastructure.exceptions;

import com.br.fiap.skillsfast.domain.exceptions.EntidadeNaoLocalizada;
import com.br.fiap.skillsfast.domain.exceptions.ValidacaoDominioException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler {

    @Provider
    public static class EntidadeNaoLocalizadaHandler implements ExceptionMapper<EntidadeNaoLocalizada> {
        @Override
        public Response toResponse(EntidadeNaoLocalizada exception) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(exception.getMessage()))
                    .build();
        }
    }

    @Provider
    public static class ValidacaoDominioExceptionHandler implements ExceptionMapper<ValidacaoDominioException> {
        @Override
        public Response toResponse(ValidacaoDominioException exception) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(exception.getMessage()))
                    .build();
        }
    }

    @Provider
    public static class InfraestruturaExceptionHandler implements ExceptionMapper<InfraestruturaException> {
        @Override
        public Response toResponse(InfraestruturaException exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erro interno do servidor: " + exception.getMessage()))
                    .build();
        }
    }

    @Provider
    public static class GenericExceptionHandler implements ExceptionMapper<Exception> {
        @Override
        public Response toResponse(Exception exception) {
            exception.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erro interno do servidor"))
                    .build();
        }
    }

    public static class ErrorResponse {
        private String message;
        private Long timestamp;

        public ErrorResponse(String message) {
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public Long getTimestamp() { return timestamp; }
        public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
    }
}