package net.engdy.melshelf.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.engdy.melshelf.model.RGBColor
import net.engdy.melshelf.model.RGBColorRepository

class RGBColorViewModel(private val repository: RGBColorRepository) : ViewModel() {
    val allColors: LiveData<List<RGBColor>> = repository.getAllColors().asLiveData()
    fun getColorById(id: Int): LiveData<RGBColor> = repository.getColorById(id).asLiveData()
    fun insert(color: RGBColor) = viewModelScope.launch {
        repository.insert(color)
    }
    fun update(color: RGBColor) = viewModelScope.launch {
        repository.update(color)
    }
    fun delete(color: RGBColor) = viewModelScope.launch {
        repository.delete(color)
    }
}

class RGBColorViewModelFactory(private val repository: RGBColorRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RGBColorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RGBColorViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}