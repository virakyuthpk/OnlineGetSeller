package com.phsartech.onlinegetseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderModel {

    @Expose
    @SerializedName("success")
    private String success;

    @Expose
    @SerializedName("data")
    private List<Data> dataList;

    public class Data {

        @Expose
        @SerializedName("id")
        private int id;

        @Expose
        @SerializedName("user_id")
        private int user_id;

        @Expose
        @SerializedName("shop_id")
        private int shop_id;

        @Expose
        @SerializedName("province_id")
        private int province_id;

        @Expose
        @SerializedName("district_id")
        private int district_id;

        @Expose
        @SerializedName("variant_id")
        private int variant_id;

        @Expose
        @SerializedName("product_id")
        private int product_id;

        @Expose
        @SerializedName("f_name")
        private String f_name;

        @Expose
        @SerializedName("l_name")
        private String l_name;

        @Expose
        @SerializedName("email")
        private String email;

        @Expose
        @SerializedName("phone")
        private String phone;

        @Expose
        @SerializedName("address")
        private String address;

        @Expose
        @SerializedName("qty")
        private int qty;

        @Expose
        @SerializedName("subtotal")
        private int subtotal;

        @Expose
        @SerializedName("amout")
        private int amout;

        @Expose
        @SerializedName("dis_price")
        private int dis_price;

        @Expose
        @SerializedName("des")
        private String des;

        @Expose
        @SerializedName("stage")
        private String stage;

        @Expose
        @SerializedName("created_at")
        private String created_at;

        @Expose
        @SerializedName("updated_at")
        private String updated_at;

        @Expose
        @SerializedName("user_name")
        private String user_name;

        @Expose
        @SerializedName("user_image")
        private String user_image;

        @Expose
        @SerializedName("count")
        private int count;

        @Expose
        @SerializedName("name")
        private String Pname;

        public String getPname() {
            return Pname;
        }

        public String getPimage() {
            return Pimage;
        }

        @Expose
        @SerializedName("image")
        private String Pimage;

        public int getId() {
            return id;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getShop_id() {
            return shop_id;
        }

        public int getProvince_id() {
            return province_id;
        }

        public int getDistrict_id() {
            return district_id;
        }

        public int getVariant_id() {
            return variant_id;
        }

        public int getProduct_id() {
            return product_id;
        }

        public String getF_name() {
            return f_name;
        }

        public String getL_name() {
            return l_name;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getAddress() {
            return address;
        }

        public int getQty() {
            return qty;
        }

        public int getSubtotal() {
            return subtotal;
        }

        public int getAmout() {
            return amout;
        }

        public int getDis_price() {
            return dis_price;
        }

        public String getDes() {
            return des;
        }

        public String getStage() {
            return stage;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getUser_image() {
            return user_image;
        }

        public int getCount() {
            return count;
        }
    }

    public String getSuccess() {
        return success;
    }

    public List<Data> getDataList() {
        return dataList;
    }
}
