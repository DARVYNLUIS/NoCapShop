package com.edu.darvyn.nocap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.edu.darvyn.nocap.navigation.NoCapNavigation
import com.edu.darvyn.nocap.ui.theme.NoCapTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoCapTheme {
                val navController = rememberNavController()
                NoCapNavigation(navHostController = navController)
            }
        }
    }
}

