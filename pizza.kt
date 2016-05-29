import java.util.*
import java.util.concurrent.ThreadLocalRandom


const val MEAL_SIZE = 0.4
const val PRICE_MAX = 1200
const val PRICE_CHEAP = 119


class Pizza(val name: String, val weight: Int, p: Int) {
    val price: Int = p
        get() = if (isWednesday()) PRICE_CHEAP else field

    fun isWednesday() = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY
}


fun main(args: Array<String>) {
       val menu = listOf(
               Pizza("Pappas spesial", 4, 159),
               Pizza("Texas", 3, 149),
               Pizza("Blue Hawaii", 7, 149),
               Pizza("Florida", 4, 149),
               Pizza("Buffalo", 4, 149),
               Pizza("Chicken", 4, 149),
               Pizza("New York", 0, 149),
               Pizza("Las Vegas", 6, 149),
               Pizza("Vegetarianer", 0, 149),
               Pizza("Filadelfia", 4, 149),
               Pizza("Hot Chicago", 7, 149),
               Pizza("Hot Express", 5, 149),
               Pizza("Kebab pizza spesial", 3, 169),
               Pizza("Egenkomponert, Pepperoni, Biff, Bacon, Skinke, løk", 9, 159),
               Pizza("Egenkomponert, Biff, Pepperoni, Bacon, Skinke, Tacokjøtt", 9, 159)
    )

    val choices = menu.flatMap { pizza -> (1..pizza.weight).map { i -> pizza } }

    val numberOfPeople = args[0].toInt()
    val numberOfPizzasNeeded = Math.floor(numberOfPeople * MEAL_SIZE).toInt()

    val order = createOrder(choices, numberOfPizzasNeeded)

    showOrder(order)
}


fun drawPizza(choices: List<Pizza>): Pizza {
    val randomIndex = ThreadLocalRandom.current().nextInt(0, choices.size);
    return choices[ randomIndex ]
}


private fun createOrder(choices: List<Pizza>, numberOfPizzasNeeded: Int): List<Pair<Int, Pizza>> {
    var price = 0
    val order = generateSequence { drawPizza(choices) }
            .take(numberOfPizzasNeeded)
            .takeWhile { price += it.price; price <= PRICE_MAX }
            .groupBy { it }
            .map { p -> Pair(p.value.count(), p.key) }
    return order
}


private fun showOrder(order: List<Pair<Int, Pizza>>) {
    println("Antall\t\tNavn")
    println("-----------------------------")
    order.forEach { println("${it.first} x \t\t${it.second.name}") }
    println("")

    val total = order.fold(0.0) { acc, p -> acc + p.first * p.second.price }
    println("SUM: $total")
}