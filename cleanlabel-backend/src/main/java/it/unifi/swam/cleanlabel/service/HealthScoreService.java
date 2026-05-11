package it.unifi.swam.cleanlabel.service;

import it.unifi.swam.cleanlabel.model.Ingredient;
import it.unifi.swam.cleanlabel.model.NutritionalValue;
import it.unifi.swam.cleanlabel.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void computeAndApply(Product product) {
        int score = computeScore(product);
        product.setHealthScore(score);
        product.setCleanLabel(isCleanLabel(product, score));
    }

    private int computeScore(Product product) {
        return nutritionalSubScore(product.getNutritionalValue())
                + cleanlinessSubScore(product.getIngredients())
                + riskSubScore(product.getIngredients())
                + positiveNutrientsSubScore(product.getNutritionalValue());
    }

    private int nutritionalSubScore(NutritionalValue nv) {
        if (nv == null) return 0;

        double sugarPenalty  = penaltyRatio(nv.getSugars(),      HIGH_SUGAR_THRESHOLD);
        double satFatPenalty = penaltyRatio(nv.getSaturatedFats(), HIGH_SAT_FAT_THRESHOLD);
        double saltPenalty   = penaltyRatio(nv.getSalt(),         HIGH_SALT_THRESHOLD);

        double avgPenalty = (sugarPenalty + satFatPenalty + saltPenalty) / 3.0;
        return (int) Math.round(MAX_NUTRITIONAL * (1.0 - avgPenalty));
    }
    private int cleanlinessSubScore(List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) return MAX_CLEANLINESS;

        long artificialCount = ingredients.stream().filter(Ingredient::isArtificial).count();
        double ratio = (double) artificialCount / ingredients.size();
        return (int) Math.round(MAX_CLEANLINESS * (1.0 - ratio));
    }

    private int riskSubScore(List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) return MAX_RISK;

        long highCount   = ingredients.stream()
                .filter(i -> i.getRiskLevel() == Ingredient.RiskLevel.HIGH).count();
        long mediumCount = ingredients.stream()
                .filter(i -> i.getRiskLevel() == Ingredient.RiskLevel.MEDIUM).count();

        int penalty = (int) (highCount * 2 + mediumCount);
        return Math.max(0, MAX_RISK - penalty);
    }

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

    private boolean isCleanLabel(Product product, int score) {
        List<Ingredient> ingredients = product.getIngredients();

        boolean hasArtificial = ingredients.stream().anyMatch(Ingredient::isArtificial);
        boolean hasHighRisk   = ingredients.stream()
                .anyMatch(i -> i.getRiskLevel() == Ingredient.RiskLevel.HIGH);

        return !hasArtificial && !hasHighRisk && score >= CLEAN_LABEL_SCORE_THRESHOLD;
    }

    private double penaltyRatio(Double value, double threshold) {
        if (value == null || value <= 0) return 0.0;
        return Math.min(1.0, value / threshold);
    }
}