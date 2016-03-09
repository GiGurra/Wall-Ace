package se.gigurra.wallace.util

import org.scalatest._

class TestFixedPoint extends WordSpec with Matchers {

  import FixedPoint._

  "FixedPoint.sqrt" should {

    "throw for negative numbers" in {
      for (square <- 1L until 100000L) {
        intercept[Exception](sqrt(-square))
      }
    }

    "be exact for 0,1,4,9,16,25,36,49,64,82,100,121,144" in {
      sqrt(0) shouldBe 0
      sqrt(1) shouldBe 1
      sqrt(4) shouldBe 2
      sqrt(9) shouldBe 3
      sqrt(16) shouldBe 4
      sqrt(25) shouldBe 5
      sqrt(36) shouldBe 6
      sqrt(49) shouldBe 7
      sqrt(64) shouldBe 8
      sqrt(81) shouldBe 9
      sqrt(100) shouldBe 10
      sqrt(121) shouldBe 11
      sqrt(144) shouldBe 12
    }

    "be exact for n^2 where n = 1 .. 10^6" in {
      for (n <- 0L until 1000000L) {
        val square = n * n
        sqrt(square) shouldBe n
      }
    }

    "be as good as possible for all values up to 1 million" in {
      for (square <- 0L until 1000000L) {
        val realsqrt = math.round(math.sqrt(square.toDouble))
        val mysqrt = sqrt(square)
        mysqrt shouldBe realsqrt
      }
    }

  }

}