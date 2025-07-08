package com.invent.inventario.exception;

import org.springframework.http.HttpStatus; // Para definir o status HTTP da exceção
import org.springframework.web.bind.annotation.ResponseStatus; // Anotação para mapear exceções a status HTTP

/**
 * Exceção personalizada para indicar que um recurso (item de inventário, neste caso) não foi encontrado.
 * Quando esta exceção é lançada, o Spring automaticamente retorna um status HTTP 404 Not Found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND) // Mapeia esta exceção para um status HTTP 404 (Not Found).
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Construtor que aceita uma mensagem de erro.
     * @param message A mensagem detalhada da exceção.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Construtor que aceita uma mensagem de erro e uma causa (outra Throwable).
     * @param message A mensagem detalhada da exceção.
     * @param cause A causa raiz da exceção.
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
