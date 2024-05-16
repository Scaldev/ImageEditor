package PRO2.projet

import fr.istic.scribble.*
import Quadtrees.*

class MySuite extends munit.FunSuite {

	// Valeurs globales.
	
	val dlength: Float = 512.0

	// **************************************************************************** \\
	// *                          	  make_area                               	  * \\
  	// **************************************************************************** \\
	
	test("make_area : zone unicolore sans contour") {

		val obtained = make_area(WHITE, false, dlength)

		val expected = LineColor(FillColor(Rectangle(dlength, dlength), WHITE), WHITE)

		assertEquals(obtained, expected)

	}
	
	test("make_area : zone unicolore avec contour") {

		val obtained = make_area(WHITE, true, dlength)

		val expected = LineColor(FillColor(Rectangle(dlength, dlength), WHITE), RED)

		assertEquals(obtained, expected)

	}	
  	// **************************************************************************** \\
  	// *                          quadtree_to_image                               * \\
  	// **************************************************************************** \\

	// Tester des images unicolores compressées revient à tester make_area de part
	// l'implémentation de quadtree_to_image. Nous testons donc uniquement des
	// quadtrees qui ne sont pas réduits à une unique feuille.
	
	test("quadtree_to_image : image non-compressee") {

		val qt: QT = N(C(WHITE), C(WHITE), C(WHITE), C(WHITE))
		val obtained = quadtree_to_image(qt, false, dlength)
		
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

		val obtained = quadtree_to_image(qt, false, dlength)

		// Expected.
		// La fonction make_area ayant été testée avant, on fait l'hypothèse
		// qu'elle fonctionne déjà afin d'alléger le code.

		val sdiv_white: Image = make_area(WHITE, false, dlength / 2)
		val sdiv_black: Image = make_area(BLACK, false, dlength / 2)

		val ssdiv_white: Image = make_area(WHITE, false, dlength / 4)
		val ssdiv_black: Image = make_area(BLACK, false, dlength / 4)

		val expected = Below(
			Beside(
				sdiv_black,
				Below(Beside(ssdiv_black, ssdiv_black), Beside(ssdiv_white, ssdiv_white))
			),
			Beside(
				Below(Beside(ssdiv_black, ssdiv_white), Beside(ssdiv_white, ssdiv_white)),
				sdiv_white
			)
		)
		assertEquals(obtained, expected)
		
	}
}
