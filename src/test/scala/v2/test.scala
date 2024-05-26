package PRO2.projet.v2

import fr.istic.scribble.*

class MySuite extends munit.FunSuite {

  // Valeurs globales.
  val dsize: Int = 9
  val dlength: Float = 512.0

  // Services.
  val service_QT: Quadtrees = ImpQuadtrees
  val service_ML: Matrices = ImpMatricesList
  val service_MV: Matrices = ImpMatricesVector

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                                  QUADTREES                               * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                          	  quadtree_to_image                     	    * \\
  // **************************************************************************** \\

  test("Quadtrees : quadtree_to_image -> zone unicolore sans contour") {

    val obtained = service_QT.quadtree_to_image(C(WHITE), false, dsize)

    val expected =
      LineColor(FillColor(Rectangle(dlength, dlength), WHITE), WHITE)

    assertEquals(obtained, expected)

  }

  test("Quadtrees : quadtree_to_image -> zone unicolore avec contour") {

    val obtained = service_QT.quadtree_to_image(C(WHITE), true, dsize)

    val expected = LineColor(FillColor(Rectangle(dlength, dlength), WHITE), RED)

    assertEquals(obtained, expected)

  }

  test("Quadtrees : quadtree_to_image -> image non-compressee") {

    val qt: QT = N(C(WHITE), C(WHITE), C(WHITE), C(WHITE))
    val obtained = service_QT.quadtree_to_image(qt, false, dsize)

    val subrect: Image = FillColor(Rectangle(dlength / 2, dlength / 2), WHITE)
    val dsubdiv: Image = LineColor(subrect, WHITE)
    val expected = Below(Beside(dsubdiv, dsubdiv), Beside(dsubdiv, dsubdiv))

    assertEquals(obtained, expected)

  }

  test("Quadtrees : quadtree_to_image -> image non-unicolore") {

    // Obtained.
    // Remarque : c'est l'exemple du cours !

    val qt: QT = N(
      C(BLACK),
      N(C(BLACK), C(BLACK), C(WHITE), C(WHITE)),
      C(WHITE),
      N(C(BLACK), C(WHITE), C(WHITE), C(WHITE))
    )

    val obtained = service_QT.quadtree_to_image(qt, false, dsize)

    // Expected.
    // La fonction make_area ayant été testée avant, on fait l'hypothèse
    // qu'elle fonctionne déjà afin d'alléger le code.

    val sdiv_white: Image =
      LineColor(FillColor(Rectangle(dlength / 2, dlength / 2), WHITE), WHITE)
    val sdiv_black: Image =
      LineColor(FillColor(Rectangle(dlength / 2, dlength / 2), BLACK), BLACK)

    val ssdiv_white: Image =
      LineColor(FillColor(Rectangle(dlength / 4, dlength / 4), WHITE), WHITE)
    val ssdiv_black: Image =
      LineColor(FillColor(Rectangle(dlength / 4, dlength / 4), BLACK), BLACK)

    val expected = Below(
      Beside(
        sdiv_black,
        Below(
          Beside(ssdiv_black, ssdiv_black),
          Beside(ssdiv_white, ssdiv_white)
        )
      ),
      Beside(
        Below(
          Beside(ssdiv_black, ssdiv_white),
          Beside(ssdiv_white, ssdiv_white)
        ),
        sdiv_white
      )
    )
    assertEquals(obtained, expected)

  }

  // **************************************************************************** \\
  // *                          	      compress                     	          * \\
  // **************************************************************************** \\

  test("Quadtrees : compress -> noeud dont les enfants sont 4 feuilles de même couleur") {

    val quadtree: QT = N(C(WHITE), C(WHITE), C(WHITE), C(WHITE))

    val obtained = service_QT.compress(quadtree)

    val expected = C(WHITE)

    assertEquals(obtained, expected)

  }

