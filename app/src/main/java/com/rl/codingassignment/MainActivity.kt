package com.rl.codingassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rl.codingassignment.presentation.AppNavigation
import com.rl.codingassignment.ui.theme.CodingAssignmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CodingAssignmentTheme {
                AppNavigation()
            }
        }
    }
}
