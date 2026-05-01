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

-- ── Ingredients ───────────────────────────────────────────────────────────────

INSERT INTO ingredients (name, additive_code, description, is_artificial, risk_level) VALUES
-- Naturali LOW risk
('Farina di frumento integrale',  NULL,    'Farina ottenuta dalla macinazione integrale del grano.',                                    false, 'LOW'),
('Avena',                         NULL,    'Fiocchi di avena interi, ricchi di fibre solubili (beta-glucano).',                          false, 'LOW'),
('Miele',                         NULL,    'Dolcificante naturale ottenuto dalle api.',                                                  false, 'LOW'),
('Olio extravergine di oliva',    NULL,    'Olio estratto a freddo dalle olive, ricco di acidi grassi monoinsaturi.',                   false, 'LOW'),
('Albume d''uovo',                NULL,    'Parte proteica dell''uovo, povera di grassi.',                                              false, 'LOW'),
('Latte intero',                  NULL,    'Latte vaccino intero non trattato ad alte temperature.',                                    false, 'LOW'),
('Yogurt naturale',               NULL,    'Latte fermentato con colture batteriche vive.',                                             false, 'LOW'),
('Farina di riso',                NULL,    'Farina priva di glutine ottenuta dalla macinazione del riso.',                              false, 'LOW'),
('Cacao amaro',                   NULL,    'Polvere di cacao senza zuccheri aggiunti, ricca di polifenoli.',                            false, 'LOW'),
('Sale marino',                   NULL,    'Sale estratto per evaporazione dell''acqua marina.',                                        false, 'LOW'),
('Prosciutto crudo',              NULL,    'Coscia di suino stagionata, fonte di proteine.',                                            false, 'LOW'),
('Bresaola',                      NULL,    'Carne bovina magra stagionata, molto proteica e povera di grassi.',                         false, 'LOW'),
('Aceto di vino',                 NULL,    'Condimento ottenuto dalla fermentazione acetica del vino.',                                 false, 'LOW'),
('Pomodoro',                      NULL,    'Pomodoro fresco o in polpa, fonte di licopene.',                                            false, 'LOW'),
('Basilico',                      NULL,    'Erba aromatica fresca o essiccata.',                                                        false, 'LOW'),
-- Naturali MEDIUM risk
('Zucchero',                      NULL,    'Saccarosio raffinato, apporta calorie vuote senza micronutrienti.',                         false, 'MEDIUM'),
('Sciroppo di glucosio',          NULL,    'Dolcificante derivato dall''idrolisi dell''amido, ad alto indice glicemico.',               false, 'MEDIUM'),
('Olio di palma',                 NULL,    'Olio vegetale ricco di acidi grassi saturi, controverso per salute e ambiente.',            false, 'MEDIUM'),
('Sale',                          NULL,    'Cloruro di sodio raffinato, associato a ipertensione se consumato in eccesso.',             false, 'MEDIUM'),
('Farina di frumento raffinata',  NULL,    'Farina bianca priva di crusca e germe, povera di fibre.',                                  false, 'MEDIUM'),
('Lievito chimico',               NULL,    'Agente lievitante naturale composto da bicarbonato e acido tartarico.',                     false, 'MEDIUM'),
-- Artificiali MEDIUM risk
('Lecitina di soia',              'E322',  'Emulsionante estratto dalla soia, generalmente sicuro ma di origine industriale.',          true,  'MEDIUM'),
('Acido citrico',                 'E330',  'Acidificante di sintesi, generalmente riconosciuto sicuro (GRAS).',                         true,  'MEDIUM'),
('Acido ascorbico',               'E300',  'Vitamina C sintetica usata come antiossidante e conservante.',                              true,  'MEDIUM'),
-- Artificiali HIGH risk
('Nitrito di sodio',              'E250',  'Conservante usato nei salumi: prolunga la shelf life ma correlato a rischi cancerogeni.',   true,  'HIGH'),
('Benzoato di sodio',             'E211',  'Conservante chimico attivo contro muffe e batteri, correlato a iperattività nei bambini.',  true,  'HIGH'),
('Aspartame',                     'E951',  'Dolcificante sintetico molto intenso, controverso per possibili effetti neurologici.',      true,  'HIGH'),
('Colorante rosso cocciniglia',   'E120',  'Colorante di origine animale, può causare reazioni allergiche.',                            true,  'HIGH'),
('Glutammato monosodico',         'E621',  'Esaltatore di sapidità artificiale, può causare sintomi nei soggetti sensibili.',           true,  'HIGH'),
('Carragenina',                   'E407',  'Addensante estratto dalle alghe rosse, correlato a infiammazioni intestinali.',             true,  'HIGH');

