package com.example.haemo_kotlin.screen.main.board.detail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.retrofit.post.HotPlaceResponsePostModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.ui.theme.boardDetailContent
import com.example.haemo_kotlin.ui.theme.boardDetailTitle
import com.example.haemo_kotlin.util.CommentWidget
import com.example.haemo_kotlin.util.ConfirmDialog
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.PostDetailAppBar
import com.example.haemo_kotlin.util.PostManagementDialog
import com.example.haemo_kotlin.util.PostUserInfo
import com.example.haemo_kotlin.util.SendReply
import com.example.haemo_kotlin.util.YesOrNoDialog
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.board.HotPlacePostViewModel
import com.example.haemo_kotlin.viewModel.boardInfo.CommentViewModel
import com.example.haemo_kotlin.viewModel.boardInfo.WishViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState

@Composable
fun HotPlacePostDetailScreen(
    pId: Int,
    postViewModel: HotPlacePostViewModel,
    wishViewModel: WishViewModel,
    mainViewModel: MainViewModel,
    commentViewModel: CommentViewModel,
    navController: NavController
) {
    val post = postViewModel.hotPlaceModel.collectAsState().value
    val user = postViewModel.user.collectAsState().value
    val postState = postViewModel.hotPlacePostState.collectAsState().value
    val content = commentViewModel.content.collectAsState().value
    val isReply = commentViewModel.isReply.collectAsState().value
    val commentList = commentViewModel.commentList.collectAsState().value
    val beforeCId = commentViewModel.beforeCId.collectAsState().value
    val replyList = commentViewModel.replyList.collectAsState().value
    val repliedCId = commentViewModel.commentId.collectAsState().value
    val isWished = wishViewModel.isWished.collectAsState().value
    val mainColor by mainViewModel.colorState.collectAsState()
    var openDialog by remember {
        mutableStateOf(false)
    }
    val deleteState by postViewModel.hotPlacePostDeleteState.collectAsState()
    var askToDeleteDialog by remember { mutableStateOf(false) }
    var deleteCompleteDialog by remember { mutableStateOf(false) }
    var deleteFailDialog by remember { mutableStateOf(false) }
    var menuDialog by remember { mutableStateOf(false) }

    if (openDialog) {
        YesOrNoDialog(content = "답글 작성을 취소하시겠습니까?", mainColor, onClickCancel = {
            openDialog = false
        }) {
            commentViewModel.isReply.value = false
        }
    }

    if (menuDialog) {
        PostManagementDialog({ menuDialog = false }) {
            askToDeleteDialog = true
        }
    }

    if (askToDeleteDialog) {
        YesOrNoDialog(content = "게시물을 삭제하시겠습니까?", mainColor, onClickCancel = {
            askToDeleteDialog = false
        }) {
            postViewModel.deletePost(pId)
        }
    }

    if (deleteCompleteDialog) {
        ConfirmDialog(content = "삭제가 완료되었습니다.", mainColor = mainColor) {
            navController.popBackStack()
            mainViewModel.beforeStack.value = "clubScreen"
            deleteCompleteDialog = false
        }
    }

    if (deleteFailDialog) {
        ConfirmDialog(content = "실패했습니다.\n다시 시도해 주세요.", mainColor = mainColor) {
            mainViewModel.beforeStack.value = "clubScreen"
            deleteFailDialog = false
        }
    }

    LaunchedEffect(deleteState) {
        if (deleteState == true) deleteCompleteDialog = true
        else if (deleteState == false) deleteFailDialog = true
    }

    if (openDialog) {
        YesOrNoDialog(content = "답글 작성을 취소하시겠습니까?", mainColor = mainColor, onClickCancel = {
            openDialog = false
        }) {
            commentViewModel.isReply.value = false
        }
    }

    LaunchedEffect(isWished) {
        wishViewModel.checkIsWishedPost(pId, 3)
    }
    LaunchedEffect(commentList) {
        postViewModel.getOneHotPlacePost(pId)
        postViewModel.getHotPlacePostUser(pId)
        commentViewModel.getCommentListByPId(pId, 3)
    }

    LaunchedEffect(replyList) {
        commentViewModel.getReplyListByCId(repliedCId, 3)
        commentViewModel.getReplyUser(repliedCId, 3)
    }

    LaunchedEffect(isReply) {
        if (beforeCId != 0) {
            commentViewModel.getReplyUser(beforeCId, 3)
            commentViewModel.getReplyListByCId(beforeCId, 3)
        }
    }

    if (beforeCId != 0) {
        LaunchedEffect(replyList[beforeCId]) {
            commentViewModel.getReplyUser(beforeCId, 3)
            commentViewModel.getReplyListByCId(beforeCId, 3)
        }
    }

    Scaffold(
        topBar = {
            if (post != null && user != null) {
                PostDetailAppBar(
                    commentViewModel,
                    wishViewModel,
                    mainViewModel,
                    mainColor,
                    post.hpId,
                    3,
                    user,
                    navController
                ) {
                    menuDialog = true
                }
            }
        },
        bottomBar = {
            SendReply(
                isReply,
                postType = 3,
                pId = pId,
                mainColor = mainColor,
                value = content,
                commentViewModel = commentViewModel,
                onValueChange = { newValue ->
                    commentViewModel.content.value = newValue
                }) {
                commentViewModel.content.value = ""
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    bottom = innerPadding.calculateBottomPadding() + 10.dp
                )
        ) {
            Divider(thickness = 1.dp, color = colorResource(mainColor))
            when (postState) {
                is Resource.Error<HotPlaceResponsePostModel> -> {
                    ErrorScreen("오류가 발생했습니다.\n잠시 후 다시 시도해 주세요.")
                }

                is Resource.Loading<HotPlaceResponsePostModel> -> {
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
                    if (user != null && post != null) {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            PostUserInfo(user, post.date, navController)
                            HotPlacePostInfo(post)
                            Spacer(modifier = Modifier.height(15.dp))
                            Divider(thickness = 0.7.dp, color = Color(0xffbbbbbb))
                            CommentWidget(
                                type = 3,
                                pId = pId,
                                commentViewModel = commentViewModel,
                                mainColor = mainColor,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HotPlacePostInfo(post: HotPlaceResponsePostModel) {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp
    val screenWidth = config.screenWidthDp

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            post.title,
            style = boardDetailTitle,
            color = colorResource(
                id = R.color.mainTextColor
            )
        )
        Spacer(Modifier.height(5.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height((screenHeight / 3.5).dp)
                .clip(RoundedCornerShape(15.dp)),
            elevation = 0.dp,
        ) {
            if (post.imageList == null) {
                Log.d("미란", "조기")
                Image(
                    painter = painterResource(id = R.drawable.dummy_image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Unspecified, RoundedCornerShape(15.dp))
                )
            } else {
                Log.d("미란", "요기")
                ImageSlider(post.imageList)
            }
        }
        Box(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
        ) {
            Text(
                post.content,
                style = boardDetailContent,
                color = colorResource(id = R.color.mainTextColor),
                maxLines = Int.MAX_VALUE
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageSlider(images: List<String>) {
    val pagerState = rememberPagerState(initialPage = 0)

    HorizontalPager(
        count = images.size,
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        Card(
            shape = RoundedCornerShape(15.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = images[page]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        CustomPagerIndicator(
            pagerState = pagerState,
            activeColor = Color.White,
            inactiveColor = Color.Unspecified
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CustomPagerIndicator(
    pagerState: PagerState,
    activeColor: Color,
    inactiveColor: Color,
    indicatorSize: Dp = 7.dp,
    indicatorSpacing: Dp = 7.dp,
    indicatorShape: androidx.compose.ui.graphics.Shape = CircleShape
) {
    val indicatorModifier = Modifier.size(indicatorSize)
    Row(
        horizontalArrangement = Arrangement.spacedBy(indicatorSpacing),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 10.dp)
    ) {
        repeat(pagerState.pageCount) { pageIndex ->
            val color = if (pageIndex == pagerState.currentPage) activeColor else inactiveColor
            Box(
                modifier = indicatorModifier
                    .clip(indicatorShape)
                    .background(color)
                    .border(1.dp, Color.White, CircleShape)
            )
        }
    }
}