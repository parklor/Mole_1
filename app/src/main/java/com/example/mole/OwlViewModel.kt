package com.example.mole

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class OwlViewModel : ViewModel() {

    // 貓頭鷹的初始 X, Y 座標
    var owlOffsetX by mutableStateOf(100)
        private set
    var owlOffsetY by mutableStateOf(400)
        private set

    fun owlDrag(dragAmountX: Int, dragAmountY: Int) {
        owlOffsetX += dragAmountX
        owlOffsetY += dragAmountY
    }
}