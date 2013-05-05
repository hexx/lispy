import com.github.hexx.lispy._

object Example {
  val example = Parser.parseAll(Parser.exp, """(begin
    (define area (lambda (r) (* 3.141592653 (* r r))))
    (print (area 3))
  )""").get
}