-- ── Associazioni ingredienti-allergeni ────────────────────────────────────────

INSERT INTO ingredient_allergens (ingredient_id, allergen_id)
SELECT i.id, a.id FROM ingredients i, allergens a
WHERE i.name = 'Farina di frumento integrale' AND a.code = 'GLUTINE';

INSERT INTO ingredient_allergens (ingredient_id, allergen_id)
SELECT i.id, a.id FROM ingredients i, allergens a
WHERE i.name = 'Farina di frumento raffinata' AND a.code = 'GLUTINE';

INSERT INTO ingredient_allergens (ingredient_id, allergen_id)
SELECT i.id, a.id FROM ingredients i, allergens a
WHERE i.name = 'Avena' AND a.code = 'GLUTINE';

INSERT INTO ingredient_allergens (ingredient_id, allergen_id)
SELECT i.id, a.id FROM ingredients i, allergens a
WHERE i.name = 'Albume d''uovo' AND a.code = 'UOVA';

INSERT INTO ingredient_allergens (ingredient_id, allergen_id)
SELECT i.id, a.id FROM ingredients i, allergens a
WHERE i.name = 'Latte intero' AND a.code = 'LATTE';

INSERT INTO ingredient_allergens (ingredient_id, allergen_id)
SELECT i.id, a.id FROM ingredients i, allergens a
WHERE i.name = 'Yogurt naturale' AND a.code = 'LATTE';

INSERT INTO ingredient_allergens (ingredient_id, allergen_id)
SELECT i.id, a.id FROM ingredients i, allergens a
WHERE i.name = 'Lecitina di soia' AND a.code = 'SOIA';

-- ── Product Categories ────────────────────────────────────────────────────────

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

INSERT INTO claim_definitions
(term, claim_type, is_regulated, is_misleading, misleading_reason,
 explanation, regulatory_reference, validation_strategy, validation_threshold)
