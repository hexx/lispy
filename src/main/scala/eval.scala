package com.github.hexx.lispy

import Env._

object Eval {
  def eval(exp: Exp, env: Env): (Any, Env) = exp match {
    case Var(v)                => (find(env, v), env)
    case NumberLit(n)          => (n, env)
    case StringLit(s)          => (s, env)
    case TrueLit               => (true, env)
    case FalseLit              => (false, env)
    case Quote(e)              => (e, env)
    case If(test, conseq, alt) =>
      val (t, env1) = eval(test, env)
      eval(if (t.asInstanceOf[Boolean]) conseq else alt, env1)
    case Set(v, e)             =>
      val (e1, env1) = eval(e, env)
      ((), env1.updated(env.indexWhere(_._1 == v.v), (v.v, e1)))
    case Define(v, e)          => ((), (v.v -> eval(e, env)) :: env)
      val (e1, env1) = eval(e, env)
      ((), (v.v -> e1) :: env1)
    case Lambda(vs, e)         => ((rs: List[Any]) => eval(e, env ++ vs.map(_.v).zip(rs))._1, env)
    case Begin(es)             => es.foldLeft(((): Any, env)) { case ((_, env1), exp1) => eval(exp1, env1) }
    case Proc(e, es)           =>
      val (lam, env1) = eval(e, env)
      val (rs, env2) = es.foldLeft(List(): List[Any], env1) { case ((rs, env1), exp1) =>
        val (r, env2) = eval(exp1, env1)
        ((rs :+ r), env2)
      }
      (lam.asInstanceOf[List[Any] => Any](rs), env2)
  }
}
