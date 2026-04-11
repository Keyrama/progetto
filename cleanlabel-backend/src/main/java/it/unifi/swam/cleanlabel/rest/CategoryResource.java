package it.unifi.swam.cleanlabel.rest;

import it.unifi.swam.cleanlabel.model.ProductCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryResource {

    @PersistenceContext
    private EntityManager em;

    @GetMapping
    public List<ProductCategory> getAll() {
        return em.createQuery("SELECT c FROM ProductCategory c ORDER BY c.name", ProductCategory.class)
                .getResultList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        ProductCategory c = em.find(ProductCategory.class, id);
        return c != null ? ResponseEntity.ok(c) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody ProductCategory category) {
        em.persist(category);
        return ResponseEntity.status(201).body(category);
    }
}
