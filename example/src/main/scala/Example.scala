import com.github.hexx.lispy._

object Example {
  val example = Parser.parseAll(Parser.exp, """(begin
    (define area (lambda (r) (* 3.141592653 (* r r))))
    (print (area 3))
    (define fact (lambda (n) (if (<= n 1) 1 (* n (fact (- n 1))))))
    (print (fact 10))
  )""").get
}
