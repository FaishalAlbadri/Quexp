package com.bintang.quexp.api

import com.bintang.quexp.data.BaseResponse
import com.bintang.quexp.data.app.HomeResponse
import com.bintang.quexp.data.awards.AwardsResponse
import com.bintang.quexp.data.banner.BannerResponse
import com.bintang.quexp.data.category.CategoryResponse
import com.bintang.quexp.data.visited.VisitedResponse
import com.bintang.quexp.data.news.NewsResponse
import com.bintang.quexp.data.notification.NotificationResponse
import com.bintang.quexp.data.places.PlacesResponse
import com.bintang.quexp.data.roadmap.RoadmapResponse
import com.bintang.quexp.data.user.UserResponse
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PartMap
import java.util.concurrent.TimeUnit

interface APIConfig {

    companion object {

        val BASE_URL = "https://quexp.000webhostapp.com/"
        val BASE_URL_IMG = BASE_URL + "assets/"
        val URL_API = BASE_URL + "api/"
        val URL_IMG_NEWS = BASE_URL_IMG + "news/"
        val URL_IMG_BANNER = BASE_URL_IMG + "banner/"
        val URL_IMG_CATEGORY = BASE_URL_IMG + "category/"
        val URL_IMG_AWARDS = BASE_URL_IMG + "awards/"
        val URL_IMG_AWARDS_BANNER = URL_IMG_AWARDS + "banner/"
        val URL_IMG_PLACES = BASE_URL_IMG + "places/"
        val URL_IMG_PROFILE = BASE_URL_IMG + "profile/"
        val URL_IMG_QRCODE = BASE_URL_IMG + "qrcode/"

        fun build(token: String = ""): APIConfig {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client: OkHttpClient = OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                    chain.proceed(request.build())
                }
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(APIConfig::class.java)
        }
    }

    /**
     * -----------------------------------  User API  -----------------------------------
     */

    @FormUrlEncoded
    @POST("user")
    fun userProfile(
        @Field("id_user") id_user: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("user/register")
    fun userRegister(
        @Field("user_email") user_email: String,
        @Field("user_password") user_password: String
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST("user/login")
    fun userLogin(
        @Field("user_email") user_email: String,
        @Field("user_password") user_password: String
    ): Call<UserResponse>

    @Multipart
    @POST("user/edit")
    fun userEditProfile(
        @PartMap params: MutableMap<String, RequestBody>
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST("user/password")
    fun userChangePassword(
        @Field("id_user") id_user: String,
        @Field("user_password") user_password: String,
        @Field("new_password") new_password: String
    ): Call<BaseResponse>

    /**
     * -----------------------------------  Place API  -----------------------------------
     */

    @FormUrlEncoded
    @POST("places")
    fun places(
        @Field("key") key: String
    ): Call<PlacesResponse>

    @GET("places/populer")
    fun placesPopuler(): Call<PlacesResponse>

    @FormUrlEncoded
    @POST("places/detail")
    fun placesDetail(
        @Field("id_place") id_place: String
    ): Call<PlacesResponse>

    @FormUrlEncoded
    @POST("places/search")
    fun placesSearch(
        @Field("key") key: String
    ): Call<PlacesResponse>

    /**
     * -----------------------------------  News API  -----------------------------------
     */

    @GET("news")
    fun news(): Call<NewsResponse>

    @FormUrlEncoded
    @POST("news/detail")
    fun newsDetail(
        @Field("id_place") id_place: String
    ): Call<NewsResponse>

    @FormUrlEncoded
    @POST("news/search")
    fun newsSearch(
        @Field("key") key: String
    ): Call<NewsResponse>

    /**
     * -----------------------------------  Visited API  -----------------------------------
     */

    @FormUrlEncoded
    @POST("visited/add")
    fun visitedAdd(
        @Field("id_place") id_place: String,
        @Field("id_user") id_user: String,
        @Field("user_lat") user_lat: Double,
        @Field("user_long") user_long: Double,
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST("visited")
    fun visited(
        @Field("id_user") id_user: String
    ): Call<VisitedResponse>

    /**
     * -----------------------------------  Awards API  -----------------------------------
     */

    @FormUrlEncoded
    @POST("awards")
    fun awards(
        @Field("id_user") id_user: String
    ): Call<AwardsResponse>

    @FormUrlEncoded
    @POST("awards/roadmap")
    fun roadmap(
        @Field("id_user") id_user: String,
        @Field("id_category") id_category: String
    ): Call<RoadmapResponse>

    /**
     * -----------------------------------  Notification API  -----------------------------------
     */

    @FormUrlEncoded
    @POST("notification")
    fun notification(
        @Field("id_user") id_user: String
    ): Call<NotificationResponse>

    /**
     * -----------------------------------  Banner API  -----------------------------------
     */

    @GET("banner")
    fun banner(): Call<BannerResponse>

    /**
     * -----------------------------------  Category API  -----------------------------------
     */

    @GET("category")
    fun category(): Call<CategoryResponse>

    /**
     * -----------------------------------  APP API  -----------------------------------
     */

    @GET("app/home")
    fun appHome(): Call<HomeResponse>
}