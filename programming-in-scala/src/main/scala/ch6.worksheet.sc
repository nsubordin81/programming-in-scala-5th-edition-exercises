// should act like a rational number in mathematics
// +, -, * and / should be supported, but they should be fractions not decimals

// remember scala is the blob, you can use scala to define new things that can act just like scala, none of this "part of the language, not part of the language confusion"

// no semicolon or curly braces when there is no body, just declare the class signature. 
// a primary constructor is automatically generated for your by the compiller
class Rational(n: Int, d: Int):
    // begin primary constructor space
    //precondition
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
    def add(that: Rational) =
        // I don't think I knew this trick, or if I did I forgot it, you scale the numerators by the other fraction's denominator and then sum them
        // this ends up having the same effect as scaling each whole fraction by the factor that you know will make the denominators into a common multiple of 
        // both prior denominators.
        Rational(numer * that.denom + that.numer * denom, denom * that.denom)
    def lessThan(that: Rational) = 
        this.numer * that.denom < that.numer * this.denom
    def max(that: Rational) = 
        // this is assumed and optional for denoting methods or fields, mandatory when it stands alone
        if this.lessThan(that) then that else this
    private def gcd(a: Int, b: Int): Int = 
        if b == 0 then a else gcd(b, a % b)



// toString method is overwritten or this would be Rational@<somehexnumber> because that is what Object.toString would do. 
val rational = new Rational(3, 4)
val twoThirds = new Rational(2, 3)
val threeFourths = new Rational(3, 4)
val seventeenTwelfths = twoThirds.add(threeFourths)
twoThirds.lessThan(seventeenTwelfths)
twoThirds.max(threeFourths)
val five = new Rational(5)
// showing that the reduction by greatest common divisor is working
val twelveeighths = new Rational(12, 8)

twoThirds.numer



val sum = rational.add(Rational(1, 2))

// fails the requirement precondition
// val rational2 = new Rational(3, 0)





            
