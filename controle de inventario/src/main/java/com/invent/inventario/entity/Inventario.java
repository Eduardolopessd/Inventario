package com.invent.inventario.entity;

import jakarta.persistence.Column; // Pacote para anotações JPA (Java Persistence API)
import jakarta.persistence.Entity; // Anotação para validação de campos não nulos/vazios
import jakarta.persistence.GeneratedValue; // Anotação para validação de tamanho de string
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entidade que representa um item de inventário no banco de dados.
 * Mapeia a classe para uma tabela no banco de dados.
 */
@Entity // Indica que esta classe é uma entidade JPA e será mapeada para uma tabela.
@Table(name = "inventario") // Especifica o nome da tabela no banco de dados.
public class Inventario {

    @Id // Marca o campo como chave primária da tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura a estratégia de geração de valor para a chave primária (auto-incremento).
    private Long id;

    @NotBlank(message = "O nome do item é obrigatório") // Valida que o campo 'nome' não pode ser nulo ou vazio.
    @Column(nullable = false) // Mapeia o campo para uma coluna no banco de dados, indicando que não pode ser nula.
    private String nome;

    private Integer etiqueta; // Campo para a etiqueta do item.

    private String numeroSerie; // Campo para o número de série.

    private String usuario; // Campo para o usuário associado ao item.

    @Size(max = 500, message = "As observações não podem ter mais de 500 caracteres") // Valida o tamanho máximo da string.
    @Column(length = 500) // Define o comprimento máximo da coluna no banco de dados.
    private String observacoes;

    // Construtor padrão (necessário para JPA)
    public Inventario() {
    }

    // Construtor com todos os campos (útil para criação de objetos)
    public Inventario(Long id, String nome, Integer etiqueta, String numeroSerie, String usuario, String observacoes) {
        this.id = id;
        this.nome = nome;
        this.etiqueta = etiqueta;
        this.numeroSerie = numeroSerie;
        this.usuario = usuario;
        this.observacoes = observacoes;
    }

    // Métodos Getters e Setters para todos os campos (Lombok pode gerar automaticamente com @Data)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(Integer etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
