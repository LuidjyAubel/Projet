# API PROJET LPMIAR 

>Cette API est un projet pour la licence LPMIAR, elle est créer en __Kotlin__ avec le framework __Springboot__.

## Route disponible

GET :

- /etudiant
- /etudiant/{id}
- /etudiant/{numEtudiant}/groupe
- /etudiant/{numEtudiant}/presence
- /etudiant/{numEtudiant}/presence/{id}
- /groupe
- /groupe/{numGroupe}
- /groupe/{numGroupe}/etudiants
- /groupe/{numGroupe}/etudiants/{numEtudiant}
- /groupe/{numGroupe}/creneaux
- /groupe/{numGroupe}/creneaux/{idCreneau}
- /cours
- /cours/{id}
- /cours/{id}/creneaux
- /cours/{id}/creneaux/{id}
-/formation
- /formation/{id}
- /formation/{id}/cours
- /formation/{id}/cours/{id}
- /prof
- /prof/{id}
- /prof/{id}/favoris
- /prof/{id}/favoris/{num}
- /prof/{id}/creneaux
- /prof/{id}/creneaux/{num}
- /creneau
- /creneau/{id}
- /presence
- /presence/{id}

POST :

- /etudiant
- /groupe
- /cours
- /formation
- /prof
- /creneau
- /creneau/{id}/presence/{num}
- /presence

PUT :

- /etudiant/{id}
- /groupe/{numGroupe}
- /cours/{id}
- /formation/{id}
- /prof/{id}
- /creneau/{id}
- /presence/{id}

DELETE :

- /etudiant/{id}
- /groupe/{numGroupe}
- /cours/{id}
- /formation/{id}
- /prof/{id}
- /creneau/{id}
- /presence/{id}

## Déployement de l'api

> 1. Cloné le projet à partir du dépot gitlab ou github
> 2. Rendez-vous dans le répertoire du projet.
> 3. Executer dans un terminal la commande `gradle build` (linux) ou `gradlew build` (windows)
> 4. Executer la commande `gradle war` (linux) ou `gradlew war` (windows)
> 5. Récupéré le war créer dans ***./build/libs***
> 6. Utilisé le war que vous avez récupéré pour le déployer sur un serveur tomcat