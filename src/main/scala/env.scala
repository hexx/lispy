package com.github.hexx.lispy

object Env {
  type Env = List[(String, Any)]

  def find(env: Env, v: String): Any = env.find(_._1 == v).get._2

  def updated(env: Env, v: String, e: Exp): Env = env.updated(env.indexWhere(_._1 == v), (v, e))
}
