package PRO2.projet.v3

import fr.istic.scribble.*

object Utils {

  // **************************************************************************** \\
  // *                      Détecter si dans un rectangle                       * \\
  // **************************************************************************** \\

  /** @param p les coordonnées du pixel en haut à gauche du rectangle.
    * @param d les dimensions du rectangle.
    * @param c la position du curseur.
    * @return true ssi c est dans le rectangle.
    */
  def is_in_rect(p: Position, d: (Int, Int), c: Position): Boolean = {

    val (i, j) = p
    val (x, y) = c
    val (i_length, j_length) = d

    val is_x_in = (i <= x && x <= i + i_length)
    val is_y_in = (j <= y && y <= j + j_length)

    is_x_in && is_y_in
  }

  // **************************************************************************** \\
  // *                      Modifier le mode si besoin                          * \\
  // **************************************************************************** \\

  /** @param m le mode actuel de l'application.
    * @param c la position du curseur après un click.
    * @return le nouveau mode selon m et c.
    */
  def update_mode_on_click(m: Mode, c: Position): Mode = {

    val (w, h) = (IMAGE_EDITOR_WIDTH, IMAGE_EDITOR_HEIGHT)
    val click_in_edit = is_in_rect((0, 0), (w, h - 64), c)

    if click_in_edit && m == Command then {
      Edit
    } else if !click_in_edit then {
      Command
    } else {
      m
    }

  }

  // **************************************************************************** \\
  // *                  Modification d'une string selon une touche              * \\
  // **************************************************************************** \\

  /** @param str le chemin du fichier image voulant être chargé.
    * @param k la touche appuyée.
    * @return la chaîne de caractère avec le caractère en plus.
    */
  def add_key_to_string(str: String, k: Key): String = {

    k match {
      case KeyAscii(c) => str + c
      case KeyDelete   => str.dropRight(1)
      case _           => str
    }

  }

  // **************************************************************************** \\
  // *                        Mettre à jour un paramètre                        * \\
  // **************************************************************************** \\

  /** @param args le tableau des mots de l'input.
    * @return la couleur associée à l'input.
    */
  def text_to_color(args: Vector[String]): Color = {

    if args.length < 4 then { // valeur par défaut.
      BLACK
    } else {

      def to_color(i: Int) = math.max(math.min(args(i).toInt, 255), 0)

      val (r, g, b) = (to_color(1), to_color(2), to_color(3))
      val a = if args.length == 4 then 255 else to_color(4)

      Color(r, g, b, a)
    }

  }

  // **************************************************************************** \\
  // *                             Hauteur de quadtree                          * \\
  // **************************************************************************** \\

  /** @param qt un quadtree.
    * @return la hauteur du quadtree qt.
    */
  def hauteur(qt: QT): Int = {
    qt match {
      case C(_) => -1
      case N(no, ne, se, so) => {
        1 + math.max(
          math.max(hauteur(no), hauteur(ne)),
          math.max(hauteur(so), hauteur(se))
        )
      }
    }
  }

  // **************************************************************************** \\
  // *                               Tous les modes                             * \\
  // **************************************************************************** \\

}
