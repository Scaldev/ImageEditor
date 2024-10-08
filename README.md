# I. Présentation générale

## 1. Principe de l'application.

ImageEditor est une application réactive permettant de manipuler des images en les considérant comme des *quadtrees*.

Les images peuvent recevoir des transformations (rotation, mirroitage, assombrissement... l'utilisateur peut même en ajouter des personnalisées). Les fonctionnalités élémentaires du dessin sont également présentes : pipette, crayon, gomme, ainsi qu'une commande pour directement sélectionner une couleur. On détaille plus loin le principe de chaque fonctionnalité.

## 2. Une application réactive.

On décrit ici le fonctionnement de l'application interactive. Les fonctionnalités depuis l'interface `Quadtrees` sont expliquées plus en détail dans le fichier `ancien_readme/README_V2.md`. A noter que ceux de la version 0 et 1 ne sont pas présents car contenus dans la version 2.

## 3. Les différents modes.

Un choix clé du développement de l'application a été l'implémentation de différentes *modes*. Ceux-ci sont au nombre de 7, et le mode en cours est visible en bas à droite de la fenêtre par un icone explicite.

**Important :** on peut sélectionner un mode en appuyant sur les touches 0, 1, 2, 3, 4, 5 ou 6 du clavier. Pour chaque mode, on indique dans le sous-titre et entre parenthèses à quel chiffre il est associé.

# II. Fonctionnalités

## 1. <u>Mode Command (0).</u> <img height=20px src="images/icons/command.png">

Par défaut, l'utilisateur est dans le mode *Command*. Cela signifie que les touches clavier sont mémorisées et écrites dans l'invité de commandes. Ce mode permet d'entrer les commandes disponibles :

- `load <filename>` permet de charger l'image du fichier de chemin relatif `<filename>`.

- `color <r> <g> <b> (<a>)` permet de sélectionner l'unique couleur associée aux valeurs `r`, `g`, `b` et `a`. Si la couleur n'existe pas, elle est remplacée par du noir. Si la valeur d'opacité n'est pas renseignée, elle vaut 255 par défaut.

Appuyer sur Entrée lance la commande, et le retour arrière est possible.

On peut passer au mode `Command` en cliquant dans le rectangle de l'invité de commande. De même, on peut sortir de ce mode en appuyant dans la fenêtre d'édition au-dessus de l'invité de commande, où en appuyant sur les chiffres de 1 à 6 du clavier.

<center>
<img height=200px src="images/readme/app_command.png">

*Exemple d'utilisation de la console. Appuyer sur Entrée charge l'image `meadow.png`.*
</center>

## 2. <u>Mode Edit (1).</u> <img height=20px src="images/icons/edit.png">

Le mode *Edit* permet de manipuler l'image sélectionnée. On peut sélectionner une image en cliquant dessus, et la désélectionner en cliquant dans le vide. Si plusieurs images sont superposées, celle la plus au-dessus sera sélectionnée.

Appuyer sur certains touches lorsqu'une image est sélectionnée permet d'intéragir avec :

- **taille** : appuyer sur `+` double ses dimensions, appuyer sur `-` les divise par 2.

- **transformations** :

    * appuyer sur `r` fait tourner l'image 90° à droite, sur `R` à gauche.

    * appuyer sur `f` fait mirroiter l'image verticalement, sur `F` horizontalement.

    * appuyer sur `d` noircit l'image à 20%, sur `D` la clarifie à 20%.

    * appuyer sur `g` la transforme en nuances de gris.

    * appuyer sur `o` affiche la grille de subdivisions du quadtree.

    * appuyer sur `c` sélectionne la transformation personnelle de l'utilisateur parmi une liste de transformations à mettre en entrée (éventuellement une liste vide). Ici, une unique fonction qui échange les valeurs de rouge, de bleu et de vert des pixels est proposée,
    elle peut donc être utilisée via cette touche. Cela permet à l'utilisateur d'utiliser ses propres transformations d'images dans l'application.

    * appuyer sur `<-` et `->` permet de se déplacer dans la liste des transformations personnelles.

    * appuyer sur `DEL` (retour arrière) supprime l'image.

<center>
<img height=200px src="images/readme/app_edit.png">

*Exemple d'utilisation en mode Edit.*
</center>

## 3. <u>Mode Move (2).</u> <img height=20px src="images/icons/move.png">

Le mode *Move* permet de déplacer l'image sélectionnée dans la fenêtre. L'image ne peut pas sortir de la fenêtre.

Cliquer en ayant une image sélectionnée la pose, cliquer sur une image sans en avoir une sélectionnée la sélectionne.

<center>
<img height=200px src="images/readme/app_move.png">
   
*Exemple d'utilisation en mode Move. L'image est ici déplacée à gauche, en suivant le curseur.*
</center>

## 4. <u>Mode Select (3).</u> <img height=20px src="images/icons/select.png">

Le mode *Select* permet de sélectionner une seconde image.

Cliquer sur une image différente de celle en sélection pendant ce mode superpose l'une sur l'autre.

<center>
<img height=200px src="images/readme/app_select.png">

*Exemple d'utilisation en mode Select. Les images `plus.png` et `cross.png` ont été chargées, agrandies deux fois, déplacées et superposées.*
</center>

## 5. <u>Mode Draw (4).</u> <img height=20px src="images/icons/draw.png">

Le mode *Draw* permet de dessiner sur une image.

Ce mode est lié à un crayon, définit par :

- **sa taille** : par défaut d'un pixel, mais modifiable via les touches `+` et `-`.

- **sa couleur** : par défaut le noir, mais modifiable via la pipette ou la commande `color`.

- **son mode** : par défaut, il faut cliquer sur chaque pixel pour le modifier. La touche `*` permet d'alterner ce mode avec un autre, où chaque pixel sous la souris est modifié. Cela permet de dessiner plus rapidement.

<center>
<img height=200px src="images/readme/app_draw.png">

*Exemple d'utilisation en mode Draw. On a écrit "Hello!" sur en haut, et fait une rature au centre.*
</center>

## 6. <u>Mode Erase (5).</u> <img height=20px src="images/icons/erase.png">

Le mode *Erase* est similaire sur de nombreux points avec le mode Draw. En particulier, la gomme possède aussi une taille et un mode modifiable de la même manière. Elle applique systématiquement la couleur transparente sur les pixels rencontrés.

<center>
<img height=200px src="images/readme/app_erase.png">

*Exemple d'utilisation en mode Draw. On a gommé la rature.*
</center>

### 7. <u>Mode Eyedropper (6).</u> <img height=20px src="images/icons/eyedropper.png">

Le mode *Eyedropper* permet de sélectionner la couleur du pixel cliqué.

<center>
<img height=200px src="images/readme/app_eyedropper.png">

*Exemple d'utilisation en mode Eyedropper. On a sélectionné certaines couleurs du dessin, puis on a dessiné sur ce qui a été gommé pour combler le trou.*
</center>
<center>
