package PRO2.projet.v3

import fr.istic.scribble.*

object Utils {

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

  def next_pow_of_2(x: Int, n: Int): Int = {
    if math.pow(2, n).toInt >= x then n
    else next_pow_of_2(x, n + 1)
  }

  def get_size_order(x: Int, y: Int): Int = {
    next_pow_of_2(math.max(x, y), 0)
  }

}
