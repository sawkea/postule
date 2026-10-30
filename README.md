# postule
Projet d'application PWA pour suivre ses candidatures lors de recherches d'emploi en JAVA et Angular.

## Getting Started

J'ai initialisé un projet en JAVA avec le framework Spring Boot en utilisant le configurateur Maven : https://start.spring.io/.

## Prérequis

- Java 21
- Docker (avec Docker Compose)
- Node.js v26
- Npm v12

### Reference Documentation
Précisions :

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/4.1.2/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.1.2/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/4.1.2/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/4.1.2/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot/4.1.2/reference/io/validation.html)
* [Flyway Migration](https://docs.spring.io/spring-boot/4.1.2/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)

### Guides

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)

### Dépendances

* Spring Web
* Spring Data JPA
* Validation
* PostgreSQL Driver
* Flyway Migration
* Lombok

### Lancer le projet en local

#### 1. Démarrer la base de données

PostgreSQL est fourni via Docker, uniquement pour le développement local.
Depuis la racine du dépôt :

```bash
docker compose up -d
```

Cela crée une base `suivi` accessible sur `localhost:5432`
(utilisateur `suivi`, mot de passe `suivi`, identifiants de développement uniquement).

Commandes utiles :

```bash
docker compose ps         # vérifier que la base tourne
docker compose down       # arrêter la base (les données sont conservées)
docker compose down -v    # arrêter et supprimer les données
```

#### 2. Démarrer le backend

```bash
cd backend
./mvnw spring-boot:run
```

Au démarrage, Flyway crée automatiquement les tables.
L'API est ensuite disponible sur http://localhost:8080.

#### 3. Frontend

```bash
npx @angular/cli new frontend --skip-git # pour créer un projet angular sans dépôt GIT
```

```bash
 ng add @angular/pwa # transforme l'application angular en PWA (Progressive Web App)
 ```

```bash
ng serve # lancer le server et ouvrir : http://localhost:4200
 ```

```bash
npx ng serve --proxy-config proxy.conf.json # (création d'un fichier proxy pour appeler la route /api.. sans erreur CORS
 ```

 * j'ai ajouté dans le package.json dans le start la commande directement : il faudra juste taper : npm start pour lancer et ouvrir la page sur :

```bash
 http://localhost:4200/api/applications
  ```