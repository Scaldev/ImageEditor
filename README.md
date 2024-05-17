<center>

# Quatrees version 0

*par REDOR Ewan et PACCINI Mathys*

</center>

## I. Fonctionnalités.

Le projet permet d'effectuer toutes les fonctionnalités d'une version 0.

### A. <u>Structure du projet et quadtrees.</u>

Le fichier `Main.scala` correspond au côté utilisateur.

Le fichier `IntQuadtrees.scala` correspond à l'interface de notre projet Quadtrees. Il contient la définition du type algébrique `QT` représentant les quadtrees, définit comme étant :

- soit `C(c: Color)`, une feuille contenant une seule valeur : sa couleur, représentée par le type `Color` de Scribble.
- soit `N(no: QT, ne: QT, se: QT, so: QT)`, un noeud interne possédant 4 sous-quadtrees où chacun représente un quart de zone.

```scala
sealed trait QT
case class C(c: Color) extends QT
case class N(no: QT, ne: QT, se: QT, so: QT) extends QT
```

L'inteface consiste pour l'instant en une unique fonction `quadtree_to_image` qui convertit un quadtree en une image Scribble.

Le fichier `ImpQuadtrees.scala` implémente l'interface ci-dessus. Elle contient 2 fonctions auxiliaires privées implémentant `quadtree_to_image`.

### B. <u>Conversion en image.</u>

Utiliser la fonction `quadtree_to_imag` nécessite de donner en paramètre :

- `quadtree` le quadtree a visualiser.
- `show_grid` un booléen spécifiant si on affiche la grille des subdivisions (`true`) ou non (`false`).
- `size_order` l'ordre de la taille de l'image, correspondant à la puissance $n$ telle que l'image sera de longueur et de largeur $2^n$.

Ci-dessous, un exemple minimal de création d'Univers via l'interface.

```scala
/* Exemple de création d'image. */

val servQT: IntQuadtrees = ???

val quadtree: QT = C(WHITE)
val show_grid: Boolean = true
val size_order: Int = 9

val image: Image = servQT.quadtree_to_image(quadtree, show_grid, size_order)
draw(image)
```

### C. <u>Grille de subdivisions.</u>

Paramétrer `show_grid` comme valant `true` (resp. `false`) affiche (resp. cache) la grille des subdivisions successives en quarts. Cette grille correspondant à un contour rouge (couleur `RED` de Scribble) autour de toutes les formes dessinées.

<img style="display: block; margin: auto; height: 512px;" src="images/grid.png">

<center>

*Figure 1 : image d'un quadtree où la grille des subdivisions est visible.*

</center>

<br>

## II. Développement du projet.

### A. <u>Avancement.</u>

Toutes les fonctionnalités demandées pour la version 0 ont été implémentées. Des tests ont également été implémentés pour vérifier la validité des fonctions au coeur du code.

### B. <u>Difficultés.</u>

Aucune difficulté particulière n'a été rencontrée.

