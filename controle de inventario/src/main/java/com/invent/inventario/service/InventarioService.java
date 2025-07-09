package com.invent.inventario.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.invent.inventario.entity.Inventario;
import com.invent.inventario.exception.ResourceNotFoundException;
import com.invent.inventario.repository.InventarioRepository;

import jakarta.persistence.criteria.Predicate;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    public List<Inventario> findAll() {
        return inventarioRepository.findAll();
    }

    public Optional<Inventario> findById(Long id) {
        return inventarioRepository.findById(id);
    }

    public Inventario save(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    public Inventario update(Long id, Inventario inventarioDetails) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item de inventário não encontrado com id: " + id));

        // Atualiza os campos do item encontrado com os detalhes fornecidos
        inventario.setNome(inventarioDetails.getNome());
        inventario.setEtiqueta(inventarioDetails.getEtiqueta());
        inventario.setNumeroSerie(inventarioDetails.getNumeroSerie());
        inventario.setUsuario(inventarioDetails.getUsuario());
        inventario.setObservacoes(inventarioDetails.getObservacoes());
        // CORREÇÃO AQUI: Usar getAtivo() porque o campo 'ativo' agora é do tipo Boolean (classe wrapper)
        inventario.setAtivo(inventarioDetails.getAtivo());
        inventario.setSetor(inventarioDetails.getSetor());

        return inventarioRepository.save(inventario);
    }

    public void delete(Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item de inventário não encontrado com id: " + id));
        inventarioRepository.delete(inventario);
    }

    /**
     * Busca itens de inventário com base em múltiplos critérios de filtro (nome, etiqueta, ativo, setor).
     * Usa Specification para construir consultas dinâmicas.
     * @param searchTerm Termo de busca para nome ou usuário (opcional).
     * @param etiqueta Número da etiqueta (opcional).
     * @param ativo Status ativo/inativo (opcional).
     * @param setor Setor (opcional).
     * @return Uma lista de itens que correspondem aos critérios de busca.
     */
    public List<Inventario> search(String searchTerm, Integer etiqueta, Boolean ativo, String setor) {
        return inventarioRepository.findAll((Specification<Inventario>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filtro por searchTerm (nome OU usuário OU etiqueta)
            if (StringUtils.hasText(searchTerm)) {
                try {
                    // Tenta converter searchTerm para Integer para buscar por etiqueta
                    // HINT: "Unnecessary temporary when converting from String"
                    // Embora o IDE possa sugerir isso, searchTerm.trim() é útil para remover
                    // espaços em branco antes de tentar a conversão ou usar em comparações,
                    // garantindo que a lógica de parseamento não falhe por espaços indesejados.
                    Integer searchEtiqueta = Integer.parseInt(searchTerm.trim());
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + searchTerm.toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("usuario")), "%" + searchTerm.toLowerCase() + "%"),
                            criteriaBuilder.equal(root.get("etiqueta"), searchEtiqueta) // Busca por etiqueta
                    ));
                } catch (NumberFormatException e) {
                    // Se não for um número, busca apenas por nome ou usuário
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + searchTerm.toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("usuario")), "%" + searchTerm.toLowerCase() + "%")
                    ));
                }
            }

            // 2. Filtro por etiqueta (se fornecida explicitamente como um campo separado)
            if (etiqueta != null) {
                predicates.add(criteriaBuilder.equal(root.get("etiqueta"), etiqueta));
            }

            // 3. Filtro por status 'ativo'
            if (ativo != null) {
                predicates.add(criteriaBuilder.equal(root.get("ativo"), ativo));
            }

            // 4. Filtro por 'setor'
            if (StringUtils.hasText(setor)) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("setor")), setor.toLowerCase()));
            }

            // HINT: "new array created just to be passed to Collection.toArray"
            // A criação de 'new Predicate[0]' é uma prática recomendada para converter
            // uma List em um array de um tipo específico, especialmente para coleções
            // genéricas. É eficiente e evita ClassCastException em tempo de execução.
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
