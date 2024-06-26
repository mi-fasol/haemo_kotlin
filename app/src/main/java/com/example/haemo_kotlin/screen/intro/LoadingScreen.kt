package com.example.haemo_kotlin.screen.intro

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.haemo_kotlin.MainActivity
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.system.navigation.NavigationRoutes
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.user.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(loginViewModel: LoginViewModel, mainViewModel: MainViewModel, navController: NavController) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        }

    val loginUser by loginViewModel.loginUser.collectAsState()
    val mainColor by mainViewModel.colorState.collectAsState()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.size((screenWidth / 2).dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.wont_icon),
                contentDescription = "",
                tint = colorResource(id = mainColor),
                modifier = Modifier
                    .weight(2f)
                    .fillMaxSize()
            )
            Icon(
                painter = painterResource(id = R.drawable.wont),
                contentDescription = "",
                tint = colorResource(id = mainColor),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            )
        }
    }

    LaunchedEffect(true) {
        loginViewModel.checkUserExists()
        delay(1500)
        when (loginUser) {
            LoginViewModel.LoginUserState.NONE -> {
                navController.navigate(NavigationRoutes.LoginScreen.route)
            }
            LoginViewModel.LoginUserState.LOGIN -> {
                navController.navigate(NavigationRoutes.RegisterScreen.route)
            }
            else -> {
                launcher.launch(Intent(context, MainActivity::class.java))
                (context as? ComponentActivity)?.finish()
            }
        }
    }
}