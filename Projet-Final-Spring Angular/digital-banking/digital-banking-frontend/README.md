# Digital Banking — Frontend Angular

Frontend Angular qui consomme l'API REST du backend Spring Boot.

## Prérequis
- Node.js 18+ et npm
- Angular CLI : `npm install -g @angular/cli`
- Le backend Spring Boot lancé sur `http://localhost:8085`

## Installation et lancement
```bash
npm install
ng serve
```
Application accessible sur http://localhost:4200

## Configuration
L'URL de l'API se règle dans `src/environments/environment.ts`
(par défaut `http://localhost:8085/api`).

## Fonctionnalités
- Liste des clients, recherche par nom, suppression
- Ajout d'un client (formulaire réactif avec validation)
- Consultation d'un compte avec historique paginé des opérations
- Opérations : débit, crédit, virement

## Important : CORS
Le backend expose déjà `@CrossOrigin("*")` sur ses contrôleurs.
Après l'ajout de Spring Security (partie suivante), il faudra
remplacer cette annotation par une configuration CORS globale,
sinon les requêtes du frontend seront bloquées avant d'atteindre
les contrôleurs.
