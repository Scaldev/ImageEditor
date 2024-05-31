package PRO2.projet.v3

import fr.istic.scribble.*

import Utils.*

class ModeMove(service_QT: Quadtrees) {

  private val UElements = UtilsElements(service_QT)
  import UElements.*

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /** @param s l'état de l'application, en mode Edit ou Command.
    * @param c la position de la souris.
    * @return le nouvel état, en ayant changé de sélection si besoin.
    */
  private def update_select_on_click(s: State, c: Position): Option[Int] = {
    s.selected match {
        case None => get_from_click(s.elements, c, 0)
        case Some(_) => None
    }
  }

  private def update_selected_position(s: State, i: Int, c: Position): State = {

    val (x, y) = c

    val w = math.pow(2, s.elements(i).size_order) / 2
    val h = math.pow(2, s.elements(i).size_order) / 2

    val close_to_width_edges = x < w || IMAGE_EDITOR_WIDTH - w < x
    val close_to_height_edges =
      y < h || IMAGE_EDITOR_HEIGHT - h < y + 64

    if close_to_width_edges || close_to_height_edges then s
    else update_selected(s, update_position(c))
  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /** @param s l'état de l'application en mode Move.
    * @param e un événement.
    * @return le nouvel état de l'application en mode Move selon e.
    */
  def update_state_move(s: State, e: Event): State = {

    e match {

      case MouseClick(x, y) => {

        val c = (x.toInt, y.toInt)
        val new_mode = update_mode_on_click(s.mode, c)
        val new_selected = update_select_on_click(s, c)
        s.copy(mode = new_mode, selected = new_selected)

      }

      case MouseMove(x, y) => {

        s.selected match {
          case None => s
          case Some(i) => {
            update_selected_position(s: State, i, (x.toInt, y.toInt))
          }
        }

      }

      case KeyPressed(KeyAscii(c))
      
        if key_to_mode.keys.toList.contains(KeyAscii(c)) => {
            s.copy(mode = key_to_mode(KeyAscii(c)))
        }

      case _ => s

    }

  }

}
