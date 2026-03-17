package com.lastregrets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lastregrets.ui.navigation.AppNavigation
import com.lastregrets.ui.theme.LastRegretsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as LastRegretsApp

        setContent {
            LastRegretsTheme {
                AppNavigation(
                    regretRepository = app.regretRepository,
                    todoRepository = app.todoRepository,
                    resonateStore = app.resonateStore
                )
            }
        }
    }
}
