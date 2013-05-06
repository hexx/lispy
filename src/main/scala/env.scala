package com.github.hexx.lispy

import scala.collection.mutable.MutableList

object Env {
  type Env = MutableList[(String, Any)]

  def find(env: Env, v: String): Any = env.find(_._1 == v).get._2

  def update(env: Env, v: String, e: Any) = env.update(env.indexWhere(_._1 == v), (v, e))
}
