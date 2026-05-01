-- ============================================================
-- Seed data for Clean Label Web Application
-- Runs at boot after Hibernate creates the schema.
-- Uses INSERT ... ON CONFLICT DO NOTHING to be idempotent.
-- ============================================================

-- ── 14 Major Allergens (EU Reg. No 1169/2011) ────────────────────────────────

INSERT INTO allergens (name, code, description) VALUES
                                                    ('Glutine',              'GLUTINE',              'Presente in frumento, segale, orzo, avena e loro derivati.'),
                                                    ('Crostacei',            'CROSTACEI',            'Gamberi, granchi, gamberi e prodotti correlati.'),
                                                    ('Uova',                 'UOVA',                 'Uova di pollame e prodotti derivati dalle uova.'),
                                                    ('Pesce',                'PESCE',                'Tutte le specie di pesce e i prodotti derivati dal pesce.'),
                                                    ('Arachidi',             'ARACHIDI',             'Arachidi e prodotti derivati dagli arachidi.'),
                                                    ('Soia',                 'SOIA',                 'Soia e prodotti derivati dalla soia.'),
                                                    ('Latte',                'LATTE',                'Latte bovino e prodotti derivati dai latticini, inclusa la lattosio.'),
                                                    ('Frutta a guscio',      'FRUTTA A GUSCIO',      'Almonds, hazelnuts, walnuts, cashews, pecans, Brazil nuts, pistachios, macadamia nuts.'),
                                                    ('Sedano',               'SEDANO',               'Sedano e prodotti derivati dal sedano.'),
                                                    ('Senape',               'SENAPE',               'Senape e prodotti derivati dalla senape.'),
                                                    ('Semi di sesamo',       'SEMI DI SESAMO',       'Semi di sesamo e prodotti derivati dai semi di sesamo.'),
                                                    ('Solfiti',              'SOLFITI',              'Solfiti in concentrazioni superiori a 10 mg/kg o 10 mg/litro, espressi come SO2.'),
                                                    ('Lupini',               'LUPINI',               'Lupini e prodotti derivati dai lupini.'),
                                                    ('Molluschi',            'MOLLUSCHI',            'Molluschi e prodotti derivati dai molluschi.')
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
('naturale',
 'MARKETING', false, true, 'NO_LEGAL_DEFINITION',
 'Il termine "naturale" non ha una definizione legale nel diritto alimentare dell''UE. Un prodotto può utilizzare questo etichetta anche se contiene agenti di lavorazione, additivi o ingredienti raffinati industrialmente.',
 NULL,
 'NONE', NULL),

('salutare',
 'MARKETING', false, true, 'NO_LEGAL_DEFINITION',
 'Il termine "salutare" non è un claim approvato nel regolamento EU 1924/2006. Il suo utilizzo è non regolamentato e non soggetto a verifica indipendente.',
 NULL,
 'NONE', NULL),

('Light',
 'MARKETING', false, true, 'VAGUE_CRITERIA',
 '"Light" è regolato solo quando ci si riferisce a un nutriente specifico (es. "light in fat"). Quando viene utilizzato in modo generico, manca di criteri precisi e può ingannare i consumatori riguardo al profilo nutrizionale complessivo del prodotto.',
 'EU Reg. 1924/2006, Art. 8',
 'NONE', NULL),

('clean',
 'MARKETING', false, true, 'NO_LEGAL_DEFINITION',
 '"Clean" non ha una definizione legale nel diritto alimentare dell''UE e viene utilizzato in modo incoerente nell''industria.',
 NULL,
 'NONE', NULL),

('sano',
 'MARKETING', false, true, 'NO_LEGAL_DEFINITION',
 '"Sano" non ha una definizione legale nel diritto alimentare dell''UE e viene utilizzato in modo incoerente nell''industria.',
 NULL,
 'NONE', NULL),

('artigianale',
 'MARKETING', false, true, 'VAGUE_CRITERIA',
 '"Artigianale" suggerisce produzione artigianale ma non ha una definizione regolamentata nel diritto alimentare dell''UE. I prodotti industriali possono portare questa etichetta.',
 NULL,
 'NONE', NULL),

-- NUTRITIONAL — EU-regulated, validated against product data
('senza zuccheri aggiunti',
 'NUTRITIONAL', true, false, 'NONE',
 'Claim regolato: il prodotto non deve contenere zuccheri aggiunti, ma può contenere zuccheri naturalmente presenti (es. in frutta o latte). Validato contro la lista degli ingredienti: qualsiasi ingrediente classificato come zucchero aggiunto invalida questo claim.',
 'EU Reg. 1924/2006, Annex',
 'SUGAR_BELOW_THRESHOLD', 0.5),

('senza zuccheri',
 'NUTRITIONAL', true, false, 'NONE',
 'Claim regolato: il prodotto deve contenere non più di 0.5g di zuccheri per 100g o 100ml.',
 'EU Reg. 1924/2006, Annex',
 'SUGAR_BELOW_THRESHOLD', 0.5),

('pochi zuccheri',
 'NUTRITIONAL', true, false, 'NONE',
 'Claim regolato: il prodotto deve contenere non più di 5g di zuccheri per 100g per solidi o 2.5g per 100ml per liquidi.',
 'EU Reg. 1924/2006, Annex',
 'SUGAR_BELOW_THRESHOLD', 5.0),

('alto contenuto di fibre',
 'NUTRITIONAL', true, false, 'NONE',
 'Claim regolato: il prodotto deve contenere almeno 6g di fibre dietetiche per 100g.',
 'EU Reg. 1924/2006, Annex',
 'HIGH_FIBER', 6.0),

('fonte di fibre',
 'NUTRITIONAL', true, false, 'NONE',
 'Claim regolato: il prodotto deve contenere almeno 3g di fibre dietetiche per 100g.',
 'EU Reg. 1924/2006, Annex',
 'HIGH_FIBER', 3.0),

('senza ingredienti artificiali',
 'NUTRITIONAL', false, false, 'NONE',
 'Indica l''assenza di additivi sintetici o artificiali. Validato contro la lista degli ingredienti: qualsiasi ingrediente contrassegnato come artificiale invalida questo claim.',
 NULL,
 'NO_ARTIFICIAL_INGREDIENTS', NULL),

('senza additivi artificiali',
 'NUTRITIONAL', false, false, 'NONE',
 'Indica l''assenza di additivi sintetici (E-numeri di origine artificiale). Validato contro la lista degli ingredienti.',
 NULL,
 'NO_ARTIFICIAL_INGREDIENTS', NULL),

('senza additivi ad alto rischio',
 'NUTRITIONAL', false, false, 'NONE',
 'Indica l''assenza di additivi classificati come AD ALTO RISCHIO. Validato contro la classificazione del rischio degli ingredienti.',
 NULL,
 'NO_HIGH_RISK_INGREDIENTS', NULL),

-- HEALTH — EU-regulated health relationship claims
('supporta la salute del cuore',
 'HEALTH', true, false, 'NONE',
 'Claim di salute permesso sotto EU Reg. 1924/2006 solo sotto specifiche condizioni (e.g. contenuto di omega-3 provato).Richiede autorizzazione da EFSA.',
 'EU Reg. 1924/2006, Art. 13',
 'NONE', NULL),

('supporta il sistema immunitario',
 'HEALTH', true, false, 'NONE',
 'Claim di salute permesso sotto EU Reg. 1924/2006 solo sotto specifiche condizioni (e.g. contenuto di vitamina C ≥ 15% NRV per 100g).',
 'EU Reg. 1924/2006, Art. 13',
 'NONE', NULL)

    ON CONFLICT (term) DO NOTHING;