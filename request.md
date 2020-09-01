# Création d'un identifiant unique

## Présentation

### Communiqué officiel du Royaume d'Hyrule
Oyé ! Oyé ! Notre magnifique Royaume se modernise !
Afin de faciliter les démarches administratives de nos 100 000 000 d'habitants, notre
douce Princesse Zelda a décidé de lancer la production de documents d'identité pour
tous ses citoyens. Tout Hyruléen devra faire une demande de ce document pour qu'il
lui soit délivré.

### Description
Chaque document possède un identifiant unique. Celui-ci a une longueur de 9
caractères et est constitué uniquement de chiffres.
Cet identifiant doit avoir les caractéristiques suivantes :
- On ne doit pas pouvoir prédire l'identifiant qui sera attribué pour un nouveau
document en connaissant un ou plusieurs des identifiants précédents.
- Deux identifiants étant donnés, on ne doit pas pouvoir déterminer s'ils ont été
générés l'un à la suite de l'autre.
- L'identifiant ne doit contenir aucune information personnelle du demandeur
du document.
Afin de s'interfacer avec les applications déjà existantes :
- Ce service devra exposer une API REST.
- Le format des requêtes est libre, tout en respectant les standards.
- La persistance (si nécessaire) se fera avec une base PostgreSQL.

### Demande du client
- Décrire une ou plusieurs façons de générer cet identifiant.
- Présenter un ou plusieurs choix d'architecture.
- Si possible, faire une implémentation en Java/Kotlin/SpringBoot/Hibernate
d'une des solutions.

### Vérification
- Décrire comment on peut s'assurer que le système génère bien des identifiants
uniques (black box testing) .
### Bonus
Point optionnels
- Une API sécurisée via un jeton JWT.
- Un client web de démonstration en React.
- Un `git log --graph --abbrev-commit --oneline` propre.
- Une démo dockerisée.
- Une implémentation du système proposé pour tester l'unicité des identifiants
- Toute autre proposition est bienvenue !