package com.github.hexx.lispy

import scala.collection.mutable.MutableList

import Env._

object Builtin {
  val env: Env = MutableList[(String, PartialFunction[List[Any], Any])](
    "+"     -> { case (n1: Double) :: (n2: Double) :: _ => n1 + n2 },
    "*"     -> { case (n1: Double) :: (n2: Double) :: _ => n1 * n2 },
    "-"     -> { case (n1: Double) :: (n2: Double) :: _ => n1 - n2 },
    "=="    -> { case (n1: Double) :: (n2: Double) :: _ => n1 == n2 },
    "<"     -> { case (n1: Double) :: (n2: Double) :: _ => n1 < n2 },
    "<="    -> { case (n1: Double) :: (n2: Double) :: _ => n1 <= n2 },
    ">"     -> { case (n1: Double) :: (n2: Double) :: _ => n1 > n2 },
    ">="    -> { case (n1: Double) :: (n2: Double) :: _ => n1 >= n2 },
    "print" -> { case es => es foreach println }
  ).asInstanceOf[Env]
}
