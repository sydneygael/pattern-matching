# Visitor Comparison

## Application

Le scenario a tester est un service qui visite une facture et calcule son total
en tenant compte du type de client et de guards metier.

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
|-- src/main/java/      # implementation Java 25, dont le scenario InvoiceTotalService
|-- src/test/java/      # tests Java du calcul de total de facture
|-- src/main/kotlin/    # implementation Kotlin, dont le scenario InvoiceTotalService
|-- src/test/kotlin/    # tests Kotlin du calcul de total de facture
`-- python/
    |-- pyproject.toml
    |-- src/            # implementation Python du scenario InvoiceTotalService
    `-- tests/          # tests pytest du calcul de total de facture
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
