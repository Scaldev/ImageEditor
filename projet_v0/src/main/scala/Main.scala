package PRO2.projet

import fr.istic.scribble.*

object ProgrammeUtilisateur extends App {

  val servQT: IntQuadtrees = ImpQuadtrees

  /* Le quadtree que l'utilisateur veut visualiser. */
  val quadtree: QT = N(
    C(BLACK),
    N(C(BLACK), C(BLACK), C(WHITE), C(WHITE)),
    C(WHITE),
    N(C(BLACK), C(WHITE), C(WHITE), C(WHITE))
  )

  /* Si oui ou non on affiche la grille des subdivisions. */
  val show_grid: Boolean = true

  /* La puissance de 2 telle que l'image sera de longueur
   * et de largeur 2^size_order pixels.
   * Si size_order est "raisonnable", la fenêtre et l'image seront de même taille.
   */
  val size_order: Int = 9

  /* Afficher le quadtree convertit en image/ */
  val image: Image =
    servQT.quadtree_to_image(quadtree, show_grid, size_order)

  draw(image)

}
