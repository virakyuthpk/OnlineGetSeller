package com.phsartech.onlinegetseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UnitModel {
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
        @SerializedName("unitID")
        private int id;

        @Expose
        @SerializedName("nameEn")
        private String nameEn;

        @Expose
        @SerializedName("nameKh")
        private String nameKh;

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
    }
}