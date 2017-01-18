## Structure client :
- MVC : 
  - Structure :
    - Vue : Affichage et boutons.
    - Modèle : Système de jeu (partie, score, "map", etc...)
    - Controlleur : partie réseau et mise à jour du modèle.  
    ![Exemple de mvc](https://developer.chrome.com/static/images/mvc.png)  
    (Diagramme a titre informatif)
  
  - Points forts/faibles :
    - Plus : Système de jeu (modèle) géré côté client et serveur, communication réseau simple.  
    - Moins :
      - Peut-être trop lourd pour ce type de client ??
      - Pas de vérification de triche.

- Autre piste, client léger :
  - Points forts/faibles :
    - Moins : Probablement trop d'informations sur le réseau (informations sur le jeu/niveau). Pas adapté à un jeu de rythme (réaction trop longue).
