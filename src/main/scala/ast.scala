package com.github.hexx.lispy

sealed trait Exp
case class Var(v: String) extends Exp
case class NumberLit(n: Double) extends Exp
case class StringLit(s: String) extends Exp
case object TrueLit extends Exp
case object FalseLit extends Exp
case class Quote(e: Exp) extends Exp
case class If(test: Exp, conseq: Exp, alt: Exp) extends Exp
case class Set(v: Var, e: Exp) extends Exp
case class Define(v: Var, e: Exp) extends Exp
case class Lambda(vs: List[Var], e: Exp) extends Exp
case class Begin(es: List[Exp]) extends Exp
case class Proc(e: Exp, es: List[Exp]) extends Exp
