package PRO2.projet.v3

import fr.istic.scribble.*
import fr.istic.pro2.qtreeslib.*
import PRO2.projet.v0.ProgrammeUtilisateur.size_order

val colorFiller = TRANSPARENT

object MatrixConversions {

  type Center = (Int, Int)

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                             image_to_matrix                              * \\
  // **************************************************************************** \\

  /** @param i un entier naturel compris entre 0 et 99.
    * Supprime dans la console l'entier précédent et affiche à la place i.
    */
  private def log_remaining_lines(i: Int): Unit = {

    print(s"\u0008\u0008${if i < 10 then " " else ""}$i")
  }

  /** @param filename nom de fichier d'une image.
    * @param m la matrice associée à l'image.
    * @param i la ligne de l'image lue (fixée).
    * @param j la colonne de l'image lue.
    * @return la matrice m dont les couleurs à la i-ème ligne correspondent
    *         aux couleurs de la i-ème ligne de l'image de nom filename.
    */
  private def image_line_to_matrix_line_aux(
      filename: String,
      m: service_M.T[Color],
      i: Int,
      j: Int
  ): service_M.T[Color] = {

    j match {
      case 0 => service_M.set_element(m, i, 0, readColor(filename)(i, 0))
      case _ => {
        val new_m = service_M.set_element(m, i, j, readColor(filename)(i, j))
        image_line_to_matrix_line_aux(filename, new_m, i, j - 1)
      }
    }
  }

  /** @param filename nom de fichier d'une image.
    * @param m la matrice associée à l'image.
    * @param i la ligne de l'image lue.
    * @return la matrice m dont les couleurs à la i-ème ligne correspondent
    *         aux couleurs de la i-ème ligne de l'image de nom filename.
    */
  private def image_line_to_matrix_line(
      filename: String,
      m: service_M.T[Color],
      i: Int
  ): service_M.T[Color] = {

    val (_, p) = service_M.get_dimensions(m)
    image_line_to_matrix_line_aux(filename, m, i, p)
  }

  /** @param filename nom de fichier d'une image.
    * @param m la matrice associée à l'image.
    * @param i la ligne de l'image lue.
    * @return la matrice m dont les couleurs à chaque ligne correspondent aux couleurs
    *         de la ligne associée dans l'image de nom filename.
    */
  private def image_to_matrix_aux(
      filename: String,
      m: service_M.T[Color],
      i: Int
  ): service_M.T[Color] = {

    // log_remaining_lines(i)

    i match {
      case 0 => image_line_to_matrix_line(filename, m, 0)
      case _ => {
        val new_m = image_line_to_matrix_line(filename, m, i)
        image_to_matrix_aux(filename, new_m, i - 1)
      }
    }
  }

  // **************************************************************************** \\
  // *                            matrix_to_quadtree                            * \\
  // **************************************************************************** \\

  /** @param m une matrice carrée.
    * @param c une position dans cette matrice.
    * @param scale l'échelle de zoom, tel que 2^square = longueur de m.
    * @return le quadtree associé à m.
    */
  private def matrix_to_quadtree_aux(
      m: service_M.T[Color],
      c: Center,
      length: Int
  ): QT = {

    val (i, j) = c

    length match {

      case 1 => C(service_M.get_element(m, i, j).get)

      case _ => {

        /* Décalage du centre étudié. La formule suivante permet de ne pas avoir
           des arrondis destructeurs pour 1 ou 2 pixels de longueur. */
        val dc = math.ceil(length.toFloat / 4).toInt
        val df = math.floor(length.toFloat / 4).toInt

        /* Calculer le quadtrees de chaque coin de la matrice. */
        val qt_no: QT = matrix_to_quadtree_aux(m, (i - dc, j - dc), length / 2)
        val qt_ne: QT = matrix_to_quadtree_aux(m, (i - dc, j + df), length / 2)
        val qt_se: QT = matrix_to_quadtree_aux(m, (i + df, j + df), length / 2)
        val qt_so: QT = matrix_to_quadtree_aux(m, (i + df, j - dc), length / 2)

        N(qt_no, qt_ne, qt_se, qt_so)

      }
    }
  }

  def get_image_height(m: service_M.T[Color]): Int = {
    val (n, p) = service_M.get_dimensions(m)
    (1 until n).toList
      .map(i => service_M.get_element(m, i, p / 2).get)
      .filter(c => c != TRANSPARENT)
      .length + 1
  }

  def get_image_width(m: service_M.T[Color]): Int = {
    val (n, p) = service_M.get_dimensions(m)
    (1 until p).toList
      .map(j => service_M.get_element(m, n / 2, j).get)
      .filter(c => c != TRANSPARENT)
      .length + 1
  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                             image_to_matrix                              * \\
  // **************************************************************************** \\

  /** @param filename le nom de fichier (chemin relatif au projet sbt) d'image
    *                 au format jpg ou png.
    * @return la matrice associée à l'image de nom filename.
    */
  def image_to_matrix(filename: String): service_M.T[Color] = {

    val (w, h) = getDimensions(filename)
    val m = service_M.init_matrix(h, w, colorFiller)

    // print("Nombre de lignes de pixels à charger :   ")
    image_to_matrix_aux(filename, m, h - 1)
  }

  def image_to_matrix_and_dimensions(
      filename: String
  ): (service_M.T[Color], (Int, Int)) = {

    val (w, h) = getDimensions(filename)
    val m = service_M.init_matrix(h, w, colorFiller)

    // print("Nombre de lignes de pixels à charger :   ")
    val m_image = image_to_matrix_aux(filename, m, h - 1)
    val d = get_image_dimensions(m_image)

    (m_image, d)
  }

  // **************************************************************************** \\
  // *                          matrix_to_quadtree                              * \\
  // **************************************************************************** \\

  /** @param m une matrice de couleurs représentant une image.
    * @return le quadtree de l'image représenté par la matrice.
    */
  def matrix_to_quadtree(m: service_M.T[Color]): QT = {

    val square_m = service_M.matrix_to_square(m, colorFiller)
    val (n, p) = service_M.get_dimensions(square_m)
    val c: Center = (n / 2, p / 2)

    matrix_to_quadtree_aux(square_m, c, n)
  }

  // **************************************************************************** \\
  // *                          get_image_dimensions                            * \\
  // **************************************************************************** \\

  def get_image_dimensions(m: service_M.T[Color]): Dimensions = {
    (get_image_width(m), get_image_height(m))
  }

  def uncompress(qt: QT, s: Int): QT = {

    qt match {

      case C(c) => {

        s match {
          case 0 => C(c)
          case _ => {
            val n = uncompress(C(c), s - 1)
            N(n, n, n, n)
          }

        }
      }

      case N(no, ne, se, so) => {
        N(
          uncompress(no, s - 1),
          uncompress(ne, s - 1),
          uncompress(se, s - 1),
          uncompress(so, s - 1)
        )
      }

    }
  }
  

}
