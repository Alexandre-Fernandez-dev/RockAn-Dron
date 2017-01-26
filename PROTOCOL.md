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
Premier message envoyé au serveur, le serveur génère alors un identifiant aléatoire, idClient, pour chacun des clients, cet identifiant sera utilisé pour construire les paquets venant des clients.
Serveur envoie :
- CONNECT OK idClient:byte
- CONNECT BAD
Renvoyé si l'indentifiant est déjà pris.

### Initialisation :
Client envoie :
- NEW GAME nbJouer:byte
Création instance de jeu côté serveur
- JOIN GAME
Permet a un client de rejoindre la partie

Serveur envoie :
- NEW GAME OK
- NEW GAME BAD
Si on gère une partie a la fois. On renvoie NEW GAME BAD lorsque l'on a reçu un NEW GAME et qu'une partie est déjà lancée.
Sinon, si on utilise des identifiants pour les différentes parties, NEW GAME BAD indique un doublon.
- JOIN GAME BAD
Si il n'y a plus de place dans la partie.

Serveur envoie :
- START GAME
5 secondes (par exemple) avant le début de partie (par exemple). Permet au client de savoir son client.

Client envoie :
- START GAME OK
Envoyé au bout des 5 secondes. La partie commence côté client et serveur si le message a été reçu autant de fois que de joueur.

### Partie Jeu :
Client envoie :
- SCORE TICK score:byte
Par exemple toutes les 500 ms.
Met à jour le score du joueur côté serveur, et leur avancement relatif par rapport a la piste (si jeu musical)

### Partie Fin :
Serveur envoie :
- GAME END idwinner:byte
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
