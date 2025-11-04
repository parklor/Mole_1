package com.example.mole

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mole.ui.theme.MoleTheme
import kotlin.math.roundToInt

// MainActivity 是應用程式的進入點
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

// MoleScreen 是遊戲主畫面
@Composable
fun MoleScreen(
    modifier: Modifier = Modifier,
    // 引入兩個 ViewModel (圖34)
    moleViewModel: MoleViewModel = viewModel(),
    owlViewModel: OwlViewModel = viewModel()
) {
    // DP-to-pixel 轉換 (圖23)
    val density = LocalDensity.current
    val moleSizeDp = 150.dp
    val moleSizePx = with(density) { moleSizeDp.roundToPx() }

    // 從 MoleViewModel 讀取地鼠偏移量
    val moleOffsetX = moleViewModel.offsetX
    val moleOffsetY = moleViewModel.offsetY

    // 從 OwlViewModel 觀察貓頭鷹的位置 (圖34)
    val owlOffsetX = owlViewModel.owlOffsetX
    val owlOffsetY = owlViewModel.owlOffsetY

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { intSize ->
                moleViewModel.getArea(intSize, moleSizePx)
            },
        contentAlignment = Alignment.Center // 圖24
    ) {
        // 1. 分數/時間顯示 (圖24)
        Text(
            text = "打地鼠遊戲(羅婉薰)\n分數: ${moleViewModel.counter} \n時間: ${moleViewModel.stay}",
        )

        // 2. 貓頭鷹 Image (圖25 & 圖35)
        Image(
            painter = painterResource(id = R.drawable.owl),
            contentDescription = "貓頭鷹",
            modifier = Modifier
                .align(Alignment.TopStart)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        // 將拖曳更新任務委託給 OwlViewModel (圖35)
                        owlViewModel.owlDrag(
                            dragAmountX = dragAmount.x.toInt(),
                            dragAmountY = dragAmount.y.toInt()
                        )
                    }
                }
                // 應用 OwlViewModel 的偏移量
                .offset {
                    IntOffset(owlOffsetX, owlOffsetY)
                }
                .size(moleSizeDp / 2)
        )

        // 3. 地鼠 Image (圖24)
        Image(
            painter = painterResource(id = R.drawable.mole),
            contentDescription = "地鼠",
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset { IntOffset(moleOffsetX, moleOffsetY) }
                .size(moleSizeDp)
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