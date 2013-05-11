package com.github.hexx.lispy

import scala.util.parsing.combinator.JavaTokenParsers

object Parser extends JavaTokenParsers {
  def sym:    Parser[String]        = """[\*\>=<\+-]+""".r | ident
  def _var:   Parser[Var]           = sym ^^ (Var(_))
  def number: Parser[NumberLit]     = decimalNumber ^^ (d => NumberLit(d.toDouble))
  def string: Parser[StringLit]     = stringLiteral ^^ (StringLit(_))
  def _true:  Parser[TrueLit.type]  = "true" ^^ (_ => TrueLit)
  def _false: Parser[FalseLit.type] = "false" ^^ (_ => FalseLit)
  def quote:  Parser[Quote]         = "(" ~ "quote"  ~> exp                         <~ ")" ^^ (Quote(_))
  def _if:    Parser[If]            = "(" ~ "if"     ~> exp ~ exp ~ exp             <~ ")" ^^ { case t ~ c ~ a => If(t, c, a) }
  def set:    Parser[Set]           = "(" ~ "set!"   ~> _var ~ exp                  <~ ")" ^^ { case v ~ e     => Set(v, e) }
  def define: Parser[Define]        = "(" ~ "define" ~> _var ~ exp                  <~ ")" ^^ { case v ~ e     => Define(v, e) }
  def lambda: Parser[Lambda]        = "(" ~ "lambda" ~ "(" ~> rep(_var) ~ ")" ~ exp <~ ")" ^^ { case vs ~ _ ~ e    => Lambda(vs, e) }
  def begin:  Parser[Begin]         = "(" ~ "begin"  ~> rep(exp)                    <~ ")" ^^ (Begin(_))
  def proc:   Parser[Proc]          = "("            ~> exp ~ rep(exp)              <~ ")" ^^ { case e ~ es    => (Proc(e, es)) }
  def exp:    Parser[Exp]           = _var | number | string | quote | _if | set | define | lambda | begin | proc
}
