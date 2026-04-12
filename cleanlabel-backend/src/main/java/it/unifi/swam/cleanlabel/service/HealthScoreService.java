package it.unifi.swam.cleanlabel.service;

import it.unifi.swam.cleanlabel.model.Ingredient;
import it.unifi.swam.cleanlabel.model.NutritionalValue;
import it.unifi.swam.cleanlabel.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Computes the health score (0–100) and the cleanLabel flag for a product.
 *
 * The score is composed of four sub-scores (each 0–25):
 *
 *   1. Nutritional quality  — based on sugars, saturated fats, salt
 *   2. Ingredient cleanliness — penalises artificial ingredients
 *   3. Risk profile         — penalises HIGH and MEDIUM risk ingredients
 *   4. Positive nutrients   — rewards proteins and fiber
 *
 * This is an intentionally simplified model suitable for a prototype.
 * A production system would use a validated model such as Nutri-Score or HFSS.
 *
 * cleanLabel = true when:
 *   - no artificial ingredients
 *   - no HIGH-risk ingredients
 *   - health score >= 60
 */
@Service
public class HealthScoreService {

    // Sub-score weights (each out of 25, total = 100)
    private static final int MAX_NUTRITIONAL  = 25;
    private static final int MAX_CLEANLINESS  = 25;
    private static final int MAX_RISK         = 25;
    private static final int MAX_POSITIVE     = 25;

    // Nutritional thresholds per 100g (EU Reg. 1924/2006 and WHO guidelines)
    private static final double HIGH_SUGAR_THRESHOLD    = 22.5; // g — "high in sugar"
    private static final double HIGH_SAT_FAT_THRESHOLD  = 5.0;  // g — "high in saturated fat"
    private static final double HIGH_SALT_THRESHOLD     = 1.5;  // g

    private static final int CLEAN_LABEL_SCORE_THRESHOLD = 60;

    /**
     * Computes and sets healthScore and cleanLabel on the product.
     * Called by ProductService after every create or update.
     */
    public void computeAndApply(Product product) {
        int score = computeScore(product);
        product.setHealthScore(score);
        product.setCleanLabel(isCleanLabel(product, score));
    }

    // ── Score computation ─────────────────────────────────────────────────────

    private int computeScore(Product product) {
        return nutritionalSubScore(product.getNutritionalValue())
                + cleanlinessSubScore(product.getIngredients())
                + riskSubScore(product.getIngredients())
                + positiveNutrientsSubScore(product.getNutritionalValue());
    }

    /**
     * Penalises high sugar, saturated fat, and salt.
     * Each nutrient contributes up to ~8 points; max total 25.
     */
    private int nutritionalSubScore(NutritionalValue nv) {
        if (nv == null) return 0;

        double sugarPenalty  = penaltyRatio(nv.getSugars(),      HIGH_SUGAR_THRESHOLD);
        double satFatPenalty = penaltyRatio(nv.getSaturatedFats(), HIGH_SAT_FAT_THRESHOLD);
        double saltPenalty   = penaltyRatio(nv.getSalt(),         HIGH_SALT_THRESHOLD);

        double avgPenalty = (sugarPenalty + satFatPenalty + saltPenalty) / 3.0;
        return (int) Math.round(MAX_NUTRITIONAL * (1.0 - avgPenalty));
    }

    /**
     * Penalises the proportion of artificial ingredients.
     * 0 artificial → 25 points. All artificial → 0 points.
     */
    private int cleanlinessSubScore(List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) return MAX_CLEANLINESS;

        long artificialCount = ingredients.stream().filter(Ingredient::isArtificial).count();
        double ratio = (double) artificialCount / ingredients.size();
        return (int) Math.round(MAX_CLEANLINESS * (1.0 - ratio));
    }

    /**
     * Penalises HIGH and MEDIUM risk ingredients.
     * HIGH → -2 points each; MEDIUM → -1 point each. Floor at 0.
     */
    private int riskSubScore(List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) return MAX_RISK;

        long highCount   = ingredients.stream()
                .filter(i -> i.getRiskLevel() == Ingredient.RiskLevel.HIGH).count();
        long mediumCount = ingredients.stream()
                .filter(i -> i.getRiskLevel() == Ingredient.RiskLevel.MEDIUM).count();

        int penalty = (int) (highCount * 2 + mediumCount);
        return Math.max(0, MAX_RISK - penalty);
    }

    /**
     * Rewards proteins (muscle maintenance) and fiber (digestive health).
     * Generous protein (>= 20g) → +12; generous fiber (>= 6g) → +13. Max 25.
     */
    private int positiveNutrientsSubScore(NutritionalValue nv) {
        if (nv == null) return 0;

        int proteinScore = nv.getProteins() != null
                ? (int) Math.min(12, nv.getProteins() * 0.6)
                : 0;

        int fiberScore = nv.getFiber() != null
                ? (int) Math.min(13, nv.getFiber() * 2.0)
                : 0;

        return proteinScore + fiberScore;
    }

    // ── CleanLabel evaluation ─────────────────────────────────────────────────

    private boolean isCleanLabel(Product product, int score) {
        List<Ingredient> ingredients = product.getIngredients();

        boolean hasArtificial = ingredients.stream().anyMatch(Ingredient::isArtificial);
        boolean hasHighRisk   = ingredients.stream()
                .anyMatch(i -> i.getRiskLevel() == Ingredient.RiskLevel.HIGH);

        return !hasArtificial && !hasHighRisk && score >= CLEAN_LABEL_SCORE_THRESHOLD;
    }

    // ── Utility ───────────────────────────────────────────────────────────────

    /**
     * Returns a penalty ratio between 0.0 and 1.0.
     * 0.0 = value is 0 (no penalty); 1.0 = value >= threshold (max penalty).
     */
    private double penaltyRatio(Double value, double threshold) {
        if (value == null || value <= 0) return 0.0;
        return Math.min(1.0, value / threshold);
    }
}