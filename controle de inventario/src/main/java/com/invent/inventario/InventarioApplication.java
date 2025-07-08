package com.invent.inventario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Classe principal da aplicação Spring Boot.
 * Esta é a porta de entrada para a aplicação.
 */
@SpringBootApplication // Anotação que combina @Configuration, @EnableAutoConfiguration e @ComponentScan.
                       // Ela habilita a configuração automática do Spring Boot e a varredura de componentes.
public class InventarioApplication {
    /**
     * Método principal que inicia a aplicação Spring Boot.
     * param args Argumentos de linha de comando.
     */
    public static void main(String[] args) {
        SpringApplication.run(InventarioApplication.class, args);  // Inicia a aplicação.
    }

}