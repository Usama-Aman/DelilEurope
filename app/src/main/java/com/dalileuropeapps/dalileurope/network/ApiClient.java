package com.dalileuropeapps.dalileurope.network;

import com.dalileuropeapps.dalileurope.api.retrofit.AllCategoriesResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessProfileResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.GalleryResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.GeneralResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.HomeResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.PostAdResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.ResponseDetailAds;
import com.dalileuropeapps.dalileurope.api.retrofit.ResponseFeaturedCategories;
import com.dalileuropeapps.dalileurope.api.retrofit.ResponseReviews;
import com.dalileuropeapps.dalileurope.api.retrofit.ResultLogin;
import com.dalileuropeapps.dalileurope.api.retrofit.ResultRegister;
import com.dalileuropeapps.dalileurope.api.retrofit.SearchResponse;
import com.dalileuropeapps.dalileurope.utils.AppController;
import com.dalileuropeapps.dalileurope.utils.Constants;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;
import com.dalileuropeapps.dalileurope.utils.URLs;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    private static Retrofit retrofit = null;
    private ApiInterface apiInterface;
    private static ApiClient apiClient;
    private Gson gson;


    private OkHttpClient getClient() {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Accept", "application/json");
                        if (SharedPreference.isUserLoggedIn(AppController.getInstance().getApplicationContext())) {
                            ongoing.addHeader("Authorization", "Bearer".concat(" ").concat(SharedPreference.getSharedPrefValue(AppController.getInstance().getApplicationContext(), Constants.USER_TOKEN)));
                        }
                        return chain.proceed(ongoing.build());
                    }
                })
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        return client;
    }

    public ApiClient() {
        this.gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(URLs.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build();

        this.apiInterface = retrofit.create(ApiInterface.class);
    }

    public static ApiClient getInstance() {
        if (apiClient == null) {
            setInstance(new ApiClient());
        }
        return apiClient;
    }

    public static void setInstance(ApiClient apiClient) {
        ApiClient.apiClient = apiClient;
    }


    public void loginUser(String email, String password, Callback<ResultLogin> callback) {
        Call<ResultLogin> call = this.apiInterface.loginUser(email, password);
        call.enqueue(callback);
    }


    public void registerUser(String firstName,
                             String lastName,
                             String email,
                             String password,
                             String confirmPassword, Callback<ResultRegister> callback) {
        Call<ResultRegister> call = this.apiInterface.registerUser(firstName,
                lastName,
                email,
                password,
                confirmPassword, "", 0);
        call.enqueue(callback);

    }


    public void userForgotPassword(
            String email, Callback<GeneralResponse> callback) {
        Call<GeneralResponse> call = this.apiInterface.userForgotPassword(email);
        call.enqueue(callback);
    }

    public void userResetPassword(
            String email,
            String otpCode,
            String newPass,
            String confirmPass, Callback<GeneralResponse> callback) {
        Call<GeneralResponse> call = this.apiInterface.userResetPassword(email, otpCode, newPass, confirmPass);
        call.enqueue(callback);
    }

    public void postFCMToken(String deviceToken, String deviceID, String deviceType, String appMode, Callback<GeneralResponse> callback) {
        Call<GeneralResponse> call = this.apiInterface.postFCMToken(deviceToken, deviceID, deviceType, appMode);
        call.enqueue(callback);
    }


    public void getFeaturedCategory(Callback<ResponseFeaturedCategories> callback) {
        Call<ResponseFeaturedCategories> call = this.apiInterface.getFeaturedCategory();
        call.enqueue(callback);
    }

    public void getAllCategories(Callback<AllCategoriesResponse> callback) {
        Call<AllCategoriesResponse> call = this.apiInterface.getAllCategories();
        call.enqueue(callback);
    }


    public void getHomeAds(Callback<HomeResponse> callback) {
        Call<HomeResponse> call = this.apiInterface.getHomeAds();
        call.enqueue(callback);
    }

    Call<SearchResponse> callSearch;


    public void getSearchByCategorySubCatId(String catId, String subCatId, int filter, double lat, double lng, int page, double searchLat, double searchLng, Callback<SearchResponse> callback) {

        if (callSearch != null)
            if (callSearch.isExecuted() && !callSearch.isCanceled())
                callSearch.cancel();


        if (filter == 0) {
            if (searchLat != 0 && searchLng != 0) {
                callSearch = this.apiInterface.getSearchByCategoryId(catId, subCatId, searchLat, searchLng, page);
                callSearch.enqueue(callback);
            } else {
                callSearch = this.apiInterface.getSearchByCategoryId(catId, subCatId, page);
                callSearch.enqueue(callback);
            }
        } else {
            if (lat != 0 && lng != 0) {
                if (searchLat != 0 && searchLng != 0) {

                    callSearch = this.apiInterface.getSearchByCategoryIdAndSubIdWithRadiusButNoSearchLocation(catId, subCatId, filter, lat, lng, searchLat, searchLng, page);
                    callSearch.enqueue(callback);
                } else {
                    callSearch = this.apiInterface.getSearchByCategoryIdAndSubIdWithRadius(catId, subCatId, filter, lat, lng, page);
                    callSearch.enqueue(callback);
                }

            } else {
                if (searchLat != 0 && searchLng != 0) {
                    callSearch = this.apiInterface.getSearchByCategoryIdAndSubId(catId, subCatId, filter, searchLat, searchLng, page);
                    callSearch.enqueue(callback);
                } else {
                    callSearch = this.apiInterface.getSearchByCategoryIdAndSubId(catId, subCatId, filter, page);
                    callSearch.enqueue(callback);
                }
            }

        }
    }


    public void getSearchByText(String searchText, String searchLocationText, int filter, double lat, double lng, int page, double searchLat, double searchLng, String searchID, Callback<SearchResponse> callback) {
        if (callSearch != null)
            if (callSearch.isExecuted() && !callSearch.isCanceled())
                callSearch.cancel();
        if (filter == 0) {
            callSearch = this.apiInterface.getSearchByText(searchText, searchLat, searchLng, page, searchID);
            callSearch.enqueue(callback);
        } else {
            if (lat != 0 && lng != 0) {
                callSearch = this.apiInterface.getSearchByText(searchText, searchLat, searchLng, filter, lat, lng, page, searchID);
                callSearch.enqueue(callback);
            } else {
                callSearch = this.apiInterface.getSearchByText(searchText, searchLat, searchLng, filter, page, searchID);
                callSearch.enqueue(callback);
            }
        }
    }

    public void getAdsDetail(int id, Callback<ResponseDetailAds> callback) {
        Call<ResponseDetailAds> call = this.apiInterface.getAdsDetail(id);
        call.enqueue(callback);
    }

    public void giveReview(RequestBody adsId, RequestBody review, RequestBody rating, MultipartBody.Part imageBodyPart, RequestBody ratingExpert, RequestBody ratingProfessional, RequestBody title, Callback<GeneralResponse> callback) {
        if (imageBodyPart != null) {
            Call<GeneralResponse> call = this.apiInterface.giveReview(adsId, review, rating, imageBodyPart, ratingExpert, ratingProfessional, title);
            call.enqueue(callback);
        } else {
            Call<GeneralResponse> call = this.apiInterface.giveReview(adsId, review, rating, ratingExpert, ratingProfessional, title);
            call.enqueue(callback);
        }
    }

    public void addToFavList(String adsId, Callback<GeneralResponse> callback) {
        Call<GeneralResponse> call = this.apiInterface.addToFavList(adsId);
        call.enqueue(callback);
    }

    public void getReviewsList(int id, int page, Callback<ResponseReviews> callback) {
        Call<ResponseReviews> call = this.apiInterface.getReviewsList(id, page);
        call.enqueue(callback);
    }

    public void getGalleryList(int param1, int page, Callback<GalleryResponse> callback) {
        Call<GalleryResponse> call = this.apiInterface.getGalleryList(param1, page);
        call.enqueue(callback);
    }

    public void postAd(boolean updatePost, RequestBody adId, RequestBody title, RequestBody category, RequestBody subCategory, RequestBody tagline, RequestBody website, RequestBody Lat, RequestBody Lon, RequestBody price, RequestBody dPrice, RequestBody sDate,
                       RequestBody eDate, RequestBody services,
                       RequestBody languages, RequestBody description,
                       RequestBody keyNameBody, RequestBody keyValueBody,
                       RequestBody paymentsBody, RequestBody adTypeBody, MultipartBody.Part[] imageParts, RequestBody adLocation,
                       RequestBody itemsDeleted, Boolean isPayment, RequestBody paymentMethod, RequestBody stripToken, RequestBody isPaid, Callback<PostAdResponse> callback) {
        if (!updatePost) {


            if (isPayment) {

                if (AppController.isStrip) {
                    Call<PostAdResponse> call = this.apiInterface.postAd(title, category, subCategory, tagline, website, Lat, Lon, price, dPrice, sDate, eDate, services, languages, description,
                            keyNameBody, keyValueBody,
                            paymentsBody, adTypeBody, imageParts, adLocation, itemsDeleted,
                            paymentMethod,
                            stripToken);
                    call.enqueue(callback);
                } else if (AppController.isPayPal) {
                    Call<PostAdResponse> call = this.apiInterface.postAdPaypal(title, category, subCategory, tagline, website, Lat, Lon, price, dPrice, sDate, eDate, services, languages, description,
                            keyNameBody, keyValueBody,
                            paymentsBody, adTypeBody, imageParts, adLocation, itemsDeleted, paymentMethod,
                            isPaid);
                    call.enqueue(callback);
                }

            } else {
                Call<PostAdResponse> call = this.apiInterface.postAd(title, category, subCategory, tagline, website, Lat, Lon, price, dPrice, sDate, eDate, services, languages, description,
                        keyNameBody, keyValueBody,
                        paymentsBody, adTypeBody, imageParts, adLocation, itemsDeleted);
                call.enqueue(callback);
            }


        } else {

            Call<PostAdResponse> call = this.apiInterface.updateAd(adId, title, category, subCategory, tagline, website, Lat, Lon, price, dPrice, sDate, eDate, services, languages, description,
                    keyNameBody, keyValueBody,
                    paymentsBody, adTypeBody, imageParts, adLocation, itemsDeleted);
            call.enqueue(callback);

        }
    }

    public void postAdPayments(String adId, String amount, String orderId, Callback<PostAdResponse> callback) {
        Call<PostAdResponse> call = this.apiInterface.postAdPayments(adId, amount, orderId);
        call.enqueue(callback);
    }

    public void postBusinessPayment(String adId, String adAmount, String orderId, Callback<BusinessProfileResponse> callback) {
        Call<BusinessProfileResponse> call = this.apiInterface.postBusinessPayment(adId, adAmount, orderId);
        call.enqueue(callback);
    }

    public void changePassword(
            String currentPass,
            String newPass,
            String confirmPass, Callback<ResultLogin> callback) {
        Call<ResultLogin> call = this.apiInterface.changePassword(currentPass, newPass, confirmPass);
        call.enqueue(callback);
    }
}