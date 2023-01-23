import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val shoppingList = mutableListOf(
    ShoppingListItem("Cucumbers", 1),
    ShoppingListItem("Tomatoes", 2),
    ShoppingListItem("Orange Juice", 3)
)

fun main() {
    embeddedServer(Netty, 9090) {
        // Each call to install adds one feature to the Ktor application:
        /*
        ContentNegotiation provides automatic content conversion of requests based
        on their Content-Type and Accept headers. Together with the json() setting,
        this enables automatic serialization and deserialization to and from JSON,
        allowing you to delegate this task to the framework.
         */
        install(ContentNegotiation) {
            json()
        }
        /*
        CORS configures Cross-Origin Resource Sharing. CORS is needed to make calls
        from arbitrary JavaScript clients and helps prevent issues later.
         */
        install(CORS) {
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Delete)
            anyHost()
        }
        /*
        Compression greatly reduces the amount of data to be sent to the client by
        gzipping outgoing content when applicable.
         */
        install(Compression) {
            gzip()
        }
        routing {
            /*
            Routes are grouped based on a common path. You don't have to specify
            the route path as a String. Instead, the path from the ShoppingListItem
            model is used.
             */
            route(ShoppingListItem.path) {
                // A get request to the model's path (/shoppingList) responds with the whole shopping list.
                get {
                    call.respond(shoppingList)
                }
                // A post request to the model's path (/shoppingList) adds an entry to the shopping list.
                post {
                    shoppingList += call.receive<ShoppingListItem>()
                    call.respond(HttpStatusCode.OK)
                }
                // A delete request to the model's path and a provided id (shoppingList/47) removes an entry from the shopping list.
                delete("/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                    shoppingList.removeIf { it.id == id }
                    call.respond(HttpStatusCode.OK)
                }
            }
            get("/hello") {
                call.respondText("Hello, API!")
            }
        }
    }.start(wait = true)
}
