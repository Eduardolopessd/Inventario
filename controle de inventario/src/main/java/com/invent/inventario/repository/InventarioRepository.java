package com.invent.inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository; // Interface principal do Spring Data JPA
import org.springframework.stereotype.Repository; // Anotação para componentes de repositório

import com.invent.inventario.entity.Inventario;

/**
 * Interface de repositório para a entidade Inventario.
 * Estende JpaRepository para fornecer operações CRUD básicas e busca personalizada.
 */
@Repository // Indica que esta interface é um componente de repositório gerenciado pelo Spring.
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    // JpaRepository fornece métodos CRUD básicos (save, findById, findAll, deleteById, etc.)
    // automaticamente para a entidade Inventario com chave primária do tipo Long.

    /**
     * Método customizado para buscar itens de inventário pelo nome, ignorando maiúsculas/minúsculas.
     * O Spring Data JPA cria a query SQL automaticamente com base no nome do método.
     * param nome Termo de busca para o nome do item.
     * return Uma lista de itens de inventário que correspondem ao termo de busca.
     */
    List<Inventario> findByNomeContainingIgnoreCase(String nome);
}
