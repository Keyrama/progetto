package it.unifi.swam.cleanlabel.rest;

import it.unifi.swam.cleanlabel.dtos.*;
import it.unifi.swam.cleanlabel.mappers.*;
import it.unifi.swam.cleanlabel.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@Transactional
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductResource {

    @PersistenceContext
    private EntityManager em;

    // ── UC1: Lista prodotti con filtri e paginazione ──────────────────────
    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean cleanLabelOnly,
            @RequestParam(required = false) Integer minScore) {

        String jpql = "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.category WHERE 1=1";
        if (search != null && !search.isBlank())
            jpql += " AND (LOWER(p.name) LIKE :search OR LOWER(p.brand) LIKE :search)";
        if (categoryId != null)
            jpql += " AND p.category.id = :categoryId";
        if (Boolean.TRUE.equals(cleanLabelOnly))
            jpql += " AND p.isCleanLabel = true";
        if (minScore != null)
            jpql += " AND p.healthScore >= :minScore";
        jpql += " ORDER BY p.healthScore DESC";

        var query = em.createQuery(jpql, Product.class);
        if (search != null && !search.isBlank())
            query.setParameter("search", "%" + search.toLowerCase() + "%");
        if (categoryId != null)
            query.setParameter("categoryId", categoryId);
        if (minScore != null)
            query.setParameter("minScore", minScore);

        long total = query.getResultList().size();
        var results = query.setFirstResult(page * size).setMaxResults(size).getResultList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("content", results.stream().map(ProductMapper::toSummaryDTO).collect(Collectors.toList()));
        response.put("totalElements", total);
        response.put("totalPages", (int) Math.ceil((double) total / size));
        response.put("number", page);
        response.put("size", size);
        return ResponseEntity.ok(response);
    }

    // ── UC1: Dettaglio prodotto ───────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        Product p = em.find(Product.class, id);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ProductMapper.toDetailDTO(p));
    }

    // ── UC2: Health score ─────────────────────────────────────────────────
    @GetMapping("/{id}/score")
    public ResponseEntity<?> getScore(@PathVariable Long id) {
        Product p = em.find(Product.class, id);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(computeScore(p));
    }

    // ── UC2: Claims ───────────────────────────────────────────────────────
    @GetMapping("/{id}/claims")
    public ResponseEntity<?> getClaims(@PathVariable Long id) {
        Product p = em.find(Product.class, id);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p.getClaims().stream()
                .map(NutritionalClaimMapper::toDTO).collect(Collectors.toList()));
    }

    // ── UC3: Alternative ──────────────────────────────────────────────────
    @GetMapping("/{id}/alternatives")
    public ResponseEntity<?> getAlternatives(@PathVariable Long id) {
        Product p = em.find(Product.class, id);
        if (p == null) return ResponseEntity.notFound().build();
        var alts = em.createQuery(
                "SELECT a FROM AlternativeSuggestion a WHERE a.sourceProduct.id = :id",
                AlternativeSuggestion.class)
                .setParameter("id", id).getResultList();
        return ResponseEntity.ok(alts.stream()
                .map(AlternativeSuggestionMapper::toDTO).collect(Collectors.toList()));
    }

    // ── UC4: Crea prodotto ────────────────────────────────────────────────
    @PostMapping
    @Transactional
    public ResponseEntity<?> createProduct(@RequestBody Map<String, Object> body) {
        Product p = new Product();
        fillProduct(p, body);
        em.persist(p);
        return ResponseEntity.status(201).body(ProductMapper.toSummaryDTO(p));
    }

    // ── UC4: Modifica prodotto ────────────────────────────────────────────
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Product p = em.find(Product.class, id);
        if (p == null) return ResponseEntity.notFound().build();
        fillProduct(p, body);
        return ResponseEntity.ok(ProductMapper.toSummaryDTO(p));
    }

    // ── UC4: Elimina prodotto ─────────────────────────────────────────────
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        Product p = em.find(Product.class, id);
        if (p == null) return ResponseEntity.notFound().build();
        em.remove(p);
        return ResponseEntity.noContent().build();
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private void fillProduct(Product p, Map<String, Object> body) {
        if (body.containsKey("name"))        p.setName((String) body.get("name"));
        if (body.containsKey("brand"))       p.setBrand((String) body.get("brand"));
        if (body.containsKey("description")) p.setDescription((String) body.get("description"));
        if (body.containsKey("isCleanLabel")) p.setCleanLabel((Boolean) body.get("isCleanLabel"));
        if (body.containsKey("categoryId")) {
            Object catId = body.get("categoryId");
            if (catId != null) {
                Long id = catId instanceof Integer ? ((Integer) catId).longValue() : (Long) catId;
                p.setCategory(em.find(ProductCategory.class, id));
            }
        }
    }

    private Map<String, Object> computeScore(Product p) {
        int nutritionScore = 40, ingredientScore = 40, claimScore = 20;
        List<String> flagged = new ArrayList<>(), misleading = new ArrayList<>();

        if (p.getNutritionalValue() != null) {
            NutritionalValue nv = p.getNutritionalValue();
            nutritionScore -= (int)(nv.getSugars() * 0.3 + nv.getSalt() * 2 + nv.getSaturatedFats() * 0.5);
            nutritionScore = Math.max(0, nutritionScore);
        }
        for (Ingredient i : p.getIngredients()) {
            if (i.getRiskLevel() == Ingredient.RiskLevel.HIGH)   { ingredientScore -= 8; flagged.add(i.getName()); }
            else if (i.getRiskLevel() == Ingredient.RiskLevel.MEDIUM) { ingredientScore -= 4; flagged.add(i.getName()); }
            if (i.isArtificial()) ingredientScore -= 2;
        }
        ingredientScore = Math.max(0, ingredientScore);
        for (NutritionalClaim c : p.getClaims()) {
            if (c.isMisleading()) { claimScore -= 7; misleading.add(c.getLabel()); }
        }
        claimScore = Math.max(0, claimScore);

        int total = nutritionScore + ingredientScore + claimScore;
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalScore", total);
        result.put("nutritionScore", nutritionScore);
        result.put("ingredientScore", ingredientScore);
        result.put("claimScore", claimScore);
        result.put("flaggedIngredients", flagged);
        result.put("misleadingClaims", misleading);
        result.put("summary", total >= 70 ? "Prodotto consigliato" : total >= 40 ? "Consumo moderato" : "Consumo sconsigliato");
        return result;
    }
}
