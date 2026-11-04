package mobiledev.unb.ca.roompersistencelab.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mobiledev.unb.ca.roompersistencelab.db.entities.Item
import mobiledev.unb.ca.roompersistencelab.repositories.ItemRepository

class ItemViewModel(private val itemRepository: ItemRepository) : ViewModel() {
    // Expose the search results.  This will be observed for change in the main activity and
    // made up of two parts: a private backing property used by the ViewModel only
    // and a public state property used by the activity

    // Backing property for internal changes
    private val _items = MutableStateFlow<List<Item>>(emptyList())

    // Read-only property exposed to the UI or collectors
    val searchItems: StateFlow<List<Item>> = _items.asStateFlow()

    // TODO
    //  Add mapping calls between the UI and Database for inserting a record

    //  Add mapping call between the UI and Database for searching the records
    fun findItemsWithName(name: String) {
        viewModelScope.launch {
            // TODO
            //  Add mapping call between the UI and Database for searching the records

            // TODO
            //  Update the internal state (Thread-Safe)
            //  HINT: In Coroutines you can modify the value of a MutableStateFlow using
            //  either the .update { } extension function (preferred)
            //  or by directly assigning a value to the .value property
        }
    }

    /**
     * Function used to reset the observable backing property
     */
    fun resetSearchItemsList() {
        // NOTE: As this is updating the private value only with no database call
        //  it does not need to run inside a viewModelScope.
        _items.update{ emptyList() }
    }
}