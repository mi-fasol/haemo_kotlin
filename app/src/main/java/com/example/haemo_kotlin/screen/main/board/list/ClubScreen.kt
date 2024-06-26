package com.example.haemo_kotlin.screen.main.board.list

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.retrofit.post.ClubPostResponseModel
import com.example.haemo_kotlin.model.system.navigation.NavigationRoutes
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.ui.theme.clubScreenDescription
import com.example.haemo_kotlin.ui.theme.clubScreenTitle
import com.example.haemo_kotlin.ui.theme.meetingScreenTitle
import com.example.haemo_kotlin.ui.theme.postUserInfo
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.MainPageAppBar
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.board.ClubPostViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ClubScreen(
    postViewModel: ClubPostViewModel,
    mainViewModel: MainViewModel,
    navController: NavController
) {
    val postList: List<ClubPostResponseModel> = postViewModel.clubPostList.collectAsState().value
    var searchText by remember { mutableStateOf("") }
    var filteredPosts by remember { mutableStateOf(postList) }
    val postListState = postViewModel.clubPostListState.collectAsState().value
    val list = if (searchText.isNotBlank()) filteredPosts else postList
    val mainColor by mainViewModel.colorState.collectAsState()

    LaunchedEffect(Unit) {
        postViewModel.getClubPostList()
    }

    Scaffold(
        topBar = {
            MainPageAppBar("소모임/동아리 게시판", mainColor, navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            Divider(thickness = 0.5.dp, color = Color(0xffbbbbbb))
            when (postListState) {
                is Resource.Error<List<ClubPostResponseModel>> -> {
                    ErrorScreen("오류가 발생했습니다.\n잠시 후 다시 시도해 주세요.")
                }

                is Resource.Loading<List<ClubPostResponseModel>> -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = colorResource(id = mainColor)
                        )
                    }
                }

                else -> {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        SearchBarWidget(
                            value = searchText,
                            mainColor,
                            onValueChange = {
                                searchText = it
                                filteredPosts = postList.filter { post ->
                                    post.title.contains(it)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ClubBoard(
                            postList = list,
                            viewModel = mainViewModel,
                            mainColor,
                            navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBarWidget(
    value: String,
    mainColor: Int,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp

    Box(
        Modifier
            .padding(top = 20.dp)
            .background(Color(0xffededed), RoundedCornerShape(23.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp, horizontal = 10.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier
                    .width((screenWidth / 0.7).dp)
                    .padding(16.dp)
                    .weight(5f),
                singleLine = true,
                textStyle = meetingScreenTitle,
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(colorResource(mainColor), CircleShape)
                    .size((screenWidth / 15).dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.search_icon),
                    contentDescription = null,
                    modifier = Modifier.size((screenWidth / 32).dp)
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ClubBoard(
    postList: List<ClubPostResponseModel>,
    viewModel: MainViewModel,
    mainColor: Int,
    navController: NavController
) {
    LazyColumn(
    ) {
        items(postList.size) { idx ->
            ClubBoardItem(postList[idx], viewModel, mainColor, navController)
            Divider()
        }
    }
}

@Composable
fun ClubBoardItem(
    post: ClubPostResponseModel,
    viewModel: MainViewModel,
    mainColor: Int,
    navController: NavController
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp

    val painter =
        if (post.image != null) rememberAsyncImagePainter(model = post.image) else painterResource(
            id = R.drawable.dummy_image
        )

    Box(
        modifier = Modifier
            .height((screenHeight / 7).dp)
            .padding(vertical = 18.dp, horizontal = 3.dp)
            .clickable {
                viewModel.beforeStack.value = "clubScreen"
                navController.navigate(NavigationRoutes.ClubPostDetailScreen.createRoute(post.pId))
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                Modifier.weight(1f)
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size((screenWidth / 6).dp)
                        .clip(CircleShape)
                        .border(2.dp, colorResource(mainColor), CircleShape)
                )
            }
            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(3.5f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column() {
                    Text(
                        text = "상시 모집",
                        style = postUserInfo,
                        color = colorResource(mainColor)
                    )
                    Text(
                        text = post.title,
                        style = clubScreenTitle,
                        color = Color(0xff353535)
                    )
                }
                Column {
                    Text(
                        post.description,
                        style = clubScreenDescription,
                        color = Color(0xff414141)
                    )
                    LazyRow {
                        items(2) {
                            Row {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .height(10.dp)
                                        .background(
                                            Color(0xffededed),
                                            RoundedCornerShape(15.dp)
                                        )
                                        .padding(horizontal = 10.dp),
                                ) {
                                    Text(
                                        "#검도남",
                                        style = clubScreenDescription,
                                        color = Color(0xff717171)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
