package PRO2.tp4

import fr.istic.scribble.*

private sealed trait QT
private case class C(c: Color) extends QT
private case class N(no: QT, ne: QT, se: QT, so: QT) extends QT

private case class State(
    quadtree: QT,
    length: Int,
    show_grid: Boolean,
    stop: Boolean
)

object Quadtrees extends Universe[State] {

  // **************************************************************************** \\
  // *                          VARIABLES GLOBALES                              * \\
  // **************************************************************************** \\

  // Paramètres de la fenêtre de jeu.
  val HEIGHT: Int = 512
  val WIDTH: Int = 512
  val name: String = "Quadtrees"

  val DEFAULT_IMG_LENGTH: Int = 512

  val DEFAULT_QUADTREE: QT =
    N(
      C(BLACK),
      N(C(BLACK), C(BLACK), C(WHITE), C(WHITE)),
      C(WHITE),
      N(C(BLACK), C(WHITE), C(WHITE), C(WHITE))
    )

  // **************************************************************************** \\
  // *                          FONCTIONS PRIVÉES                               * \\
  // **************************************************************************** \\

  /**
    * @param qt le quadtree représentant une image.
    * @param show_grid indique si on affiche la grille des subdivisions ou non.
    * @param length le nombre de pixels de longueur de l'image.
    * @return l'image de longueur length basée du quadtree qt.
    */
  private def quadtree_to_image(qt: QT, show_grid: Boolean, length: Int): Image = {

    qt match {

      // Cas de base : toute la zone n'est qu'une seule couleur.
      case C(c) => {

        val img_unicolor: Image = FillColor(Rectangle(length.toFloat, length.toFloat), c)
        val line_color: Color = if show_grid then RED else c
        LineColor(img_unicolor, line_color)

      }

      // Cas récursif : diviser la zone en 4 sous-zones, puis régner (les réunir).
      case N(no, ne, se, so) => {

        val img_no: Image = quadtree_to_image(no, show_grid, length / 2)
        val img_ne: Image = quadtree_to_image(ne, show_grid, length / 2)
        val img_se: Image = quadtree_to_image(se, show_grid, length / 2)
        val img_so: Image = quadtree_to_image(so, show_grid, length / 2)

        val img_top: Image = Beside(img_no, img_ne)
        val img_bot: Image = Beside(img_so, img_se)
        val img_all: Image = Below(img_top, img_bot)

        img_all
      }
    }

  }

  // **************************************************************************** \\
  // *                          FONCTIONS PUBLIQUES                             * \\
  // **************************************************************************** \\

  /** @return l'état initial du jeu.
    */
  def init: State = {

    State(DEFAULT_QUADTREE, DEFAULT_IMG_LENGTH, false, false)

  }

  /** @param s l'état de jeu.
    * @return l'affichage du jeu en fonction de l'état de jeu donné.
    */
  def toImage(s: State): Image = {

    quadtree_to_image(s.quadtree, s.show_grid, s.length)

  }

  /** @param s l'état de jeu.
    * @return true ssi le joueur a appuyé sur "x".
    */
  def stopWhen(s: State): Boolean = {
    s.stop == true
  }

  /** @param s l'état de jeu.)
    * @param e un événement.
    * @return le nouvel état de jeu en fonction de l'événement e.
    */
  def react(s: State, e: Event): State = {

    e match {

      // Dimensions de la fenêtre
      case KeyPressed(KeyAscii('+')) => {
        State(s.quadtree, math.min(HEIGHT, s.length * 2), s.show_grid, s.stop)
      }

      case KeyPressed(KeyAscii('-')) => {
        State(s.quadtree, math.max(1, s.length / 2), s.show_grid, s.stop)
      }

      // Show grid
      case KeyPressed(KeyAscii('g')) => {
        State(s.quadtree, s.length, !s.show_grid, s.stop)
      }

      // End
      case KeyPressed(KeyAscii('x')) => {
        State(s.quadtree, s.length, s.show_grid, true)
      }

      case _ => {
        s
      }

    }

  }
}
