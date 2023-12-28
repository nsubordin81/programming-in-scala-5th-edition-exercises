// step one, define a type that wraps your context paramter (this ensures it won't collide with other givens in the context)
class PreferredPrompt(val preference: String)

// step 2, separate the parameter into a new parameter list for the function that uses it, and use the 'using' keyword
object Greeter:
  def greet(name: String)(using prompt: PreferredPrompt) =
    println(s"Welcome $name. the system is ready.")
    println(prompt.preference)

object JillsPrefs:
  // step 3, use 'given' keyword somewhere in scope to supply an instance of your type that the compiler can find and inject
  given prompt: PreferredPrompt =
    PreferredPrompt("Your wish> ")
// step 4, profit! I mean, you use the object without explicitly supplying the context parameter
// pretend we did this, Greeter.greet("Jill")
// and got something back like 'no impolicit argument of type PreferredPrompt was found for parameter prompt of method greet in object Greeter'
// but wait! it fails to compile becauase we haven't imported Jill's prefs into scope yet. let's do that and see what we get:
import JillsPrefs.prompt
Greeter.greet("Jill")

//try uncommentying the below line, and it won't work, because you can't explicitly reference context params without 'using'
// Greeter.greet("Jill")(JillsPrefs.prompt)

// but this will work because you called it out as being a given in the callsite using the 'using' keyword
Greeter.greet("Jill")(using JillsPrefs.prompt)

// let's try something now with two types for context params
