package com.github.hexx.lispy

import scala.collection.JavaConverters._

import org.mozilla.javascript._
import org.mozilla.javascript.{Parser => RParser}
import org.mozilla.javascript.ast._

package object rhino {
  def expToJS(exp: Exp): AstNode = exp match {
    case Var(v) =>
      val js = new Name
      js.setIdentifier(v)
      js
    case NumberLit(n) => new NumberLiteral(n)
    case StringLit(s) =>
      val js = new StringLiteral
      js.setValue(s)
      js
    case TrueLit =>
      val js = new KeywordLiteral
      js.setType(Token.TRUE)
      js
    case FalseLit =>
      val js = new KeywordLiteral
      js.setType(Token.FALSE)
      js
    // case Quote(e) => e
    case If(test, conseq, alt) =>
      val js = new IfStatement
      js.setCondition(expToJS(test))
      js.setThenPart(expToJS(conseq))
      js.setElsePart(expToJS(alt))
      js
    case Set(v, e) => new Assignment(expToJS(v), expToJS(e))
    case Define(v, e) =>
      val vi = new VariableInitializer
      vi.setNodeType(Token.VAR)
      vi.setTarget(expToJS(v))
      vi.setInitializer(expToJS(e))
      val js = new VariableDeclaration
      js.addVariable(vi)
      js
    case Lambda(vs, e) =>
      val js = new FunctionNode
      js.setParams(vs.map(expToJS).asJava)
      js.setBody(expToJS(e))
      js
    case Begin(es) =>
      val js = new Block
      es.foreach(e => js.addStatement(expToJS(e)))
      js
    // case Proc(e, es) =>
    //   val js = new FunctionCall
    //   js
    case _ =>
      val js = new KeywordLiteral
      js.setType(Token.NULL)
      js
  }

  def lispyToJS(lispy: String) = {
    val exp = Parser.parseAll(Parser.exp, lispy).map(Option(_)).getOrElse(None)
    val context = Context.enter
    val scope = context.initStandardObjects
    exp map expToJS map (_.toSource)
  }

  def parseJS(js: String, sourceName: String = "", lineno: Int = 0) = {
    val parser = new RParser
    val ast = parser.parse(js, sourceName, lineno)
    println(ast.debugPrint)
    ast
  }
}
