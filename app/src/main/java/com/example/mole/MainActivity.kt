package com.example.mole

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mole.ui.theme.MoleTheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.setValue
import kotlin.math.roundToInt
// MainActivity 是應用程式的進入點
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 為了最精確的修正，我們將內邊距應用到 MoleScreen 上
                    MoleScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// MoleScreen 是遊戲主畫面
@Composable
fun MoleScreen(
    modifier: Modifier = Modifier,
    moleViewModel: MoleViewModel = viewModel()
) {
    // 尺寸計算
    val density = LocalDensity.current
    val moleSizeDp = 150.dp
    val moleSizePx = with(density) { moleSizeDp.roundToPx() }

    // 從 ViewModel 讀取響應式狀態
    val currentCounter = moleViewModel.counter
    val currentTime = moleViewModel.stay

    var owlOffsetX by remember { mutableStateOf(100) }
    var owlOffsetY by remember { mutableStateOf(400) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { intSize ->  // 獲取 Box 的尺寸
                // 傳遞尺寸給 ViewModel，用於計算地鼠移動邊界
                moleViewModel.getArea(intSize, moleSizePx)
            },
        Alignment.TopCenter // 將內容預設對齊頂部中央
    ) {
        // 1. 分數/時間顯示
        Text(
            text = "分數: $currentCounter \n時間: ${currentTime}",
            modifier = Modifier.padding(24.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.owl),
            contentDescription = "貓頭鷹",
            modifier = Modifier
                .offset { IntOffset(owlOffsetX,owlOffsetY) }
                .size(moleSizeDp/2)
        )


        // 2. 地鼠 Image
        Image(
            painter = painterResource(id = R.drawable.mole),
            contentDescription = "地鼠",
            modifier = Modifier
                // 將地鼠對齊 Box 的左上角
                .align(Alignment.TopStart)
                // 應用 ViewModel 計算出的偏移量
                .offset { IntOffset(moleViewModel.offsetX, moleViewModel.offsetY) }
                .size(moleSizeDp)
                // 點擊時呼叫 ViewModel 邏輯
                .clickable { moleViewModel.incrementCounter() }
        )
    }
}

// 預覽功能
@Preview(showBackground = true)
@Composable
fun MoleScreenPreview() {
    MoleTheme {
        MoleScreen()
    }
}