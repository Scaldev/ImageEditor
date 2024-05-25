package PRO2.projet.v2
import fr.istic.scribble.*

type Matrix[Elt] = List[List[Elt]]

trait Matrices[Elt] {

  /** @param n un entier naturel strictement positif.
    * @param z l'élément nul.
    * @return la matrice carré de taille n comportant uniquement des z.
    */
  def null_matrix(n: Int, z: Elt): Matrix[Elt]

  /** @param m une matrice.
    * @return le couple (hauteur, largeur) de la matrice.
    */
  def get_dimensions(m: Matrix[Elt]): (Int, Int)

  /** @param m une matrice.
    * @param i le numéro de la ligne.
    * @return la liste des éléments de la ligne i, de gauche à droite.
    */
  def get_row(m: Matrix[Elt], i: Int): List[Elt]

  /** @param m une matrice.
    * @param j le numéro de la colonne.
    * @return la liste des éléments de la ligne j, de haut en bas.
    */
  def get_column(m: Matrix[Elt], j: Int): List[Elt]

  /** @param m une matrice.
    * @param i le numéro de la ligne.
    * @param j le numéro de la colonne.
    * @return l'élément à la ligne i et à la colonne j, s'il existe.
    */
  def get_element(m: Matrix[Elt], i: Int, j: Int): Option[Elt]

  /** @param m une matrice.
    * @param i le numéro de la ligne.
    * @param j le numéro de la colonne.
    * @param e l'élement.
    * @return la matrice m où l'élément de coordonnées (i, j) vaut e.
    * @note si (i, j) n'est pas une coordonné valide, m est inchangée.
    */
  def set_element(m: Matrix[Elt], i: Int, j: Int, e: Elt): Matrix[Elt]

}
