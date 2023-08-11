package com.example.inventory.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch

class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    val allItems: LiveData<List<Item>> = itemDao.getAll().asLiveData()

    fun retrieveItemById(id: Int): LiveData<Item> = itemDao.getById(id).asLiveData()

    fun addNewItemEntry(itemName: String, itemPrice: String, itemQuantity: String) {
        val item = createItem(itemName, itemPrice, itemQuantity)
        insertItem(item)
    }

    fun isEntryValid(itemName: String, itemPrice: String, itemQuantity: String): Boolean {
        return !(itemName.isBlank() || itemPrice.isBlank() || itemQuantity.isBlank())
    }

    fun sellItem(item: Item) {
        if (item.quantity > 0) {
            val newItem = item.copy(quantity = item.quantity - 1)
            updateItem(newItem)
        }
    }

    fun editItem(id: Int, itemName: String, itemPrice: String, itemQuantity: String) {
        val editedItem = createItem(itemName, itemPrice, itemQuantity, id)
        updateItem(editedItem)
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }

    fun isStockAvailable(item: Item) = item.quantity > 0

    private fun createItem(itemName: String, itemPrice: String, itemQuantity: String, id: Int = 0) =
        Item(id, itemName, itemPrice.toDouble(), itemQuantity.toInt())

    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    private fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.update(item)
        }
    }
}

class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}