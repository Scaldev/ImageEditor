package PRO2.projet.v2

import fr.istic.scribble.*

object ImpMatricesList extends Matrices {

  opaque type T[Elt] = List[List[Elt]]

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /** @param p un entier naturel.
    * @param e un élément.
    * @return la liste de taille p contenant uniquement l'élément e.
    */
  private def init_list[Elt](p: Int, e: Elt): List[Elt] = {
    p match {
      case 0 => Nil
      case _ => e :: init_list(p - 1, e)
    }
  }

  /** @param m une matrice.
    * @param i le numéro d'une ligne.
    * @return Some(l), où l est là i-ème ligne de m (si elle existe), None sinon.
    */
  private def get_matrix_line[Elt](m: T[Elt], i: Int): Option[List[Elt]] = {
    m match {
      case Nil     => None
      case l :: ls => if i == 0 then Some(l) else get_matrix_line(ls, i - 1)
    }
  }

  /** @param m une matrice.
    * @param nl la nouvelle ligne.
    * @param i l'emplacement de nl.
    * @return la matrice m où la i-ème ligne vaut nl.
    * @note si m possède moins de i lignes, elle reste inchangée.
    */
  private def set_matrix_line[Elt](m: T[Elt], nl: List[Elt], i: Int): T[Elt] = {
    m match {
      case Nil => Nil
      case l :: ls =>
        if i == 0 then nl :: ls else set_matrix_line(ls, nl, i - 1)
    }
  }

  /** @param l une liste.
    * @param e le nouvel élément.
    * @param i l'indice du nouvel élément.
    * @return la liste l où le i-ème élément vaut e.
    * @note par convention, le premier élément de l est à l'indice 0.
    *       si la liste a moins de i éléments, elle reste inchangée.
    */
  private def set_list_element[Elt](l: List[Elt], e: Elt, i: Int): List[Elt] = {
    l match {
      case Nil     => Nil
      case x :: xs => if i == 0 then e :: xs else set_list_element(l, e, i - 1)
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
      case 0 => Nil
      case _ => init_list(p, e) :: init_matrix(n - 1, p, e)
    }

  }

  /** @param m une matrice.
    * @return le couple (hauteur, largeur) de la matrice.
    */
  def get_dimensions[Elt](m: T[Elt]): (Int, Int) = {

    m match {
      case Nil     => (0, 0)
      case l :: ls => (m.length, l.length)
    }

  }

  /** @param m une matrice de taille n * p.
    * @param i un numéro de ligne.
    * @param j un numéro de colonne.
    * @return Some(e), où e est l'élément de m à la position (i, j)
    *         si une telle position est valide, None sinon.
    */
  def get_element[Elt](m: T[Elt], i: Int, j: Int): Option[Elt] = {

    get_matrix_line(m, i) match {
      case None    => None
      case Some(l) => l.lift(j)
    }

  }

  /** @param m une matrice.
    * @param i le numéro de la ligne.
    * @param j le numéro de la colonne.
    * @param e l'élement.
    * @return la matrice m où si un élément a les coordonnées (i, j), il vaut désormais e.
    */
  def set_element[Elt](m: T[Elt], i: Int, j: Int, e: Elt): T[Elt] = {

    get_matrix_line(m, i) match {
      case None => m
      case Some(l) => {
        val nl: List[Elt] = set_list_element(l, e, j)
        set_matrix_line(m, nl, i)
      }
    }

  }

  /** @param l une matrice.
    * @param n le nombre de lignes.
    * @param p le nombre de colonnes.
    * @return la matrice de taille n * p contenant les éléments de l,
    *         ordonnés ligne par ligne.
    */
  def list_to_matrix[Elt](l: List[Elt], n: Int, p: Int): T[Elt] = {
    n match {
      case 0 => Nil
      case _ => l.take(p) :: list_to_matrix(l.drop(p), n - 1, p)
    }
  }

  /** @param m une matrice.
    * @return la liste des éléments de m, ordonnés ligne par ligne.
    */
  def matrix_to_list[Elt](m: T[Elt]): List[Elt] = {
    m.foldRight(Nil: List[Elt])((l, acc) => l ++ acc)
  }

}
