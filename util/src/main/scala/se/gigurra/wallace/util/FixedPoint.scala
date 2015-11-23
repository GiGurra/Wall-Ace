package se.gigurra.wallace.util

object FixedPoint {
  
  def sqrt(a_nInput: Int): Int = {
    require(a_nInput >= 0, "sqrt input < 0")
    
    var op  = a_nInput
    var res = 0
    var one = 1 << 30 // The second-to-top bit is set: use 1u << 14 for uint16_t type use 1uL<<30 for uint32_t type


    // "one" starts at the highest power of four <= than the argument.
    while (one > op)
    {
      one >>>= 2
    }

    while (one != 0)
    {
      if (op >= res + one)
      {
        op = op - (res + one)
        res = res +  2 * one
      }
      res >>>= 1
      one >>>= 2
    }

    if (op > res)
    {
      res+=1
    }

    return res
    
  }
  
}
