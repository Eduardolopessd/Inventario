package com.invent.inventario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // Para retornar status HTTP
import org.springframework.http.ResponseEntity; // Para construir respostas HTTP completas
import org.springframework.web.bind.annotation.CrossOrigin; // Para habilitar CORS
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // Para extrair variáveis do caminho da URL
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody; // Para mapear o corpo da requisição para um objeto
import org.springframework.web.bind.annotation.RequestMapping; // Para mapear requisições HTTP
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus; // Para definir o status HTTP da resposta
import org.springframework.web.bind.annotation.RestController; // Combina @Controller e @ResponseBody

import com.invent.inventario.entity.Inventario;
import com.invent.inventario.exception.ResourceNotFoundException; // Exceção personalizada
import com.invent.inventario.service.InventarioService;

import jakarta.validation.Valid; // Anotação para ativar a validação de beans

/**
 * Controlador REST para gerenciar operações de inventário.
 * Define os endpoints da API para CRUD de itens de inventário.
 */
@RestController // Indica que esta classe é um controlador Spring que lida com requisições REST.
@RequestMapping("/api/inventario") // Define o caminho base para todos os endpoints neste controlador.
@CrossOrigin(origins = "*") // Habilita o CORS para permitir requisições de qualquer origem.
                            // Em produção, ajuste para domínios específicos.
public class InventarioController {

    @Autowired // Injeta uma instância de InventarioService (gerenciada pelo Spring).
    private InventarioService inventarioService;

    /**
     * Endpoint para criar um novo item de inventário.
     * Requisição: POST /api/inventario/itens
     * @param inventario O objeto Inventario enviado no corpo da requisição.
     * @return O item de inventário salvo.
     */
    @PostMapping("/itens") // Mapeia requisições POST para /api/inventario/itens.
    @ResponseStatus(HttpStatus.CREATED) // Retorna status HTTP 201 Created em caso de sucesso.
    public Inventario createItem(@Valid @RequestBody Inventario inventario) { // @Valid ativa a validação do bean.
        return inventarioService.save(inventario);
    }

    /**
     * Endpoint para atualizar um item de inventário existente.
     * Requisição: PUT /api/inventario/itens/{id}
     * @param id O ID do item a ser atualizado (extraído do caminho da URL).
     * @param inventarioDetails Os detalhes atualizados do item enviados no corpo da requisição.
     * @return ResponseEntity com o item de inventário atualizado e status HTTP 200 OK.
     */
    @PutMapping("/itens/{id}") // Mapeia requisições PUT para /api/inventario/itens/{id}.
    public ResponseEntity<Inventario> updateItem(@PathVariable Long id, @Valid @RequestBody Inventario inventarioDetails) {
        Inventario updatedInventario = inventarioService.update(id, inventarioDetails);
        return ResponseEntity.ok(updatedInventario); // Retorna 200 OK com o objeto atualizado.
    }

    /**
     * Endpoint para buscar todos os itens de inventário.
     * Requisição: GET /api/inventario/itens
     * @return Uma lista de todos os itens de inventário.
     */
    @GetMapping("/itens") // Mapeia requisições GET para /api/inventario/itens.
    public List<Inventario> getAllItems() {
        return inventarioService.findAll();
    }

    /**
     * Endpoint para buscar um item de inventário pelo ID.
     * Requisição: GET /api/inventario/itens/{id}
     * @param id O ID do item a ser buscado (extraído do caminho da URL).
     * @return ResponseEntity com o item de inventário e status HTTP 200 OK.
     * @throws ResourceNotFoundException Se o item não for encontrado.
     */
    @GetMapping("/itens/{id}") // Mapeia requisições GET para /api/inventario/itens/{id}.
    public ResponseEntity<Inventario> getItemById(@PathVariable Long id) {
        Inventario inventario = inventarioService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item de inventário não encontrado com id: " + id));
        return ResponseEntity.ok(inventario); // Retorna 200 OK com o objeto encontrado.
    }

    /**
     * Endpoint para deletar um item de inventário pelo ID.
     * Requisição: DELETE /api/inventario/itens/{id}
     * @param id O ID do item a ser deletado (extraído do caminho da URL).
     * @return ResponseEntity com status HTTP 204 No Content.
     */
    @DeleteMapping("/itens/{id}") // Mapeia requisições DELETE para /api/inventario/itens/{id}.
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna status HTTP 204 No Content em caso de sucesso (sem corpo na resposta).
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        inventarioService.delete(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content.
    }

    /**
     * Endpoint para buscar itens de inventário por um termo de busca no nome.
     * Requisição: GET /api/inventario/itens/search?term={searchTerm}
     * @param searchTerm O termo de busca (parâmetro de query).
     * @return Uma lista de itens que correspondem ao termo de busca.
     */
    @GetMapping("/itens/search") // Mapeia requisições GET para /api/inventario/itens/search.
    public List<Inventario> searchItems(@RequestParam(required = false) String searchTerm) { // @RequestParam mapeia o parâmetro de query.
        return inventarioService.search(searchTerm);
    }
}
