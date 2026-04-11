# Clean Label Backend

Prototipo Backend RESTful per il progetto SWAM 2024-2025 — University of Florence.

## Tecnologie

- **Java 17** + **Jakarta EE 10**
- **WildFly 30** (server applicativo)
- **Hibernate 6** (implementazione JPA)
- **MySQL 8** (database)
- **Docker + Docker Compose** (per avvio semplificato)

---

## Avvio con Docker (consigliato)

### Prerequisiti
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installato e avviato
- [Maven](https://maven.apache.org/download.cgi) installato (`mvn -v` per verificare)
- [Java 17+](https://adoptium.net/) installato (`java -version` per verificare)

### Step 1 — Compila il progetto
```bash
mvn clean package -DskipTests
```
Questo produce il file `target/cleanlabel-backend-1.0-SNAPSHOT.war`.

### Step 2 — Avvia i container
```bash
docker-compose up -d --build
```
Questo comando:
1. Scarica e avvia **MySQL 8** con il database `cleanlabel_db` già creato
2. Scarica e avvia **WildFly 30** con il datasource MySQL pre-configurato
3. Deploya automaticamente il file `.war` su WildFly
4. Il `DatabaseSeeder` popola il DB con dati di esempio al primo avvio

### Step 3 — Verifica che funzioni
Apri il browser e vai su:
```
http://localhost:8080/cleanlabel-backend/api/products
```
Dovresti vedere la lista dei 5 prodotti di esempio in formato JSON.

---

## Endpoints disponibili

| Metodo | URL | Descrizione |
|--------|-----|-------------|
| GET | `/api/products` | Lista prodotti (con filtri) |
| GET | `/api/products/{id}` | Dettaglio prodotto |
| GET | `/api/products/{id}/score` | Health score breakdown |
| GET | `/api/products/{id}/claims` | Claims del prodotto |
| GET | `/api/products/{id}/alternatives` | Alternative più sane |
| GET | `/api/products/{id}/ingredients` | Ingredienti del prodotto |
| POST | `/api/products` | Crea prodotto |
| PUT | `/api/products/{id}` | Aggiorna prodotto |
| DELETE | `/api/products/{id}` | Elimina prodotto |
| GET | `/api/categories` | Lista categorie |
| GET | `/api/ingredients` | Lista ingredienti |
| GET | `/api/ingredients/search?q=...` | Cerca ingrediente |
| GET | `/api/auth/mock-user` | Utente mock CORPORATE |
| GET | `/api/auth/mock-consumer` | Utente mock CONSUMER |

### Esempi di filtro su GET /api/products
```
?category=Snacks
?minScore=60
?maxScore=40
?allergen=GLUTEN          ← esclude prodotti con glutine
?cleanOnly=true
?page=0&size=10
```

---

## Fermare i container
```bash
docker-compose down
```

Per fermare E cancellare anche i dati del database:
```bash
docker-compose down -v
```

---

## Struttura del progetto

```
src/main/java/it/unifi/swam/cleanlabel/
├── model/          Entity JPA (tabelle del DB)
├── dtos/           Data Transfer Objects (JSON scambiati con il FE)
├── mappers/        Conversione Entity ↔ DTO
├── rest/           Endpoint JAX-RS (logica HTTP)
└── startup/        DatabaseSeeder (dati di esempio)

src/main/resources/
└── META-INF/
    └── persistence.xml   Configurazione JPA

docker/
└── wildfly/
    └── standalone.xml    Configurazione WildFly (datasource MySQL)

docker-compose.yml        Orchestrazione MySQL + WildFly
pom.xml                   Dipendenze Maven
```

---

## Credenziali database (solo per sviluppo)

| Parametro | Valore |
|-----------|--------|
| Host | `localhost:3306` |
| Database | `cleanlabel_db` |
| Utente | `cleanlabel_user` |
| Password | `cleanlabel_pass` |
| Root password | `cleanlabel_root` |
