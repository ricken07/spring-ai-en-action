# Spécifications Fonctionnelles et Techniques – Demo App

## 1. Présentation Générale

### 1.1 Objectif de l'application
Demo App est une application destinée à exposer les informations d'une bibliothèque à travers une API REST. Elle est conçue selon une approche orientée données, en utilisant les dernières fonctionnalités de Java 23+. Les données sont stockées localement à l'aide d'une structure de type `Map`.

### 1.2 Public cible
Cette API est destinée aux développeurs, aux gestionnaires de bibliothèque et à toute application souhaitant accéder ou manipuler les données de la bibliothèque.

### 1.3 Fonctionnalités principales
- Consulter la liste des livres
- Ajouter un livre
- Modifier un livre existant
- Supprimer un livre
- Rechercher un livre par titre, auteur ou ISBN

## 2. Architecture de l'application

### 2.1 Technologies utilisées
- Langage : Java 23+
- Architecture : RESTful API
- Base de données : Stockage local via `Map`
- Construction du projet : Maven

### 2.2 Organisation des packages
```
com.demo.bibliotheque
│
├── modele
│   └── Livre.java
├── controleur
│   └── LivreControleur.java
├── service
│   └── LivreService.java
├── repository
│   └── LivreRepository.java
├── dto
│   └── LivreDTO.java
└── DemoApp.java
```

## 3. Modèle de Données

### 3.1 Classe Livre
```java
public record Livre(String id, String titre, String auteur, String isbn, int anneePublication) {}
```

### 3.2 Stockage des données
Utilisation d'une `Map<String, Livre>` dans le `LivreRepository`, la clé étant l'`id` du livre.

## 4. API REST – Détail des Endpoints

### 4.1 Récupérer tous les livres
- **Méthode** : `GET`
- **URL** : `/livres`
- **Description** : Retourne la liste de tous les livres.

### 4.2 Récupérer un livre par ID
- **Méthode** : `GET`
- **URL** : `/livres/{id}`
- **Description** : Retourne les détails d’un livre spécifique.

### 4.3 Ajouter un livre
- **Méthode** : `POST`
- **URL** : `/livres`
- **Description** : Ajoute un nouveau livre à la bibliothèque.

### 4.4 Modifier un livre
- **Méthode** : `PUT`
- **URL** : `/livres/{id}`
- **Description** : Met à jour les informations d’un livre existant.

### 4.5 Supprimer un livre
- **Méthode** : `DELETE`
- **URL** : `/livres/{id}`
- **Description** : Supprime un livre par son identifiant.

### 4.6 Rechercher un livre par titre, auteur ou ISBN
- **Méthode** : `GET`
- **URL** : `/livres/recherche`
- **Paramètres** : `titre`, `auteur`, `isbn`
- **Description** : Filtre les livres en fonction des critères fournis.

## 5. Comportement Métier

### 5.1 Règles de validation
- Le titre, l’auteur et l’ISBN sont obligatoires pour l’ajout et la mise à jour.
- L’ISBN doit être unique dans la base.
- L’année de publication ne peut pas être dans le futur.

### 5.2 Gestion des erreurs
- **404** si l'identifiant du livre n'existe pas.
- **400** si les données fournies sont invalides.

## 6. Services Internes

### 6.1 LivreService
Contient la logique métier : vérification des doublons, validation des entrées, transformations DTO ↔ modèle.

### 6.2 LivreRepository
Classe de stockage simulant une base de données avec une `Map`. Responsable des opérations CRUD.

## 7. Exemple de flux

### Ajouter un livre
1. L'utilisateur envoie un `POST /livres` avec les données.
2. Le `LivreService` valide les informations.
3. Si tout est correct, le `LivreRepository` ajoute le livre dans la `Map`.
4. Le livre est retourné avec son ID généré.

## 8. Sécurité
- Pas de gestion des utilisateurs pour cette version démo.
- Les données sont accessibles en clair.

## 9. Extensibilité
- Possibilité d’ajouter un moteur de persistance réel plus tard (JPA, MongoDB).
- Support de pagination et tri pour les requêtes futures.
- Intégration facile avec Spring Boot ou Quarkus si besoin.

## 10. Exemples de Requêtes

### POST /livres
```json
{
  "titre": "L'Étranger",
  "auteur": "Albert Camus",
  "isbn": "9782070360024",
  "anneePublication": 1942
}
```

### GET /livres/recherche?titre=Camus
Retourne tous les livres dont le titre ou l’auteur contient "Camus".

## 11. Tests

### 11.1 Types de tests
- Tests unitaires (JUnit 5)
- Tests d’intégration sur les endpoints REST (MockMvc ou HTTPClient)

### 11.2 Objectifs
- Vérifier les cas de base (ajout, suppression, récupération)
- Tester les cas limites (ISBN en double, année invalide, etc.)

## 12. Déploiement
- Exécution locale via la méthode `main()`
- Aucun serveur d’application requis (application Java simple)

## 13. Journalisation
- Utilisation de `System.out.println` ou d’un logger basique pour traçabilité
- Niveau INFO pour les opérations normales, ERROR pour les exceptions

## 14. Limitations
- Données volatiles (pas de persistance réelle)
- Non sécurisé
- Pas de gestion d’utilisateur ou de rôles

## 15. Conclusion
Demo App est un prototype simple mais extensible, mettant en œuvre les principes REST et la modernité de Java 23+. Elle constitue une base solide pour des évolutions futures, y compris une intégration avec une base de données réelle, une couche de sécurité, et une interface utilisateur.
