<center>

# Quatrees version 1

*par REDOR Ewan et PACCINI Mathys*

</center>

## I. Structure du projet.

### 1. L'interface.

Le fichier `IntQuadtrees.scala` correspond à l'interface de notre projet Quadtrees. Il contient la définition du type algébrique `QT` représentant les quadtrees, définit comme étant :

- soit `C(c: Color)`, une feuille contenant une seule valeur : sa couleur, représentée par le type `Color` de Scribble.
- soit `N(no: QT, ne: QT, se: QT, so: QT)`, un noeud interne possédant 4 sous-quadtrees où chacun représente un quart de zone.

```scala
sealed trait QT
case class C(c: Color) extends QT
case class N(no: QT, ne: QT, se: QT, so: QT) extends QT
```

L'interface contient les fonctionnalités suivantes (voir le fichier source pour les spécifications complètes) :

```scala
trait IntQuadtrees {

  // convertit un quadtree en une image Scribble.
  def quadtree_to_image(qt: QT, grid: Boolean, size_order: Int): Image

  // compresse un quadtree.
  def compress(qt: QT): QT

  // applique une transformation à un quadtree.
  def transform(qt: QT, transfo: Transformation): QT

  // applique une liste de transformations à un quadtree.
  def transforms(qt: QT, transfos: List[Transformation]): QT

}
```

### 2. L'implémentation.

Le fichier `ImpQuadtrees.scala` implémente l'interface décrite dans la section précédente. Il est lié à un fichier auxiliaire `Transformations.scala`.

### 3. L'utilisation.

Le fichier `Main.scala` correspond au côté utilisateur. Il est lié à un fichier auxiliaire `Templates.scala`.


## II. Fonctionnalités.

### 1. <u>Conversion en image.</u>

Utiliser la fonction `quadtree_to_imag` nécessite de donner en paramètre :

- `quadtree` le quadtree a visualiser.
- `grid` un booléen spécifiant si on affiche la grille des subdivisions (`true`) ou non (`false`).
- `size_order` l'ordre de la taille de l'image, correspondant à la puissance $n$ telle que l'image sera de longueur et de largeur $2^n$.

Ci-dessous, un exemple minimal de création d'Univers via l'interface.

```scala
/* Exemple de création d'image dans Main.scala. */

val serv_vQT: IntQuadtrees = ???

val quadtree: QT = C(WHITE)
val grid: Boolean = true
val size_order: Int = 9

val image: Image = serv_vQT.quadtree_to_image(quadtree, grid, size_order)

draw(image)
```

### 2. <u>Grille de subdivisions.</u>

Paramétrer `grid` comme valant `true` (resp. `false`) affiche (resp. cache) la grille des subdivisions successives en quarts. Cette grille correspondant à un contour rouge (couleur `RED` de Scribble) autour de toutes les formes dessinées.

<img style="display: block; margin: auto; height: 512px;" src="images/grid.png">

<center>

*Figure 1 : image d'un quadtree où la grille des subdivisions est visible.*

</center>

<br>

### 3. <u>Transformations.</u>

La fonction `transform` permet d'appliquer une transformation à un quadtree. Une transformation peut consister à :

- **tourner un quadtree** à 90° dans le sens trigonométrique (`RotationLeft`) ou de l'horloge (`RotationRight`).
- **retourner un quadtree** selon l'axe vertical (`FlipVertical`) ou horizontal (`FlipHonrizontal`).
- **colorier un quadtree** en nuance de gris (`ColorGray`), éclaircir (`ColorLighten`) ou noircir (`ColorDarken`).

On peut également enchaîner les transformations avec la fonction `transforms`.

```scala
val transfos = ColorLighten :: ColorLighten :: ColorLighten :: ColorLighten :: Nil
val qt = serv_QT.transforms(quadtree, transfos)

val image = serv_QT.quadtree_to_image(qt, grid, size_order)
```

## III. Développement du projet.

### 1. <u>Avancement.</u>

Toutes les fonctionnalités demandées pour la version 1 ont été implémentées.

Des fonctionnalités supplémentaires ont également été ajoutées, comme la fonction `transforms`.

Des tests ont été implémentés pour vérifier la validité des fonctions au coeur du code.

### 2. <u>Difficultés.</u>

Il a fallu du temps pour implémenter la fonction `transforms` avec la structure du projet que nous avions. Ainsi, nous avons dû restructurer le code afin de pouvoir lister facilement des transformations à effectuer.

