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

    /**
     * 根據拖曳更新貓頭鷹的位置
     * 參數直接接受 Int，與 Composable 中 .toInt() 呼叫匹配。
     */
    fun owlDrag(dragAmountX: Int, dragAmountY: Int) {
        owlOffsetX += dragAmountX
        owlOffsetY += dragAmountY
    }
}