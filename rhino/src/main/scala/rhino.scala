package com.github.hexx.lispy

import scala.collection.JavaConverters._

import org.mozilla.javascript._
import org.mozilla.javascript.{Parser => RParser}
import org.mozilla.javascript.ast._

package object rhino {
  val infixTable = Map(
    "+"  -> Token.ADD,
    "*"  -> Token.MUL,
    "-"  -> Token.SUB,
    "==" -> Token.EQ,
    "<"  -> Token.LT,
    "<=" -> Token.LE,
    ">"  -> Token.GT,
    ">=" -> Token.GE)

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
      val js = new ConditionalExpression
      js.setTestExpression(expToJS(test))
      js.setTrueExpression(expToJS(conseq))
      js.setFalseExpression(expToJS(alt))
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
      def encloseBody(b: Exp) = b match {
        case Begin(es) =>
          Begin((es.reverse match {
            case e1 :: es1 => Proc(Var("return"), List(e1)) :: es1
            case Nil => Nil
          }).reverse)
        case _ => Begin(List(Proc(Var("return"), List(b))))
      }
      val js = new FunctionNode
      js.setParams(vs.map(expToJS).asJava)
      js.setBody(expToJS(encloseBody(e)))
      js
    case Begin(es) =>
      val js = new Block
      es.foreach(e => js.addStatement(expToJS(e)))
      js
    case Proc(e, es) =>
      def infixToken(o: Exp) = o match {
        case Var(v) => infixTable.get(v)
        case _ => None
      }
      infixToken(e).map { t =>
        val js = new InfixExpression
        js.setOperator(t)
        js.setLeft(expToJS(es(0)))
        js.setRight(expToJS(es(1)))
        js
      } getOrElse {
        val js = new FunctionCall
        js.setTarget(expToJS(e))
        js.setArguments(es.map(expToJS).asJava)
        js
      }
    case _ => new Undefined
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
