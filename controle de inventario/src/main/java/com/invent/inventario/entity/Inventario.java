package com.invent.inventario.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull; // Importação para validação de boolean
import jakarta.validation.constraints.Size;

/**
 * Entidade que representa um item de inventário no banco de dados.
 * Mapeia a classe para uma tabela no banco de dados.
 */
@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do item é obrigatório")
    @Column(nullable = false)
    private String nome;

    private Integer etiqueta;

    private String numeroSerie;

    private String usuario;

    @Size(max = 500, message = "As observações não podem ter mais de 500 caracteres")
    @Column(length = 500)
    private String observacoes;

    @NotNull(message = "O status 'ativo' é obrigatório") // Validação para o campo ativo
    @Column(nullable = false)
    private Boolean ativo; // ALTERADO: Agora é Boolean (classe wrapper) para permitir null

    @NotBlank(message = "O setor é obrigatório") // Validação para o campo setor
    @Size(max = 100, message = "O setor não pode ter mais de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String setor; // Novo campo: setor onde o item está localizado

    // Construtor padrão (necessário para JPA)
    public Inventario() {
    }

    // Construtor com todos os campos (útil para criação de objetos)
    public Inventario(Long id, String nome, Integer etiqueta, String numeroSerie, String usuario, String observacoes, Boolean ativo, String setor) {
        this.id = id;
        this.nome = nome;
        this.etiqueta = etiqueta;
        this.numeroSerie = numeroSerie;
        this.usuario = usuario;
        this.observacoes = observacoes;
        this.ativo = ativo; // Agora aceita Boolean
        this.setor = setor;
    }

    // Métodos Getters e Setters para todos os campos
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

    public Boolean getAtivo() { // ALTERADO: Getter agora retorna Boolean
        return ativo;
    }

    public void setAtivo(Boolean ativo) { // ALTERADO: Setter agora aceita Boolean
        this.ativo = ativo;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }
}
