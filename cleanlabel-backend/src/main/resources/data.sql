-- ============================================================
-- Seed data for Clean Label Web Application
-- Runs at boot after Hibernate creates the schema.
-- Uses INSERT ... ON CONFLICT DO NOTHING to be idempotent.
-- ============================================================

-- ── 14 Major Allergens (EU Reg. No 1169/2011) ────────────────────────────────

INSERT INTO allergens (name, code, description) VALUES
                                                    ('Gluten',              'GLUTEN',    'Present in wheat, rye, barley, oats and their derivatives.'),
                                                    ('Crustaceans',         'CRUSTACEAN','Shrimps, crabs, lobsters and related products.'),
                                                    ('Eggs',                'EGGS',      'Eggs from poultry and egg-derived products.'),
                                                    ('Fish',                'FISH',      'All fish species and fish-derived products.'),
                                                    ('Peanuts',             'PEANUTS',   'Peanuts and peanut-derived products.'),
                                                    ('Soybeans',            'SOY',       'Soybeans and soybean-derived products.'),
                                                    ('Milk',                'MILK',      'Cow milk and dairy-derived products, including lactose.'),
                                                    ('Nuts',                'NUTS',      'Almonds, hazelnuts, walnuts, cashews, pecans, Brazil nuts, pistachios, macadamia nuts.'),
                                                    ('Celery',              'CELERY',    'Celery stalks, leaves, seeds and celeriac.'),
                                                    ('Mustard',             'MUSTARD',   'Mustard plant seeds, leaves, flowers and derived products.'),
                                                    ('Sesame seeds',        'SESAME',    'Sesame seeds and sesame-derived products (e.g. tahini).'),
                                                    ('Sulphur dioxide',     'SULPHITES', 'Sulphur dioxide and sulphites at concentrations > 10 mg/kg.'),
                                                    ('Lupin',               'LUPIN',     'Lupin seeds and lupin-derived flour products.'),
                                                    ('Molluscs',            'MOLLUSCS',  'Clams, mussels, oysters, scallops, squid and related products.')
    ON CONFLICT (code) DO NOTHING;

-- ── Product Definitions Library ──────────────────────────────────────────────

INSERT INTO product_categories (name, description) VALUES
    ('Cereali e colazione',       'Cereali, muesli, granola e prodotti per la colazione'),
    ('Snack e patatine',          'Patatine, cracker, popcorn e snack salati'),
    ('Bevande',                   'Succhi, bibite, energy drink e acque aromatizzate'),
    ('Latticini',                 'Yogurt, formaggi, latte e derivati'),
    ('Dolci e biscotti',          'Biscotti, merendine, cioccolato e dolciumi'),
    ('Salumi e affettati',        'Prosciutto, salame, bresaola e insaccati'),
    ('Condimenti e salse',        'Ketchup, maionese, pesto e condimenti vari'),
    ('Pane e prodotti da forno',  'Pane, grissini, crackers e prodotti da forno')
    ON CONFLICT (name) DO NOTHING;

    
-- ── Claim Definitions Library ─────────────────────────────────────────────────
-- Sources: EU Reg. 1924/2006, EFSA guidelines, consumer protection literature.
-- validation_threshold unit: g per 100g product.

INSERT INTO claim_definitions
(term, claim_type, is_regulated, is_misleading, misleading_reason,
 explanation, regulatory_reference, validation_strategy, validation_threshold)
VALUES

-- MARKETING — inherently misleading, no automatic data validation possible
('natural',
 'MARKETING', false, true, 'NO_LEGAL_DEFINITION',
 'The term "natural" has no legal definition in EU food law. A product may use this label even if it contains processing aids, additives, or industrially refined ingredients.',
 NULL,
 'NONE', NULL),

('healthy',
 'MARKETING', false, true, 'NO_LEGAL_DEFINITION',
 '"Healthy" is not an approved claim under EU Reg. 1924/2006. Its use is unregulated and not subject to independent verification.',
 NULL,
 'NONE', NULL),

