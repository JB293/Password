# Generateur de mots de passe securises

## 1. Presentation du projet

Ce projet est une application en ligne de commande developpee en Java 21. Elle permet de generer des mots de passe selon des criteres choisis par l'utilisateur, puis de verifier leur robustesse avec un outil externe execute dans un conteneur Docker.

L'objectif est de separer deux responsabilites :

- Java genere les mots de passe et gere l'interaction avec l'utilisateur ;
- Docker execute l'outil de validation charge de donner le score de securite.

Le validateur utilise est `zxcvbn`, un outil connu pour estimer la force d'un mot de passe en tenant compte de sa longueur, de sa composition et de certains schemas faciles a deviner.

## 2. Fonctionnalites

L'application propose les fonctionnalites demandees dans le cahier des charges :

- choix de la longueur du mot de passe ;
- choix des minuscules ;
- choix des majuscules ;
- choix des chiffres ;
- choix des symboles ;
- generation d'un ou plusieurs mots de passe en mode rafale ;
- affichage d'un indicateur de force pour chaque mot de passe.

Les niveaux de force affiches sont :

- Tres faible ;
- Faible ;
- Moyen ;
- Fort ;
- Tres fort.

L'utilisation se fait uniquement dans le terminal avec des saisies utilisateur.

## 3. Structure du projet

```text
Dynamic_Password/
├── Docker/
│   ├── Dockerfile
│   └── check-password.sh
├── src/
│   └── main/
│       └── java/
│           ├── Main.java
│           ├── generateurPassword.java
│           └── ValidateurDocker.java
├── pom.xml
└── README.md
```

## 4. Analyse technique

### Main.java

La classe `Main` gere l'interface CLI. Elle demande les parametres de generation, verifie que les valeurs saisies sont correctes, lance la generation et affiche les resultats.

Elle gère aussi le mode rafale en demandant combien de mots de passe doivent etre produits.

### generateurPassword.java

La classe `generateurPassword` contient la logique de generation.

Elle utilise `SecureRandom`, qui est plus adapte a la generation de donnees sensibles qu'un generateur aleatoire classique.

Pour respecter les choix de l'utilisateur, le programme force au moins un caractere de chaque type selectionne. Par exemple, si l'utilisateur choisit majuscules, chiffres et symboles, le mot de passe final contiendra au moins une majuscule, un chiffre et un symbole.

Le mot de passe est ensuite melange pour eviter que les caracteres obligatoires apparaissent toujours au meme endroit.

### ValidateurDocker.java

La classe `ValidateurDocker` lance un conteneur Docker depuis Java avec `ProcessBuilder`.

Le mot de passe genere est transmis au conteneur `password-validator`. Le conteneur calcule le score avec `zxcvbn` et renvoie uniquement un nombre entre 0 et 4.

Java recupere ce score et le convertit en indicateur lisible :

```text
0 -> Tres faible
1 -> Faible
2 -> Moyen
3 -> Fort
4 -> Tres fort
```


## 5. Validation Docker

Le dossier `Docker` contient les fichiers necessaires a la construction de l'image.

Le fichier `Dockerfile` installe l'outil `zxcvbn` dans une image Node.js Alpine.

Le script `check-password.sh` reçoit le mot de passe en argument, appelle `zxcvbn`, puis affiche seulement le score numérique attendu par Java.

## 6. Installation

Prerequis :

- Java 21 ;
- Docker ;
- Maven, si vous souhaitez utiliser la compilation Maven.

Construire l'image Docker :

```powershell
docker build -t password-validator -f Docker/Dockerfile .
```

Tester le conteneur seul :

```powershell
docker run --rm password-validator "Test123@"
```

La commande doit afficher un nombre entre `0` et `4`.

## 7. Compilation et execution

Compilation avec Maven :

```powershell
mvn clean compile
```

Compilation directe avec Java :

```powershell
javac -d out src/main/java/*.java
```

Execution apres compilation directe :

```powershell
java -cp out Main
```

## 8. Exemple d'utilisation

```text
Entrez la longueur du mot de passe (min 8): 12
Inclure des minuscules (abcd) (oui/non) : oui
Inclure des majuscules (ABCD) (oui/non) : oui
Inclure des chiffres (0123) (oui/non) : oui
Inclure des symboles (%@#) (oui/non) : oui
Combien de mots de passe souhaitez-vous generer ? 3

=========================================
RESULTATS DE GENERATION DU MOT DE PASSE
=========================================
[1] Exemple1@Abc --> [Indicateur: Fort]
[2] Exemple2#Def --> [Indicateur: Tres fort]
[3] Exemple3%Ghi --> [Indicateur: Fort]
```



