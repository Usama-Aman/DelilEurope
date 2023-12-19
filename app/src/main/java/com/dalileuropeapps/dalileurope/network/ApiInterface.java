package com.dalileuropeapps.dalileurope.network;


import com.dalileuropeapps.dalileurope.api.retrofit.AllCategoriesResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinesCatResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessProfileResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.CityResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.CountryResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.GalleryResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.GeneralResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.HomeResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.LanguageResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.PostAdResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.ResponseDetailAds;
import com.dalileuropeapps.dalileurope.api.retrofit.ResponseFeaturedCategories;
import com.dalileuropeapps.dalileurope.api.retrofit.ResponseReviews;
import com.dalileuropeapps.dalileurope.api.retrofit.ResultLogin;
import com.dalileuropeapps.dalileurope.api.retrofit.ResultRegister;
import com.dalileuropeapps.dalileurope.api.retrofit.SearchResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.SearchesResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.StateResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.SubscriptionsResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.adslist.AddListingResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.adslist.ContentPageRespnse;
import com.dalileuropeapps.dalileurope.api.retrofit.adslist.FavAddListingResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.message.GenResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.message.MessageListResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.message.MessagesChatResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.message.SendMessageResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {


    @FormUrlEncoded
    @POST("login")
    Call<ResultLogin> loginUser(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<ResultRegister> registerUser(@Field("first_name") String name,
                                      @Field("last_name") String mobile,
                                      @Field("email") String email,
                                      @Field("password") String pass,
                                      @Field("confirm_password") String location,
                                      @Field("social_type") String socialType,
                                      @Field("social_id") int socialID);


    @FormUrlEncoded
    @POST("forgot_password")
    Call<GeneralResponse> userForgotPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("reset_password")
    Call<GeneralResponse> userResetPassword(@Field("email") String email, @Field("verification_code") String otpCode,
                                            @Field("password") String newPass,
                                            @Field("confirm_password") String confirmPass);

    @POST("register_device_token")
    Call<GeneralResponse> postFCMToken(@Query("device_token") String deviceToken,
                                       @Query("device_id") String deviceID,
                                       @Query("device_type") String deviceType,
                                       @Query("app_mode") String appMode);

    @GET("list_featured_categories")
    Call<ResponseFeaturedCategories> getFeaturedCategory();

    @GET("list_all_categories")
    Call<AllCategoriesResponse> getAllCategories();

    @POST("home_search")
    Call<HomeResponse> getHomeAds();

    @FormUrlEncoded
    @POST("search")
    Call<SearchResponse> getSearchByCategoryId(@Field("category_id") String category_id, @Field("sub_category_id") String subcategory_id, @Field("page") int page);


    @FormUrlEncoded
    @POST("search")
    Call<SearchResponse> getSearchByCategoryId(@Field("category_id") String category_id, @Field("sub_category_id") String subcategory_id
            , @Field("search_lat") double search_lat
            , @Field("search_long") double search_long, @Field("page") int page);

    @FormUrlEncoded
    @POST("search")
    Call<SearchResponse> getSearchByCategoryIdAndSubId(@Field("category_id") String category_id, @Field("sub_category_id") String subcategory_id, @Field("orderby") int filter
            , @Field("search_lat") double search_lat
            , @Field("search_long") double search_long, @Field("page") int page);


    @FormUrlEncoded
    @POST("search")
    Call<SearchResponse> getSearchByCategoryIdAndSubId(@Field("category_id") String category_id, @Field("sub_category_id") String subcategory_id, @Field("orderby") int filter, @Field("page") int page);

    @FormUrlEncoded
    @POST("search")
    Call<SearchResponse> getSearchByCategoryIdAndSubIdWithRadius(@Field("category_id") String category_id, @Field("sub_category_id") String subcategory_id, @Field("orderby") int filter,
                                                                 @Field("lat") double lat, @Field("lng") double lng, @Field("page") int page);

    @FormUrlEncoded
    @POST("search")
    Call<SearchResponse> getSearchByCategoryIdAndSubIdWithRadiusButNoSearchLocation(@Field("category_id") String category_id, @Field("sub_category_id") String subcategory_id, @Field("orderby") int filter,
                                                                                    @Field("lat") double lat, @Field("lng") double lng,
                                                                                    @Field("search_lat") double search_lat
            , @Field("search_long") double search_long, @Field("page") int page);

    @FormUrlEncoded
    @POST("search-by-key")
    Call<SearchResponse> getSearchByText(@Field("search_key") String searchKey, @Field("search_lat") double search_lat
            , @Field("search_long") double search_long, @Field("page") int page, @Field("search_id") String search_id);

    @FormUrlEncoded
    @POST("search-by-key")
    Call<SearchResponse> getSearchByText(@Field("search_key") String searchKey, @Field("search_lat") double search_lat
            , @Field("search_long") double search_long, @Field("orderby") int filter, @Field("page") int page, @Field("search_id") String search_id);

    @FormUrlEncoded
    @POST("search-by-key")
    Call<SearchResponse> getSearchByText(@Field("search_key") String searchKey, @Field("search_lat") double search_lat
            , @Field("search_long") double search_long, @Field("orderby") int filter,
                                         @Field("lat") double lat, @Field("lng") double lng, @Field("page") int page, @Field("search_id") String search_id);


    @FormUrlEncoded
    @Headers({"Accept:application/json"})
    @POST("api/list_messages")
    Call<MessagesChatResponse> getMessagesList(@Header("Authorization") String authHeader,
                                               @Field("thread_id") String thread_id, @Field("page") int page);

    @Headers({"Accept:application/json"})
    @GET("api/user_messages_thread")
    Call<MessageListResponse> getMessagesThread(@Header("Authorization") String authHeader, @Query("page") int page);

    @FormUrlEncoded
    @Headers({"Accept:application/json"})
    @POST("api/send_message")
    Call<SendMessageResponse> sendMessage(@Header("Authorization") String authHeader,
                                          @Field("thread_id") String thread_id,
                                          @Field("to_user_id") String to_user_id,
                                          @Field("message_text") String message_text);

    @FormUrlEncoded
    @Headers({"Accept:application/json"})
    @POST("api/delete_message")
    Call<GenResponse> delMessage(@Header("Authorization") String authHeader,
                                 @Field("id") String id);

    @FormUrlEncoded
    @Headers({"Accept:application/json"})
    @POST("api/read_message")
    Call<GenResponse> readMessage(@Header("Authorization") String authHeader,
                                  @Field("message_id") String message_id);

    @FormUrlEncoded
    @POST("ad_detail")
    Call<ResponseDetailAds> getAdsDetail(@Field("id") int id);


    @FormUrlEncoded
    @POST("list_ad_gallery")
    Call<GalleryResponse> getGalleryList(@Field("id") int id, @Field("page") int page);

    @FormUrlEncoded
    @POST("list_ad_reviews")
    Call<ResponseReviews> getReviewsList(@Field("id") int id, @Field("page") int page);

    @FormUrlEncoded
    @POST("add_fav_ads")
    Call<GeneralResponse> addToFavList(@Field("id") String id);

    @Headers({"Accept:application/json"})
    @GET("api/list_my_ads")
    Call<AddListingResponse> getAddList(@Header("Authorization") String authHeader, @Query("page") int page);

    @FormUrlEncoded
    @Headers({"Accept:application/json"})
    @POST("api/delete_ad")
    Call<GenResponse> delAdd(@Header("Authorization") String authHeader,
                             @Field("id") String id);


    @Headers({"Accept:application/json"})
    @GET("api/my_favorite_ads")
    Call<FavAddListingResponse> getFavAddList(@Header("Authorization") String authHeader, @Query("page") int page);

    @FormUrlEncoded
    @Headers({"Accept:application/json"})
    @POST("api/delete_fav_ads")
    Call<GenResponse> delFavAdd(@Header("Authorization") String authHeader,
                                @Field("id") String id);

    @FormUrlEncoded
    @Headers({"Accept:application/json"})
    @POST("api/page")
    Call<ContentPageRespnse> getPageContent(@Header("Authorization") String authHeader,
                                            @Field("page_key") String page_key);

    @Multipart
    @POST("save_update_ad")
    Call<PostAdResponse> postAd(@Part("name") RequestBody title,
                                @Part("category_id") RequestBody category,
                                @Part("sub_category_id") RequestBody subCategory,
                                @Part("tag") RequestBody tagline,
                                @Part("ad_url") RequestBody website,
                                @Part("lat") RequestBody Lat,
                                @Part("lng") RequestBody Lon,
                                @Part("ad_price") RequestBody price,
                                @Part("ad_disc_price") RequestBody dPrice,
                                @Part("start_date") RequestBody sDate,
                                @Part("end_date") RequestBody eDate,
                                @Part("ad_service") RequestBody services,
                                @Part("ad_languages") RequestBody languages,
                                @Part("description") RequestBody description,
                                @Part("key_name") RequestBody keyNameBody,
                                @Part("value") RequestBody keyValueBody,
                                @Part("payment_methods") RequestBody paymentsBody,
                                @Part("is_featured") RequestBody adTypeBody,
                                @Part MultipartBody.Part[] imageParts,
                                @Part("ad_location") RequestBody adLocation,
                                @Part("delete_images") RequestBody deleteImagesHaspMap);

    @Multipart
    @POST("save_update_ad")
    Call<PostAdResponse> updateAd(@Part("ad_id") RequestBody adId, @Part("name") RequestBody title,
                                  @Part("category_id") RequestBody category,
                                  @Part("sub_category_id") RequestBody subCategory,
                                  @Part("tag") RequestBody tagline,
                                  @Part("ad_url") RequestBody website,
                                  @Part("lat") RequestBody Lat,
                                  @Part("lng") RequestBody Lon,
                                  @Part("ad_price") RequestBody price,
                                  @Part("ad_disc_price") RequestBody dPrice,
                                  @Part("start_date") RequestBody sDate,
                                  @Part("end_date") RequestBody eDate,
                                  @Part("ad_service") RequestBody services,
                                  @Part("ad_languages") RequestBody languages,
                                  @Part("description") RequestBody description,
                                  @Part("key_name") RequestBody keyNameBody,
                                  @Part("value") RequestBody keyValueBody,
                                  @Part("payment_methods") RequestBody paymentsBody,
                                  @Part("is_featured") RequestBody adTypeBody,
                                  @Part MultipartBody.Part[] imageParts,
                                  @Part("ad_location") RequestBody adLocation, @Part("delete_images") RequestBody deleteImagesHaspMap);

    @Multipart
    @POST("post_review")
    Call<GeneralResponse> giveReview(@Part("id") RequestBody id, @Part("comment") RequestBody comment, @Part("overall_rating") RequestBody ratings,
                                     @Part("expertise_rating") RequestBody ratingsExperts, @Part("professionalism_rating") RequestBody ratingsProfessional, @Part("title") RequestBody title);

    @Multipart
    @POST("post_review")
    Call<GeneralResponse> giveReview(@Part("id") RequestBody id, @Part("comment") RequestBody comment, @Part("overall_rating") RequestBody ratings, @Part MultipartBody.Part image
            ,
                                     @Part("expertise_rating") RequestBody ratingsExperts, @Part("professionalism_rating") RequestBody ratingsProfessional, @Part("title") RequestBody title);

    //profile
    @Headers({"Accept:application/json"})
    @GET("api/list_country")
    Call<CountryResponse> getallCountries(@Header("Authorization") String authHeader);

    @Headers({"Accept:application/json"})
    @GET("api/list_states/{id}")
    Call<StateResponse> getallStates(@Header("Authorization") String authHeader, @Path("id") String id);

    @Headers({"Accept:application/json"})
    @GET("api/list_cities_where/{id}")
    Call<CityResponse> getallCities(@Header("Authorization") String authHeader, @Path("id") String id);

    @Headers({"Accept:application/json"})
    @GET("api/get_profile")
    Call<ResultLogin> getUserDetail(@Header("Authorization") String authHeader);


    @Headers({"Accept:application/json"})
    @Multipart
    @POST("api/edit_profile")
    Call<ResultLogin> updateProfile(@Header("Authorization") String authHeader,
                                    @Part("first_name") RequestBody first_name,
                                    @Part("last_name") RequestBody last_name,
                                    @Part("phone_number") RequestBody phone_number,
                                    @Part("address") RequestBody address,
                                    @Part("latitude") RequestBody latitude,
                                    @Part("longitude") RequestBody longitude,
                                    @Part("country") RequestBody country,
                                    @Part("state") RequestBody state,
                                    @Part("city") RequestBody city,
                                    @Part MultipartBody.Part image,
                                    @Part("zip_code") RequestBody zip_code,
                                    @Part("about_me") RequestBody about_me
    );


    @FormUrlEncoded
    @Headers({"Accept:application/json"})
    @POST("api/save_language")
    Call<LanguageResponse> updateLanguage(@Header("Authorization") String authHeader,
                                          @Field("language") String language);

    @Headers({"Accept:application/json"})
    @GET("api/get_business_profile")
    Call<BusinessProfileResponse> getUserBusinesProfile(@Header("Authorization") String authHeader);


    @Headers({"Accept:application/json"})
    @GET("api/list_all_categories")
    Call<BusinesCatResponse> getallCategories(@Header("Authorization") String authHeader);


    @Multipart
    @POST("api/edit_business_profile")
    Call<BusinessProfileResponse> updateBusinessProfileStrip(@Header("Authorization") String authHeader,
                                                             @Part("name") RequestBody name,
                                                             @Part("category_id") RequestBody category_id,
                                                             @Part("sub_category_id") RequestBody sub_category_id,
                                                             @Part("email") RequestBody email,
                                                             @Part("address") RequestBody address,
                                                             @Part("latitude") RequestBody latitude,
                                                             @Part("longitude") RequestBody longitude,
                                                             @Part("country") RequestBody country,
                                                             @Part("state") RequestBody state,
                                                             @Part("city") RequestBody city,
                                                             @Part("zip_code") RequestBody zip_code,
                                                             @Part("phone_number") RequestBody phone_number,
                                                             @Part("website") RequestBody website,
                                                             @Part MultipartBody.Part logo,
                                                             @Part("about_business") RequestBody about_business,
                                                             @Part("start_time") RequestBody start_time,
                                                             @Part("end_time") RequestBody end_time,
                                                             @Part("day") RequestBody day,
                                                             @Part("subscription_id") RequestBody subscription_id,
                                                             @Part("organisation_no") RequestBody organisation_no,
                                                             @Part("payment_type") RequestBody payment_type,
                                                             @Part("stripe_token") RequestBody stripe_token,
                                                             @Part("delete_images") RequestBody delete_images,
                                                             @Part MultipartBody.Part[] business_image
    );


    @Multipart
    @POST("save_update_ad")
    Call<PostAdResponse> postAd(@Part("name") RequestBody title,
                                @Part("category_id") RequestBody category,
                                @Part("sub_category_id") RequestBody subCategory,
                                @Part("tag") RequestBody tagline,
                                @Part("ad_url") RequestBody website,
                                @Part("lat") RequestBody Lat,
                                @Part("lng") RequestBody Lon,
                                @Part("ad_price") RequestBody price,
                                @Part("ad_disc_price") RequestBody dPrice,
                                @Part("start_date") RequestBody sDate,
                                @Part("end_date") RequestBody eDate,
                                @Part("ad_service") RequestBody services,
                                @Part("ad_languages") RequestBody languages,
                                @Part("description") RequestBody description,
                                @Part("key_name") RequestBody keyNameBody,
                                @Part("value") RequestBody keyValueBody,
                                @Part("payment_methods") RequestBody paymentsBody,
                                @Part("is_featured") RequestBody adTypeBody,
                                @Part MultipartBody.Part[] imageParts,
                                @Part("ad_location") RequestBody adLocation,
                                @Part("delete_images") RequestBody deleteImagesHaspMap,
                                @Part("payment_type") RequestBody payment_type,
                                @Part("stripe_token") RequestBody stripe_token);


    @Multipart
    @POST("save_update_ad")
    Call<PostAdResponse> postAdPaypal(@Part("name") RequestBody title,
                                      @Part("category_id") RequestBody category,
                                      @Part("sub_category_id") RequestBody subCategory,
                                      @Part("tag") RequestBody tagline,
                                      @Part("ad_url") RequestBody website,
                                      @Part("lat") RequestBody Lat,
                                      @Part("lng") RequestBody Lon,
                                      @Part("ad_price") RequestBody price,
                                      @Part("ad_disc_price") RequestBody dPrice,
                                      @Part("start_date") RequestBody sDate,
                                      @Part("end_date") RequestBody eDate,
                                      @Part("ad_service") RequestBody services,
                                      @Part("ad_languages") RequestBody languages,
                                      @Part("description") RequestBody description,
                                      @Part("key_name") RequestBody keyNameBody,
                                      @Part("value") RequestBody keyValueBody,
                                      @Part("payment_methods") RequestBody paymentsBody,
                                      @Part("is_featured") RequestBody adTypeBody,
                                      @Part MultipartBody.Part[] imageParts,
                                      @Part("ad_location") RequestBody adLocation,
                                      @Part("delete_images") RequestBody deleteImagesHaspMap,
                                      @Part("payment_type") RequestBody payment_type,
                                      @Part("is_paid") RequestBody is_Paid);


    @Multipart
    @POST("api/edit_business_profile")
    Call<BusinessProfileResponse> updateBusinessProfile(@Header("Authorization") String authHeader,
                                                        @Part("name") RequestBody name,
                                                        @Part("category_id") RequestBody category_id,
                                                        @Part("sub_category_id") RequestBody sub_category_id,
                                                        @Part("email") RequestBody email,
                                                        @Part("address") RequestBody address,
                                                        @Part("latitude") RequestBody latitude,
                                                        @Part("longitude") RequestBody longitude,
                                                        @Part("country") RequestBody country,
                                                        @Part("state") RequestBody state,
                                                        @Part("city") RequestBody city,
                                                        @Part("zip_code") RequestBody zip_code,
                                                        @Part("phone_number") RequestBody phone_number,
                                                        @Part("website") RequestBody website,
                                                        @Part MultipartBody.Part logo,
                                                        @Part("about_business") RequestBody about_business,
                                                        @Part("delete_images") RequestBody delete_images,
                                                        @Part MultipartBody.Part[] business_image,
                                                        @Part("start_time") RequestBody start_time,
                                                        @Part("end_time") RequestBody end_time,
                                                        @Part("day") RequestBody day,
                                                        @Part("subscription_id") RequestBody subscription_id,
                                                        @Part("organisation_no") RequestBody organisation_no,
                                                        @Part("payment_type") RequestBody payment_type, @Part("is_paid") RequestBody is_Paid
    );

    @Multipart
    @POST("api/edit_business_profile")
    Call<BusinessProfileResponse> updateBusinessProfile(@Header("Authorization") String authHeader,
                                                        @Part("name") RequestBody name,
                                                        @Part("category_id") RequestBody category_id,
                                                        @Part("sub_category_id") RequestBody sub_category_id,
                                                        @Part("email") RequestBody email,
                                                        @Part("address") RequestBody address,
                                                        @Part("latitude") RequestBody latitude,
                                                        @Part("longitude") RequestBody longitude,
                                                        @Part("country") RequestBody country,
                                                        @Part("state") RequestBody state,
                                                        @Part("city") RequestBody city,
                                                        @Part("zip_code") RequestBody zip_code,
                                                        @Part("phone_number") RequestBody phone_number,
                                                        @Part("website") RequestBody website,
                                                        @Part MultipartBody.Part logo,
                                                        @Part("about_business") RequestBody about_business,
                                                        @Part("delete_images") RequestBody delete_images,
                                                        @Part MultipartBody.Part[] business_image,
                                                        @Part("start_time") RequestBody start_time,
                                                        @Part("end_time") RequestBody end_time,
                                                        @Part("day") RequestBody day,
                                                        @Part("subscription_id") RequestBody subscription_id,
                                                        @Part("organisation_no") RequestBody organisation_no
    );


    @FormUrlEncoded
    @POST("update_ad_status")
    Call<PostAdResponse> postAdPayments(@Field("ad_id") String adId, @Field("ad_amount") String adAmount, @Field("order_id") String orderId);

    @FormUrlEncoded
    @POST("update_business_status")
    Call<BusinessProfileResponse> postBusinessPayment(@Field("business_id") String adId, @Field("amount") String adAmount, @Field("order_id") String orderId);


    @Headers({"Accept:application/json"})
    @GET("api/list_subscription_plan")
    Call<SubscriptionsResponse> getallSubscriptions(@Header("Authorization") String authHeader);

    @FormUrlEncoded
    @POST("change_password")
    Call<ResultLogin> changePassword(@Field("current_password") String current_password,
                                     @Field("password") String newPass,
                                     @Field("confirm_password") String confirmPass);

    @Headers({"Accept:application/json"})
    @GET("api/business_category")
    Call<SearchesResponse> getSearches(@Header("Authorization") String authHeader);
}