VALUES
    ('naturale',
     'MARKETING', false, true, 'NO_LEGAL_DEFINITION',
     'Il termine "naturale" non ha una definizione legale nel diritto alimentare dell''UE.',
     NULL, 'NONE', NULL),
    ('salutare',
     'MARKETING', false, true, 'NO_LEGAL_DEFINITION',
     'Il termine "salutare" non è un claim approvato nel regolamento EU 1924/2006.',
     NULL, 'NONE', NULL),
    ('Light',
     'MARKETING', false, true, 'VAGUE_CRITERIA',
     '"Light" è regolato solo quando ci si riferisce a un nutriente specifico.',
     'EU Reg. 1924/2006, Art. 8', 'NONE', NULL),
    ('clean',
     'MARKETING', false, true, 'NO_LEGAL_DEFINITION',
     '"Clean" non ha una definizione legale nel diritto alimentare dell''UE.',
     NULL, 'NONE', NULL),
    ('sano',
     'MARKETING', false, true, 'NO_LEGAL_DEFINITION',
     '"Sano" non ha una definizione legale nel diritto alimentare dell''UE.',
     NULL, 'NONE', NULL),
    ('artigianale',
     'MARKETING', false, true, 'VAGUE_CRITERIA',
     '"Artigianale" suggerisce produzione artigianale ma non ha una definizione regolamentata.',
     NULL, 'NONE', NULL),
    ('senza zuccheri aggiunti',
     'NUTRITIONAL', true, false, 'NONE',
     'Claim regolato: nessuno zucchero aggiunto, ma possono essere presenti zuccheri naturali.',
     'EU Reg. 1924/2006, Annex', 'SUGAR_BELOW_THRESHOLD', 0.5),
    ('senza zuccheri',
     'NUTRITIONAL', true, false, 'NONE',
     'Claim regolato: massimo 0.5g di zuccheri per 100g o 100ml.',
     'EU Reg. 1924/2006, Annex', 'SUGAR_BELOW_THRESHOLD', 0.5),
    ('pochi zuccheri',
     'NUTRITIONAL', true, false, 'NONE',
     'Claim regolato: massimo 5g di zuccheri per 100g (solidi) o 2.5g per 100ml (liquidi).',
     'EU Reg. 1924/2006, Annex', 'SUGAR_BELOW_THRESHOLD', 5.0),
    ('alto contenuto di fibre',
     'NUTRITIONAL', true, false, 'NONE',
     'Claim regolato: almeno 6g di fibre dietetiche per 100g.',
     'EU Reg. 1924/2006, Annex', 'HIGH_FIBER', 6.0),
    ('fonte di fibre',
     'NUTRITIONAL', true, false, 'NONE',
     'Claim regolato: almeno 3g di fibre dietetiche per 100g.',
     'EU Reg. 1924/2006, Annex', 'HIGH_FIBER', 3.0),
    ('senza ingredienti artificiali',
     'NUTRITIONAL', false, false, 'NONE',
     'Indica l''assenza di additivi sintetici. Invalidato da qualsiasi ingrediente artificiale.',
     NULL, 'NO_ARTIFICIAL_INGREDIENTS', NULL),
    ('senza additivi artificiali',
     'NUTRITIONAL', false, false, 'NONE',
     'Indica l''assenza di additivi sintetici (E-numeri di origine artificiale).',
     NULL, 'NO_ARTIFICIAL_INGREDIENTS', NULL),
    ('senza additivi ad alto rischio',
     'NUTRITIONAL', false, false, 'NONE',
     'Indica l''assenza di additivi classificati come AD ALTO RISCHIO.',
     NULL, 'NO_HIGH_RISK_INGREDIENTS', NULL),
    ('supporta la salute del cuore',
     'HEALTH', true, false, 'NONE',
     'Claim di salute permesso sotto EU Reg. 1924/2006 solo sotto specifiche condizioni.',
     'EU Reg. 1924/2006, Art. 13', 'NONE', NULL),
    ('supporta il sistema immunitario',
     'HEALTH', true, false, 'NONE',
     'Claim di salute permesso sotto EU Reg. 1924/2006 solo sotto specifiche condizioni.',
     'EU Reg. 1924/2006, Art. 13', 'NONE', NULL)
    ON CONFLICT (term) DO NOTHING;

-- ── Valori nutrizionali ───────────────────────────────────────────────────────
-- (calories, proteins, carbohydrates, sugars, fats, saturated_fats, salt, fiber)

-- Cereali e colazione
INSERT INTO nutritional_values (calories, proteins, carbohydrates, sugars, fats, saturated_fats, salt, fiber) VALUES (380, 12.0, 62.0,  8.0,  7.0,  1.2, 0.3,  9.5); -- Muesli Bio
INSERT INTO nutritional_values (calories, proteins, carbohydrates, sugars, fats, saturated_fats, salt, fiber) VALUES (420,  6.0, 78.0, 28.0,  8.0,  3.5, 0.8,  2.0); -- Corn Flakes

-- Snack e patatine
INSERT INTO nutritional_values (calories, proteins, carbohydrates, sugars, fats, saturated_fats, salt, fiber) VALUES (450,  6.0, 58.0,  1.5, 22.0,  2.0, 1.2,  4.0); -- Chips Oliva
INSERT INTO nutritional_values (calories, proteins, carbohydrates, sugars, fats, saturated_fats, salt, fiber) VALUES (530,  5.5, 55.0,  2.0, 32.0, 14.0, 2.1,  1.5); -- Patatine Palma

