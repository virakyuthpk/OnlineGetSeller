package com.phsartech.onlinegetseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GalleryModel {
    @Expose
    @SerializedName("gallery")
    private List<DataEntity> gallery;

    public class DataEntity {

        @Expose
        @SerializedName("id")
        private int id;

        @Expose
        @SerializedName("path")
        private String path;

        @Expose
        @SerializedName("galleryable_id")
        private String galleryable_id;

        @Expose
        @SerializedName("galleryable_type")
        private String galleryable_type;

        @Expose
        @SerializedName("status")
        private String status;

        @Expose
        @SerializedName("caption")
        private String caption;

        @Expose
        @SerializedName("created_at")
        private String created_at;

        @Expose
        @SerializedName("updated_at")
        private String updated_at;

        public int getId() {
            return id;
        }

        public String getPath() {
            return path;
        }

        public String getGalleryable_id() {
            return galleryable_id;
        }

        public String getGalleryable_type() {
            return galleryable_type;
        }

        public String getStatus() {
            return status;
        }

        public String getCaption() {
            return caption;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }

    public List<DataEntity> getGallery() {
        return gallery;
    }
}