package com.phsartech.onlinegetseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BrandModel {
    @Expose
    @SerializedName("data")
    private List<DataEntity> data;

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public class DataEntity {

        @Expose
        @SerializedName("productId")
        private int id;

        @Expose
        @SerializedName("productNameEn")
        private String nameEn;

        @Expose
        @SerializedName("productKh")
        private String nameKh;

        @Expose
        @SerializedName("featureImage")
        private String image;

        @Expose
        @SerializedName("website")
        private String website;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

        public String getNameKh() {
            return nameKh;
        }

        public void setNameKh(String nameKh) {
            this.nameKh = nameKh;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }
    }
}