('light',
 'MARKETING', false, true, 'VAGUE_CRITERIA',
 '"Light" is regulated only when referring to a specific nutrient (e.g. "light in fat"). When used generically it lacks precise criteria and may mislead consumers about the product''s overall nutritional profile.',
 'EU Reg. 1924/2006, Art. 8',
 'NONE', NULL),

('clean',
 'MARKETING', false, true, 'NO_LEGAL_DEFINITION',
 '"Clean" has no legal definition in EU food law and is used inconsistently across the industry.',
 NULL,
 'NONE', NULL),

('wholesome',
 'MARKETING', false, true, 'NO_LEGAL_DEFINITION',
 '"Wholesome" has no standardised legal definition and cannot be verified against objective nutritional criteria.',
 NULL,
 'NONE', NULL),

('artisan',
 'MARKETING', false, true, 'VAGUE_CRITERIA',
 '"Artisan" or "artisanal" suggests handcrafted production but has no regulated definition in EU food law. Industrial products may carry this label.',
 NULL,
 'NONE', NULL),

-- NUTRITIONAL — EU-regulated, validated against product data
('no added sugars',
 'NUTRITIONAL', true, false, 'NONE',
 'Regulated claim: no monosaccharides or disaccharides have been added. The product may still contain naturally occurring sugars from ingredients such as fruit or milk.',
 'EU Reg. 1924/2006, Annex',
 'SUGAR_BELOW_THRESHOLD', 0.5),

('sugar free',
 'NUTRITIONAL', true, false, 'NONE',
 'Regulated claim: the product must contain no more than 0.5g of sugars per 100g or 100ml.',
 'EU Reg. 1924/2006, Annex',
 'SUGAR_BELOW_THRESHOLD', 0.5),

('low sugar',
 'NUTRITIONAL', true, false, 'NONE',
 'Regulated claim: the product must contain no more than 5g of sugars per 100g for solids or 2.5g per 100ml for liquids.',
 'EU Reg. 1924/2006, Annex',
 'SUGAR_BELOW_THRESHOLD', 5.0),

('high fibre',
 'NUTRITIONAL', true, false, 'NONE',
 'Regulated claim: the product must contain at least 6g of dietary fibre per 100g.',
 'EU Reg. 1924/2006, Annex',
 'HIGH_FIBER', 6.0),

('source of fibre',
 'NUTRITIONAL', true, false, 'NONE',
 'Regulated claim: the product must contain at least 3g of dietary fibre per 100g.',
 'EU Reg. 1924/2006, Annex',
 'HIGH_FIBER', 3.0),

('no artificial ingredients',
 'NUTRITIONAL', false, false, 'NONE',
 'Indicates the absence of synthetic or artificial additives. Validated against the ingredient list: any ingredient flagged as artificial invalidates this claim.',
 NULL,
 'NO_ARTIFICIAL_INGREDIENTS', NULL),

('no artificial additives',
 'NUTRITIONAL', false, false, 'NONE',
 'Indicates the absence of synthetic additives (E-numbers of artificial origin). Validated against the ingredient list.',
 NULL,
 'NO_ARTIFICIAL_INGREDIENTS', NULL),

('no high risk additives',
 'NUTRITIONAL', false, false, 'NONE',
 'Indicates the absence of additives classified as HIGH risk. Validated against the ingredient risk classification.',
 NULL,
 'NO_HIGH_RISK_INGREDIENTS', NULL),

-- HEALTH — EU-regulated health relationship claims
('supports heart health',
 'HEALTH', true, false, 'NONE',
 'Health claim permitted under EU Reg. 1924/2006 only under specific conditions (e.g. proven omega-3 content). Requires authorisation from EFSA.',
 'EU Reg. 1924/2006, Art. 13',
 'NONE', NULL),

('supports immune system',
 'HEALTH', true, false, 'NONE',
 'Permitted health claim under EU Reg. 1924/2006 only when specific vitamin/mineral thresholds are met (e.g. Vitamin C ≥ 15% NRV per 100g).',
 'EU Reg. 1924/2006, Art. 13',
 'NONE', NULL)

    ON CONFLICT (term) DO NOTHING;