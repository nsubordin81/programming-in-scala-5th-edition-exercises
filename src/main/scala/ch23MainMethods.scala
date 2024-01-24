
// I don't know why this is coming up in the typeclasses chapter, but we will see, won't we? 

/* so it goes back over the part where we talk about that annotating a method with the main annotation is a 
way to make it possible to get the main method to be set as the method for that program. it also shows you you can 
use the scala interpreter without compiling to run this script and get it to generate the output and execute the 
function as intended. but then there is another thing you can do, you can compile it and then you 
can invoke scala again but then for the main method instead of the .scala script file itself.
*/

// this is a simple main method that has the main annotation. 
// @main def echo(args: String*) =
//     println(args.mkString(" "))

/* ok and for an example of a different argument list, since main methods can take any kind of argument, 
let's use this one */
// @main def repeat(word: String, count: Int) =

//     val msg = 
//         if count > 0 then
//             val words = List.fill(count)(word)
//             words.mkString(" ")
//         else
//             "Please enter a word and a positive integer count."

//     println(msg)
/* ok so not we are getting to the meat of things. there is a typeclass that knows how to parse the command line 
argument I just provided for repeat, the integer 3, from a string into an int. the implementation of the main 
annotation has a using() for FromString[T] 
trait FromString[T]:
    def fromString(s: String): T

so there are some given instances defined in the standard library in the FromString companion object to the trait 
and one of them is Int so that is how the repeat method works. what if I wanted to use a command line parameter 
that wasn't part of the typeclass yet? well that would be possible without having to augment main

the example they are giving is using an enum to represent mood and then using that in main. let's see how it works
*/

enum Mood:
    case Surprised, Angry, Neutral

object Mood:

    // ok, important to know you have to import the method in order to do this
    import scala.util.CommandLineParser.FromString

    given moodFromString: FromString[Mood] with
        def fromString(s: String): Mood = 
            s.trim.toLowerCase match
                case "angry" => Mood.Angry
                case "surprised" => Mood.Surprised
                case "neutral" => Mood.Neutral
                case _ => throw new IllegalArgumentException(errmsg)


val errmsg = 
    "Please enter a word , a positive integer count, and\n" +
    "a mood (one of 'angry', 'surprised', or 'neutral')"

@main def repeat(word: String, count: Int, mood: Mood) =

    val msg = 
        if count > 0 then
            val words = List.fill(count)(word)
            val punc = 
                mood match
                    case Mood.Angry => "!"
                    case Mood.Surprised => "?"
                    case Mood.Neutral => ""
            val sep = punc + " "
            words.mkString(sep) + punc
            else errmsg

    println(msg)

/* some closing words about this, this is a good choice for command line argument parsers for main methods because yo don't really
need to have a command line argument parser for every type, and the types are really not that related, they don't have to be 
anyway. command line arguments can be any type. the existing parser definition in the standard library defines given instances
for all of the basic types from scala, so it makes senseyou would want to extend this to include other typs that you define 
later tha tthe standard library authors don't know about. cool. I should think about typeclasses and their usefullness in this context
when I'm looking at the typelevel code.lazy val 
*/



