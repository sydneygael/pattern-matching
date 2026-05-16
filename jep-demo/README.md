# JEP Demo — Avant / Après

Projet Gradle illustrant les évolutions du langage Java via les JEPs,
avec des tests unitaires JUnit 5 montrant le comportement avant et après chaque fonctionnalité.

## JEPs couvertes

| Classe de test | JEP | Fonctionnalité | Java |
|---|---|---|---|
| `Jep361Test` | JEP 361 | Switch Expressions | 14 |
| `Jep394Test` | JEP 394 | Pattern Matching for instanceof | 16 |
| `Jep395Test` | JEP 395 | Records | 16 |
| `Jep409_441Test` | JEP 409 | Sealed Classes | 17 |
| `Jep409_441Test` | JEP 440 | Record Patterns | 21 |
| `Jep409_441Test` | JEP 441 | Pattern Matching for Switch | 21 |

## Prérequis

- Java 21+
- Gradle 8.x (ou utiliser le wrapper)

## Lancer les tests

```bash
./gradlew test
```

## Structure

```
src/
├── main/java/jep/
│   ├── Jep361_SwitchExpressions.java
│   ├── Jep394_PatternMatchingInstanceof.java
│   ├── Jep395_Records.java
│   └── Jep409_441_SealedAndSwitch.java
└── test/java/jep/
    ├── Jep361Test.java
    ├── Jep394Test.java
    ├── Jep395Test.java
    └── Jep409_441Test.java
```

## Philosophie des tests

Chaque test est labellisé `[AVANT]` ou `[APRÈS]` pour montrer :
- `[AVANT]` : comportement obtenu avec l'ancienne syntaxe (cast manuel, switch statement, POJO...)
- `[APRÈS]` : comportement équivalent avec la nouvelle fonctionnalité JEP
- Les tests `[AVANT/APRÈS]` vérifient l'**équivalence comportementale** des deux approches
