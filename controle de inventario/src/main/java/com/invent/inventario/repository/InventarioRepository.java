package com.invent.inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.invent.inventario.entity.Inventario;

/**
 * Interface de repositório para a entidade Inventario.
 * Estende JpaRepository para fornecer operações CRUD básicas e busca personalizada.
 */
@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long>, JpaSpecificationExecutor<Inventario> {
    // JpaRepository fornece métodos CRUD básicos (save, findById, findAll, deleteById, etc.)
    // automaticamente para a entidade Inventario com chave primária do tipo Long.

    /**
     * Método customizado para buscar itens de inventário pelo nome, ignorando maiúsculas/minúsculas.
     * O Spring Data JPA cria a query SQL automaticamente com base no nome do método.
     * @param nome Termo de busca para o nome do item.
     * @return Uma lista de itens de inventário que correspondem ao termo de busca.
     */
    List<Inventario> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca itens de inventário pelo status 'ativo'.
     * @param ativo O status (true para ativo, false para inativo).
     * @return Uma lista de itens de inventário com o status especificado.
     */
    List<Inventario> findByAtivo(boolean ativo);

    /**
     * Busca itens de inventário pelo setor, ignorando maiúsculas/minúsculas.
     * @param setor Termo de busca para o setor do item.
     * @return Uma lista de itens de inventário que correspondem ao setor especificado.
     */
    List<Inventario> findBySetorContainingIgnoreCase(String setor);

    /**
     * Busca itens de inventário pelo nome, status 'ativo' e setor, ignorando maiúsculas/minúsculas.
     * Este método combina os filtros para uma busca mais específica.
     * @param nome Termo de busca para o nome do item.
     * @param ativo O status (true para ativo, false para inativo).
     * @param setor Termo de busca para o setor do item.
     * @return Uma lista de itens de inventário que correspondem aos critérios.
     */
    List<Inventario> findByNomeContainingIgnoreCaseAndAtivoAndSetorContainingIgnoreCase(String nome, boolean ativo, String setor);

    /**
     * Busca itens de inventário pelo nome e status 'ativo', ignorando maiúsculas/minúsculas.
     * @param nome Termo de busca para o nome do item.
     * @param ativo O status (true para ativo, false para inativo).
     * @return Uma lista de itens de inventário que correspondem aos critérios.
     */
    List<Inventario> findByNomeContainingIgnoreCaseAndAtivo(String nome, boolean ativo);

    /**
     * Busca itens de inventário pelo nome e setor, ignorando maiúsculas/minúsculas.
     * @param nome Termo de busca para o nome do item.
     * @param setor Termo de busca para o setor do item.
     * @return Uma lista de itens de inventário que correspondem aos critérios.
     */
    List<Inventario> findByNomeContainingIgnoreCaseAndSetorContainingIgnoreCase(String nome, String setor);

    /**
     * Busca itens de inventário pelo status 'ativo' e setor, ignorando maiúsculas/minúsculas.
     * Este é o método que adicionamos especificamente para evitar o NPE reportado,
     * permitindo que o banco de dados filtre diretamente por ativo e setor.
     * @param ativo O status (true para ativo, false para inativo).
     * @param setor Termo de busca para o setor do item.
     * @return Uma lista de itens de inventário que correspondem aos critérios.
     */
    List<Inventario> findByAtivoAndSetorContainingIgnoreCase(boolean ativo, String setor);
}