-- Bevande
INSERT INTO nutritional_values (calories, proteins, carbohydrates, sugars, fats, saturated_fats, salt, fiber) VALUES ( 20,  0.5,  4.5,  4.0,  0.0,  0.0, 0.05, 0.5); -- Succo Arancia
INSERT INTO nutritional_values (calories, proteins, carbohydrates, sugars, fats, saturated_fats, salt, fiber) VALUES ( 42,  0.0, 10.6, 10.6,  0.0,  0.0, 0.02, 0.0); -- Energy Drink

-- Latticini
INSERT INTO nutritional_values (calories, proteins, carbohydrates, sugars, fats, saturated_fats, salt, fiber) VALUES ( 65,  5.5,  4.5,  4.5,  2.0,  1.3, 0.1,  0.0); -- Yogurt Bianco
INSERT INTO nutritional_values (calories, proteins, carbohydrates, sugars, fats, saturated_fats, salt, fiber) VALUES (120,  3.5, 18.0, 16.5,  3.5,  2.3, 0.15, 0.0); -- Yogurt Frutti

-- Dolci e biscotti
INSERT INTO nutritional_values (calories, proteins, carbohydrates, sugars, fats, saturated_fats, salt, fiber) VALUES (410,  8.0, 58.0, 12.0, 16.0,  3.5, 0.4,  5.0); -- Biscotti Avena
INSERT INTO nutritional_values (calories, proteins, carbohydrates, sugars, fats, saturated_fats, salt, fiber) VALUES (490,  5.5, 65.0, 38.0, 22.0, 11.0, 0.6,  1.0); -- Merendine Cacao

-- Salumi e affettati
INSERT INTO nutritional_values (calories, proteins, carbohydrates, sugars, fats, saturated_fats, salt, fiber) VALUES (157, 29.0,  0.0,  0.0,  4.0,  1.2, 2.5,  0.0); -- Bresaola
INSERT INTO nutritional_values (calories, proteins, carbohydrates, sugars, fats, saturated_fats, salt, fiber) VALUES (310, 18.0,  2.0,  1.0, 26.0,  9.5, 3.2,  0.0); -- Salame

-- Condimenti e salse
INSERT INTO nutritional_values (calories, proteins, carbohydrates, sugars, fats, saturated_fats, salt, fiber) VALUES (130,  2.5,  8.0,  6.5, 10.0,  1.5, 1.2,  2.0); -- Pesto
INSERT INTO nutritional_values (calories, proteins, carbohydrates, sugars, fats, saturated_fats, salt, fiber) VALUES (105,  1.5, 25.0, 22.0,  0.5,  0.1, 2.8,  0.5); -- Ketchup

-- Pane e prodotti da forno
INSERT INTO nutritional_values (calories, proteins, carbohydrates, sugars, fats, saturated_fats, salt, fiber) VALUES (230,  9.0, 44.0,  2.0,  2.0,  0.3, 0.9,  6.5); -- Pane Integrale
INSERT INTO nutritional_values (calories, proteins, carbohydrates, sugars, fats, saturated_fats, salt, fiber) VALUES (280,  8.0, 56.0,  4.5,  3.5,  0.8, 1.5,  1.5); -- Pane Bianco

-- ── Prodotti ──────────────────────────────────────────────────────────────────
-- Ogni coppia: prodotto migliore (health_score alto, clean_label true)
-- e prodotto peggiore (health_score basso, clean_label false).
-- AlternativeSuggestionService suggerisce il migliore quando si apre il peggiore.

-- Cereali e colazione
INSERT INTO products (name, brand, description, health_score, sustainability_score, is_clean_label, category_id, nutritional_value_id)
SELECT 'Muesli Integrale Bio', 'GranoVerde',
       'Muesli con fiocchi di avena integrali e miele, senza zuccheri aggiunti.',
       78, 82, true, c.id, n.id
FROM product_categories c, nutritional_values n
WHERE c.name = 'Cereali e colazione'
  AND n.calories = 380 AND n.proteins = 12.0 AND n.fiber = 9.5;

INSERT INTO products (name, brand, description, health_score, sustainability_score, is_clean_label, category_id, nutritional_value_id)
SELECT 'Corn Flakes al Miele', 'CerealTop',
       'Fiocchi di mais soffiati con aggiunta di zucchero e aromi artificiali.',
       35, 40, false, c.id, n.id
