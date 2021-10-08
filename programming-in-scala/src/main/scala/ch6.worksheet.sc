// should act like a rational number in mathematics
// +, -, * and / should be supported, but they should be fractions not decimals

// remember scala is the blob, you can use scala to define new things that can act just like scala, none of this "part of the language, not part of the language confusion"

// no semicolon or curly braces when there is no body, just declare the class signature. 
// a primary constructor is automatically generated for your by the compiller
class Rational(n: Int, d: Int):
    //precondition
    require(d != 0)
    // this line gets added to the primary constructor
    // println("Created " + n + "/" + d)
    // in scala you only need to declare fields when you need to access them on instances outside of this definition
    // like in the add method below you have a that param which is type Rational this object wouldn't have access to it's n and d
    val numer: Int = n
    val denom: Int = d
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



// toString method is overwritten or this would be Rational@<somehexnumber> because that is what Object.toString would do. 
val rational = new Rational(3, 4)
val twoThirds = new Rational(2, 3)
val threeFourths = new Rational(3, 4)
val seventeenTwelves = twoThirds.add(threeFourths)
twoThirds.lessThan(seventeenTwelves)
twoThirds.max(threeFourths)

twoThirds.numer



val sum = rational.add(Rational(1, 2))

// fails the requirement precondition
// val rational2 = new Rational(3, 0)





            
