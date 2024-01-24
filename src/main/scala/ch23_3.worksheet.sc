/* 
Implicit Conversions use case for typeclasses

these are the ogs of the typeclass world in scala. the idea was get rid of the boilerplate 
that occurs with type conversions that are explicit in scala code. 

the interesting story here though is that implicit conversions were too sneaky and sometimes 
people didn't realize and had trouble tracing why types were working in some contexts when 
they should be converted to other types. The idiomatic way to do many conversions in scala 3 
is with extension methods and also context params (givens). 

there is still room for them and they are supported but only with a feature flag import. 

the story of how they work is very similar to the story of how context parameters and typeclasses 
work. the compiler will encounter what should be errors with the type, and it will attempt to heal it before
throwing it by searching its context for something that will heal it. If you see an X but need a Y, go find the
X -> Y converter if it exists. 


 */

 case class Street(value: String)

 val street = Street("123 Main St")

//  val streeStr: String = street < -- this is a type error, street is a Street, not a string

// val streetStr: String = street.value

// in scala 3 there is a pattern for defining implicit conversions. you can use a given instance of 
// Converstion[Street, String] and that is a subtype of the function type Street => String


// abstract class Conversion[-T, +U] extends (T => U):
//     def apply(x: T): U

// this is apparently defined leveraging a SAM function literal
// that stands for 'single abstract method'
// you know this from programming java, it is the same 
// principle, but in scala it applies to classes and traits
given streetToString: Conversion[Street, String] = _.value

// can't just use this though in scala 3, have to enable them

import scala.language.implicitConversions

val streetStr: String = street

// compiler finds implicit converstion above and inserts the application of the converter

// complier does this:
// val streetStr: String = streetToString(street)

// you didn't tell the compiler to do this in your code, it went and found the one you created. 

// hence the implicit parrt. 

// another exqample of implicit converstion that happens in scala 3, Int to Dobule. just adding that .0
// the recommendation is that you should do conversions that go from constrained to general. 
// Int is an example, it is constrained in what numbers it can represent, whereas doubles can be any int plus a lot 
// more.