FROM product_categories c, nutritional_values n
WHERE c.name = 'Cereali e colazione'
  AND n.calories = 420 AND n.proteins = 6.0 AND n.fiber = 2.0;

-- Snack e patatine
INSERT INTO products (name, brand, description, health_score, sustainability_score, is_clean_label, category_id, nutritional_value_id)
SELECT 'Chips Olio di Oliva', 'NaturSnack',
       'Patatine cotte in olio extravergine di oliva, senza aromi artificiali.',
       55, 60, true, c.id, n.id
FROM product_categories c, nutritional_values n
WHERE c.name = 'Snack e patatine'
  AND n.calories = 450 AND n.fats = 22.0;

INSERT INTO products (name, brand, description, health_score, sustainability_score, is_clean_label, category_id, nutritional_value_id)
SELECT 'Patatine Gusto Panna', 'SnackMax',
       'Patatine fritte con olio di palma, glutammato e coloranti artificiali.',
       22, 25, false, c.id, n.id
FROM product_categories c, nutritional_values n
WHERE c.name = 'Snack e patatine'
  AND n.calories = 530 AND n.fats = 32.0;

-- Bevande
INSERT INTO products (name, brand, description, health_score, sustainability_score, is_clean_label, category_id, nutritional_value_id)
SELECT 'Succo di Arancia 100%', 'FruttaBio',
       'Succo di arancia puro senza zuccheri aggiunti né conservanti.',
       72, 70, true, c.id, n.id
FROM product_categories c, nutritional_values n
WHERE c.name = 'Bevande'
  AND n.calories = 20 AND n.sugars = 4.0;

INSERT INTO products (name, brand, description, health_score, sustainability_score, is_clean_label, category_id, nutritional_value_id)
SELECT 'Energy Drink Tropical', 'BoomEnergy',
       'Bevanda energizzante con caffeina, taurina, aspartame e coloranti artificiali.',
       18, 20, false, c.id, n.id
FROM product_categories c, nutritional_values n
WHERE c.name = 'Bevande'
  AND n.calories = 42 AND n.sugars = 10.6;

-- Latticini
INSERT INTO products (name, brand, description, health_score, sustainability_score, is_clean_label, category_id, nutritional_value_id)
SELECT 'Yogurt Bianco Intero', 'MonteLatte',
       'Yogurt intero con fermenti lattici vivi, senza zuccheri aggiunti.',
       80, 65, true, c.id, n.id
FROM product_categories c, nutritional_values n
WHERE c.name = 'Latticini'
  AND n.calories = 65 AND n.sugars = 4.5;

INSERT INTO products (name, brand, description, health_score, sustainability_score, is_clean_label, category_id, nutritional_value_id)
SELECT 'Yogurt Frutti Rossi Zuccherato', 'DolceLatte',
       'Yogurt con preparazione ai frutti rossi, zucchero aggiunto e coloranti.',
       38, 42, false, c.id, n.id
FROM product_categories c, nutritional_values n
WHERE c.name = 'Latticini'
  AND n.calories = 120 AND n.sugars = 16.5;

-- Dolci e biscotti
INSERT INTO products (name, brand, description, health_score, sustainability_score, is_clean_label, category_id, nutritional_value_id)
SELECT 'Biscotti Avena e Miele', 'NaturDolce',
       'Biscotti integrali con avena, miele e olio di oliva. Senza conservanti.',
       65, 68, true, c.id, n.id
FROM product_categories c, nutritional_values n
WHERE c.name = 'Dolci e biscotti'
  AND n.calories = 410 AND n.fiber = 5.0;

INSERT INTO products (name, brand, description, health_score, sustainability_score, is_clean_label, category_id, nutritional_value_id)
SELECT 'Merendine Cacao Cremose', 'DolciGolosi',
       'Merendine farcite con crema al cacao, olio di palma e conservanti.',
       20, 18, false, c.id, n.id
FROM product_categories c, nutritional_values n
WHERE c.name = 'Dolci e biscotti'
  AND n.calories = 490 AND n.fiber = 1.0;

