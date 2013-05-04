package com.github.hexx.lispy

import Env._

object Builtin {
  val env: List[(String, PartialFunction[List[Any], Any])] = List(
    "+" -> { case (n1: Double) :: (n2: Double) :: _ => n1 + n2 },
    "*" -> { case (n1: Double) :: (n2: Double) :: _ => n1 * n2 },
    "print" -> { case es => es foreach println }
  )
}
