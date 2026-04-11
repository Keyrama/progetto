package it.unifi.swam.cleanlabel.rest;

import it.unifi.swam.cleanlabel.model.Ingredient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@CrossOrigin(origins = "http://localhost:4200")
public class IngredientResource {

    @PersistenceContext
    private EntityManager em;

    @GetMapping
    public List<Ingredient> getAll(
            @RequestParam(required = false) String riskLevel,
            @RequestParam(required = false) Boolean artificialOnly) {
        String jpql = "SELECT i FROM Ingredient i WHERE 1=1";
        if (riskLevel != null) jpql += " AND i.riskLevel = :risk";
        if (Boolean.TRUE.equals(artificialOnly)) jpql += " AND i.isArtificial = true";
        var q = em.createQuery(jpql, Ingredient.class);
        if (riskLevel != null) q.setParameter("risk", Ingredient.RiskLevel.valueOf(riskLevel));
        return q.getResultList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Ingredient i = em.find(Ingredient.class, id);
        return i != null ? ResponseEntity.ok(i) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody Ingredient ingredient) {
        em.persist(ingredient);
        return ResponseEntity.status(201).body(ingredient);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Ingredient updated) {
        Ingredient i = em.find(Ingredient.class, id);
        if (i == null) return ResponseEntity.notFound().build();
        i.setName(updated.getName());
        i.setRiskLevel(updated.getRiskLevel());
        i.setArtificial(updated.isArtificial());
        return ResponseEntity.ok(i);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Ingredient i = em.find(Ingredient.class, id);
        if (i == null) return ResponseEntity.notFound().build();
        em.remove(i);
        return ResponseEntity.noContent().build();
    }
}
