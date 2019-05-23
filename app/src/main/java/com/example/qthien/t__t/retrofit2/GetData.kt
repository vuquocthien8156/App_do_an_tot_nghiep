package com.example.qthien.t__t.retrofit2

import com.example.qthien.t__t.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface GetData {
    // User
    @FormUrlEncoded
    @POST("/api/login")
    fun loginUser(@Field("username") email : String,
                  @Field("password") mat_khau : String) : Call<ResponseLogin>

    @FormUrlEncoded
    @POST("/api/login-by-phone")
    fun loginUserPhone(@Field("username") username : String) : Call<ResponseLogin>

    @FormUrlEncoded
    @POST("/api/login-fb")
    fun loginUserFacebook(@Field("id_fb") id_fb : String,
                          @Field("email") email : String?,
                          @Field("name") name : String?) : Call<ResponseLogin>

    @FormUrlEncoded
    @POST("/api/register")
    fun registerUser(@Field("username") email : String,
                     @Field("password") pass : String,
                     @Field("name" ) nameUser : String,
                     @Field("gender") gender : Int,
                     @Field("birthday") birthday : String,
                     @Field("phone") numberPhone : String) : Call<ResponseLogin>

    @GET("/api/checkLoginExist")
    fun checkExistUser(@Query("username") username : String) : Call<ResponseCheckExist>

    @GET("/api/getInfoByEmail")
    fun getInfoByEmail(@Query("email") email : String) : Call<ResponseLogin>

    @Multipart
    @POST("/api/uploadImage")
    fun uploadImage(@Part avata: MultipartBody.Part?) : Call<ResponseUploadImage>

    @FormUrlEncoded
    @POST("/api/updateInfo")
    fun updateUser(
        @Field("id") idUser: Int,
        @Field("email") email: String?,
        @Field("name" ) nameUser: String?,
        @Field("gender") gender: Int?,
        @Field("birth_day") birthday: String?,
        @Field("phone") phone: String?,
        @Field("avatar_path") urlAvata: String?) : Call<ResponseDefault>

    //Cart
    @GET("/api/getCartOfCustomer")
    fun getCartOfUser(@Query("id_KH") idCustomer: Int) : Call<ResponseCart>

    @POST("/api/add-cart")
    fun addCart(@Body cartPlus : CartPlus) : Call<ResponseDefault>

    @FormUrlEncoded
    @POST("/api/delete-cart")
    fun deleteCart(@Field("id_GH") idCart : Int) : Call<ResponseDefault>

    @FormUrlEncoded
    @POST("/api/delete-all-cart-of-customer")
    fun deleteCartCustomer(@Field("id_KH") idCustomer : Int) : Call<ResponseDefault>

    @FormUrlEncoded
    @POST("/api/update-quantity")
    fun updateQuantity(@Field("id_GH") idCart : Int,
                       @Field("type") type : Int) : Call<ResponseDefault>

    // News
    @GET("/api/news")
    fun getNews() : Call<ResponseNews>

    // Product
    @GET("/api/listProduct")
    fun getAllProduct() : Call<ResponseProducts>

    @GET("/api/listProduct")
    fun getAllProductByMainCate(@Query("loai_chinh") idMainCate: Int?) : Call<ResponseProducts>

    @GET("/api/listProduct")
    fun getProductByCatalogy (@Query("ma_loai") idCatelogy: Int?) : Call<ResponseProducts>

    @GET("/api/listRankProduct")
    fun getProductBestBuy()  : Call<ResponseProducts>

    @GET("/api/TheMostFavoriteProduct")
    fun getProductBestFavorite()  : Call<ResponseProducts>

    @GET("/api/likedProduct")
    fun getProductFavoritedByUser(@Query("id") idUser : Int) : Call<ResponseProducts>

    @GET("/api/checkLikeByUser")
    fun checkFavoriteProduct(@Query("id") idUser : Int,
                             @Query("id_sp") idProduct : Int) : Call<ResponseCheckFavorite>

    @GET("/api/like")
    fun favoriteProduct(@Query("id_product") idProduct : Int,
                        @Query("id_user") idUser : Int,
                        @Query("like") favorite : Int) : Call<ResponseDefault>

    @GET("/api/productType")
    fun getAllCatalogy() : Call<ResponseCatalogy>
}