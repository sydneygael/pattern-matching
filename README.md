# Pattern Matching

Projet Gradle multi-module pour explorer les evolutions recentes du langage Java,
en particulier les fonctionnalites liees au pattern matching et aux JEPs associees.

## Modules

| Module | Description |
|---|---|
| `jep-demo` | Exemples avant/apres autour des switch expressions, pattern matching, records, sealed classes, record patterns et pattern matching for switch. |
| `bytecode-demo` | Comparaison de bytecode entre style `instanceof` traditionnel et `switch` avec pattern matching, avec rapport detaille via `BytecodeInspector.report(...)` (taille `.class`, methodes, interfaces, opcodes). |
| `scaled-list` | Liste chainee immutable en Java 25 (`ScaleList`) et version Python equivalente avec `cons`, `fold`, `map`, `flat_map`, `filter`, `append`, `concat`. |

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
| Python | 3.10+ |
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

Lancer les tests du module `bytecode-demo` :

```powershell
.\gradlew.bat :bytecode-demo:test
```

Relancer et afficher aussi les `print` du rapport bytecode :

```powershell
.\gradlew.bat :bytecode-demo:test --rerun-tasks
```

Lancer les tests du module `scaled-list` :

```powershell
.\gradlew.bat :scaled-list:test
```

Lancer le programme Python du module `scaled-list` :

```powershell
python .\scaled-list\python\main.py
```

Lancer les tests Python du module `scaled-list` :

```powershell
python -m unittest discover -s .\scaled-list\python -p "test_*.py"
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
|-- bytecode-demo/
|   |-- build.gradle.kts
|   `-- src/
`-- scaled-list/
    |-- build.gradle.kts
    |-- README.md
    |-- main/java/
    |-- test/java/
    `-- python/
```

## Tests

Les tests JVM utilisent JUnit Jupiter et AssertJ. Les dependances de test sont
centralisees dans le build racine et appliquees aux sous-projets Java.

Le module `bytecode-demo` imprime dans les tests un rapport d'inspection
bytecode pour `AvantPatternMatching` et `ApresPatternMatching` incluant :
taille du fichier `.class`, modificateurs, super-classe, interfaces, methodes
declarees, et opcodes de la methode `aire`.

Le module `scaled-list` dispose de tests JUnit pour la version Java et de tests
`unittest` pour la version Python.
