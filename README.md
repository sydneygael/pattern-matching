# Pattern Matching

Projet Gradle multi-module pour explorer les evolutions recentes du langage Java,
en particulier les fonctionnalites liees au pattern matching et aux JEPs associees.

## Modules

| Module | Description |
|---|---|
| `jep-demo` | Exemples avant/apres autour des switch expressions, pattern matching, records, sealed classes, record patterns et pattern matching for switch. |
| `visitor-comparison` | Meme scenario facture en Java, Kotlin et Python : lignes heterogenes, guards metier, type de client et calcul du total. |

## Prerequis

- Java 25
- Gradle Wrapper fourni avec le projet

Le projet configure Java 25 via les toolchains Gradle pour le projet racine et tous
les sous-projets.

## Versions principales

| Outil / bibliotheque | Version |
|---|---|
| Gradle | 9.3.1 |
| Java | 25 |
| Kotlin JVM | 2.3.21 |
| Python | 3.14.3 |
| JUnit | 6.0.3 |
| AssertJ | 3.27.7 |

## Commandes utiles

Lister les modules :

```powershell
.\gradlew.bat projects
```

Lancer tous les tests :

```powershell
.\gradlew.bat test
```

Lancer les tests du module `jep-demo` :

```powershell
.\gradlew.bat :jep-demo:test
```

Lancer les tests Java, Kotlin et Python du module `visitor-comparison` :

```powershell
.\gradlew.bat :visitor-comparison:check
```

Afficher la version de Gradle utilisee par le wrapper :

```powershell
.\gradlew.bat --version
```

## Structure

```text
.
|-- build.gradle.kts
|-- settings.gradle.kts
|-- gradlew
|-- gradlew.bat
|-- gradle/
|-- jep-demo/
|   |-- build.gradle
|   |-- README.md
|   `-- src/
`-- visitor-comparison/
    |-- build.gradle.kts
    |-- README.md
    |-- src/
    `-- python/
```

## Tests

Les tests JVM utilisent JUnit Jupiter et AssertJ. Les dependances de test sont
centralisees dans le build racine et appliquees aux sous-projets Java.

Le module `visitor-comparison` ajoute aussi des tests Python executables via
`uv` et pytest. Sa tache `check` couvre les tests Java, Kotlin et Python.
