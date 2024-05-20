package com.example.haemo_kotlin.screen.setting

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.util.NavigationRoutes
import com.example.haemo_kotlin.util.SettingScreenAppBar
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.google.firebase.database.collection.BuildConfig

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SettingScreen(mainViewModel: MainViewModel, navController: NavController) {
    val context = LocalContext.current
    val mainColor = SharedPreferenceUtil(context).getInt("themeColor", R.color.mainColor)

    Scaffold(
        topBar = {
            SettingScreenAppBar("설정", mainColor, navController = navController)
        },
        modifier = Modifier.background(Color(0xfff4f4f4))
    ) { innerPadding ->
        BoxWithConstraints {
            Column(
                modifier = Modifier
                    .padding(
                        bottom = innerPadding.calculateBottomPadding()
                    )
            ) {
                Divider(thickness = 0.5.dp, color = Color(0xffbbbbbb))

                Column(
                    //    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    AccountSettingField(mainColor, navController)
                    AppSettingField(mainColor, navController)
                    AppInfoField(mainColor = mainColor, mainViewModel, navController = navController)
                }
            }
        }
    }
}

@Composable
fun AccountSettingField(mainColor: Int, navController: NavController) {
    val textList = listOf("로그아웃", "계정 탈퇴")
    val navRoutes = listOf(
        NavigationRoutes.ThemeChangeScreen.route,
        NavigationRoutes.ThemeChangeScreen.route
    )

    Column() {
        SettingTitleField(text = "계정 관리")
        textList.forEach { text ->
            SettingContentField(
                text = text,
                mainColor = mainColor,
                route = navRoutes[textList.indexOf(text)],
                navController = navController
            )
            if (textList.indexOf(text) < textList.size - 1)
                Divider(
                    thickness = 2.5.dp,
                    color = colorResource(id = R.color.settingScreenBackgroundColor)
                )
        }
    }
}

@Composable
fun AppSettingField(mainColor: Int, navController: NavController) {
    val textList = listOf("알림 설정", "화면 설정")
    val navRoutes = listOf(
        NavigationRoutes.ThemeChangeScreen.route,
        NavigationRoutes.ThemeChangeScreen.route
    )

    Column() {
        SettingTitleField(text = "앱 설정")
        textList.forEach { text ->
            SettingContentField(
                text = text,
                mainColor = mainColor,
                route = navRoutes[textList.indexOf(text)],
                navController = navController
            )
            if (textList.indexOf(text) < textList.size - 1)
                Divider(
                    thickness = 2.5.dp,
                    color = colorResource(id = R.color.settingScreenBackgroundColor)
                )
        }
    }
}

@Composable
fun AppInfoField(mainColor: Int, mainViewModel: MainViewModel, navController: NavController) {
    val textList = listOf("문의하기", "공지사항")
    val navRoutes = listOf(
        NavigationRoutes.ThemeChangeScreen.route,
        NavigationRoutes.ThemeChangeScreen.route
    )

    Column() {
        SettingTitleField(text = "앱 정보")
        AppVersionField(mainColor, mainViewModel)
        Divider(
            thickness = 2.5.dp,
            color = colorResource(id = R.color.settingScreenBackgroundColor)
        )
        textList.forEach { text ->
            SettingContentField(
                text = text,
                mainColor = mainColor,
                route = navRoutes[textList.indexOf(text)],
                navController = navController
            )
            if (textList.indexOf(text) < textList.size - 1)
                Divider(
                    thickness = 2.5.dp,
                    color = colorResource(id = R.color.settingScreenBackgroundColor)
                )
        }
    }
}

@Composable
fun SettingTitleField(text: String) {
    val conf = LocalConfiguration.current
    val screenHeight = conf.screenHeightDp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height((screenHeight / 17).dp)
            .background(colorResource(id = R.color.settingScreenBackgroundColor)),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text,
            color = colorResource(id = R.color.settingScreenTitleTextColor),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}

@Composable
fun SettingContentField(text: String, mainColor: Int, route: String, navController: NavController) {
    val conf = LocalConfiguration.current
    val screenHeight = conf.screenHeightDp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(route)
            }
            .height((screenHeight / 17).dp)
            .background(Color.White),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text,
                color = colorResource(id = R.color.settingScreenContentTextColor),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(12f),
                fontSize = 16.sp
            )
            IconButton(
                onClick = { navController.navigate(route) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = colorResource(
                        id = mainColor
                    )
                )
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun AppVersionField(mainColor: Int, mainViewModel: MainViewModel) {
    val conf = LocalConfiguration.current
    val screenHeight = conf.screenHeightDp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height((screenHeight / 17).dp)
            .background(Color.White),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "앱 버전",
                color = colorResource(id = R.color.settingScreenContentTextColor),
                modifier = Modifier
                    .padding(start = 10.dp),
                fontSize = 16.sp
            )
            Text(
                mainViewModel.getVersion().toString(),
                color = colorResource(id = mainColor),
                modifier = Modifier
                    .padding(start = 10.dp),
                fontSize = 16.sp
            )
        }
    }
}