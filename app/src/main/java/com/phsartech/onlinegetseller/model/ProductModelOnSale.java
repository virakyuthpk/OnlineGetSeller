package com.phsartech.onlinegetseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductModelOnSale {
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
        @SerializedName("pcode")
        private String pcode;

        @Expose
        @SerializedName("category_id")
        private int category_id;

        @Expose
        @SerializedName("parent_category")
        private int parent_category;

        @Expose
        @SerializedName("user_id")
        private int user_id;

        @Expose
        @SerializedName("sub_id")
        private int sub_id;

        @Expose
        @SerializedName("braind_id")
        private int braind_id;

        @Expose
        @SerializedName("supplier_id")
        private int supplier_id;

        @Expose
        @SerializedName("unit_id")
        private int unit_id;

        @Expose
        @SerializedName("name_en")
        private String name_en;

        @Expose
        @SerializedName("name_kh")
        private String name_kh;

        @Expose
        @SerializedName("detail_en")
        private String detail_en;

        @Expose
        @SerializedName("detail_kh")
        private String detail_kh;

        @Expose
        @SerializedName("des_en")
        private String des_en;

        @Expose
        @SerializedName("des_kh")
        private String des_kh;

        @Expose
        @SerializedName("sell_price")
        private double sell_price;

        @Expose
        @SerializedName("overall_qty")
        private int overall_qty;

        @Expose
        @SerializedName("qty")
        private int qty;

        @Expose
        @SerializedName("model")
        private String model;

        @Expose
        @SerializedName("special")
        private String special;

        @Expose
        @SerializedName("image")
        private String image;

        @Expose
        @SerializedName("video")
        private String video;

        @Expose
        @SerializedName("max_order")
        private int max_order;

        @Expose
        @SerializedName("status")
        private String status;

        public int getId() {
            return id;
        }

        public String getPcode() {
            return pcode;
        }

        public int getCategory_id() {
            return category_id;
        }

        public int getParent_category() {
            return parent_category;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getSub_id() {
            return sub_id;
        }

        public int getBraind_id() {
            return braind_id;
        }

        public int getSupplier_id() {
            return supplier_id;
        }

        public int getUnit_id() {
            return unit_id;
        }

        public String getName_en() {
            return name_en;
        }

        public String getName_kh() {
            return name_kh;
        }

        public String getDetail_en() {
            return detail_en;
        }

        public String getDetail_kh() {
            return detail_kh;
        }

        public String getDes_en() {
            return des_en;
        }

        public String getDes_kh() {
            return des_kh;
        }

        public double getSell_price() {
            return sell_price;
        }

        public int getOverall_qty() {
            return overall_qty;
        }

        public int getQty() {
            return qty;
        }

        public String getModel() {
            return model;
        }

        public String getSpecial() {
            return special;
        }

        public String getImage() {
            return image;
        }

        public String getVideo() {
            return video;
        }

        public int getMax_order() {
            return max_order;
        }

        public String getStatus() {
            return status;
        }
    }

    public String getSuccess() {
        return success;
    }

    public List<Data> getDataList() {
        return dataList;
    }
}
