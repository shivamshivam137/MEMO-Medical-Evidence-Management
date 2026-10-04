package com.memo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.memo.app.navigation.MemoNavGraph
import com.memo.app.ui.theme.MEMOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MEMOTheme {
                MemoNavGraph()
            }
        }
    }
}
