package PRO2.projet.v2

import fr.istic.scribble.*
import fr.istic.pro2.qtreeslib.*
import PRO2.projet.v2.ImpMatricesList.init_matrix

class MatrixConversions(serv: Matrices) {

  val serv_M: Matrices = serv

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /** @param m une matrice de taille n * p.
    * @param l une liste.
    * @param i le numéro d'une ligne de m fixé.
    * @param j le numéro d'une colonne de m.
    * @return le couple (new_m, new_l) :
    *         - new_m est la matrice faite de m où la i-ème ligne est constitué
    *           des p premiers éléments de l.
    *         - new_l est la liste l privée de ses p premiers éléments.
    */
  private def set_matrix_line_from_list_aux[Elt](
      m: serv_M.T[Elt],
      l: List[Elt],
      i: Int,
      j: Int
  ): (serv_M.T[Elt], List[Elt]) = {

    val (n, p) = serv_M.get_dimensions(m)

    // j est itéré de 0 à p afin de changer chaque élément d'une ligne de m.
    p - j match {

      case 0 => (m, l)

      case _ => {
        val (new_m, new_l) = (serv_M.set_element(m, i, j, l.head), l.tail)
        set_matrix_line_from_list_aux(new_m, new_l, i, j + 1)
      }

    }
  }

  /** @param m une matrice de taille n * p.
    * @param l une liste.
    * @param i le numéro d'une ligne de m.
    * @return le couple (new_m, new_l) :
    *         - new_m est la matrice m où la i-ème ligne est constituée
    *           des p premiers éléments de l.
    *         - new_l est la liste l privée de ses p premiers éléments.
    */
  private def set_matrix_line_from_list[Elt](
      m: serv_M.T[Elt],
      l: List[Elt],
      i: Int
  ): (serv_M.T[Elt], List[Elt]) = {

    set_matrix_line_from_list_aux(m, l, i, 0)
  }

  /** @param m une matrice de taille n * p.
    * @param l une liste de n * p éléments.
    * @param i le numéro d'une ligne de m.
    * @return la matrice aux dimensions de m et où chaque ligne i est constituée
    *         des éléments de l d'indice appartenant à l'intervalle [i*p, (i+1)*p[.
    * @example pour l = 1::2::3::4 Nil, on obtient la matrice ci-dessous :
    *          |1  2|
    *          |3  4|
    */
  private def set_matrix_from_list_aux[Elt](
      m: serv_M.T[Elt],
      l: List[Elt],
      i: Int
  ): serv_M.T[Elt] = {

    val (n, p) = serv_M.get_dimensions(m)

    n - i match {

      case 0 => m

      case _ => {
        val (new_m, new_l) = set_matrix_line_from_list(m, l, i)
        set_matrix_from_list_aux(new_m, new_l, i + 1)
      }

    }
  }

  /**
    * @param m une matrice.
    * @param l une liste.
    * @param i le numéro d'une ligne de m fixé.
    * @param j le numéro d'une colonne de m.
    * @return la liste des éléments de la i-ème ligne de m, ordonnés dans le sens de lecture.
    */
  private def get_matrix_line_aux[Elt](
      m: serv_M.T[Elt],
      l: List[Elt],
      i: Int,
      j: Int
  ): List[Elt] = {

    j match {
      case 0 => l
      case _ =>
        serv_M.get_element(m, i, j).get :: get_matrix_line_aux(m, l, i, j - 1)
        // la spécification fait l'hypothèse qu'(i, j) soit une coordonnée valide.
        // on peut donc directement récupérer la valeur de Some(e).
    }

  }

  /**
    * @param m une matrice.
    * @param l une liste.
    * @param i le numéro d'une ligne de m.
    * @return la liste des éléments de la i-ème ligne de m, ordonnés dans le sens de lecture.
    */
  private def get_matrix_line[Elt](m: serv_M.T[Elt], l: List[Elt], i: Int): List[Elt] = {

    val (_, p) = serv_M.get_dimensions(m)
    get_matrix_line_aux(m, l, i, p)
  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /** @param l une matrice.
    * @param n le nombre de lignes.
    * @param p le nombre de colonnes.
    * @param e un élément par défaut.
    * @return Some de la matrice de taille n * p contenant les éléments de l,
    *         ordonnés ligne par ligne si les dimensions sont bonnes ; None sinon.
    */
  def list_to_matrix[Elt](
      l: List[Elt],
      n: Int,
      p: Int,
      e: Elt
  ): Option[serv_M.T[Elt]] = {

    if l.length != n * p then {
      None
    } else {
      val m = serv_M.init_matrix(n, p, l.head)
      val new_m = set_matrix_from_list_aux(m, l, 0)
      Some(new_m)
    }

  }

  /** @param m une matrice.
    * @return la liste des éléments de m, ordonnés ligne par ligne.
    */
  def matrix_to_list[Elt](m: serv_M.T[Elt]): List[Elt] = {
    Nil
  }

  /*
  val serv_MT: Matrices[Elt] = ???

  def fill_lines[Elt](m: Matrix[Elt], z: Elt, n: Int): Matrix[Elt] = {
    val (iMax, jMax): (Int, Int) = Matrices[Elt].get_dimensions(m)
    (1 to iMax).toList.map(i =>
      Matrices[Elt].get_row(i) ++ (1 to n).toList(x => z)
    )
  }


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


  /** @param m une matrice carrée.
   * @return le quadtree représentant la même image que m.
   */
  def matrix_to_quadtree(m: Matrix[Color]): QT = {
    C(WHITE)
  }
   */

}
