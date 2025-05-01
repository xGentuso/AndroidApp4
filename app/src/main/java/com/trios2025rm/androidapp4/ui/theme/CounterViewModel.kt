package com.trios2025rm.androidapp4.ui.theme

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CounterViewModel : ViewModel() {
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    private val _maxValue = 100
    private val _minValue = 0

    fun increment() {
        if (_count.value < _maxValue) {
            _count.value++
        }
    }

    fun decrement() {
        if (_count.value > _minValue) {
            _count.value--
        }
    }

    fun reset() {
        _count.value = 0
    }

    fun isAtMax(): Boolean = _count.value >= _maxValue
    fun isAtMin(): Boolean = _count.value <= _minValue
} 