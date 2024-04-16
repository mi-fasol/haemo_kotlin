package com.example.haemo_kotlin.screen.main.board

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.haemo_kotlin.model.post.PostResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.*
import com.example.haemo_kotlin.viewModel.PostViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@Composable
fun PostRegisterScreen(viewModel: PostViewModel, navController: NavController) {

    val title = viewModel.title.collectAsState().value
    val content = viewModel.content.collectAsState().value
    val postRegisterState = viewModel.postRegisterState.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(postRegisterState) {
        when (postRegisterState) {
            is Resource.Success<PostResponseModel> -> {
                navController.popBackStack()
            }

            else -> {

            }
        }
    }

    Box(
        modifier = Modifier.background(Color.White)
    ) {
        Column {
            PostRegisterAppBar("모임 등록", navController)
            Column(modifier = Modifier.padding(horizontal = 40.dp)) {
                TextEnterField("모임", title) {
                    viewModel.title.value = it
                }
                PostInfo(viewModel)
                ContentEnterField(value = content) {
                    if (it.length <= 300) {
                        viewModel.content.value = it
                    }
                }
                PostRegisterButton(viewModel, null, null, 1, navController) {
                    viewModel.registerPost(context)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostInfo(viewModel: PostViewModel) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp

    Column {
        Row(
            Modifier.width((screenWidth * 0.65).dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PostSelectDropDownMenu(
                "0명",
                list = personList,
                modifier = Modifier
                    .weight(1f)
                    .width((screenWidth / 5).dp)
            ) {
                viewModel.person.value = personList.indexOf(it) + 1
            }
            PostSelectDropDownMenu(
                "모임 카테고리",
                list = categoryList,
                modifier = Modifier
                    .weight(1.5f)
                    .width((screenWidth / 3.5).dp)
            ) {
                viewModel.category.value = it
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PostSelectDropDownMenu(
                "2024년",
                list = yearList,
                modifier = Modifier
                    .width((screenWidth / 5.5).dp)
            ) {
                viewModel.deadlineYear.value = it
            }
            PostSelectDropDownMenu(
                "1월",
                list = monthList,
                modifier = Modifier.width((screenWidth / 8).dp)
            ) {
                viewModel.deadlineMonth.value = it
            }

            PostSelectDropDownMenu(
                "1일",
                list = dayList,
                modifier = Modifier.width((screenWidth / 8).dp)
            ) {
                viewModel.deadlineDay.value = it
            }

            PostSelectDropDownMenu(
                "01시",
                list = hourList,
                modifier = Modifier.width((screenWidth / 8).dp)
            ) {
                viewModel.deadlineTime.value = it
            }
        }
    }
}