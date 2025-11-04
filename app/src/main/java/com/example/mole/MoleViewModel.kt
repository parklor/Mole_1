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

    // 遊戲時間狀態
    var stay by mutableLongStateOf(0)
        private set

    // 螢幕範圍的最大 X 座標 (圖20)
    var maxX by mutableStateOf(0)
        private set

    // 螢幕範圍的最大 Y 座標 (圖20)
    var maxY by mutableStateOf(0)
        private set

    // 地鼠的 X 和 Y 座標偏移 (圖21)
    var offsetX by mutableStateOf(0)
        private set
    var offsetY by mutableStateOf(0)
        private set

    init {
        // 在 ViewModel 初始化時啟動計時協程 (圖22)
        startCounting()
    }

    /**
     * 啟動計時和地鼠移動的協程 (圖22)
     */
    private fun startCounting() {
        viewModelScope.launch {
            while (true) {
                delay(1000L) // 延遲 1 秒
                stay++ // 計數器加 1 (時間遞增)
                moveMole() // 每秒移動地鼠
            }
        }
    }

    /**
     * 點擊事件：增加分數並隨機移動地鼠
     */
    fun incrementCounter() {
        counter++
    }

    /**
     * 隨機移動地鼠到新位置 (圖21)
     */
    fun moveMole() {
        if (maxX >= 0 && maxY >= 0) {
            // 使用範圍函式 (0..max).random()
            offsetX = (0..maxX).random()
            offsetY = (0..maxY).random()
        }
    }

    /**
     * 根據螢幕寬度、高度及地鼠圖片大小，計算螢幕範圍 (圖20)
     */
    fun getArea(gameSize: IntSize, moleSize: Int) {
        // 計算地鼠可移動的最大 X 和 Y 座標
        maxX = gameSize.width - moleSize
        maxY = gameSize.height - moleSize

        // 首次設定地鼠位置
        if (offsetX == 0 && offsetY == 0) {
            moveMole()
        }
    }
}