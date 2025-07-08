package com.invent.inventario.exception;

import java.time.LocalDateTime; // Gera construtor com todos os argumentos
import java.util.Map; // Gera getters, setters, toString, equals, hashCode

import lombok.AllArgsConstructor; // Gera construtor sem argumentos
import lombok.Data; // Para registrar o timestamp do erro
import lombok.NoArgsConstructor; // Para armazenar erros de validação

/**
 * Classe de DTO (Data Transfer Object) para padronizar as respostas de erro da API.
 * Fornece um formato consistente para retornar detalhes de exceções aos clientes.
 */
@Data // Anotação Lombok que gera automaticamente getters, setters, toString(), equals() e hashCode().
@AllArgsConstructor // Anotação Lombok que gera um construtor com todos os campos.
@NoArgsConstructor // Anotação Lombok que gera um construtor sem argumentos.
public class ErrorDetails {
    private LocalDateTime timestamp; // Data e hora em que o erro ocorreu.
    private String message; // Mensagem principal do erro.
    private String details; // Detalhes adicionais, como a descrição da requisição.
    private String errorCode; // Novo campo para um código de erro específico (ex: "NOT_FOUND_ERROR").
    private Map<String, String> validationErrors; // Opcional: para erros de validação de campos (mapa de campo -> mensagem de erro).

    /**
     * Construtor sem validationErrors (para a maioria dos casos de erro que não são de validação).
     * @param timestamp Data e hora do erro.
     * @param message Mensagem do erro.
     * @param details Detalhes da requisição/erro.
     * @param errorCode Código específico do erro.
     */
    public ErrorDetails(LocalDateTime timestamp, String message, String details, String errorCode) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.errorCode = errorCode;
    }
}
