package com.phsartech.onlinegetseller.retrofit;

import com.google.gson.JsonObject;
import com.phsartech.onlinegetseller.model.BrandModel;
import com.phsartech.onlinegetseller.model.CategoryModel;
import com.phsartech.onlinegetseller.model.OrderModel;
import com.phsartech.onlinegetseller.model.ProductModelOnSale;
import com.phsartech.onlinegetseller.model.ProductModelSold;
import com.phsartech.onlinegetseller.model.SupplierModel;
import com.phsartech.onlinegetseller.model.UnitModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIClient {

    //login
    @FormUrlEncoded
    @POST("v4/login")
    Call<JsonObject> getLogin(
            @Field("email") String phone,
            @Field("password") String password);

    //getproductall
    @GET("v4/product/{shop_id}")
    Call<OrderModel> getProductAll(
            @Path("shop_id") int shop_id,
            @Header("Authorization") String token);

    //getproductallnext
    @GET("v4/product/{shop_id}/{item}")
    Call<OrderModel> getProductAllNext(
            @Path("shop_id") int shop_id,
            @Path("item") int item
    );

    //getproductcanceled
    @GET("v4/product-cancel/{shop_id}")
    Call<OrderModel> getProductCanceled(
            @Path("shop_id") int shop_id,
            @Header("Authorization") String token);

    //getproductcancelednext
    @GET("v4/product-cancel/{shop_id}/{item}")
    Call<OrderModel> getProductCanceledNext(
            @Path("shop_id") int shop_id,
            @Path("item") int item
    );

    //getproductdelivery
    @GET("v4/product-delivery/{shop_id}")
    Call<OrderModel> getProductDelivery(
            @Path("shop_id") int shop_id,
            @Header("Authorization") String token);

    //getproductcancelednext
    @GET("v4/product-delivery/{shop_id}/{item}")
    Call<OrderModel> getProductDeliveryNext(
            @Path("shop_id") int shop_id,
            @Path("item") int item
    );

    //getproductshipping
    @GET("v4/product-shipping/{shop_id}")
    Call<OrderModel> getProductShipping(
            @Path("shop_id") int shop_id,
            @Header("Authorization") String token);

    //getproductshippingnext
    @GET("v4/product-shipping/{shop_id}/{item}")
    Call<OrderModel> getProductShippingNext(
            @Path("shop_id") int shop_id,
            @Path("item") int item
    );

    //getproductpending
    @GET("v4/product-pending/{shop_id}")
    Call<OrderModel> getProductPending(
            @Path("shop_id") int shop_id,
            @Header("Authorization") String token);

    //getproductpendingnext
    @GET("v4/product-pending/{shop_id}/{item}")
    Call<OrderModel> getProductPendingNext(
            @Path("shop_id") int shop_id,
            @Path("item") int item
    );

    //getorderreport
    @GET("v4/order-report/{shop_id}")
    Call<JsonObject> getOrderReport(
            @Path("shop_id") int shop_id,
            @Header("Authorization") String token);

    //getproductreport
    @GET("v4/product-report/{user_id}")
    Call<JsonObject> getProductReport(
            @Path("user_id") int user_id,
            @Header("Authorization") String token);

    //getshopdetail
    @GET("v4/shop-detail/{id}")
    Call<JsonObject> getShopDetail(
            @Path("id") int id,
            @Header("Authorization") String token);

    //getterm
    @GET("v4/term")
    Call<JsonObject> getTerm(@Header("Authorization") String token);

    //getprofile
    @GET("v4/profile/{id}")
    Call<JsonObject> getProfileDetail(
            @Path("id") int id,
            @Header("Authorization") String token);

    //getabout
    @GET("v4/about")
    Call<JsonObject> getAbout(@Header("Authorization") String token);

    //getpolicy
    @GET("v4/policy")
    Call<JsonObject> getPolicy(@Header("Authorization") String token);

    //getsaleononlineget
    @GET("v4/saleononlineget")
    Call<JsonObject> getSaleOnOnlineGet(@Header("Authorization") String token);

    //changepassword
    @FormUrlEncoded
    @POST("v4/change-password")
    Call<JsonObject> changePassword(
            @Field("user_id") int id,
            @Header("Authorization") String token,
            @Field("current_password") String current,
            @Field("new_password") String set);

    //registeracc
    @FormUrlEncoded
    @POST("v4/login-fb")
    Call<JsonObject> login_fb(
            @Field("social_id") String social_id,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("username") String username,
            @Field("email") String email,
            @Field("image_path") String image_path);

    //getproductonsale
    @GET("v4/product-onsale/{id}")
    Call<ProductModelOnSale> getProductOnSale(
            @Path("id") int id,
            @Header("Authorization") String token);

    //getproductonsalenext
    @GET("v4/product-onsale/{id}/{item}")
    Call<ProductModelOnSale> getProductOnSaleNext(
            @Path("id") int id,
            @Path("item") int item
    );    //getproductonsale

    @GET("v4/product-sold/{shop_id}")
    Call<ProductModelSold> getProductSold(
            @Path("shop_id") int shop_id,
            @Header("Authorization") String token);

    //getproductonsalenext
    @GET("v4/product-sold/{id}/{item}")
    Call<ProductModelSold> getProductSoldNext(
            @Path("id") int id,
            @Path("item") int item
    );

    //getcategory
    @GET("v4/category")
    Call<CategoryModel> getCategory();

    //getcategory-parent
    @GET("v4/category-parent/{parent_id}")
    Call<CategoryModel> getCategoryParent(
            @Path("parent_id") int parent_id
    );

    //getcategory-parent
    @GET("v4/category-sub/{parent_id}/{sub_id}")
    Call<CategoryModel> getCategorySub(
            @Path("parent_id") int parent_id,
            @Path("sub_id") int sub_id
    );

    //getbrand
    @GET("v4/brand")
    Call<BrandModel> getBrand();

    //getsupplier
    @GET("v4/supplier")
    Call<SupplierModel> getSupplier();

    //getunit
    @GET("v4/unit")
    Call<UnitModel> getUnit();

    //addproduct
    @FormUrlEncoded
    @POST("v4/add-product")
    Call<JsonObject> addProduct(
            @Header("Authorization") String token,
            @Field("user_id") int id,
            @Field("name_en") String name_en,
            @Field("qty") int qty,
            @Field("sell_price") int sell_price,
            @Field("video") String video,
            @Field("des_en") String des_en,
            @Field("image") String[] image,
            @Field("category_id") int category_id,
            @Field("parent_category") int parent_category,
            @Field("sub_id") int sub_id,
            @Field("braind_id") int braind_id,
            @Field("supplier_id") int supplier_id,
            @Field("unit_id") int unit_id);

    //editusername
    @FormUrlEncoded
    @POST("v4/profile-edit-username")
    Call<JsonObject> editUsername(
            @Header("Authorization") String token,
            @Field("user_id") int id,
            @Field("username") String username
    );

    //editemail
    @FormUrlEncoded
    @POST("v4/profile-edit-email")
    Call<JsonObject> editEmail(
            @Header("Authorization") String token,
            @Field("user_id") int id,
            @Field("email") String email
    );

    //editphone
    @FormUrlEncoded
    @POST("v4/profile-edit-phone")
    Call<JsonObject> editPhone(
            @Header("Authorization") String token,
            @Field("user_id") int id,
            @Field("phone") String phone
    );

    //editaddress
    @FormUrlEncoded
    @POST("v4/profile-edit-address")
    Call<JsonObject> editAddress(
            @Header("Authorization") String token,
            @Field("user_id") int id,
            @Field("address") String address
    );

    //editbio
    @FormUrlEncoded
    @POST("v4/profile-edit-bio")
    Call<JsonObject> editBio(
            @Header("Authorization") String token,
            @Field("user_id") int id,
            @Field("bio") String bio
    );

    //editshopname
    @FormUrlEncoded
    @POST("v4/shop-edit-name")
    Call<JsonObject> editShopName(
            @Header("Authorization") String token,
            @Field("shop_id") int id,
            @Field("name") String name
    );

    //editshopemail
    @FormUrlEncoded
    @POST("v4/shop-edit-email")
    Call<JsonObject> editShopEmail(
            @Header("Authorization") String token,
            @Field("shop_id") int id,
            @Field("email") String email
    );

    //editshopphone
    @FormUrlEncoded
    @POST("v4/shop-edit-phone")
    Call<JsonObject> editShopPhone(
            @Header("Authorization") String token,
            @Field("shop_id") int id,
            @Field("phone") String phone
    );
    //editshopaddress
    @FormUrlEncoded
    @POST("v4/shop-edit-address")
    Call<JsonObject> editShopAddress(
            @Header("Authorization") String token,
            @Field("shop_id") int id,
            @Field("address") String address
    );
    //editshopdetail
    @FormUrlEncoded
    @POST("v4/shop-edit-detail")
    Call<JsonObject> editShopDetail(
            @Header("Authorization") String token,
            @Field("shop_id") int id,
            @Field("detail") String detail
    );
}
