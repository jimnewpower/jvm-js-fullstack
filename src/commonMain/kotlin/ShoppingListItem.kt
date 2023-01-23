import kotlinx.serialization.Serializable

/*
The @Serializable annotation comes from the multiplatform kotlinx.serialization
library, which allows you to define models directly in common code.

Once you use this serializable ShoppingListItem class from the JVM and JS
platforms, code for each platform will be generated. This code takes care of
serialization and deserialization.
 */
@Serializable
data class ShoppingListItem(val desc: String, val priority: Int) {
    /*
    This sample computes a simple id from the hashCode() of its description.
    In this case, that's enough, but when working with real data, it would be
    preferable to include tried and tested mechanisms to generate identifiers
    for your objects – from UUIDs to auto-incrementing IDs backed by the
    database of your choice.
     */
    val id: Int = desc.hashCode()

    /*
    The companion object stores additional information about the model − in this
    case, the path under which you will be able to access it in the API. By
    referring to this variable instead of defining routes and requests as strings,
    you can change the path to model operations. Any changes to the endpoint name
    only need to be made here - the client and server are adjusted automatically.
     */
    companion object {
        const val path = "/shoppingList"
    }
}
