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
- CONNECTBAD
Renvoyé si l'indentifiant est déjà pris.

### Initialisation :
Client envoie :
- NEWGAME nbJouer:byte levelID:int levelLength:long
Création instance de jeu côté serveur. levelid: utilisé pour que les clients reconnaissent le niveau.
- JOINGAME
Permet a un client de rejoindre la partie

Serveur envoie :
- NEWGAME OK
- NEWGAME BAD
Si on gère une partie a la fois. On renvoie NEW GAME BAD lorsque l'on a reçu un NEW GAME et qu'une partie est déjà lancée.
Sinon, si on utilise des identifiants pour les différentes parties, NEW GAME BAD indique un doublon.
- JOINGAME OK levelID:int
- JOINGAME BAD
Si il n'y a plus de place dans la partie.

Serveur envoie :
- STARTGAME
5 secondes (par exemple) avant le début de partie (par exemple). Permet au client de savoir son client.

Client envoie :
- STARTGAMEOK
Envoyé au bout des 5 secondes. La partie commence côté client et serveur si le message a été reçu autant de fois que de joueur.

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
