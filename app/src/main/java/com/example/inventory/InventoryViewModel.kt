package com.example.inventory

import com.example.inventory.data.Item
import androidx.lifecycle.*
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch

class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()
//this fun will insert the object to db
    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

//this fun will create the object
    private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String): Item {
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }
    //this function will check the valid
    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }
    //this fun will call previous functions to add new valid item to database
    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
        if(isEntryValid(itemName ,itemPrice ,  itemCount)) {
            val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
            insertItem(newItem)
        }
    }

}







class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}