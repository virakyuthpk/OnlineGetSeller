package com.phsartech.onlinegetseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CategoryModel implements Serializable {
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
        @SerializedName("cateID")
        private int id;

        @Expose
        @SerializedName("titleEn")
        private String titleEn;

        @Expose
        @SerializedName("titleKh")
        private String icon;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitleEn() {
            return titleEn;
        }

        public void setTitleEn(String titleEn) {
            this.titleEn = titleEn;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}