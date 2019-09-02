package com.phsartech.onlinegetseller.callback;

import com.phsartech.onlinegetseller.model.CategoryModel;

public interface CallBackFucntionOnButtonCategorySubClick {
    void onButtonCategorySubClick(CategoryModel.DataEntity item, CategoryModel.DataEntity item_parent, CategoryModel.DataEntity item_sub);
}
