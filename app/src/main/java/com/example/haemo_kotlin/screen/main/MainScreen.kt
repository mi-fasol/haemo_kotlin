package com.example.haemo_kotlin.screen.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.haemo_kotlin.util.MainBottomNavigation
import com.example.haemo_kotlin.viewModel.PostViewModel


@Composable
fun MainScreen(navController: NavController, postViewModel: PostViewModel) {
    Scaffold(
        bottomBar = { MainBottomNavigation(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MeetingScreen(postViewModel = postViewModel, navController = navController)
        }
    }
}