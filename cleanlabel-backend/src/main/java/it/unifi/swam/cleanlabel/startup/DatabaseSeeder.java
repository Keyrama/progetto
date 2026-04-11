package it.unifi.swam.cleanlabel.startup;

import it.unifi.swam.cleanlabel.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseSeeder {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public String seed() {
        Long count = em.createQuery("SELECT COUNT(p) FROM Product p", Long.class).getSingleResult();
        if (count > 0) return "Database già popolato con " + count + " prodotti.";

        // Categorie
        ProductCategory cereals   = new ProductCategory("Cereali",   "Cereali da colazione e muesli");
        ProductCategory snacks    = new ProductCategory("Snacks",    "Patatine, cracker e snack salati");
        ProductCategory beverages = new ProductCategory("Bevande",   "Succhi, bibite e bevande energetiche");
        ProductCategory dairy     = new ProductCategory("Latticini", "Yogurt, formaggi e derivati");
        em.persist(cereals); em.persist(snacks); em.persist(beverages); em.persist(dairy);

        // Allergeni
        Allergen gluten = new Allergen("Glutine",         "GLUTEN", "Presente in frumento, segale, orzo");
        Allergen milk   = new Allergen("Latte",           "MILK",   "Presente in tutti i derivati del latte");
        Allergen nuts   = new Allergen("Frutta a guscio", "NUTS",   "Noci, nocciole, mandorle");
        em.persist(gluten); em.persist(milk); em.persist(nuts);

        // Ingredienti
        Ingredient oats     = new Ingredient("Avena integrale",       null,   "Cereale ricco di fibre", false, Ingredient.RiskLevel.LOW);
        Ingredient honey    = new Ingredient("Miele",                 null,   "Dolcificante naturale",  false, Ingredient.RiskLevel.LOW);
        Ingredient sugar    = new Ingredient("Zucchero",              null,   "Saccarosio",             false, Ingredient.RiskLevel.LOW);
        Ingredient skimMilk = new Ingredient("Latte scremato",        null,   "Latte scremato",         false, Ingredient.RiskLevel.LOW);
        Ingredient water    = new Ingredient("Acqua",                 null,   "Acqua potabile",         false, Ingredient.RiskLevel.LOW);
        Ingredient salt     = new Ingredient("Sale",                  null,   "Cloruro di sodio",       false, Ingredient.RiskLevel.LOW);
        Ingredient sunflOil = new Ingredient("Olio di girasole",      null,   "Olio vegetale",          false, Ingredient.RiskLevel.LOW);
        Ingredient e621     = new Ingredient("Glutammato monosodico", "E621", "Esaltatore di sapidità", true,  Ingredient.RiskLevel.MEDIUM);
        Ingredient e110     = new Ingredient("Giallo tramonto FCF",   "E110", "Colorante sintetico",    true,  Ingredient.RiskLevel.HIGH);
        Ingredient e211     = new Ingredient("Benzoato di sodio",     "E211", "Conservante",            true,  Ingredient.RiskLevel.HIGH);
        Ingredient e951     = new Ingredient("Aspartame",             "E951", "Dolcificante artificiale",true, Ingredient.RiskLevel.MEDIUM);
        for (Ingredient i : new Ingredient[]{oats,honey,sugar,skimMilk,water,salt,sunflOil,e621,e110,e211,e951})
            em.persist(i);

        // Prodotto 1: Muesli Bio
        Product muesli = new Product("Muesli Bio Integrale", "NaturaBio", "Muesli a base di avena integrale, miele e frutta secca.", cereals, true);
        muesli.setHealthScore(88); muesli.setSustainabilityScore(88);
        muesli.setNutritionalValue(new NutritionalValue(370.0,10.0,58.0,18.0,7.0,1.2,0.1,8.0));
        muesli.addIngredient(oats); muesli.addIngredient(honey); muesli.addIngredient(sugar);
        muesli.addAllergen(gluten); muesli.addAllergen(nuts);
        muesli.addClaim(new NutritionalClaim("100% Naturale", true, false, "Tutti gli ingredienti sono naturali."));
        em.persist(muesli.getNutritionalValue()); em.persist(muesli); muesli.getClaims().forEach(em::persist);

        // Prodotto 2: Yogurt Greco
        Product yogurt = new Product("Yogurt Greco Naturale", "LattePuro", "Yogurt greco ad alto contenuto proteico.", dairy, true);
        yogurt.setHealthScore(82); yogurt.setSustainabilityScore(72);
        yogurt.setNutritionalValue(new NutritionalValue(97.0,9.0,4.0,4.0,5.0,3.4,0.1,0.0));
        yogurt.addIngredient(skimMilk); yogurt.addAllergen(milk);
        yogurt.addClaim(new NutritionalClaim("Alto contenuto proteico", true, false, "9g proteine per 100g."));
        em.persist(yogurt.getNutritionalValue()); em.persist(yogurt); yogurt.getClaims().forEach(em::persist);

        // Prodotto 3: Succo Multifrutta
        Product juice = new Product("Succo Multifrutta", "FruttaViva", "Bevanda a base di succhi concentrati.", beverages, false);
        juice.setHealthScore(48); juice.setSustainabilityScore(55);
        juice.setNutritionalValue(new NutritionalValue(45.0,0.5,10.5,10.0,0.1,0.0,0.02,0.5));
        juice.addIngredient(water); juice.addIngredient(sugar); juice.addIngredient(e211);
        juice.addClaim(new NutritionalClaim("100% Frutta", false, true, "Contiene succo concentrato ricostruito."));
        em.persist(juice.getNutritionalValue()); em.persist(juice); juice.getClaims().forEach(em::persist);

        // Prodotto 4: Patatine
        Product chips = new Product("Patatine Gusto Formaggio", "SnackTime", "Patatine fritte aromatizzate.", snacks, false);
        chips.setHealthScore(28); chips.setSustainabilityScore(30);
        chips.setNutritionalValue(new NutritionalValue(530.0,6.0,55.0,1.5,32.0,4.5,1.8,3.0));
        chips.addIngredient(salt); chips.addIngredient(sunflOil); chips.addIngredient(e621); chips.addIngredient(e110);
        chips.addAllergen(gluten);
        chips.addClaim(new NutritionalClaim("Gusto Naturale", false, true, "Il gusto è ottenuto tramite aromi artificiali."));
        em.persist(chips.getNutritionalValue()); em.persist(chips); chips.getClaims().forEach(em::persist);

        // Prodotto 5: Energy Drink
        Product energy = new Product("PowerBlast Energy Drink", "EnergyX", "Bevanda energetica con caffeina e taurina.", beverages, false);
        energy.setHealthScore(14); energy.setSustainabilityScore(15);
        energy.setNutritionalValue(new NutritionalValue(11.0,0.0,2.7,0.0,0.0,0.0,0.12,0.0));
        energy.addIngredient(water); energy.addIngredient(e951); energy.addIngredient(e211); energy.addIngredient(e110);
        energy.addClaim(new NutritionalClaim("Zero Zuccheri", true, false, "Confermato, usa dolcificanti artificiali."));
        energy.addClaim(new NutritionalClaim("Boost Naturale", false, true, "La caffeina è sintetica."));
        em.persist(energy.getNutritionalValue()); em.persist(energy); energy.getClaims().forEach(em::persist);

        // Alternative
        em.persist(new AlternativeSuggestion(chips, muesli, "Meno grassi e nessun additivo artificiale.", 60));
        em.persist(new AlternativeSuggestion(energy, juice, "Nessun dolcificante artificiale.", 34));
        em.persist(new AlternativeSuggestion(energy, yogurt, "Ingredienti naturali, zero additivi.", 68));

        return "Seed completato: 4 categorie, 3 allergeni, 11 ingredienti, 5 prodotti.";
    }
}
