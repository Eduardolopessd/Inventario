package com.invent.inventario.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.invent.inventario.entity.Inventario;
import com.invent.inventario.exception.ResourceNotFoundException;
import com.invent.inventario.service.InventarioService; // Importar Logger

import jakarta.validation.Valid; // Importar LoggerFactory

/**
 * Controlador REST para gerenciar operações de inventário.
 * Define os endpoints da API para CRUD de itens de inventário.
 */
@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*")
public class InventarioController {

    private static final Logger logger = LoggerFactory.getLogger(InventarioController.class); // Inicializa o logger

    @Autowired
    private InventarioService inventarioService;

    /**
     * Endpoint para criar um novo item de inventário.
     * Requisição: POST /api/inventario/itens
     * @param inventario O objeto Inventario enviado no corpo da requisição.
     * @return O item de inventário salvo.
     */
    @PostMapping("/itens")
    @ResponseStatus(HttpStatus.CREATED)
    public Inventario createItem(@Valid @RequestBody Inventario inventario) {
        try {
            logger.info("Recebida requisição para criar item: {}", inventario);
            Inventario newItem = inventarioService.save(inventario);
            logger.info("Item criado com sucesso: {}", newItem);
            return newItem;
        } catch (Exception e) {
            logger.error("Erro ao criar item: {}", e.getMessage(), e); // Loga a exceção completa
            throw e; // Relança a exceção para ser tratada pelo GlobalExceptionHandler
        }
    }

    /**
     * Endpoint para atualizar um item de inventário existente.
     * Requisição: PUT /api/inventario/itens/{id}
     * @param id O ID do item a ser atualizado (extraído do caminho da URL).
     * @param inventarioDetails Os detalhes atualizados do item enviados no corpo da requisição.
     * @return ResponseEntity com o item de inventário atualizado e status HTTP 200 OK.
     */
    @PutMapping("/itens/{id}")
    public ResponseEntity<Inventario> updateItem(@PathVariable Long id, @Valid @RequestBody Inventario inventarioDetails) {
        try {
            logger.info("Recebida requisição para atualizar item com ID {}: {}", id, inventarioDetails);
            Inventario updatedInventario = inventarioService.update(id, inventarioDetails);
            logger.info("Item com ID {} atualizado com sucesso: {}", id, updatedInventario);
            return ResponseEntity.ok(updatedInventario);
        } catch (Exception e) {
            logger.error("Erro ao atualizar item com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Endpoint para buscar todos os itens de inventário.
     * Requisição: GET /api/inventario/itens
     * @return Uma lista de todos os itens de inventário.
     */
    @GetMapping("/itens")
    public List<Inventario> getAllItems() {
        try {
            logger.info("Recebida requisição para buscar todos os itens.");
            List<Inventario> items = inventarioService.findAll();
            logger.info("Retornando {} itens.", items.size());
            return items;
        } catch (Exception e) {
            logger.error("Erro ao buscar todos os itens: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Endpoint para buscar um item de inventário pelo ID.
     * Requisição: GET /api/inventario/itens/{id}
     * @param id O ID do item a ser buscado (extraído do caminho da URL).
     * @return ResponseEntity com o item de inventário e status HTTP 200 OK.
     * @throws ResourceNotFoundException Se o item não for encontrado.
     */
    @GetMapping("/itens/{id}")
    public ResponseEntity<Inventario> getItemById(@PathVariable Long id) {
        try {
            logger.info("Recebida requisição para buscar item com ID: {}", id);
            Inventario inventario = inventarioService.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Item de inventário não encontrado com id: " + id));
            logger.info("Item com ID {} encontrado: {}", id, inventario);
            return ResponseEntity.ok(inventario);
        } catch (Exception e) {
            logger.error("Erro ao buscar item com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Endpoint para deletar um item de inventário pelo ID.
     * Requisição: DELETE /api/inventario/itens/{id}
     * @param id O ID do item a ser deletado (extraído do caminho da URL).
     * @return ResponseEntity com status HTTP 204 No Content.
     */
    @DeleteMapping("/itens/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        try {
            logger.info("Recebida requisição para deletar item com ID: {}", id);
            inventarioService.delete(id);
            logger.info("Item com ID {} deletado com sucesso.", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Erro ao deletar item com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Endpoint para buscar itens de inventário com base em múltiplos critérios de filtro.
     * Requisição: GET /api/inventario/itens/search?term={searchTerm}&ativo={true/false}&setor={setor}
     * @param searchTerm Termo de busca para o nome do item (opcional).
     * @param ativo Status ativo/inativo (opcional, null para todos).
     * @param setor Termo de busca para o setor (opcional).
     * @return Uma lista de itens que correspondem aos critérios de busca.
     */
 @GetMapping("/itens/search")
    public List<Inventario> searchItems(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) String setor) {
        try {
            logger.info("Recebida requisição de busca com searchTerm='{}', ativo='{}', setor='{}'.", searchTerm, ativo, setor);
            // Passa null para etiqueta, pois searchTerm já tenta parsear para etiqueta se for um número
            List<Inventario> items = inventarioService.search(searchTerm, null, ativo, setor);
            logger.info("Retornando {} itens para a busca.", items.size());
            return items;
        } catch (Exception e) {
            logger.error("Erro ao realizar busca de itens: {}", e.getMessage(), e);
            throw e;
        }
    }
}
