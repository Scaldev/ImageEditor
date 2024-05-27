package PRO2.projet.v2

import fr.istic.scribble.*

object ImpMatricesVector extends Matrices {

  opaque type T[Elt] = Vector[Vector[Elt]]

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /** @param p un entier naturel.
    * @param e un élément.
    * @return le vecteur de taille p contenant uniquement l'élément e.
    */
  private def init_vector[Elt](p: Int, e: Elt): Vector[Elt] = {

    Vector.fill[Elt](p)(e)
  }

  /**
    * @param v un vecteur de taille n.
    * @param e un élément par défaut.
    * @param k un entier naturel.
    * @return le vecteur de taille n + k constitué des éléments de v
    *         ainsi que de k éléments e.
    */
  private def add_elements[Elt](v: Vector[Elt], e: Elt, k: Int): Vector[Elt] = {
    k match {
      case 0 => v
      case _ => add_elements(v, e, k - 1) :+ e
    }
  }

  /**
    * @param m une matrice.
    * @param e un élément par défaut.
    * @param k le nombre de lignes à ajouter.
    * @return la matrice m avec k lignes de plus, contenant toutes
    *         uniquement l'élément e.
    */
  private def add_lines[Elt](m: T[Elt], e: Elt, k: Int): T[Elt] = {

    val (_, p) = get_dimensions(m)
    
    k match {
      case 0 => m
      case _ => add_lines(m, e, k - 1) :+ add_elements(Vector(), e, p)
    }
  }

  /**
    * @param m une matrice.
    * @param e un élément par défaut.
    * @param k le nombre de colonnes à ajouter.
    * @return la matrice m avec k colonnes de plus, contenant toutes
    *         uniquement l'élément e.
    */
  private def add_columns[Elt](m: T[Elt], e: Elt, k: Int): T[Elt] = {

    k match {
      case 0 => m
      case _ => m.map(l => add_elements(l, e, k))
    }
    
  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /** @param n un entier naturel.
    * @param p un entier naturel.
    * @param e un élément.
    * @return la matrice de taille n * p ne contenant que des éléments e.
    */
  def init_matrix[Elt](n: Int, p: Int, e: Elt): T[Elt] = {

    n match {
      case 0 => Vector()
      case _ => init_matrix(n - 1, p, e) :+ init_vector(p, e)
    }

  }

  /** @param m une matrice.
    * @return le couple (hauteur, largeur) de la matrice.
    */
  def get_dimensions[Elt](m: T[Elt]): (Int, Int) = {
    m match {
      case l :+ ls => (m.length, ls.length)
      case _       => (0, 0)
    }
  }

  /** @param m une matrice de taille n * p.
    * @param i un entier naturel.
    * @param j un entier naturel.
    * @return Some(e), où e est l'élément de m à la position (i, j)
    *         si une telle position est valide, None sinon.
    */
  def get_element[Elt](m: T[Elt], i: Int, j: Int): Option[Elt] = {

    val (n, p) = get_dimensions(m)

    if (i >= n || j >= p) then None
    
    else Some(m(i)(j))
  }

  /** @param m une matrice.
    * @param i le numéro de la ligne.
    * @param j le numéro de la colonne.
    * @param e l'élement.
    * @return la matrice m où si un élément a les coordonnées (i, j), il vaut désormais e.
    */
  def set_element[Elt](m: T[Elt], i: Int, j: Int, e: Elt): T[Elt] = {

    val (n, p) = get_dimensions(m)

    if i >= n || j >= p then {
      m
    } else {
      val new_line: Vector[Elt] = m(i).updated(j, e)
      m.updated(i, new_line)
    }

  }

  /** @param lines une liste de listes de même taille.
    * @return la matrice formée de la liste de listes, si possible.
    */
  def list_to_matrix[Elt](lines: List[List[Elt]]): T[Elt] = {

    val hasMatrixShape = lines.length == 0 || lines.forall(l => l.length == lines.head.length)

    if !hasMatrixShape then {
      throw Exception("Invalid input: size_order < 1.")
    } else lines.map(line => line.toVector).toVector

  }

  /** @param lines une liste de listes de même taille.
    * @return la matrice formée à partir de lines.
    */
  def matrix_to_list[Elt](m: T[Elt]): List[Elt] = {
    
    m.foldRight(Nil: List[Elt])((l, acc) => l.toList ++ acc)
  }

  /** @param m une matrice de taille n * p.
    * @param e l'élément comblant les nouvelles cases.
    * @return la matrice m carrée de taille max(n, p), dont les
    *         nouveaux éléments valent colorTransparent.
    */
  def matrix_to_square[Elt](m: T[Elt], e: Elt): T[Elt] = {
    
    val (n, p) = get_dimensions(m)

    if n > p then add_columns(m, e, n - p)
    else if n < p then add_lines(m, e, p - n)
    else m
  }

}
