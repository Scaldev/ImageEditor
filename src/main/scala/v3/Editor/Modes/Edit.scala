package PRO2.projet.v3

import fr.istic.scribble.*

import Utils.*

class ModeEdit(service_QT: Quadtrees) {

  private val UElements = UtilsElements(service_QT)
  import UElements.*

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\


  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /** @param s l'état de l'application en mode Edit.
    * @param e un événement.
    * @return le nouvel état de l'application en mode Edit selon e.
    */
  def update_state_edit(s: State, e: Event): State = {

    e match {

      /* Souris (priorité). */

      case MouseClick(x, y) => {

        val c = (x.toInt, y.toInt)
        val new_mode = update_mode_on_click(s.mode, c)
        val new_selected = get_from_click(s.elements, c, 0)
        s.copy(mode = new_mode, selected = new_selected)

      }

      /* Changement de mode. */

      case KeyPressed(KeyAscii(c)) if key_to_mode.keys.toList.contains(KeyAscii(c)) => {
        s.copy(mode = key_to_mode(KeyAscii(c)))
      }

      /* Modifier le numéro de la transformation custom en cours. */
      
      case KeyPressed(KeyLeft) => {
        val new_customs = (s.customs match {
          case Nil     => Nil
          case t :: ts => ts :+ (t)
        })
        s.copy(customs = new_customs)
      }

      case KeyPressed(KeyRight) => {
        val new_customs = (s.customs match {
          case Nil => Nil
          case _   => s.customs.last :: s.customs.dropRight(1)
        })
        s.copy(customs = new_customs)
      }

      /* Application la transformation voulue à l'élément. */

      case KeyPressed(KeyAscii('c')) => {
        s.customs match {
          case Nil     => s
          case t :: ts => update_selected(s, update_quadtree(t))
        }
      }

      case KeyPressed(KeyAscii(c)) if key_to_trans(service_QT).keys.toList.contains(KeyAscii(c)) => {
        val f = update_quadtree(key_to_trans(service_QT)(KeyAscii(c)))
        update_selected(s, f)
      }

      /* Paramètres de l'élément. */

      case KeyPressed(KeyDelete) => {
        s.selected match {
          case None    => s
          case Some(i) => s.copy(elements = delete_element(s.elements, i))
        }
      }

      case KeyPressed(KeyAscii('-')) => update_selected(s, update_size(-1))

      case KeyPressed(KeyAscii('+')) => update_selected(s, update_size(1))

      case KeyPressed(KeyAscii('o')) => update_selected(s, update_grid)

      /* Autres. */

      case _ => s

    }

  }

}
