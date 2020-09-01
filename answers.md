# Création d'un identifiant unique

Afin de s'interfacer avec les applications déjà existantes:
- Ce service devra exposer une API REST.
- Le format des requêtes est libre, tout en respectant les standards.
- La persistance (si nécessaire) se fera avec une base PostgreSQL.

## Méthodes possibles
Pour générer des identifiants uniques, il est possible de se baser sur :
 - un compteur incrémenté à chaque nouvelle requête
 - le système de temps du serveur, en donnant un nombre de millisecondes écoulées depuis un instant précis
 - une agrégation d'informations renseignée par l'utilisateur, dont l'ensemble serait unique
 - une composition des méthodes précédentes
  
Cela dit, les contraintes mène à certaines conclusions :
 - l'absence de données personnelles empêche l'utilisation des données de l'utilisateur
 - la non-continuité empêche d'utiliser un simple compteur, ou le système de temps
 - l'imprédictibilité, implique une sortie statistiquement aléatoire, 
 elle peut être obtenue en passant les identifiants via une fonction d’hashage, mais on risque des collisions de hash
 
Quitte à passer par une fonction d’hashage et gérer les collisions d’hash, 
on peut directement utiliser un générateur de nombre pseudo aléatoire et en retirer les doublons.

## Architecture possibles
Pour implémenter la solution à partir d'un générateur de nombre aléatoire, on peut réfléchir à plusieurs architectures :
 - la génération de la liste complète des identifiants possibles dans un ordre aléatoire, puis le renvoi de ces identifiants à chaque requête
 - le renvoi d'une sélection aléatoire dans une liste des identifiants pas encore renvoyés par le service
 - la génération d'un nombre aléatoire, et deduplication à partir de l'historique des identifiants déjà renvoyés par le service

## Comment vérifier
Si on peut prouver que l'algorithme proposé fonctionne sur une étendue plus courte, on peut supposer que le service sera rendu comme attendu

## Bonus
Point optionnels
 - TODO - Une API sécurisée via un token JWT.
 - TODO - Un client web de démonstration en React.
 - DONE - Un git log --graph --abbrev-commit --oneline propre.
 - DONE - Une démo dockerisée.
 - DONE - Une implémentation du système proposé pour tester l'unicité des identifiants
 - TODO - Toute autre proposition est bienvenue !