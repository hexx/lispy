package com.github.hexx.lispy

import Env._

object Eval {
  def eval(exp: Exp, env: Env): Any = exp match {
    case Var(v)                => find(env, v)
    case NumberLit(n)          => n
    case StringLit(s)          => s
    case TrueLit               => true
    case FalseLit              => false
    case Quote(e)              => e
    case If(test, conseq, alt) => eval(if (eval(test, env).asInstanceOf[Boolean]) conseq else alt, env)
    case Set(v, e)             => update(env, v.v, eval(e, env))
    case Define(v, e)          => (v.v -> eval(e, env)) +=: env
    case Lambda(vs, e)         => (rs: List[Any]) => eval(e, env ++ vs.map(_.v).zip(rs))
    case Begin(es)             => es.foldLeft((): Any)((_, exp1) => eval(exp1, env))
    case Proc(e, es)           =>
      val lam = eval(e, env)
      val rs = es.foldLeft(List(): List[Any])((rs, exp1) => (rs :+ eval(exp1, env)))
      lam.asInstanceOf[List[Any] => Any](rs)
  }
}
