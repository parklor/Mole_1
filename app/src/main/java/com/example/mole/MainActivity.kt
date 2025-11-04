package com.example.mole

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mole.ui.theme.MoleTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MoleScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MoleScreen(
    modifier: Modifier = Modifier,
    moleViewModel: MoleViewModel = viewModel(),
    owlViewModel: OwlViewModel = viewModel()
) {
    // DP-to-pixel 轉換
    val density = LocalDensity.current
    val moleSizeDp = 150.dp
    val moleSizePx = with(density) { moleSizeDp.roundToPx() }

    // 從 ViewModel 讀取狀態
    val moleOffsetX = moleViewModel.offsetX
    val moleOffsetY = moleViewModel.offsetY
    val owlOffsetX = owlViewModel.owlOffsetX
    val owlOffsetY = owlViewModel.owlOffsetY

    // 讓 Box 佔滿整個螢幕，但不設置 contentAlignment
    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { intSize -> // 用來獲取全螢幕尺寸px
                // 這是關鍵：將整個螢幕尺寸傳遞給 ViewModel 計算移動範圍
                moleViewModel.getArea(intSize, moleSizePx)
            }
    ) {

        // 🚨 關鍵修改：使用 Column 包含文字，並將 Column 置於 Box 的中央
        Column(
            modifier = Modifier
                .align(Alignment.Center) // 確保 Column (包含文字) 垂直置中
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally // 確保文字在 Column 中水平置中
        ) {
            // 1. 分數/時間顯示 (現在會在畫面的垂直中間區域)
            Text(
                text = if (moleViewModel.stay >= 60) {
                    // 遊戲結束時的文字 (符合圖片908429的要求)
                    "打地鼠遊戲(羅婉薰)\n分數: ${moleViewModel.counter} \n時間: 60"
                } else {
                    // 遊戲進行中的文字
                    "打地鼠遊戲(羅婉薰)\n分數: ${moleViewModel.counter} \n時間: ${moleViewModel.stay}"
                },
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp) // 與下方的圖案區隔
            )
        }

        // 2. 貓頭鷹 Image
        // 🚨 注意：這裡保留了 TopStart 對齊，以確保 (0,0) 座標位於螢幕左上角，
        // 這樣貓頭鷹的初始位置 (100, 400) 和地鼠的移動範圍才能覆蓋整個螢幕。
        Image(
            painter = painterResource(id = R.drawable.owl),
            contentDescription = "貓頭鷹",
            modifier = Modifier
                .align(Alignment.TopStart) // 保持 (0,0) 在左上角
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount -> // 拖曳事件發生時
                        change.consume()
                        owlViewModel.owlDrag( // 委託給 ViewModel
                            dragAmountX = dragAmount.x.toInt(),
                            dragAmountY = dragAmount.y.toInt()
                        )
                    }
                }
                .offset {
                    IntOffset(owlOffsetX, owlOffsetY) // 應用 ViewModel 的偏移量 (初始為 100, 400)
                }
                .size(moleSizeDp / 2) // 圖片大小為地鼠的一半
        )

        // 3. 地鼠 Image
        Image(
            painter = painterResource(id = R.drawable.mole),
            contentDescription = "地鼠",
            modifier = Modifier
                .align(Alignment.TopStart) // 保持 (0,0) 在左上角
                .offset { IntOffset(moleOffsetX, moleOffsetY) } // 應用 ViewModel 的隨機偏移量
                .size(moleSizeDp)
                .clickable { moleViewModel.incrementCounter() } // 點擊呼叫 ViewModel
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MoleScreenPreview() {
    MoleTheme {
        MoleScreen()
    }
}