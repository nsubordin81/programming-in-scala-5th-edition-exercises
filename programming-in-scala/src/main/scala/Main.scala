// Step 4
@main def hello(args: String*): Unit = 
  println("hellow, " + args(0) + ", from a script!")

// commenting other steps below because it creates a lot of main methods

/*
// Step 5 - iteration
@main def printargs(args: String*) =
    var i = 0
    while i < args.length do
        println(args(i))
        i += 1

@main def echoargs(args: String*) =
    var i = 0
    while i < args.length do
        if i != 0 then
            print(" ")
        print(args(i))
        i += 1
    println(

// Step 6 - functional style
@main def argsfunctional(args: String*) =
    // replace arg => println(arg) with just println
    args.foreach(println)

// intro to for comprehension
@main def forargs(args: String*) =
    for arg <- args do
        println(arg)




    


