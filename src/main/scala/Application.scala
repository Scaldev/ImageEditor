package PRO2.projet

import fr.istic.scribble.*
import Quadtrees.*

/**
  * L'état de l'univers, définit comme étant :
  * - show_grid : si la grille des subdivisions est affichée ou non.
  * - stop : si l'univers est arrêté ou non.
  * 
  * Remarque : dans cette version 0, il n'y a pas besoin de mettre le quadtree
  * dans le State car celui-ci ne change jamais. Cela changera dans la version 1.
  */
case class State(
    show_grid: Boolean,
    stop: Boolean
)

class Application(quadtree: QT, size_order: Int) extends Universe[State] {

  // **************************************************************************** \\
  // *                          VARIABLES GLOBALES                              * \\
  // **************************************************************************** \\

  // Paramètres de la fenêtre de jeu.

  if size_order < 1 then
    throw Exception("Invalid input : size_order doit être strictement positive.")

  val length: Float = math.pow(2.0, size_order.toDouble).toFloat
  val HEIGHT: Int = length.toInt
  val WIDTH: Int = length.toInt

  val name: String = "Quadtrees"

  // **************************************************************************** \\
  // *                          FONCTIONS PUBLIQUES                             * \\
  // **************************************************************************** \\

  /** @return l'état initial du jeu.
    */
  def init: State = {
    State(quadtree, false, false)
  }

  /** @param s l'état de l'univers.
    * @return l'affichage de l'image en fonction de l'état de l'univers s.
    */
  def toImage(s: State): Image = {
    quadtree_to_image(quadtree, s.show_grid, length)
  }

  /** @param s l'état de l'univers.
    * @return true ssi le joueur a appuyé sur "x".
    */
  def stopWhen(s: State): Boolean = {
    s.stop == true
  }

  /** @param s l'état de l'univers.
    * @param e un événement.
    * @return le nouvel état de l'univers en fonction de l'événement e.
    */
  def react(s: State, e: Event): State = {

    e match {

      // Afficher / cacher la grille des subdivisions.
      case KeyPressed(KeyAscii('g')) => {
        State(!s.show_grid, s.stop)
      }

      // Arrêter l'univers.
      case KeyPressed(KeyAscii('x')) => {
        State(s.show_grid, true)
      }

      // Ne rien modifier.
      case _ => s

    }

  }
}
