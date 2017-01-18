# RockAn’Dron

## Présentation
  Projet d'étude de master 1 STL à l'UPMC. Matière : PSTL
  Etudiants Sylvain Ung et Alexandre Fernandez

## Description du projet
Jeux de drone "RockAn’Dron"

Dans le cadre des activités robotiques au sein du M2 STL, nous aurions besoin d’explorer la programmation sur les drones. A l’heure actuelle, toutes les réalisations robotiques du M2 STL se basent sur des plateformes mobiles terrestres. Il est souhaitable d’explorer d’autres possibilités de support. Nous proposons de nous attaquer à ce projet sous un angle "Kid games", en récoltant des données de jeux multi-joueurs et en envoyant des commandes de déplacement selon ces données sur un drone baptisé "RockAn’Dron".

Le premier de tels jeux serait un Tambour-Hero où de multiples joueurs jouent en parallèle un jeu de rythme sur des smartphones Android en mode "client". Les résultats de chaque joueur sont évalués dynamiquement sur chaque support "client" avant d’être synchronisés sur un smartphone Android en mode "serveur". Les évaluations de chaque joueur doivent refléter sa capacité à imiter un batteur dans un morceau de musique folle. A chaque synchronisation sur le "serveur"(de l’ordre de la seconde par exemple), le smartphone "serveur" décide le gagnant actuel et envoie le drone faire un pas vers le gagnant en question. Le jeu termine quand un des joueurs arrive à attirer vers lui le drone, porteur de gros paquets de bonbons, ou de nouilles, voire d’autres choses selon des envies du moment.

Ce PSTL s’adresse à des étudiants en M1 STL curieux des réalisations robotiques ouvertes au grand publique de type "tout en kit" (1). Dépendant du niveau de chaque participant au PSTL, on commencerait avec une partie de programmation purement Android, puis, on réfléchirait à l’intégrer dans les actionneurs du drone ; ou l’inverse ; voire on ferait des choses complètement imprévues. Une réalisation, cependant, est attendue au terme du PSTL, avec poster de présentation et montage de vidéo-clip.

L’avantage de ce PSTL est qu’il permet d’acquérir des compétences à la fois en programmation Android (partie jeu multi-joueurs) et en programmation sur les drone (partie RockAn’Dron). Le PSTL introduit également aux activités robotiques au sein du M2 STL. Le point délicat de ce PSTL est qu’un drone autonome est toujours très dangereux, parfois même assez suicidaire, à l’instar de ceux envoyés sur Mars...

## Idees protocole communication (Entre Téléphones clients et Laptop ou Téléphone serveur) :
Protocole UDP, une partie = 2 clients.

### Connection :
Client envoie
- NEW GAME  
Quand 2 new game reçu, création instance de jeu côté serveur

### Initialisation :
Serveur envoie :
- START GAME byte (id joueur)  
5 secondes (par exemple) avant le début de partie (par exemple)

Client envoie :
- START GAME OK  
Envoyé au bout des 5 secondes. La partie commence côté client et serveur.

### Partie Jeu :
Client envoie :
- SCORE TICK byte  
Par exemple toutes les 500 ms.
Met à jour le score du joueur côté serveur, et leur avancement relatif par rapport a la piste (si jeu musical)

### Partie Fin :
- GAME END byte (id joueur gagnant)  
Envoyé lorsqu'un gagnant a été décidé, plusieurs idées pour définir les règles : Plus on approche de la fin de la piste, plus le drone va avancer vers le jouer au meilleur score. Ou bien, sans prendre en compte la fin de la piste (peut finir avant, ou reboucler sur la piste mais plus vite) demande plus d'équilibrage sur le gameplay.

## Idées d'extentions possibles :
- Contrôle d'erreur (non réception de paquet) surtout au cours de la partie Jeu
- A l'initialisation : Gérer position du drone par rapport aux 2 portables
- Mise à jour d'une barre de progression informative sur l'avancement de la partie et des joueurs sur l'écran du joueur
- Gestion du choix du niveau au début de la partie

