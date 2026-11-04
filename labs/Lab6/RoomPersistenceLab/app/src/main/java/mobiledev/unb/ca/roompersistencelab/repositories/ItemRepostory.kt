package mobiledev.unb.ca.roompersistencelab.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobiledev.unb.ca.roompersistencelab.db.dao.ItemDao
import mobiledev.unb.ca.roompersistencelab.db.entities.Item

/**
 * Repository class for interfacing with the Item records.  The functions included here
 * are one-shot suspend operations which enforce execution on the IO thread pool
 * @param itemDao ItemDao object to use
 * @param ioDispatcher Dispatcher object for use (defaults to Displatchers.IO)
 */
class ItemRepository(
    private val itemDao: ItemDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    // TODO
    //  Add function to insert a record
    //  Additional details can be found here - https://kotlinlang.org/docs/coroutines-basics.html#suspending-functions

    // TODO
    //  Add function to perform the search and return a list of Item objects
    //  Additional details can be found here - https://kotlinlang.org/docs/coroutines-basics.html#suspending-functions
}
