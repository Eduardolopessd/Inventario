package com.invent.inventario.controller; // Note: Esta classe deveria estar no pacote 'exception' ou 'handler' para melhor organização.

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map; // Exceção para erros de validação de argumentos de método

import org.springframework.http.HttpStatus; // Anotação para tratamento de exceções global
import org.springframework.http.ResponseEntity; // Anotação para especificar o tipo de exceção a ser tratada
import org.springframework.web.bind.MethodArgumentNotValidException; // Para acessar detalhes da requisição
import org.springframework.web.bind.annotation.ControllerAdvice; // DTO para detalhes de erro
import org.springframework.web.bind.annotation.ExceptionHandler; // Exceção personalizada
import org.springframework.web.context.request.WebRequest;

import com.invent.inventario.exception.ErrorDetails;
import com.invent.inventario.exception.ResourceNotFoundException;

/**
 * Classe de tratamento de exceções global para a API.
 * Captura exceções em toda a aplicação e retorna respostas de erro padronizadas.
 */
@ControllerAdvice // Anotação que permite que esta classe trate exceções de forma global em todos os controladores.
public class GlobalExceptionHandler {

    /**
     * Trata ResourceNotFoundException.
     * Retorna status 404 Not Found com um corpo de erro padronizado.
     * @param ex A exceção ResourceNotFoundException que foi lançada.
     * @param request O contexto da requisição web.
     * @return ResponseEntity com ErrorDetails e status HTTP 404 NOT_FOUND.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(), // Timestamp atual
                ex.getMessage(), // Mensagem da exceção
                request.getDescription(false), // Descrição da requisição (ex: "uri=/api/inventario/itens/1")
                "NOT_FOUND_ERROR" // Código de erro personalizado
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Trata exceções de validação de argumentos de método (ex: quando @Valid falha no @RequestBody).
     * Retorna status 400 Bad Request com detalhes dos erros de validação.
     * @param ex A exceção MethodArgumentNotValidException.
     * @param request O contexto da requisição web.
     * @return ResponseEntity com ErrorDetails (incluindo erros de validação) e status HTTP 400 BAD_REQUEST.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        // Coleta todos os erros de campo da exceção de validação.
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage()); // Mapeia nome do campo -> mensagem de erro.
        });

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "Erro de validação", // Mensagem geral para erros de validação
                request.getDescription(false),
                "VALIDATION_ERROR", // Código de erro personalizado para validação
                errors // Inclui o mapa com os detalhes dos erros de validação
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Trata todas as outras exceções genéricas (catch-all).
     * Retorna status 500 Internal Server Error.
     * Em ambiente de produção, é recomendável retornar uma mensagem mais genérica por segurança.
     * @param ex A exceção genérica.
     * @param request O contexto da requisição web.
     * @return ResponseEntity com ErrorDetails e status HTTP 500 INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(
            Exception ex, WebRequest request) {

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(), // Mensagem da exceção (pode ser mais genérica em produção)
                request.getDescription(false),
                "INTERNAL_SERVER_ERROR" // Código de erro personalizado
        );
        // Em produção, você pode querer logar a exceção completa, mas retornar uma mensagem mais genérica ao cliente
        // Logger.error("Erro interno do servidor: ", ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