-- Salumi e affettati
INSERT INTO products (name, brand, description, health_score, sustainability_score, is_clean_label, category_id, nutritional_value_id)
SELECT 'Bresaola Valtellinese IGP', 'AlpiSalumi',
       'Bresaola di manzo stagionata senza nitriti aggiunti, magra e proteica.',
       74, 55, true, c.id, n.id
FROM product_categories c, nutritional_values n
WHERE c.name = 'Salumi e affettati'
  AND n.calories = 157 AND n.proteins = 29.0;

INSERT INTO products (name, brand, description, health_score, sustainability_score, is_clean_label, category_id, nutritional_value_id)
SELECT 'Salame Milano Classico', 'NorciaSapori',
       'Salame di suino con conservanti al nitrito, spezie e aromi artificiali.',
       28, 30, false, c.id, n.id
FROM product_categories c, nutritional_values n
WHERE c.name = 'Salumi e affettati'
  AND n.calories = 310 AND n.proteins = 18.0;

-- Condimenti e salse
INSERT INTO products (name, brand, description, health_score, sustainability_score, is_clean_label, category_id, nutritional_value_id)
SELECT 'Pesto Genovese DOP', 'LiguriaGusto',
       'Pesto tradizionale con basilico DOP, olio EVO e parmigiano. Senza conservanti.',
       70, 72, true, c.id, n.id
FROM product_categories c, nutritional_values n
WHERE c.name = 'Condimenti e salse'
  AND n.calories = 130 AND n.fats = 10.0;

INSERT INTO products (name, brand, description, health_score, sustainability_score, is_clean_label, category_id, nutritional_value_id)
SELECT 'Ketchup Classico', 'SalsaKing',
       'Ketchup con pomodoro concentrato, zucchero, benzoato di sodio e coloranti.',
       25, 28, false, c.id, n.id
FROM product_categories c, nutritional_values n
WHERE c.name = 'Condimenti e salse'
  AND n.calories = 105 AND n.sugars = 22.0;

-- Pane e prodotti da forno
INSERT INTO products (name, brand, description, health_score, sustainability_score, is_clean_label, category_id, nutritional_value_id)
SELECT 'Pane Integrale al Farro', 'FornoAntico',
       'Pane con farina di farro integrale, lievito madre e sale marino.',
       76, 75, true, c.id, n.id
FROM product_categories c, nutritional_values n
WHERE c.name = 'Pane e prodotti da forno'
  AND n.calories = 230 AND n.fiber = 6.5;

INSERT INTO products (name, brand, description, health_score, sustainability_score, is_clean_label, category_id, nutritional_value_id)
SELECT 'Pane Bianco in Cassetta', 'PanSoft',
       'Pane morbido con farina raffinata, olio di palma e conservanti.',
       30, 32, false, c.id, n.id
FROM product_categories c, nutritional_values n
WHERE c.name = 'Pane e prodotti da forno'
  AND n.calories = 280 AND n.fiber = 1.5;

-- ── Ingredienti per prodotto ──────────────────────────────────────────────────

INSERT INTO product_ingredients (product_id, ingredient_id)
SELECT p.id, i.id FROM products p, ingredients i
WHERE p.name = 'Muesli Integrale Bio'
  AND i.name IN ('Avena', 'Miele', 'Farina di frumento integrale');

INSERT INTO product_ingredients (product_id, ingredient_id)
SELECT p.id, i.id FROM products p, ingredients i
WHERE p.name = 'Corn Flakes al Miele'
  AND i.name IN ('Farina di frumento raffinata', 'Zucchero', 'Sciroppo di glucosio', 'Aspartame', 'Sale');

INSERT INTO product_ingredients (product_id, ingredient_id)
SELECT p.id, i.id FROM products p, ingredients i
WHERE p.name = 'Chips Olio di Oliva'
  AND i.name IN ('Olio extravergine di oliva', 'Sale marino', 'Acido citrico');

