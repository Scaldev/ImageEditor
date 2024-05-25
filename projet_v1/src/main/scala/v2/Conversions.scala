package PRO2.projet.v2

import fr.istic.scribble.*
import fr.istic.pro2.qtreeslib.*

object Conversions {

  /*
  val serv_MT: Matrices[Elt] = ???

  def fill_lines[Elt](m: Matrix[Elt], z: Elt, n: Int): Matrix[Elt] = {
    val (iMax, jMax): (Int, Int) = Matrices[Elt].get_dimensions(m)
    (1 to iMax).toList.map(i =>
      Matrices[Elt].get_row(i) ++ (1 to n).toList(x => z)
    )
  }
   */

  /** @param filename le nom d'un fichier image.
    * @param i une ligne de l'image.
    * @param jMax la largeur de l'image.
    * @return la liste de taille jMax des couleurs de la ligne i de filename.
    */
  def image_line_to_list(filename: String, i: Int, jMax: Int): List[Color] = {
    (1 to jMax).toList.map(j => readColor(filename)(i, j))
  }

  /** @param file le nom du fichier image.
    * @return la matrice représentant l'image.
    */
  def image_to_matrix(filename: String): Matrix[Color] = {
    val (iMax, jMax) = getDimensions(filename)
    (1 to iMax).toList.map(i => image_line_to_list(filename, i, jMax))
  }

  /*
  /** @param m une matrice carrée.
   * @return le quadtree représentant la même image que m.
   */
  def matrix_to_quadtree(m: Matrix[Color]): QT = {
    C(WHITE)
  }
   */

}
