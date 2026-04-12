# VerifNow Spring Boot – Exemple de validation

> **Projet exemple public** montrant comment intégrer le SDK Spring [VerifNow](https://verifnow.io) (`io.verifnow:verifnow-spring-boot-starter:2.1.0`) dans une application Spring Boot.

English version: see [README.md](README.md)

| Stack | Version |
|-------|---------|
| Java | 21 |
| Spring Boot | 3.5.13 |
| SDK VerifNow | 2.1.0 |
| Build | Maven |

---

## Ce que ce projet démontre

Cette application exemple couvre les trois principaux patterns d'intégration du SDK Spring VerifNow :

### 1. Validation par annotations sur les DTO

La classe `UserDto` utilise les annotations VerifNow pour valider automatiquement les champs via le mécanisme standard Bean Validation (`@Valid`) — aucun appel API manuel n'est nécessaire :

```java
public class UserDto {

  @VerifNowEmail(maxRiskScore = 30)   // rejette les emails avec un score de risque > 30
  private String email;

  @VerifNowPhone
  private String phone;
}
```

### 2. Gestion globale des erreurs avec réponse JSON structurée

Le `ValidationExceptionHandler` (`@RestControllerAdvice`) intercepte les erreurs de validation et retourne un corps JSON structuré au lieu de la réponse 400 par défaut de Spring :

```json
{
  "timestamp": "2026-04-12T14:30:00.000Z",
  "status": 400,
  "error": "Validation failed",
  "fieldErrors": {
    "email": "Adresse email invalide"
  }
}
```

### 3. Personnalisation des messages d'erreur

Surchargez les messages par défaut du SDK en plaçant un fichier `ValidationMessages.properties` dans `src/main/resources/` :

```properties
validation.verifnow.email.invalid=Adresse email invalide
validation.verifnow.phone.invalid=Numéro de téléphone invalide
```

Les variantes par locale sont également supportées : `ValidationMessages_fr.properties`, `ValidationMessages_en.properties`, etc.

---

## Démarrage rapide

### Pré-requis

- JDK 21 installé et configuré dans votre IDE
- Maven 3.9+
- Une clé API VerifNow ([obtenir une clé](https://verifnow.io))

### 1 – Configurer votre clé API

L'application lit sa configuration via des variables d'environnement. Exportez-les dans votre shell ou configurez-les dans votre IDE :

```bash
export VERIFNOW_API_KEY="<votre_clef_api_verifnow>"
# Optionnel – par défaut https://api.verifnow.io
export VERIFNOW_BASE_URL="https://api.verifnow.io"
```

> **Note :** Spring Boot ne charge pas automatiquement un fichier `.env`. Si vous préférez `.env`, assurez-vous que votre shell (ex : direnv) ou votre IDE le charge.

### 2 – Build et exécution

```bash
./mvnw clean package
java -jar target/validation-demo-1.0.0.jar
```

Le service démarre sur le port **8090** par défaut.

### 3 – Tester

**Requête valide :**

```bash
curl -s -X POST http://localhost:8090/users \
  -H "Content-Type: application/json" \
  -d '{"email":"john.doe@example.com","phone":"+14155552671"}'
```

→ `200 OK` – `User is valid and accepted!`

**Requête invalide (email incorrect) :**

```bash
curl -s -X POST http://localhost:8090/users \
  -H "Content-Type: application/json" \
  -d '{"email":"not-an-email","phone":"+14155552671"}'
```

→ `400 Bad Request` avec un corps d'erreur structuré :

```json
{
  "timestamp": "2026-04-12T14:30:00.000Z",
  "status": 400,
  "error": "Validation failed",
  "fieldErrors": {
    "email": "Adresse email invalide"
  }
}
```

---

## Configuration du SDK

Déclaration dans `pom.xml` :

```xml
<dependency>
    <groupId>io.verifnow</groupId>
    <artifactId>verifnow-spring-boot-starter</artifactId>
    <version>2.1.0</version>
</dependency>
```

Configuration runtime dans `src/main/resources/application.yml` (avec surcharge par variables d'environnement) :

| Propriété | Variable d'env | Défaut |
|-----------|---------------|--------|
| `verifnow.api.base-url` | `VERIFNOW_BASE_URL` | `https://api.verifnow.io` |
| `verifnow.api.api-key` | `VERIFNOW_API_KEY` | *(aucun – obligatoire)* |

En général, il suffit de définir `VERIFNOW_API_KEY`.

---

## Structure du projet

```
src/main/java/com/example/
├── DemoApplication.java              # Point d'entrée Spring Boot
├── controller/
│   └── UserController.java           # Endpoint POST /users avec @Valid
├── dto/
│   └── UserDto.java                  # @VerifNowEmail, @VerifNowPhone
└── exception/
    └── ValidationExceptionHandler.java  # Gestionnaire d'erreurs global (@RestControllerAdvice)

src/main/resources/
├── application.yml                   # Configuration SDK & serveur
└── ValidationMessages.properties     # Surcharge des messages d'erreur

postman/                              # Collection & environnement Postman
```

---

## Exécution depuis IntelliJ IDEA

1. Définissez le **Project SDK** sur JDK 21.
2. Alignez l'import Maven et le Runner Maven sur le même JDK 21.
3. Ré-importez Maven, puis lancez `DemoApplication`.

Si vous voyez une erreur du type :

```
java.lang.ExceptionInInitializerError com.sun.tools.javac.code.TypeTag :: UNKNOWN
```

Cela signifie souvent que l'IDE a compilé avec un JDK différent de Maven. Alignez tout sur JDK 21 et recompilez.

---

## Postman

Utilisez les fichiers du répertoire `postman/` :

- Importez `local.postman_environment.json` et vérifiez que `baseUrl` vaut `http://localhost:8090`.
- Importez `UserController.postman_collection.json` — elle contient trois requêtes prêtes à l'emploi :
  - **Create User – Valid** (attend 200)
  - **Create User – Invalid Email** (attend 400)
  - **Create User – Invalid Phone** (attend 400)

---

## Sécurité

- Aucune clé n'est committée. Fournissez votre clé via des variables d'environnement.
- Cet exemple ne configure pas d'authentification. Ajoutez Spring Security selon vos besoins.

## Licence

MIT – voir le fichier [LICENSE](LICENSE).
