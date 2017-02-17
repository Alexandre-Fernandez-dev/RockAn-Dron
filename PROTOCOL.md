## Idées protocole communication  
Entre Téléphones clients et Laptop ou Téléphone serveur :  
Protocole UDP, une partie = n clients. Besoin d'un id pour identifier les clients.

Si on souhaite contrôler la perte de paquet, les messages seront encapsulés dans la structure suivante :  
Paquet = [idClient] [MESSAGE]
Où nPaquet est le numéro du paquet. Le serveur devra alors renvoyer des aquitements pour chaque paquet reçu.  
Ainsi les paquets manquant pourront être renvoyés par le client.

### Connection :
Client envoie :
- CONNECT pseudo:string  
Premier message envoyé au serveur.
Serveur envoie :
- CONNECTOK  
  S'il reçoit ce message ce cas Client envoie :
  - READYRECEIVE Celà permet au serveur d'attendre que le client ait bien initialisé sa socket (s'il ne reçoit pas ce message il renvoie le CONNECTOK et au bout de 4 tentatives il supprime le client)
- CONNECTBAD  
Renvoyé si l'indentifiant est déjà pris.

### Initialisation/gestion de la partie :
Client envoie :
- AULIST  
Demande au serveur la liste des utilisateurs

Serveur envoie :
- ULIST nom1 nom2 ...  
Renvoie la liste des utilisateurs au client

Client envoie :
- STARTGAMEOK

Serveur envoie :
- STARTGAME nbsec:int  
Envoyé quand tout les clients de la partie ont envoyé STARTGAMEOK. La partie commence pour le client et le serveur au bout des nbSec

### Partie Jeu :
Client envoie :
- SCORETICK score:byte  
Par exemple toutes les 500 ms.
Met à jour le score du joueur côté serveur, et leur avancement relatif par rapport a la piste (si jeu musical)

### Partie Fin :
Serveur envoie :
- GAMEEND pseudoWinner:string  
Envoyé lorsqu'un gagnant a été décidé, plusieurs idées pour définir les règles : Plus on approche de la fin de la piste, plus le drone va avancer vers le jouer au meilleur score. Ou bien, sans prendre en compte la fin de la piste (peut finir avant, ou reboucler sur la piste mais plus vite) demande plus d'équilibrage sur le gameplay.

### Déconnection
Client envoie :
- DISCONNECT  
Si jamais le client n'est pas déconnecté, nettoyer les clients qui ne jouent plus depuis un certain temps.

## Idées d'extentions possibles :
- Contrôle d'erreur (non réception de paquet) surtout au cours de la partie Jeu
- A l'initialisation : Gérer position du drone par rapport aux 2 portables
- Mise à jour d'une barre de progression informative sur l'avancement de la partie et des joueurs sur l'écran du joueur
- Gestion du choix du niveau au début de la partie
