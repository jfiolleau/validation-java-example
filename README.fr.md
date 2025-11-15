# Exemple Spring Boot – VerifNow SDK

Ce dépôt fournit un exemple minimal et soigné pour intégrer le SDK VerifNow pour Spring Boot (`io.verifnow:verifnow-spring-boot-starter`) afin de valider des champs (email, téléphone) via des annotations.

- Framework : Spring Boot 3.5.7
- Java : 21
- Build : Maven

## Ce que fait l'application
Le DTO `UserDto` utilise les annotations VerifNow pour valider automatiquement les entrées :
- `@VerifNowEmail`
- `@VerifNowPhone`

Les requêtes envoyées sur `POST /users` sont validées avant l'exécution du code du contrôleur. En cas de données invalides, Spring renvoie un 400 avec un corps d'erreur de validation standard.

## Démarrage rapide

1) Pré‑requis
- JDK 21 installé et configuré dans votre IDE
- Maven 3.9+

2) Configurer votre clé API (obligatoire)

L'application lit la configuration via des variables d'environnement. Exportez‑les dans votre shell ou via la configuration d'exécution de votre IDE :

```
export VERIFNOW_API_KEY="<votre_clef_api_verifnow>"
# Optionnel, par défaut https://api.verifnow.io
export VERIFNOW_BASE_URL="https://api.verifnow.io"
```

Remarque : Spring Boot ne charge pas automatiquement un fichier `.env`. Si vous préférez utiliser `.env`, assurez‑vous que votre shell (ex: direnv) ou votre IDE le charge. Un exemple `.env.example` est fourni.

3) Build et exécution

```
./mvnw clean package
java -jar target/validation-demo-1.0.0.jar
```

Le service démarre sur le port 8090 par défaut.

4) Tester rapidement
- POST http://localhost:8090/users
- Corps :
```
{
  "email": "john.doe@example.com",
  "phone": "+14155552671"
}
```
- Attendu : 200 OK avec le message `User is valid and accepted!`
- Données invalides : 400 avec détails d'erreur de validation.

Test rapide via curl :

```
curl -i \
  -H "Content-Type: application/json" \
  -d '{"email":"john.doe@example.com","phone":"+14155552671"}' \
  http://localhost:8090/users
```

## Configuration du SDK
Déclaration dans `pom.xml` :
- Group : `io.verifnow`
- Artifact : `verifnow-spring-boot-starter`
- Version : `1.0.0`

Configuration runtime (avec overrides via env) dans `src/main/resources/application.yml` :
- `verifnow.api.base-url` (env : `VERIFNOW_BASE_URL`, défaut `https://api.verifnow.io`)
- `verifnow.api.api-key` (env : `VERIFNOW_API_KEY`, pas de défaut ; requis)

## Structure du projet
- `DemoApplication` – classe principale Spring Boot
- `UserController` – expose `POST /users`
- `UserDto` – illustre `@VerifNowEmail` et `@VerifNowPhone`
- `application.yml` – configuration du SDK (priorité aux env)
- `postman/` – environnements et collections pour tester

## Exécution depuis IntelliJ IDEA
1) Définissez le Project SDK sur JDK 21.
2) Alignez l'import Maven et le Runner Maven sur le même JDK 21.
3) Ré‑importez Maven, puis lancez `DemoApplication`.

Si vous voyez une erreur du type :
```
java.lang.ExceptionInInitializerError com.sun.tools.javac.code.TypeTag :: UNKNOWN
```
Cela signifie souvent que l'IDE a compilé avec un JDK différent de Maven. Alignez tout sur JDK 21 et re‑compilez.

## Postman
- Importez `postman/local.postman_environment.json` et vérifiez que `baseUrl` vaut `http://localhost:8090`.
- Importez `postman/UserController.postman_collection.json` pour tester l'endpoint.

## Sécurité
- Aucune clé n'est committée. Fournissez votre clé via des variables d'environnement.
- L'exemple ne met pas en place d'authentification. Ajoutez Spring Security selon vos besoins.

## Licence
Publié sous licence MIT (voir le fichier LICENSE).

