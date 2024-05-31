package PRO2.projet.v3

import fr.istic.scribble.*

object ImpMatricesMap extends Matrices {

  // Type d'une table associant une coordonnée (i, j) à l'élément de type Elt.
  type CoordTable[Elt] = Map[(Int, Int), Elt]

  // une Matrice est représentée ici par sa table d'associassion et ses dimensions.
  private case class MT[Elt](t: CoordTable[Elt], n: Int, p: Int)

  opaque type T[Elt] = MT[Elt]

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /** @param m une matrice de taille n * p.
    * @param k une coordonnée de la forme (i, j).
    * @return true ssi (0 <= i < n et 0 <= j < p).
    */
  def is_in_matrix[Elt](m: T[Elt], k: (Int, Int)) = {
    val (i, j) = k
    0 <= i && i < m.n && 0 <= j && j < m.p
  }

  /** @param l une liste.
    * @param t une table d'association (coordonnées -> élément d'une matrice).
    * @param i le numéro fixé d'une ligne de la matrice.
    * @param j le numéro d'une colonne de la matrice.
    * @return la table t dont les éléments de la liste l ont été assignées à la i-ème
    *         ligne de la matrice, dans le sens de lecture.
    */
  private def list_to_matrix_line[Elt](
      l: List[Elt],
      t: CoordTable[Elt],
      i: Int,
      j: Int
  ): CoordTable[Elt] = {
    l match {
      case Nil     => t
      case v :: vs => list_to_matrix_line(vs, t, i, j + 1) + ((i, j) -> v)
    }
  }

  /** @param m une matrice représentée par une liste de listes.
    * @param t une table d'association (coordonnées -> élément d'une matrice).
    * @param i le numéro d'une ligne de la matrice.
    * @return la table t associant une coordonnée à sa valeur dans la matrice m.
    */
  private def lists_to_matrix[Elt](
      m: List[List[Elt]],
      t: CoordTable[Elt],
      i: Int
  ): CoordTable[Elt] = {
    m match {
      case Nil => t
      case l :: ls =>
        lists_to_matrix(ls, t, i + 1) ++ list_to_matrix_line(l, t, i, 0)
    }
  }

  /** @param t une table d'association (coordonnées -> élément d'une matrice).
    * @param i le numéro fixé d'une ligne de la matrice.
    * @param j le numéro d'une colonne de la matrice.
    * @param v l'élément par défaut.
    * @return la table t dont les éléments d'index (i, _) valent v s'ils n'étaient pas déjà définis.
    */
  private def complete_line[Elt](
      t: CoordTable[Elt],
      d: (Int, Int),
      i: Int,
      j: Int,
      v: Elt
  ): CoordTable[Elt] = {

    val (_, p) = d

    p - j match {
      case 0 => t
      case _ => {
        val new_t = if t.contains((i, j)) then t else t + ((i, j) -> v)
        new_t ++ complete_line(t, d, i, j + 1, v)
      }
    }

  }

  /** @param t une table d'association (coordonnées -> élément d'une matrice).
    * @param i le numéro d'une ligne de la matrice.
    * @param p la longueur de la matrice associée à t.
    * @param v l'élément par défaut.
    * @return la table t dont toutes les clés valides non-associées sont désormais associées à v.
    */
  private def complete_table[Elt](
      t: CoordTable[Elt],
      d: (Int, Int),
      i: Int,
      v: Elt
  ): CoordTable[Elt] = {

    val (n, p) = d

    n - i match {
      case 0 => t
      case _ => complete_line(t, d, i, 0, v) ++ complete_table(t, d, i + 1, v)
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
    
    val t = complete_table(Map(), (n, p), 0, e)
    MT(t, n, p)
  }

  /** @param m une matrice.
    * @return le couple (hauteur, largeur) de la matrice.
    */
  def get_dimensions[Elt](m: T[Elt]): (Int, Int) = {

    (m.n, m.p)
  }

  /** @param m une matrice de taille n * p.
    * @param i un numéro de ligne.
    * @param j un numéro de colonne.
    * @return Some(e), où e est l'élément de m à la position (i, j)
    *         si une telle position est valide, None sinon.
    */
  def get_element[Elt](m: T[Elt], i: Int, j: Int): Option[Elt] = {

    m.t.get((i, j))
  }

  /** @param m une matrice.
    * @param i le numéro de la ligne.
    * @param j le numéro de la colonne.
    * @param e l'élement.
    * @return la matrice m où si un élément a les coordonnées (i, j), il vaut désormais e.
    */
  def set_element[Elt](m: T[Elt], i: Int, j: Int, e: Elt): T[Elt] = {

    if !is_in_matrix(m, (i, j)) then {
      m
    } else {
      MT(m.t.updated((i, j), e), m.n, m.p)
    }
  }

  /** @param lines une liste de listes de même taille.
    * @return la matrice formée de la liste de listes, si possible.
    */
  def list_to_matrix[Elt](lines: List[List[Elt]]): T[Elt] = {

    val t = lists_to_matrix(lines, Map(), 0)

    lines match {
      case Nil     => MT(t, 0, 0)
      case l :: ls => MT(t, lines.length, l.length)
    }

  }

  /** @param lines une liste de listes de même taille.
    * @return la matrice formée à partir de lines.
    */
  def matrix_to_list[Elt](m: T[Elt]): List[Elt] = {

    // Produit cartésien {1, ..., m.n - 1 } X { 1, ..., m.p - 1 }.
    val keys = (0 until m.n).toList

      // i => (i, 1) :: (i, 2) :: ..... :: (i, m.p - 1) :: Nil
      .map(i => (0 until m.p).toList.map(j => (i, j)))

      // concaténation des listes de chaque i dans { 1, ..., m.n - 1 }.
      .foldRight(Nil: List[(Int, Int)])((l, acc) => l ++ acc)

    keys.map(c => get_element(m, c._1, c._2).get)

  }

  /** @param m une matrice de taille n * p.
    * @param e l'élément comblant les nouvelles cases.
    * @return la matrice m carrée de taille max(n, p), dont les
    *         nouveaux éléments valent colorTransparent.
    */
  def matrix_to_square[Elt](m: T[Elt], e: Elt): T[Elt] = {

    val d = math.max(m.n, m.p)
    val new_t = complete_table(m.t, (d, d), 0, e)

    MT(new_t, d, d)
  }


}
