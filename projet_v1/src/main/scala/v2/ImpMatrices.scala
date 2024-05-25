package PRO2.projet.v2

import fr.istic.scribble.*

class ImpMatrices[Elt] extends Matrices[Elt] {

  /** @param l, une liste d'élements de type Elt
    * @param index, l'indice indiquant à quel position atteindre l'élément
    * @return optionnelement l'élément à l'indice index de la liste l
    */
  private def get_elt_by_index(l: List[Elt], index: Int): Option[Elt] = {
    l match {
      case x :: Nil => None
      case x :: xs =>
        if index == 0 then Some(x) else get_elt_by_index(xs, index - 1)
    }
  }

  /** @param l, une liste d'éléments de type Elt
    * @param index, l'indice inquiquant la position dans la liste l de l'élément à remplacer
    * @param e, l'élément à placer à l'indice index
    * @return la liste avec l'élément e placé à l'indice index
    */
  private def change_elt_in_list(
      l: List[Elt],
      index: Int,
      e: Elt
  ): List[Elt] = {
    l match {
      case x :: xs =>
        if index == 0 then e :: xs
        else x :: change_elt_in_list(xs, index - 1, e)
      case Nil => Nil
    }
  }

  /** @param n un entier naturel strictement positif.
    * @param z l'élément nul.
    * @return la matrice carré de taille n comportant uniquement des z.
    */
  def null_matrix(n: Int, z: Elt): Matrix[Elt] = {
    (1 to n).toList.map(_ => (1 to n).toList.map(_ => z))
  }

  /** @param m une matrice.
    * @return le couple (hauteur, largeur) de la matrice.
    */
  def get_dimensions(m: Matrix[Elt]): (Int, Int) = {
    m match {
      case Nil     => (0, 0)
      case l :: ls => (m.length, l.length)
    }
  }

  /** @param m une matrice.
    * @param i le numéro de la ligne.
    * @return la liste des éléments de la ligne i, de gauche à droite.
    */
  def get_row(m: Matrix[Elt], i: Int): List[Elt] = {
    (m, i) match {
      case (Nil, _)     => Nil
      case (l :: ls, 0) => l
      case (l :: ls, _) => get_row(ls, i - 1)
    }
  }

  /** @param m une matrice.
    * @param j le numéro de la colonne.
    * @return la liste des éléments de la ligne j, de haut en bas.
    */
  def get_column(m: Matrix[Elt], j: Int): List[Elt] = {
    if j < get_dimensions(m)._2 then m.map(x => get_elt_by_index(x, j).get)
    else Nil
  }

  /** @param m une matrice.
    * @param i la numéro de la ligne.
    * @param j le numéro de la colonne.
    * @return optionnelement l'élément situé à la ligne i et à la collone j de la matrice m.
    */
  def get_element(m: Matrix[Elt], i: Int, j: Int): Option[Elt] = {
    get_elt_by_index(get_row(m, j), i)
  }

  /** @param m une matrice.
    * @param i le numéro de la ligne.
    * @param j le numéro de la colonne.
    * @param e l'élement.
    * @return la matrice m où l'élément de coordonnées (i, j) vaut e.
    * @note si (i, j) n'est pas une coordonné valide, m est inchangée.
    */
  def set_element(m: Matrix[Elt], i: Int, j: Int, e: Elt): Matrix[Elt] = {
    m match {
      case x :: xs =>
        if i == 0 then change_elt_in_list(x, i, e) :: xs
        else x :: set_element(xs, i - 1, j, e)
      case Nil => Nil
    }
  }

}
