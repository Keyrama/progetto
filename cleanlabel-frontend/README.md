# CleanLabel Frontend — Angular 17

## Avvio rapido

```bash
npm install
ng serve
```
Apri http://localhost:4200

## Funzionalità UC1
- Griglia prodotti con score colorato (verde/arancio/rosso)
- Filtro per nome/brand, categoria, clean label, score minimo
- Paginazione
- **Modalità offline automatica**: se il backend non risponde, usa i mock data

## Connessione al backend
Il frontend chiama `http://localhost:8080/cleanlabel-backend/api`.
Quando il backend è attivo, usa i dati reali. Se non risponde, passa 
automaticamente ai mock (badge "Modalità offline" nella navbar).

## Struttura
```
src/app/
├── models/          # Interfacce TypeScript
├── services/        # ProductService (con mock fallback)
└── components/
    ├── navbar/
    ├── product-list/   # UC1 - pagina principale
    ├── product-card/   # Singola card prodotto
    └── product-filter/ # Sidebar filtri
```
