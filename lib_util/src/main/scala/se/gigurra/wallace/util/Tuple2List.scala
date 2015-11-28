package se.gigurra.wallace.util

object Tuple2List {

  def apply[T](tt: Tuple1[T]) =
    List(tt._1)
  def apply[T](tt: Tuple2[T, T]) =
    List(tt._1, tt._2)
  def apply[T](tt: Tuple3[T, T, T]) =
    List(tt._1, tt._2, tt._3)
  def apply[T](tt: Tuple4[T, T, T, T]) =
    List(tt._1, tt._2, tt._3, tt._4)
  def apply[T](tt: Tuple5[T, T, T, T, T]) =
    List(tt._1, tt._2, tt._3, tt._4, tt._5)
  def apply[T](tt: Tuple6[T, T, T, T, T, T]) =
    List(tt._1, tt._2, tt._3, tt._4, tt._5, tt._6)
  def apply[T](tt: Tuple7[T, T, T, T, T, T, T]) =
    List(tt._1, tt._2, tt._3, tt._4, tt._5, tt._6, tt._7)
  def apply[T](tt: Tuple8[T, T, T, T, T, T, T, T]) =
    List(tt._1, tt._2, tt._3, tt._4, tt._5, tt._6, tt._7, tt._8)
  def apply[T](tt: Tuple9[T, T, T, T, T, T, T, T, T]) =
    List(tt._1, tt._2, tt._3, tt._4, tt._5, tt._6, tt._7, tt._8, tt._9)
  def apply[T](tt: Tuple10[T, T, T, T, T, T, T, T, T, T]) =
    List(tt._1, tt._2, tt._3, tt._4, tt._5, tt._6, tt._7, tt._8, tt._9, tt._10)
  def apply[T](tt: Tuple11[T, T, T, T, T, T, T, T, T, T, T]) =
    List(tt._1, tt._2, tt._3, tt._4, tt._5, tt._6, tt._7, tt._8, tt._9, tt._10, tt._11)
  def apply[T](tt: Tuple12[T, T, T, T, T, T, T, T, T, T, T, T]) =
    List(tt._1, tt._2, tt._3, tt._4, tt._5, tt._6, tt._7, tt._8, tt._9, tt._10, tt._11, tt._12)
  def apply[T](tt: Tuple13[T, T, T, T, T, T, T, T, T, T, T, T, T]) =
    List(tt._1, tt._2, tt._3, tt._4, tt._5, tt._6, tt._7, tt._8, tt._9, tt._10, tt._11, tt._12, tt._13)
  def apply[T](tt: Tuple14[T, T, T, T, T, T, T, T, T, T, T, T, T, T]) =
    List(tt._1, tt._2, tt._3, tt._4, tt._5, tt._6, tt._7, tt._8, tt._9, tt._10, tt._11, tt._12, tt._13, tt._14)
  def apply[T](tt: Tuple15[T, T, T, T, T, T, T, T, T, T, T, T, T, T, T]) =
    List(tt._1, tt._2, tt._3, tt._4, tt._5, tt._6, tt._7, tt._8, tt._9, tt._10, tt._11, tt._12, tt._13, tt._14, tt._15)
  def apply[T](tt: Tuple16[T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T]) =
    List(tt._1, tt._2, tt._3, tt._4, tt._5, tt._6, tt._7, tt._8, tt._9, tt._10, tt._11, tt._12, tt._13, tt._14, tt._15, tt._16)
  def apply[T](tt: Tuple17[T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T]) =
    List(tt._1, tt._2, tt._3, tt._4, tt._5, tt._6, tt._7, tt._8, tt._9, tt._10, tt._11, tt._12, tt._13, tt._14, tt._15, tt._16, tt._17)
  def apply[T](tt: Tuple18[T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T]) =
    List(tt._1, tt._2, tt._3, tt._4, tt._5, tt._6, tt._7, tt._8, tt._9, tt._10, tt._11, tt._12, tt._13, tt._14, tt._15, tt._16, tt._17, tt._18)
  def apply[T](tt: Tuple19[T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T]) =
    List(tt._1, tt._2, tt._3, tt._4, tt._5, tt._6, tt._7, tt._8, tt._9, tt._10, tt._11, tt._12, tt._13, tt._14, tt._15, tt._16, tt._17, tt._18, tt._19)
  def apply[T](tt: Tuple20[T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T]) =
    List(tt._1, tt._2, tt._3, tt._4, tt._5, tt._6, tt._7, tt._8, tt._9, tt._10, tt._11, tt._12, tt._13, tt._14, tt._15, tt._16, tt._17, tt._18, tt._19, tt._20)
  def apply[T](tt: Tuple21[T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T]) =
    List(tt._1, tt._2, tt._3, tt._4, tt._5, tt._6, tt._7, tt._8, tt._9, tt._10, tt._11, tt._12, tt._13, tt._14, tt._15, tt._16, tt._17, tt._18, tt._19, tt._20, tt._21)
  def apply[T](tt: Tuple22[T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T]) =
    List(tt._1, tt._2, tt._3, tt._4, tt._5, tt._6, tt._7, tt._8, tt._9, tt._10, tt._11, tt._12, tt._13, tt._14, tt._15, tt._16, tt._17, tt._18, tt._19, tt._20, tt._21, tt._22)

}
