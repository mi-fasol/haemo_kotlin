package com.example.haemo_kotlin.screen.setting.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.retrofit.post.HotPlaceResponsePostModel
import com.example.haemo_kotlin.model.system.navigation.NavigationRoutes
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.ui.theme.hotPlaceScreenTitle
import com.example.haemo_kotlin.ui.theme.myWishInfo
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.MyPageListAppBar
import com.example.haemo_kotlin.util.WishButton
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.boardInfo.WishViewModel

@Composable
fun MyWishHotPlaceScreen(
    wishViewModel: WishViewModel,
    mainViewModel: MainViewModel,
    navController: NavController
) {
    val post = wishViewModel.wishHotPlaceList.collectAsState().value
    val postState = wishViewModel.hotPlaceModelListState.collectAsState().value
    val mainColor by mainViewModel.colorState.collectAsState()

    LaunchedEffect(post) {
        wishViewModel.getWishHotPlace()
    }

    Scaffold(
        topBar = {
            MyPageListAppBar(mainColor, navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    bottom = innerPadding.calculateBottomPadding() + 10.dp
                )
        ) {
            Divider(thickness = 1.dp, color = colorResource(id = mainColor))
            when (postState) {
                is Resource.Error<List<HotPlaceResponsePostModel>> -> {
                    ErrorScreen("오류가 발생했습니다.\n잠시 후 다시 시도해 주세요.")
                }

                is Resource.Loading<List<HotPlaceResponsePostModel>> -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colorResource(id = mainColor))
                    }
                }

                else -> {
                    when (post.size) {
                        0 ->
                            Box(
                                Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                ErrorScreen("찜한 장소가 아직 없어요!")
                            }

                        else ->
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                            ) {
                                Text(
                                    "가고 싶은 모임",
                                    style = myWishInfo,
                                    color = colorResource(
                                        id = R.color.myBoardColor
                                    ),
                                    modifier = Modifier.padding(vertical = 15.dp)
                                )
                                Divider(thickness = 0.7.dp, color = Color(0xffbbbbbb))
                                MyWishHotPlaceList(
                                    post,
                                    mainColor,
                                    wishViewModel,
                                    navController
                                )
                            }
                    }
                }
            }
        }
    }
}

@Composable
fun MyWishHotPlaceList(
    postList: List<HotPlaceResponsePostModel>, mainColor: Int, viewModel: WishViewModel,
    navController: NavController
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 5.dp,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        content = {
            items(postList.size) { idx ->
                MyWishHotPlaceItem(postList[idx], viewModel, mainColor, navController)
            }
        }
    )
}

@Composable
fun MyWishHotPlaceItem(
    post: HotPlaceResponsePostModel,
    viewModel: WishViewModel,
    mainColor: Int,
    navController: NavController
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp

    Box(
        modifier = Modifier
            .height((screenHeight / 6).dp)
            .padding(top = 15.dp)
            .width((screenWidth / 3.5).dp)
            .clickable {
                navController.navigate(NavigationRoutes.HotPlacePostDetailScreen.createRoute(post.hpId))
            },
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
                    .background(Color.Black, RoundedCornerShape(15.dp))
                    .alpha(0.7f)
            )
        }
        Column(
            Modifier
                .padding(13.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            WishButton(
                post = null,
                clubPost = null,
                hotPlacePost = post,
                mainColor = mainColor,
                type = 3,
                wishViewModel = viewModel
            )
            Text(
                post.title,
                style = hotPlaceScreenTitle,
                color = Color.White
            )
        }
    }
}