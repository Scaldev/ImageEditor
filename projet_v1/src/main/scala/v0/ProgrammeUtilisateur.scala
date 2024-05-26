package PRO2.projet.v0

import fr.istic.scribble.*
import Templates.*

object ProgrammeUtilisateur extends App {

  val service_QT: Quadtrees = ImpQuadtrees

  /* Le quadtree que l'utilisateur veut visualiser. */
  val quadtree: QT = quadtree_plain

  /* Si oui ou non on affiche la grille des subdivisions. */
  val show_grid: Boolean = true

  /* La puissance de 2 telle que l'image sera de longueur
   * et de largeur 2^size_order pixels.
   * Si size_order est "raisonnable", la fenêtre et l'image seront de même taille.
   */
  val size_order: Int = 9

  /* Afficher le quadtree convertit en image/ */
  val image: Image =
    service_QT.quadtree_to_image(quadtree, show_grid, size_order)

  draw(image)

}
