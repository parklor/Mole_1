package com.example.mole

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope // 引入 viewModelScope
import kotlinx.coroutines.delay // 引入 delay
import kotlinx.coroutines.launch // 引入 launch
import kotlin.random.Random
import androidx.compose.runtime.mutableStateOf // 引入 mutableStateOf
import androidx.lifecycle.viewmodel.compose.viewModel

class MoleViewModel : ViewModel() {

    // --- 遊戲狀態 ---
    var counter by mutableLongStateOf(0)
        private set

    // 遊戲時間狀態 (現在是計數，每秒 +1)
    var stay by mutableLongStateOf(0)
        private set

    // 地鼠的 X 和 Y 座標偏移 (為了讓 moveMole() 能正常運行，這裡也補齊)
    private var maxAreaWidth = 0
    private var maxAreaHeight = 0
    var offsetX by mutableStateOf(0)
        private set
    var offsetY by mutableStateOf(0)
        private set

    init {
        // 在 ViewModel 初始化時啟動計時協程
        startCounting()
    }

    /**
     * 啟動計時和地鼠移動的協程
     */
    private fun startCounting() {
        // 透過 viewModelScope.launch 啟動協程，
        // 確保當 ViewModel 被清除時 (onCleared)，這個協程會自動停止。
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
        moveMole()
    }

    /**
     * 隨機移動地鼠到新位置
     */
    private fun moveMole() {
        if (maxAreaWidth > 0 && maxAreaHeight > 0) {
            // 隨機生成新的 X 和 Y 偏移量
            offsetX = Random.nextInt(from = 0, until = maxAreaWidth)
            offsetY = Random.nextInt(from = 0, until = maxAreaHeight)
        }
    }

    // 為了讓 UI 傳入尺寸，必須保留這個函式
    fun getArea(intSize: androidx.compose.ui.unit.IntSize, moleSizePx: Int) {
        maxAreaWidth = intSize.width - moleSizePx
        maxAreaHeight = intSize.height - moleSizePx

        // 首次設定地鼠位置
        if (offsetX == 0 && offsetY == 0) {
            moveMole()
        }
    }
}
