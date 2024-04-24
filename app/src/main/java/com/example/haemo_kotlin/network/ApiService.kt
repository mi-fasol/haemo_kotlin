package com.example.haemo_kotlin.network

import com.example.haemo_kotlin.model.acceptation.AcceptationResponseModel
import com.example.haemo_kotlin.model.comment.comment.CommentModel
import com.example.haemo_kotlin.model.comment.comment.CommentResponseModel
import com.example.haemo_kotlin.model.comment.reply.ReplyModel
import com.example.haemo_kotlin.model.comment.reply.ReplyResponseModel
import com.example.haemo_kotlin.model.post.ClubPostModel
import com.example.haemo_kotlin.model.post.ClubPostResponseModel
import com.example.haemo_kotlin.model.post.HotPlacePostModel
import com.example.haemo_kotlin.model.post.HotPlaceResponsePostModel
import com.example.haemo_kotlin.model.post.PostModel
import com.example.haemo_kotlin.model.post.PostResponseModel
import com.example.haemo_kotlin.model.user.LoginModel
import com.example.haemo_kotlin.model.user.UserModel
import com.example.haemo_kotlin.model.user.UserResponseModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    // 게시물
    @POST("post")
    suspend fun registerPost(@Body post: PostModel): Response<PostResponseModel>

    @GET("post")
    suspend fun getPost(): Response<List<PostResponseModel>>

    @GET("post/{id}")
    suspend fun getOnePost(@Path("id") idx: Int): Response<PostResponseModel>

    @GET("post/postUser/{id}")
    suspend fun getPostingUser(@Path("id") idx: Int): Response<UserResponseModel>

    @GET("post/24hours")
    suspend fun getTodayPost(): Response<List<PostResponseModel>>

    // 유저

    @POST("login")
    suspend fun tryLogin(@Body loginModel: LoginModel): Response<Boolean>

    @POST("user")
    suspend fun registerUser(@Body user: UserModel): Response<UserResponseModel>

    @GET("user/find/{id}")
    suspend fun getUserInfoById(@Path("id") uId: Int): Response<UserResponseModel>

    @GET("user/{nickname}")
    suspend fun getUserByNickname(@Path("nickname") nickname: String): Response<UserResponseModel>

    // 소모임

    @POST("club")
    suspend fun registerClubPost(@Body post: ClubPostModel): Response<ClubPostResponseModel>

    @GET("club")
    suspend fun getClubPost(): Response<List<ClubPostResponseModel>>

    @GET("club/{id}")
    suspend fun getClubPostById(@Path("id") idx: Int): Response<ClubPostResponseModel>

    @GET("club/clubPostUser/{id}")
    suspend fun getClubPostingUser(@Path("id") idx: Int): Response<UserResponseModel>

    // 핫플레이스

    @POST("hot")
    suspend fun registerHotPlacePost(@Body post: HotPlacePostModel): Response<HotPlaceResponsePostModel>


    @GET("hot")
    suspend fun getHotPlacePost(): Response<List<HotPlaceResponsePostModel>>

    @GET("hot/{id}")
    suspend fun getHotPlacePostById(@Path("id") idx: Int): Response<HotPlaceResponsePostModel>

    @GET("hot/popular")
    suspend fun getPopularHotPlacePost(): Response<List<HotPlaceResponsePostModel>>

    @GET("hot/hotPlaceUser/{id}")
    suspend fun getHotPlacePostingUser(@Path("id") idx: Int): Response<UserResponseModel>

    // 모임 참여

    @GET("accept/{id}")
    suspend fun getJoinUserByPId(@Path("id") pId: Int): Response<List<AcceptationResponseModel>>

    // 댓글 가져오기
    @GET("postComment/commentPost/{id}")
    suspend fun getCommentListByPId(@Path("id") pId: Int): Response<List<CommentResponseModel>>

    @GET("clubComment/commentPost/{id}")
    suspend fun getClubCommentListByPId(@Path("id") pId: Int): Response<List<CommentResponseModel>>

    @GET("hotComment/commentPost/{id}")
    suspend fun getHotPlaceCommentListByPId(@Path("id") pId: Int): Response<List<CommentResponseModel>>

    // 댓글 작성 유저 가져오기
    @GET("postComment/commentUser/{id}")
    suspend fun getCommentUserList(@Path("id") pId: Int): Response<List<UserResponseModel>>

    @GET("clubComment/commentUser/{id}")
    suspend fun getClubCommentUserList(@Path("id") pId: Int): Response<List<UserResponseModel>>

    @GET("hotComment/commentUser/{id}")
    suspend fun getHotPlaceCommentUserList(@Path("id") pId: Int): Response<List<UserResponseModel>>

    // 댓글 작성
    @POST("postComment")
    suspend fun registerPostComment(@Body comment: CommentModel): Response<CommentResponseModel>

    @POST("clubComment")
    suspend fun registerClubComment(@Body comment: CommentModel): Response<CommentResponseModel>

    @POST("hotComment")
    suspend fun registerHotPlaceComment(@Body comment: CommentModel): Response<CommentResponseModel>

    // 대댓글 작성
    @POST("postReply")
    suspend fun registerPostReply(@Body reply: ReplyModel): Response<ReplyResponseModel>

    @POST("clubReply")
    suspend fun registerClubPostReply(@Body reply: ReplyModel): Response<ReplyResponseModel>

    @POST("hotRely")
    suspend fun registerHotPlacePostReply(@Body reply: ReplyModel): Response<ReplyResponseModel>

    // 대댓글 가져오기
    @GET("postReply/find/{id}")
    suspend fun getReplyByCId(@Path("id") pId: Int): Response<List<ReplyResponseModel>>

    @GET("clubReply/find/{id}")
    suspend fun getClubReplyByCId(@Path("id") pId: Int): Response<List<ReplyResponseModel>>

    @GET("hotReply/find/{id}")
    suspend fun getHotPlaceReplyByCId(@Path("id") pId: Int): Response<List<ReplyResponseModel>>

    // 대댓글 작성 유저
    @GET("postReply/replyUser/{id}")
    suspend fun getReplyUserListByCId(@Path("id") pId: Int): Response<List<UserResponseModel>>

    @GET("clubReply/replyUser/{id}")
    suspend fun getClubReplyUserListByCId(@Path("id") pId: Int): Response<List<UserResponseModel>>

    @GET("hotReply/replyUser/{id}")
    suspend fun getHotPlaceReplyUserListByCId(@Path("id") pId: Int): Response<List<UserResponseModel>>


    // wish

    @GET("wishMeeting/myList/{id}")
    suspend fun getWishMeetingPost(@Path("id") uId: Int): Response<List<PostResponseModel>>

    @GET("wishClub/myList/{id}")
    suspend fun getWishClubPost(@Path("id") uId: Int): Response<List<ClubPostResponseModel>>

    @GET("wish/myList/{id}")
    suspend fun getWishHotPlacePost(@Path("id") uId: Int): Response<List<HotPlaceResponsePostModel>>

    // 이미지
    @Multipart
    @POST("image")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<String>

    @Multipart
    @POST("image/list")
    suspend fun uploadImageList(@Part files: List<MultipartBody.Part>): Response<List<String>>
}