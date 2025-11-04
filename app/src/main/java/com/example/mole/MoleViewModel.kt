package com.example.mole

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.IntSize
import kotlin.random.Random

class MoleViewModel : ViewModel() {

    // --- 遊戲狀態 ---
    var counter by mutableLongStateOf(0)
        private set

    var stay by mutableLongStateOf(0)
        private set

    var maxX by mutableStateOf(0)
        private set

    var maxY by mutableStateOf(0)
        private set

    var offsetX by mutableStateOf(0)
        private set
    var offsetY by mutableStateOf(0)
        private set

    init {
        startCounting()
    }

    /**
     * 啟動計時和地鼠移動的協程
     */
    private fun startCounting() {
        viewModelScope.launch {
            // 🚨 關鍵：60 秒停止遊戲條件
            while (stay < 60) { // 當 stay >= 60 時，迴圈和 moveMole() 將停止執行
                delay(1000L)
                stay++
                moveMole() // 隨機移動地鼠
            }
        }
    }

    /**
     * 點擊事件：增加分數 (只有在 stay < 60 時才允許加分)
     */
    fun incrementCounter() {
        if (stay < 60) {
            counter++
        }
    }

    /**
     * 隨機移動地鼠到新位置
     */
    fun moveMole() {
        if (maxX >= 0 && maxY >= 0) {
            offsetX = (0..maxX).random()
            offsetY = (0..maxY).random()
        }
    }

    /**
     * 根據螢幕寬度、高度及地鼠圖片大小，計算螢幕範圍
     */
    fun getArea(gameSize: IntSize, moleSize: Int) {
        maxX = gameSize.width - moleSize
        maxY = gameSize.height - moleSize

        if (offsetX == 0 && offsetY == 0) {
            moveMole()
        }
    }
}