INSERT INTO product_ingredients (product_id, ingredient_id)
SELECT p.id, i.id FROM products p, ingredients i
WHERE p.name = 'Patatine Gusto Panna'
  AND i.name IN ('Olio di palma', 'Sale', 'Glutammato monosodico', 'Colorante rosso cocciniglia', 'Benzoato di sodio');

INSERT INTO product_ingredients (product_id, ingredient_id)
SELECT p.id, i.id FROM products p, ingredients i
WHERE p.name = 'Succo di Arancia 100%'
  AND i.name IN ('Acido ascorbico', 'Acido citrico');

INSERT INTO product_ingredients (product_id, ingredient_id)
SELECT p.id, i.id FROM products p, ingredients i
WHERE p.name = 'Energy Drink Tropical'
  AND i.name IN ('Sciroppo di glucosio', 'Aspartame', 'Benzoato di sodio', 'Colorante rosso cocciniglia', 'Acido citrico');

INSERT INTO product_ingredients (product_id, ingredient_id)
SELECT p.id, i.id FROM products p, ingredients i
WHERE p.name = 'Yogurt Bianco Intero'
  AND i.name IN ('Latte intero', 'Yogurt naturale');

INSERT INTO product_ingredients (product_id, ingredient_id)
SELECT p.id, i.id FROM products p, ingredients i
WHERE p.name = 'Yogurt Frutti Rossi Zuccherato'
  AND i.name IN ('Latte intero', 'Zucchero', 'Colorante rosso cocciniglia', 'Acido citrico', 'Carragenina');

INSERT INTO product_ingredients (product_id, ingredient_id)
SELECT p.id, i.id FROM products p, ingredients i
WHERE p.name = 'Biscotti Avena e Miele'
  AND i.name IN ('Avena', 'Miele', 'Olio extravergine di oliva', 'Farina di frumento integrale', 'Albume d''uovo', 'Lievito chimico');

INSERT INTO product_ingredients (product_id, ingredient_id)
SELECT p.id, i.id FROM products p, ingredients i
WHERE p.name = 'Merendine Cacao Cremose'
  AND i.name IN ('Farina di frumento raffinata', 'Zucchero', 'Olio di palma', 'Cacao amaro', 'Lecitina di soia', 'Aspartame', 'Benzoato di sodio', 'Latte intero');

INSERT INTO product_ingredients (product_id, ingredient_id)
SELECT p.id, i.id FROM products p, ingredients i
WHERE p.name = 'Bresaola Valtellinese IGP'
  AND i.name IN ('Bresaola', 'Sale marino', 'Aceto di vino');

INSERT INTO product_ingredients (product_id, ingredient_id)
SELECT p.id, i.id FROM products p, ingredients i
WHERE p.name = 'Salame Milano Classico'
  AND i.name IN ('Prosciutto crudo', 'Sale', 'Nitrito di sodio', 'Glutammato monosodico', 'Acido ascorbico');

INSERT INTO product_ingredients (product_id, ingredient_id)
SELECT p.id, i.id FROM products p, ingredients i
WHERE p.name = 'Pesto Genovese DOP'
  AND i.name IN ('Basilico', 'Olio extravergine di oliva', 'Sale marino', 'Acido ascorbico');

INSERT INTO product_ingredients (product_id, ingredient_id)
SELECT p.id, i.id FROM products p, ingredients i
WHERE p.name = 'Ketchup Classico'
  AND i.name IN ('Pomodoro', 'Zucchero', 'Sale', 'Acido citrico', 'Benzoato di sodio', 'Colorante rosso cocciniglia');

INSERT INTO product_ingredients (product_id, ingredient_id)
SELECT p.id, i.id FROM products p, ingredients i
WHERE p.name = 'Pane Integrale al Farro'
  AND i.name IN ('Farina di frumento integrale', 'Sale marino', 'Lievito chimico');

INSERT INTO product_ingredients (product_id, ingredient_id)
SELECT p.id, i.id FROM products p, ingredients i
WHERE p.name = 'Pane Bianco in Cassetta'
  AND i.name IN ('Farina di frumento raffinata', 'Olio di palma', 'Sale', 'Zucchero', 'Lecitina di soia', 'Benzoato di sodio');