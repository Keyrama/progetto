# Clean Label

Prototipo full-stack per il progetto SWAM 2024-2025 — Università di Firenze.

Il sistema analizza i claim presenti sulle etichette di prodotti alimentari confezionati, valutandone la conformità rispetto alla normativa EU (Reg. 1924/2006) e ai dati nutrizionali/ingredienti dichiarati.

---

## Stack tecnologico

### Backend
- **Java 21** + **Spring Boot 3.2**
- **Spring Data JPA** + **Hibernate 6**
- **PostgreSQL 15**
- **Liquibase** (migration e seed del database)
- **MapStruct 1.6** (mapping Entity ↔ DTO)
- **Caffeine** (cache in-memory)

### Frontend
- **Angular 17**
- **Bootstrap 5** + **Bootstrap Icons**
- **TypeScript 5.4**

---

## Prerequisiti

- Java 21+
- Maven 3.9+
- Node.js 18+ e npm
- Docker Desktop (per avviare PostgreSQL)

---

## Avvio del backend

### 1. Avvia PostgreSQL con Docker

```bash
cd cleanlabel-backend
docker-compose up -d
```

Questo avvia un container PostgreSQL 16 con:
- Database: `cleanlabel`
- Utente: `postgres` / Password: `postgres`
- Porta: `5432`

I dati persistono tra i riavvii grazie al volume `cleanlabel_data`.

Per fermare il container:
```bash
docker-compose down
```

Per fermare e cancellare anche i dati:
```bash
docker-compose down -v
```

### 2. Avvia il backend

```bash
mvn spring-boot:run
```

Al primo avvio **Liquibase** esegue automaticamente le migration in ordine:
- `V1__schema.sql` — crea tutte le tabelle
- `V2__seed_data.sql` — popola il database con:
- 14 allergeni EU (Reg. 1169/2011)
- 30 ingredienti a vari livelli di rischio
- 8 categorie di prodotto
- 16 prodotti (2 per categoria, uno migliore e uno peggiore per testare i suggerimenti alternativi)
- 16 claim definitions (libreria EU)

Ai riavvii successivi Liquibase verifica la tabella `DATABASECHANGELOG` e non riesegue script già applicati — i dati inseriti manualmente dalla UI vengono preservati.

Per aggiungere nuovi dati seed in futuro è sufficiente creare un nuovo script `V3__...sql` nella cartella delle migration.

Il backend è raggiungibile su:
```
http://localhost:8080/cleanlabel-backend
```

---

## Avvio del frontend

```bash
cd cleanlabel-frontend
npm install
ng serve
```

Il frontend è raggiungibile su `http://localhost:4200`.

---

## Autenticazione mock

Il sistema usa un'autenticazione simulata tramite header HTTP `X-Mock-User-Role`. Non è richiesto login — il ruolo si seleziona direttamente dalla navbar.

| Ruolo | Permessi |
|---|---|
| `CONSUMER` | Lettura catalogo, dettaglio prodotti |
| `SPECIALIST` | + Analisi claim su prodotti, gestione libreria claim |
| `CORPORATE` | + Creazione/modifica/eliminazione prodotti, ingredienti, categorie |

---

## Endpoints API principali

Tutti gli endpoint sono sotto `/cleanlabel-backend/api`.

### Prodotti
| Metodo | URL | Ruolo |
|---|---|---|
| GET | `/products` | tutti |
| GET | `/products/{id}` | tutti |
| GET | `/products/{id}/alternatives` | tutti |
| GET | `/products/{id}/claims` | tutti |
| POST | `/products/{id}/claims/analyze` | SPECIALIST, CORPORATE |
| POST | `/products` | CORPORATE |
| PUT | `/products/{id}` | CORPORATE |
| DELETE | `/products/{id}` | CORPORATE |

### Filtri disponibili su `GET /products`
```
?search=bio
?category=3
?cleanLabel=true
```
I filtri sono combinabili: `?search=bio&cleanLabel=true` funziona correttamente.

### Claim definitions (libreria)
| Metodo | URL | Ruolo |
|---|---|---|
| GET | `/claims/definitions` | tutti |
| POST | `/claims/definitions` | SPECIALIST, CORPORATE |
| PUT | `/claims/definitions/{id}` | SPECIALIST, CORPORATE |
| DELETE | `/claims/definitions/{id}` | CORPORATE |

### Altri endpoint
| Metodo | URL | Descrizione |
|---|---|---|
| GET | `/categories` | Lista categorie |
| GET | `/ingredients` | Lista ingredienti |
| GET | `/allergens` | Lista allergeni EU |

---

## Struttura del progetto

```
cleanlabel-backend/
└── src/main/java/it/unifi/swam/cleanlabel/
    ├── config/         RoleGuard (autenticazione mock), CORS
    ├── controller/     REST controllers
    ├── dtos/           Data Transfer Objects
    ├── exception/      GlobalExceptionHandler, ResourceNotFoundException
    ├── mappers/        MapStruct (Entity ↔ DTO)
    ├── model/          Entità JPA
    ├── repository/     Spring Data JPA + Specifications
    │   └── spec/       Predicati componibili per i filtri
    └── service/
        ├── validator/  Strategia di validazione claim (Strategy pattern)
        └── *.java      Business logic

cleanlabel-frontend/
└── src/app/
    ├── components/
    │   ├── admin-catalogue/          Gestione catalogo (CORPORATE)
    │   ├── admin-categories/         Gestione categorie
    │   ├── admin-claim-definitions/  Libreria claim (SPECIALIST+CORPORATE)
    │   ├── admin-ingredients/        Gestione ingredienti
    │   ├── navbar/
    │   ├── product-alternatives/     Suggerimenti prodotti alternativi
    │   ├── product-card/
    │   ├── product-detail/           Dettaglio + analisi claim
    │   ├── product-filter/
    │   └── product-list/
    ├── models/         Interfacce TypeScript
    └── services/       HTTP client verso il backend
```

---

## Note di design

- **Claim analysis** — processo in due fasi: matching del termine nella libreria, poi validazione dinamica contro ingredienti e valori nutrizionali tramite Strategy pattern.
- **Alternative suggerite** — calcolate a runtime comparando prodotti della stessa categoria con health score superiore. Nessuna entità persistita.
- **Filtri prodotti** — implementati con JPA Specification per permettere combinazioni arbitrarie di parametri nella stessa query.
- **DDL** — gestito da **Liquibase** con script versionati in `db/migration/`. `ddl-auto=validate` verifica che lo schema sia allineato alle entità senza modificarlo. Per un reset completo basta droppare il DB e riavviare — Liquibase ricrea tutto da zero.