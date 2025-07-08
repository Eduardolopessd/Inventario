package com.invent.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired; // Anotação para injeção de dependência
import org.springframework.stereotype.Service; // Anotação para componentes de serviço

import com.invent.inventario.entity.Inventario;
import com.invent.inventario.exception.ResourceNotFoundException; // Exceção personalizada
import com.invent.inventario.repository.InventarioRepository;

/**
 * Classe de serviço que contém a lógica de negócios para a entidade Inventario.
 * Interage com o InventarioRepository para operações de persistência.
 */
@Service // Indica que esta classe é um componente de serviço Spring.
public class InventarioService {

    @Autowired // Injeta uma instância de InventarioRepository (gerenciada pelo Spring).
    private InventarioRepository inventarioRepository;

    /**
     * Busca todos os itens de inventário.
     * @return Uma lista de todos os itens de inventário.
     */
    public List<Inventario> findAll() {
        return inventarioRepository.findAll();
    }

    /**
     * Busca um item de inventário pelo ID.
     * @param id O ID do item a ser buscado.
     * @return Um Optional contendo o item, se encontrado.
     */
    public Optional<Inventario> findById(Long id) {
        return inventarioRepository.findById(id);
    }

    /**
     * Salva um novo item de inventário ou atualiza um existente.
     * @param inventario O item de inventário a ser salvo.
     * @return O item de inventário salvo/atualizado.
     */
    public Inventario save(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    /**
     * Atualiza um item de inventário existente.
     * @param id O ID do item a ser atualizado.
     * @param inventarioDetails Os detalhes do item com as informações atualizadas.
     * @return O item de inventário atualizado.
     * @throws ResourceNotFoundException Se o item com o ID especificado não for encontrado.
     */
    public Inventario update(Long id, Inventario inventarioDetails) {
        // Busca o item pelo ID ou lança uma exceção se não encontrado.
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item de inventário não encontrado com id: " + id));

        // Atualiza os campos do item encontrado com os detalhes fornecidos
        inventario.setNome(inventarioDetails.getNome());
        inventario.setEtiqueta(inventarioDetails.getEtiqueta());
        inventario.setNumeroSerie(inventarioDetails.getNumeroSerie());
        inventario.setUsuario(inventarioDetails.getUsuario());
        inventario.setObservacoes(inventarioDetails.getObservacoes());

        return inventarioRepository.save(inventario); // Salva o item atualizado.
    }

    /**
     * Deleta um item de inventário pelo ID.
     * @param id O ID do item a ser deletado.
     * @throws ResourceNotFoundException Se o item com o ID especificado não for encontrado.
     */
    public void delete(Long id) {
        // Busca o item pelo ID ou lança uma exceção se não encontrado.
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item de inventário não encontrado com id: " + id));
        inventarioRepository.delete(inventario); // Deleta o item.
    }

    /**
     * Busca itens de inventário por um termo de busca no nome.
     * Se o termo de busca for nulo ou vazio, retorna todos os itens.
     * @param searchTerm O termo a ser buscado no nome do item.
     * @return Uma lista de itens que correspondem ao termo de busca.
     */
    public List<Inventario> search(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return inventarioRepository.findAll(); // Retorna todos se o termo de busca for vazio.
        }
        // Utiliza o método customizado do repositório para busca por nome (ignorando case).
        return inventarioRepository.findByNomeContainingIgnoreCase(searchTerm);
    }
}
