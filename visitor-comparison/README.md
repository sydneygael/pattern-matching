# Visitor Comparison

## Application

Ce module compare le meme scenario facture en Java 25, Kotlin et Python.

Le scenario teste un service qui visite une facture et calcule son total en
tenant compte du type de client et de guards metier.

Une facture (`Invoice`) contient des lignes heterogenes :

- `PhysicalProduct` : un produit physique avec un prix unitaire et une quantite ;
- `OnlineProduct` : un produit en ligne avec un prix unitaire et un nombre de licences.

La facture porte aussi un `CustomerType` :

- `STANDARD` : pas de remise ;
- `PREMIUM` : remise de 5% a partir de 100 EUR ;
- `VIP` : remise de 5%, ou 10% a partir de 200 EUR.

Le service `InvoiceTotalService` parcourt les lignes de facture, additionne :

- `unitPriceCents * quantity` pour un produit physique ;
- `unitPriceCents * licenseCount` pour un produit en ligne.

Il applique ensuite les guards suivants :

- une facture doit contenir au moins un produit ;
- un panier ne peut pas depasser 10 produits au total ;
- les quantites, nombres de licences et prix unitaires doivent etre strictement positifs.

Les tests Java, Kotlin et Python verifient qu'une facture mixte, composee de
produits physiques et de produits en ligne, produit le total attendu, applique
les remises par type de client et rejette les paniers invalides.

## Implementations

| Langage | Approche |
|---|---|
| Java 25 | Visitor explicite avec `InvoiceLineVisitor`, types scelles et guards dans des `switch` avec pattern matching. |
| Kotlin | `sealed interface` et `when` exhaustif pour remplacer le double-dispatch du Visitor Java. |
| Python | Protocoles de typage et methode `accept` pour garder une version Visitor simple. |

## Regles testees

| Cas | Attendu |
|---|---|
| Facture `STANDARD` mixte | Total brut, sans remise. |
| Facture `PREMIUM` >= 100 EUR | Remise de 5%. |
| Facture `VIP` >= 200 EUR | Remise de 10%. |
| Facture vide | Rejetee. |
| Plus de 10 produits | Rejetee. |
| Quantite, licences ou prix <= 0 | Rejete. |

## Versions

| Langage / outil | Version |
|---|---|
| Java | 25 |
| Kotlin JVM | 2.3.21 |
| Python | 3.14.3 |
| pytest | 9.0.3 |
| JUnit | 6.0.3 |
| AssertJ | 3.27.7 |

## Structure

```text
visitor-comparison/
|-- build.gradle.kts
|-- src/main/java/      # implementation Java 25
|-- src/test/java/      # tests Java
|-- src/main/kotlin/    # implementation Kotlin
|-- src/test/kotlin/    # tests Kotlin
`-- python/
    |-- pyproject.toml
    |-- src/            # implementation Python
    `-- tests/          # tests pytest
```

## Executer les tests

Depuis la racine du depot, executer tous les tests du module :

```powershell
.\gradlew.bat :visitor-comparison:check
```

Executer uniquement les tests Java et Kotlin :

```powershell
.\gradlew.bat :visitor-comparison:test
```

Executer uniquement les tests Python :

```powershell
.\gradlew.bat :visitor-comparison:pythonTest
```

Executer pytest directement :

```powershell
cd visitor-comparison/python
uv run python -m pytest
```

`uv` installe les dependances Python declarees dans `python/pyproject.toml`.
