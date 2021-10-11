// should act like a rational number in mathematics
// +, -, * and / should be supported, but they should be fractions not decimals

// remember scala is the blob, you can use scala to define new things that can act just like scala, none of this "part of the language, not part of the language confusion"

// no semicolon or curly braces when there is no body, just declare the class signature.
// a primary constructor is automatically generated for your by the compiller
class Rational(n: Int, d: Int):
  // begin primary constructor space
  //precondition
  //this require method is part of Predef, which was talked about when covering imports.
  // just remember Predef and everything it defines is imported into all scala source files.
  require(d != 0)
  // can I just say I love that functions with no args don't require () to compile
  private val g = gcd(n.abs, d.abs)
  // initialize fields
  val numer = n / g
  val denom = d / g
  // aux constructor, you use this to construct the object.
  // cool fact, the implicit primary constructor is the single entry point and all other constructors call down to it
  // only the primary constructor (which the compiler is generating in this case) can call the superclass, this is more
  // restrictive than java, but was a design choice. What are we cutting out by not letting alternative constructors
  // call super()? probably not much would be my guess given that constructors are so easy to create issues with anyway
  // and that is one reason for static factory methods, better names, more control over creation etc. So there is a lot of
  // design surface area still to work with without having to have situations where alternate constructors are calling super.
  // END primary constructor space??
  def this(n: Int) = this(n, 1)
  // this line gets added to the primary constructor
  // println("Created " + n + "/" + d)
  // in scala you only need to declare fields when you need to access them on instances outside of this definition
  // like in the add method below you have a that param which is type Rational this object wouldn't have access to it's n and d
  override def toString = s"$numer/$denom"
  def +(that: Rational): Rational =
    // I don't think I knew this trick, or if I did I forgot it, you scale the numerators by the other fraction's denominator and then sum them
    // this ends up having the same effect as scaling each whole fraction by the factor that you know will make the denominators into a common multiple of
    // both prior denominators.
    Rational(numer * that.denom + that.numer * denom, denom * that.denom)
  def +(that: Integer): Rational =
    this + Rational(that)
  def -(that: Rational): Rational =
    this + Rational(-that.numer, that.denom)
  def -(that: Integer): Rational =
    this - Rational(that)
  def *(that: Rational): Rational =
    // notice how even though you could write A + B * C with this which is A.+(B).*(C), Scala Compiler takes care of knowing that
    // * and + have precedence in mathematics and will turn it into A.+(B.*(C))
    Rational(numer * that.numer, denom * that.denom)
  def *(that: Integer): Rational =
    this * Rational(that)
  def /(that: Rational): Rational =
    this * Rational(that.denom, that.numer)
  def /(that: Integer): Rational =
    this / Rational(that)
  def lessThan(that: Rational): Boolean =
    this.numer * that.denom < that.numer * this.denom
  def max(that: Rational): Rational =
    // this is assumed and optional for denoting methods or fields, mandatory when it stands alone
    if this.lessThan(that) then that else this
  private def gcd(a: Int, b: Int): Int =
    if b == 0 then a else gcd(b, a % b)

// Extension methods!!! Wow what a cool concept.
extension (x: Int)
  def +(y: Rational) = Rational(x) + y
  def -(y: Rational) = Rational(x) - y
  def *(y: Rational) = Rational(x) * y
  def /(y: Rational) = Rational(x) / y

// Notes about identifiers, there are four kinds.
// 1. alphanumeric, must start with [a-z, _] starting with $, you can do this and it counts as alphanumeric, however the scala compiler vars all start with this so you could collide,
// limit your use of starts with or contains _, this isn't python, use camelCase. you can run into wierd behavior with _ like the compiler thinking name_: is an identfier when the : was not
// scala has constants in addition to vals, vals are variables and can be reassigned, constants are for values you only ever want to assign once and never change.
// 2. operators [+, -, *, /, **, ...] printable ascii character sequences, + ++ ::: <?> :-> all are operators. operators in scala need to not contain spaces because they can be arbitrarily long
// and as a result they need to be parsed as one chunk so you get a different interpretation of the characters that make up an operator if it contains spaces.
// 3. Mixed identifiers: start with alphanumeric identifier, then has an underscore and then an operator identifier. these are identifiers that define operations but they have special meanings
// so I guess you would just use the operator as normal but because of the context in which it is used this mixed operator woudl be applied instead.
// 4. literal identifiers are a way to escape reserved words and other runtime expressions in scala so that they can be used as identifiers. So it is like telling the compioler "hey ignore what you would normally do with" +
// this symbol, just make an identifier"

// toString method is overwritten or this would be Rational@<somehexnumber> because that is what Object.toString would do.
val rational = new Rational(3, 4)
val twoThirds = new Rational(2, 3)
val threeFourths = new Rational(3, 4)
val seventeenTwelfths = twoThirds + threeFourths
twoThirds.lessThan(seventeenTwelfths)
twoThirds.max(threeFourths)
val five = new Rational(5)
// showing that the reduction by greatest common divisor is working
val twelveeighths = new Rational(12, 8)

twoThirds.numer

val sum = rational + Rational(1, 2)
val product = rational * Rational(1, 2)

twoThirds + 2
2 + twoThirds

// fails the requirement precondition
// val rational2 = new Rational(3, 0)

/* Final word of caution in the chapter.
they covered both operator overloading and extension methods.
These things let your scala code be shorter and more natural,
as if the things you were defining were part of scala itself.
However, operators that you define might not make sense to client programmers
and also the connection between extension methods and the class they are extending
is implicit and managed by the scala compiler so they might lose track of which
extension methods are in scope for a particular part of the code which coudl lead to bugs
depending on how the extension methods are used and the intent.
 */
