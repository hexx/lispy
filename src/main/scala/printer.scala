package com.github.hexx.lispy

object Printer {
  def print(exp: Exp): String = exp match {
    case Var(v)                => v
    case NumberLit(n)          => n.toString
    case StringLit(s)          => s
    case TrueLit               => "true"
    case FalseLit              => "false"
    case Quote(e)              => s"(quote ${print(e)})"
    case If(test, conseq, alt) => s"(if ${print(test)} ${print(conseq)} ${print(alt)})"
    case Set(v, e)             => s"(set! ${print(v)} ${print(e)})"
    case Define(v, e)          => s"(define ${print(v)} ${print(e)})"
    case Lambda(vs, e)         => s"""(lambda (${vs.map(print(_)).mkString(" ")}) ${print(e)})"""
    case Begin(es)             => s"""(begin es.map(print(_)).mkString(" "))"""
    case Proc(e, es)           => s"""(${print(e)} ${es.map(print(_)).mkString(" ")})"""
  }
}
