## Idées protocole communication  
Entre Téléphones clients et Laptop ou Téléphone serveur :  
Protocole UDP, une partie = n clients. Besoin d'un id pour identifier les clients.

Afin d'identifier les messages venant des clients, ces messages seront encapsulés dans une structure suivante :  
Paquet = [idClient] [MESSAGE]
NB: Si le contrôle de perte de paquet est activé, on pourra utilise celle ci à la place :
Paquet = [nPaquet idClient] [MESSAGE]
Où nPaquet est le numéro du paquet. Le serveur devra alors renvoyer des aquitements pour chaque paquet reçu.  
Ainsi les paquets manquant pourront être renvoyés par le client.

### Connection :
Client envoie
- NEW GAME  
Quand 2 new game reçu, création instance de jeu côté serveur
Le serveur génère alors un identifiant aléatoire (noté id id sur cette page) pour chacun des clients

### Initialisation :
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

## Idées d'extentions possibles :
- Contrôle d'erreur (non réception de paquet) surtout au cours de la partie Jeu
- A l'initialisation : Gérer position du drone par rapport aux 2 portables
- Mise à jour d'une barre de progression informative sur l'avancement de la partie et des joueurs sur l'écran du joueur
- Gestion du choix du niveau au début de la partie
