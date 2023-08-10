package com.example.inventory.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch

class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {
    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    fun addNewItemEntry(itemName: String, itemPrice: String, itemQuantity: String) {
        val item =
            Item(name = itemName, price = itemPrice.toDouble(), quantity = itemQuantity.toInt())
        insertItem(item)
    }

    fun isEntryValid(itemName: String, itemPrice: String, itemQuantity: String): Boolean {
        return !(itemName.isBlank() || itemPrice.isBlank() || itemQuantity.isBlank())
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