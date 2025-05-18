# XingXian (LogIn)
> Application Mobile de Gestion de Contacts avec Carte Baidu

## Présentation

Cette application Android permet de gérer des contacts et d’interagir avec une carte fournie par le SDK **Baidu Maps**. L’utilisateur peut ajouter des contacts et calculer la **distance** entre sa position actuelle et une position choisie sur la carte. La communication avec le backend se fait via **XAMPP** (PHP/MySQL).

Vous pouvez consulter **[quelques photos](https://docs.google.com/document/d/1mtSCVTp90lHcjNMrptoBCmE3s-0sa-bQoWcKFXzIKRM/edit?usp=sharing)** de l'application pour mieux visualiser son interface.

## Technologies Utilisées

* **Android Studio** (Java)
* **Baidu Map SDK**
* **XAMPP** (Apache, MySQL, PHP)
* **PHP** (côté serveur)
* **MySQL** (base de données)

## Installation et Configuration

### 1. Backend (XAMPP)

1. Installer **XAMPP** et démarrer **Apache** + **MySQL**.
2. Copier les fichiers PHP dans `xampp/htdocs/login/`.
3. Créer une base de données **econtact** avec les **tables suivantes** :

   ```sql
   CREATE DATABASE econtact;
   USE econtact;

   CREATE TABLE contact (
     id_nom INT,
     id_contact INT
   );

   CREATE TABLE utilisateur (
     id_nom INT,
     nom CHAR(50),
     prenom CHAR(50),
     num CHAR(50),
     email CHAR(100)
   );
   ```
4. Vérifier les paramètres de connexion dans `connectBD.php`.

### 2. Application Android

1. Ouvrir le projet dans **Android Studio**.
2. Connecter ou configurer un appareil virtuel Android. Par exemple, tu peux utiliser un **émulateur "Medium Phone - API 34"**.
3. Lancer l’activité principale :
   `app/java/com.example.login/.MainActivity`

## Fonctionnalités

* **Inscription & Connexion** des utilisateurs.
* **Ajout & suppression de contacts**.
* **Affichage des contacts enregistrés**.
* **Calcul de la distance** entre la position actuelle de l'utilisateur et un point sélectionné sur la carte.

## Remarques

* L’application ne montre **pas** directement la position des contacts sur la carte.
* Le calcul de la distance se fait uniquement entre **votre position** et le **point que vous sélectionnez manuellement** sur la carte.
* Le serveur XAMPP doit être actif pour utiliser les fonctionnalités réseau.

