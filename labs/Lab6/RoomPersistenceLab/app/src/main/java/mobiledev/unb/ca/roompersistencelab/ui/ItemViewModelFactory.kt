package mobiledev.unb.ca.roompersistencelab.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mobiledev.unb.ca.roompersistencelab.repositories.ItemRepository

/**
 * by viewModels() requires a factory to be able to instantiate any required ViewModel attributes.
 * In this case we need the ItemRepository.
 */
class ItemViewModelFactory(private val itemRepository: ItemRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            return ItemViewModel(itemRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}