  test("Quadtrees : compress -> quadtree déjà compresé") {

    val obtained = service_QT.compress(C(WHITE))

    val expected = C(WHITE)

    assertEquals(obtained, expected)

  }

  // **************************************************************************** \\
  // *                          	  transformations                     	      * \\
  // **************************************************************************** \\

  test("Quadtrees : rotation_left") {

    val quadtree: QT = N(C(RED), C(BLUE), C(BLACK), C(WHITE))
    val obtained = service_QT.rotation_left(quadtree)

    val expected = N(C(BLUE), C(BLACK), C(WHITE), C(RED))

    assertEquals(obtained, expected)

  }

  test("Quadtrees : rotation_right") {

    val quadtree: QT = N(C(RED), C(BLUE), C(BLACK), C(WHITE))
    val obtained = service_QT.rotation_right(quadtree)

    val expected = N(C(WHITE), C(RED), C(BLUE), C(BLACK))

    assertEquals(obtained, expected)

  }

  test("Quadtrees : flip_vertical") {

    val quadtree: QT = N(C(RED), C(BLUE), C(BLACK), C(WHITE))
    val obtained = service_QT.flip_vertical(quadtree)

    val expected = N(C(BLUE), C(RED), C(WHITE), C(BLACK))

    assertEquals(obtained, expected)

  }

  test("Quadtrees : flip_horizontal") {

    val quadtree: QT = N(C(RED), C(BLUE), C(BLACK), C(WHITE))
    val obtained = service_QT.flip_horizontal(quadtree)

    val expected = N(C(WHITE), C(BLACK), C(BLUE), C(RED))

    assertEquals(obtained, expected)

  }

  test("Quadtrees : transform") {

    val quadtree: QT = N(C(RED), C(BLUE), C(BLACK), C(WHITE))
    val transfos: List[Transformation] =
      service_QT.rotation_left :: service_QT.flip_vertical :: Nil

    val obtained = service_QT.transform(quadtree, transfos)

    val expected = N(C(BLACK), C(BLUE), C(RED), C(WHITE))

    assertEquals(obtained, expected)

  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                                  MATRICES                                * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                              get_dimensions                        	    * \\
  // **************************************************************************** \\

  test("Matrices : get_dimensions (implémentation listes)") {

    val m = service_ML.init_matrix(2, 3, 0)

    val obtained = service_ML.get_dimensions(m)
    val expected = (2, 3)

    assertEquals(obtained, expected)
  }

  test("Matrices : get_dimensions (implémentation vecteurs)") {

    val m = service_MV.init_matrix(2, 3, 0)

    val obtained = service_MV.get_dimensions(m)
    val expected = (2, 3)

    assertEquals(obtained, expected)
  }

  // **************************************************************************** \\
  // *                                get_element                          	    * \\
  // **************************************************************************** \\

  test("Matrices : get_dimensions -> élément présent (implémentation listes)") {

    val m = service_ML.init_matrix(2, 3, 0)

    val obtained = service_ML.get_element(m, 1, 2)
    val expected = Some(0)

    assertEquals(obtained, expected)
  }

  test("Matrices : get_dimensions -> élément présent (implémentation vecteurs)") {

    val m = service_MV.init_matrix(2, 3, 0)

    val obtained = service_MV.get_element(m, 1, 2)
    val expected = Some(0)


    assertEquals(obtained, expected)
  }

  test("Matrices : get_dimensions -> élément absent (implémentation listes)") {

    val m = service_ML.init_matrix(2, 3, 0)

    val obtained = service_ML.get_element(m, 2, 2)
    val expected = None

    assertEquals(obtained, expected)
  }

  test("Matrices : get_dimensions -> élément absent (implémentation vecteurs)") {

    val m = service_MV.init_matrix(2, 3, 0)

    val obtained = service_MV.get_element(m, 2, 2)
    val expected = None
    
    assertEquals(obtained, expected)
  }

  // **************************************************************************** \\
  // *                                set_element                          	    * \\
  // **************************************************************************** \\


}
