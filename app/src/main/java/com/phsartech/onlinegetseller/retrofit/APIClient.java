package com.phsartech.onlinegetseller.retrofit;

import com.google.gson.JsonObject;
import com.phsartech.onlinegetseller.model.BrandModel;
import com.phsartech.onlinegetseller.model.CategoryModel;
import com.phsartech.onlinegetseller.model.GalleryModel;
import com.phsartech.onlinegetseller.model.OrderModel;
import com.phsartech.onlinegetseller.model.ProductModelOnSale;
import com.phsartech.onlinegetseller.model.ProductModelSold;
import com.phsartech.onlinegetseller.model.SupplierModel;
import com.phsartech.onlinegetseller.model.UnitModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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
    );

    //getproductonsale
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

    //showcategory
    @GET("v4/category-show/{category_id}/{parent_id}/{sub_id}")
    Call<JsonObject> showCategory(
            @Header("Authorization") String token,
            @Path("category_id") int category_id,
            @Path("parent_id") int parent_id,
            @Path("sub_id") int sub_id
    );

    //showbrand
    @GET("v4/brand-show/{brand_id}")
    Call<JsonObject> showBrand(
            @Header("Authorization") String token,
            @Path("brand_id") int brand_id);

    //showsupplier
    @GET("v4/supplier-show/{supplier_id}")
    Call<JsonObject> showSupplier(
            @Header("Authorization") String token,
            @Path("supplier_id") int supplier_id);

    //showunit
    @GET("v4/unit-show/{unit_id}")
    Call<JsonObject> showUnit(
            @Header("Authorization") String token,
            @Path("unit_id") int unit_id);

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
    @Multipart
    @POST("v4/add-product")
    Call<JsonObject> addProduct(
            @Header("Authorization") String token,
            @Part("user_id") RequestBody id,
            @Part("name_en") RequestBody name_en,
            @Part("qty") RequestBody qty,
            @Part("sell_price") RequestBody sell_price,
            @Part("video") RequestBody video,
            @Part("des_en") RequestBody des_en,
            @Part("category_id") RequestBody category_id,
            @Part("parent_category") RequestBody parent_category,
            @Part("sub_id") RequestBody sub_id,
            @Part("braind_id") RequestBody braind_id,
            @Part("supplier_id") RequestBody supplier_id,
            @Part("unit_id") RequestBody unit_id,
            @Part MultipartBody.Part[] image);

    //editprofile
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

    //getitemorder
    @GET("v4/product-all-item/{shop_id}/{user_id}")
    Call<OrderModel> getItemOrderAll(
            @Header("Authorization") String token,
            @Path("shop_id") int shop_id,
            @Path("user_id") int user_id
    );

    //getitemorder-pending
    @GET("v4/product-pending-item/{shop_id}/{user_id}")
    Call<OrderModel> getItemOrderPending(
            @Header("Authorization") String token,
            @Path("shop_id") int shop_id,
            @Path("user_id") int user_id
    );

    //getitemorder-shipping
    @GET("v4/product-shipping-item/{shop_id}/{user_id}")
    Call<OrderModel> getItemOrderShipping(
            @Header("Authorization") String token,
            @Path("shop_id") int shop_id,
            @Path("user_id") int user_id
    );

    //getitemorder-delivery
    @GET("v4/product-delivery-item/{shop_id}/{user_id}")
    Call<OrderModel> getItemOrderDelivery(
            @Header("Authorization") String token,
            @Path("shop_id") int shop_id,
            @Path("user_id") int user_id
    );

    //getitemorder-canceled
    @GET("v4/product-canceled-item/{shop_id}/{user_id}")
    Call<OrderModel> getItemOrderCanceled(
            @Header("Authorization") String token,
            @Path("shop_id") int shop_id,
            @Path("user_id") int user_id
    );

    @Multipart
    @POST("v4/profile-edit-profile")
    Call<JsonObject> editProfile(
            @Header("Authorization") String token,
            @Part MultipartBody.Part image,
            @Part("user_id") RequestBody user_id
    );

    @Multipart
    @POST("v4/shop-edit-logo")
    Call<JsonObject> editLogo(
            @Header("Authorization") String token,
            @Part MultipartBody.Part image,
            @Part("shop_id") RequestBody shop_id
    );


    @Multipart
    @POST("v4/shop-edit-cover")
    Call<JsonObject> editCover(
            @Header("Authorization") String token,
            @Part MultipartBody.Part image,
            @Part("shop_id") RequestBody shop_id
    );

    //getgallery
    @GET("v4/gallery/{product_id}")
    Call<GalleryModel> getGallery(
            @Header("Authorization") String token,
            @Path("product_id") int product_id
    );

    //deleteproduct
    @FormUrlEncoded
    @POST("v4/delete-product")
    Call<JsonObject> deleteProduct(
            @Header("Authorization") String token,
            @Field("id") int id
    );

    //editcategory
    @FormUrlEncoded
    @POST("v4/product-edit-category")
    Call<JsonObject> editProductCategory(
            @Header("Authorization") String token,
            @Field("id") int id,
            @Field("category_id") int category_id,
            @Field("parent_id") int parent_id,
            @Field("sub_id") int sub_id
    );

    //editname
    @FormUrlEncoded
    @POST("v4/product-edit-name")
    Call<JsonObject> editProductName(
            @Header("Authorization") String token,
            @Field("id") int id,
            @Field("name") String name
    );

    //editqty
    @FormUrlEncoded
    @POST("v4/product-edit-qty")
    Call<JsonObject> editProductQty(
            @Header("Authorization") String token,
            @Field("id") int id,
            @Field("qty") int qty
    );

    //editsaleprice
    @FormUrlEncoded
    @POST("v4/product-edit-sellprice")
    Call<JsonObject> editProductSellPrice(
            @Header("Authorization") String token,
            @Field("id") int id,
            @Field("price") int price
    );

    //editbrand
    @FormUrlEncoded
    @POST("v4/product-edit-brand")
    Call<JsonObject> editProductBrand(
            @Header("Authorization") String token,
            @Field("id") int id,
            @Field("brand_id") int brand_id
    );

    //editsupplier
    @FormUrlEncoded
    @POST("v4/product-edit-supplier")
    Call<JsonObject> editProductSupplier(
            @Header("Authorization") String token,
            @Field("id") int id,
            @Field("supplier_id") int supplier_id
    );

    //editunit
    @FormUrlEncoded
    @POST("v4/product-edit-unit")
    Call<JsonObject> editProductUnit(
            @Header("Authorization") String token,
            @Field("id") int id,
            @Field("unit_id") int unit_id
    );

    //editvideo
    @FormUrlEncoded
    @POST("v4/product-edit-video")
    Call<JsonObject> editProductVideo(
            @Header("Authorization") String token,
            @Field("id") int id,
            @Field("video") String video
    );

    //editdes
    @FormUrlEncoded
    @POST("v4/product-edit-des")
    Call<JsonObject> editProductDes(
            @Header("Authorization") String token,
            @Field("id") int id,
            @Field("des") String des
    );
}
