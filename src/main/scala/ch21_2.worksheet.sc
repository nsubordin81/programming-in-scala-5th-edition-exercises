class PreferredPrompt(val preference: String)
class PreferredDrink(val preference: String)

object Greeter:
  def greet(
      name: String
  )(using prompt: PreferredPrompt, drink: PreferredDrink) =
    println(s"Welcome, $name. The system is ready.")
    print("But while you work, ")
    println(s"why not enjoy a cup of ${drink.preference}?")
    println(prompt.preference)

object JoesPrefs:
  given prompt: PreferredPrompt = PreferredPrompt("relax> ")
  given drink: PreferredDrink = PreferredDrink("tea")

// proof that this works with a parameter list that is more than one, you can use using for each of them
// you will have to import them into scope though to make it work, see how that is done? check out the context.
import JoesPrefs.{drink, prompt}

Greeter.greet("Joe")





