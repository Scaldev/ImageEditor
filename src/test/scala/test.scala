package PRO2.projet

import fr.istic.scribble.*

class MySuite extends munit.FunSuite {

  // Valeurs globales.

  val serv_QT: IntQuadtrees = ImpQuadtrees
  val dsize: Int = 9
  val dlength: Float = 512.0

  // **************************************************************************** \\
  // *                          	  quadtree_to_image                     	    * \\
  // **************************************************************************** \\

  test("quadtree_to_image : zone unicolore sans contour") {

    val obtained = serv_QT.quadtree_to_image(C(WHITE), false, dsize)

    val expected =
      LineColor(FillColor(Rectangle(dlength, dlength), WHITE), WHITE)

    assertEquals(obtained, expected)

  }

  test("quadtree_to_image : zone unicolore avec contour") {

    val obtained = serv_QT.quadtree_to_image(C(WHITE), true, dsize)

    val expected = LineColor(FillColor(Rectangle(dlength, dlength), WHITE), RED)

    assertEquals(obtained, expected)

  }

  test("quadtree_to_image : image non-compressee") {

    val qt: QT = N(C(WHITE), C(WHITE), C(WHITE), C(WHITE))
    val obtained = serv_QT.quadtree_to_image(qt, false, dsize)

    val subrect: Image = FillColor(Rectangle(dlength / 2, dlength / 2), WHITE)
    val dsubdiv: Image = LineColor(subrect, WHITE)
    val expected = Below(Beside(dsubdiv, dsubdiv), Beside(dsubdiv, dsubdiv))

    assertEquals(obtained, expected)

  }

  test("quadtree_to_image : image non-unicolore") {

    // Obtained.
    // Remarque : c'est l'exemple du cours !

    val qt: QT = N(
      C(BLACK),
      N(C(BLACK), C(BLACK), C(WHITE), C(WHITE)),
      C(WHITE),
      N(C(BLACK), C(WHITE), C(WHITE), C(WHITE))
    )

    val obtained = serv_QT.quadtree_to_image(qt, false, dsize)

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

  // **************************************************************************** \\
  // *                          	     transform                     	          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                          	     transforms                     	        * \\
  // **************************************************************************** \\

}
