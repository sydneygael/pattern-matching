# Scaled List

Module de demonstration d'une liste chainee immutable (`ScaleList`) en Java 25,
avec une version equivalente en Python.

## Structure

```text
scaled-list/
|-- build.gradle.kts
|-- main/java/list/ScaleList.java
|-- test/java/list/ScaleListTest.java
`-- python/
    |-- scaled_list.py
    |-- main.py
    `-- test_scaled_list.py
```

## Lancer la version Java

Depuis la racine du repository :

```powershell
.\gradlew.bat :scaled-list:test
```

## Lancer la version Python

Depuis la racine du repository :

```powershell
python .\scaled-list\python\main.py
```

## Lancer les tests Python

Depuis la racine du repository :

```powershell
python -m unittest discover -s .\scaled-list\python -p "test_*.py"
```

