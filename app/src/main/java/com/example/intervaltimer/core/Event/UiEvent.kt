package com.example.intervaltimer.core.Event

sealed class UiEvent {
    data class ShowToast(val message: Int): UiEvent()
}