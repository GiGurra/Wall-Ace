package se.gigurra.wallace.util

object FixedPoint {

  def sqrt(a_nInput: Long): Long = {
    require(a_nInput >= 0L, "sqrt input < 0")

    var op = a_nInput
    var res = 0L
    var one = 1L << 62L // The second-to-top bit is set: use 1u << 14 for uint16_t type use 1uL<<30 for uint32_t type


    // "one" starts at the highest power of four <= than the argument.
    while (one > op) {
      one >>>= 2L
    }

    while (one != 0L) {
      if (op >= res + one) {
        op = op - (res + one)
        res = res + 2L * one
      }
      res >>>= 1L
      one >>>= 2L
    }

    if (op > res) {
      res += 1L
    }

    return res

  }

}
