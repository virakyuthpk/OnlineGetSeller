package com.phsartech.onlinegetseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SupplierModel {
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
        @SerializedName("supplierId")
        private int id;

        @Expose
        @SerializedName("supplierNameEn")
        private String nameEn;

        @Expose
        @SerializedName("supplierKh")
        private String nameKh;

        @Expose
        @SerializedName("phoneNumber")
        private String phone;

        @Expose
        @SerializedName("email")
        private String email;

        @Expose
        @SerializedName("address")
        private String address;

        @Expose
        @SerializedName("description")
        private String des;

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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }
    }
}