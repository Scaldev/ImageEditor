package PRO2.projet.v3

import fr.istic.scribble.*

import Utils.*

class ModeCommand(service_QT: Quadtrees) {

  val UElements = UtilsElements(service_QT)
  import UElements.*

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

    /** @param s l'état de l'application.
    * @return l'état de jeu comportant un nouvel élément correspondant
    *         à l'image du fichier de chemin s.input._2, et dont
    *         s.input = (false, "") (input remis à zéro).
    */
  private def load_file(s: State): State = {

    val (w, h) = (IMAGE_EDITOR_WIDTH, IMAGE_EDITOR_HEIGHT)
    val image_path = s.input.drop(5) // "load <filename>"
    val new_qt = service_QT.file_to_quadtree(image_path)

    val l = hauteur(new_qt) + 1

    val new_qte = QTE(new_qt, (w / 2, h / 2), false, 7, l)
    val new_elements = new_qte +: s.elements
    val new_selected = Some(new_elements.length - 1)

    s.copy(
      elements = new_elements,
      selected = new_selected,
      input = ""
    )

  }

  /**
    * @param s l'état de l'application en mode Command.
    * @return s après avoir agit selon l'input.
    */
  private def interpreter(s: State): State = {

    val args = s.input.split(" ").toVector

    args(0) match {

      case "load" => load_file(s)

      case "color" => {
        val new_pencil = s.pencil.copy(color = text_to_color(args))
        s.copy(input = "", pencil = new_pencil)
      }

      case _ => s.copy(input = "")
    }
  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

    /** @param s l'état de l'application en mode Command.
    * @param e un événement.
    * @return le nouvel état de l'application en mode Command selon e.
    */
  def update_state_command(s: State, e: Event): State = {

    e match {

      // CLICK : changer de mode si besoin.
      case MouseClick(x, y) => {
        s.copy(mode = update_mode_on_click(s.mode, (x.toInt, y.toInt)))
      }

      // ENTRÉE : interpréter la commande entrée.
      case KeyPressed(KeyReturn) => interpreter(s)

      // AUTRES TOUCHES CLAVIER : ajouter les caractère.
      case KeyPressed(k) => s.copy(input = Utils.add_key_to_string(s.input, k))

      // SINON : rien faire.
      case _             => s

    }
  }

}