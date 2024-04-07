package com.example.haemo_kotlin.screen.main

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.post.ClubPostModel
import com.example.haemo_kotlin.model.post.HotPlacePostModel
import com.example.haemo_kotlin.model.post.PostModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.MainBottomNavigation
import com.example.haemo_kotlin.util.MainPageAppBar
import com.example.haemo_kotlin.viewModel.HotPlacePostViewModel
import com.example.haemo_kotlin.viewModel.PostViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HotPlaceScreen(postViewModel: HotPlacePostViewModel, navController: NavController) {
    val postList = postViewModel.hotPlacePostList.collectAsState().value
    val popularPostList = postViewModel.popularHotPlace.collectAsState().value
    val postListState = postViewModel.hotPlacePostListState.collectAsState().value

    LaunchedEffect(postList) {
        postViewModel.getHotPlacePost()
    }
    LaunchedEffect(popularPostList) {
        postViewModel.getPopularHotPlace()
    }

    Scaffold(
        topBar = {
            MainPageAppBar("요즘 핫한 핫플레이스", navController)
        },
        bottomBar = { MainBottomNavigation(navController = navController) },
    ) { innerPadding ->
        BoxWithConstraints {
            Column(
                modifier = Modifier
                    .padding(
                        bottom = innerPadding.calculateBottomPadding() + 10.dp
                    )
            ) {
                Divider(thickness = 0.5.dp, color = Color(0xffbbbbbb))
                when (postListState) {
                    is Resource.Error<List<HotPlacePostModel>> -> {
                        ErrorScreen("오류가 발생했습니다.\n잠시 후 다시 시도해 주세요.")
                    }

                    is Resource.Loading<List<HotPlacePostModel>> -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    else -> {
                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                            PopularPlace(popularPostList, postViewModel)
                            HotPlaceBoard(postList = postList, viewModel = postViewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PopularPlace(postList: List<HotPlacePostModel>, viewModel: HotPlacePostViewModel) {
    when (postList.size) {
        0 -> {
            Box {}
        }

        else -> {
            Column(
                Modifier.padding(top = 15.dp, bottom = 10.dp)
            ) {
                Text(
                    "현재, 가장 인기있는 핫플",
                    fontSize = 15.sp,
                    color = Color(0xff414141),
                    fontWeight = FontWeight.SemiBold
                )
                LazyRow(
                ) {
                    items(postList.size) { idx ->
                        PopularPlaceItem(postList[idx], viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun PopularPlaceItem(post: HotPlacePostModel, viewModel: HotPlacePostViewModel) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    Row {
        Box(
            modifier = Modifier
                .height((screenHeight / 3.4).dp)
                .padding(top = 5.dp, bottom = 15.dp)
                .width((screenWidth / 2).dp)
                .background(Color.Unspecified, RoundedCornerShape(15.dp))
                .graphicsLayer(alpha = 0.8f),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Unspecified, RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(15.dp)),
                    elevation = 0.dp,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.dummy_image),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Unspecified, RoundedCornerShape(15.dp))
                    )
                }
                Column(
                    Modifier
                        .padding(13.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size((screenWidth / 20).dp)
                            .fillMaxWidth()
                            .align(Alignment.End)
                    )
                    Column(
                    ) {
                        Text(
                            post.title,
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            post.content,
                            color = Color.White,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
    }
}

@Composable
fun HotPlaceBoard(postList: List<HotPlacePostModel>, viewModel: HotPlacePostViewModel) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 5.dp,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth(),
        content = {
            items(postList.size) { idx ->
                HotPlaceBoardItem(postList[idx], viewModel)
            }
        }
    )
}

@Composable
fun HotPlaceBoardItem(post: HotPlacePostModel, viewModel: HotPlacePostViewModel) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    Box(
        modifier = Modifier
            .height((screenHeight / 5.5).dp)
            .padding(top = 5.dp, bottom = 15.dp)
            .width((screenWidth / 3.5).dp)
            .background(Color.Unspecified, RoundedCornerShape(15.dp))
            .graphicsLayer(alpha = 0.8f),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Unspecified, RoundedCornerShape(15.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(15.dp)),
                elevation = 0.dp,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dummy_image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Unspecified, RoundedCornerShape(15.dp))
                )
            }
            Column(
                Modifier
                    .padding(13.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size((screenWidth / 20).dp)
                        .fillMaxWidth()
                        .align(Alignment.End)
                )
                Text(
                    post.title,
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
