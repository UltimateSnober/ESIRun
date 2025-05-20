# ESIRun - Système de Gestion de Transport

ESIRun est une application Java de gestion de titres de transport et d'usagers pour un réseau de transport urbain. Elle permet de gérer les usagers (employés et clients), d'acheter et de valider des titres de transport (tickets et cartes de navigation), et de consulter les listes des usagers et des titres.

## Fonctionnalités principales

- **Gestion des usagers** : Ajout, affichage et filtrage des usagers (employés et clients).
- **Achat de titres** : Achat de tickets ou de cartes de navigation avec gestion des types de cartes et des réductions.
- **Validation de titres** : Vérification de la validité d'un titre de transport par son identifiant.
- **Persistance des données** : Sauvegarde et chargement automatiques des données des usagers et des titres.
- **Interface graphique** : Application JavaFX conviviale avec navigation entre les différentes fonctionnalités.

## Structure du projet

- `transport.core` : Logique métier (usagers, titres, gestion des fichiers, etc.)
- `transport.control` : Contrôleurs JavaFX pour l'interface utilisateur
- `transport.ui` : Fichiers FXML et ressources graphiques

## Lancement

Lancer la classe `Main` pour démarrer l'application. Les données sont automatiquement chargées et sauvegardées à chaque démarrage/fermeture.

```
javac Main.java
java Main
```

---
