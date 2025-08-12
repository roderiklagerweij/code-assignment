package com.rl.abnassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rl.abnassignment.navigation.AppNavigation
import com.rl.abnassignment.ui.theme.ABNAssignmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ABNAssignmentTheme {
                AppNavigation()
            }
        }
    }
}
