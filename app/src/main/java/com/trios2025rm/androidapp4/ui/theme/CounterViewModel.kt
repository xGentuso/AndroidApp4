package com.trios2025rm.androidapp4.ui.theme

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trios2025rm.androidapp4.data.DataStoreManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

data class CounterHistoryItem(
    val value: Int,
    val timestamp: Long = System.currentTimeMillis()
)

class CounterViewModel(context: Context) : ViewModel() {
    private val dataStoreManager = DataStoreManager(context)
    
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    private val _minValue = MutableStateFlow(-100)
    val minValue: StateFlow<Int> = _minValue.asStateFlow()

    private val _maxValue = MutableStateFlow(100)
    val maxValue: StateFlow<Int> = _maxValue.asStateFlow()

    private val _history = MutableStateFlow<List<CounterHistoryItem>>(emptyList())
    val history: StateFlow<List<CounterHistoryItem>> = _history.asStateFlow()

    init {
        viewModelScope.launch {
            // Load saved values
            dataStoreManager.counterValue.collect { savedCount ->
                _count.value = savedCount
            }
        }
        viewModelScope.launch {
            dataStoreManager.minValue.collect { savedMin ->
                _minValue.value = savedMin
            }
        }
        viewModelScope.launch {
            dataStoreManager.maxValue.collect { savedMax ->
                _maxValue.value = savedMax
            }
        }
    }

    fun increment() {
        if (!isAtMax()) {
            val newValue = _count.value + 1
            _count.value = newValue
            addToHistory(newValue)
            viewModelScope.launch {
                dataStoreManager.saveCounterValue(newValue)
            }
        }
    }

    fun decrement() {
        if (!isAtMin()) {
            val newValue = _count.value - 1
            _count.value = newValue
            addToHistory(newValue)
            viewModelScope.launch {
                dataStoreManager.saveCounterValue(newValue)
            }
        }
    }

    fun reset() {
        _count.value = 0
        addToHistory(0)
        viewModelScope.launch {
            dataStoreManager.saveCounterValue(0)
        }
    }

    fun setMinValue(value: Int) {
        _minValue.value = value
        viewModelScope.launch {
            dataStoreManager.saveMinValue(value)
        }
    }

    fun setMaxValue(value: Int) {
        _maxValue.value = value
        viewModelScope.launch {
            dataStoreManager.saveMaxValue(value)
        }
    }

    fun isAtMin(): Boolean = _count.value <= _minValue.value
    fun isAtMax(): Boolean = _count.value >= _maxValue.value

    private fun addToHistory(value: Int) {
        val newHistory = _history.value.toMutableList()
        newHistory.add(CounterHistoryItem(value))
        _history.value = newHistory
    }

    fun clearHistory() {
        _history.value = emptyList()
    }
} 