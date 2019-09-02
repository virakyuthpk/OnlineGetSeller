package com.phsartech.onlinegetseller.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.activity.AddProductActivity;
import com.phsartech.onlinegetseller.adapter.CategorySubAdapter;
import com.phsartech.onlinegetseller.callback.CallBackFucntionOnButtonCategorySubClick;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnButtonCategorySubDialogClick;
import com.phsartech.onlinegetseller.model.CategoryModel;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategorySubDialog extends DialogFragment implements CallBackFunctionOnButtonCategorySubDialogClick {

    private static String TAG = "CategoryParentDialog";
    private RecyclerView recyclerView;
    private List<CategoryModel.DataEntity> list;
    private CategorySubAdapter categorySubAdapter;
    private CategoryModel.DataEntity item, item_parent;
    private CallBackFucntionOnButtonCategorySubClick callBackFucntionOnButtonCategorySubClick;

    public CategorySubDialog(CallBackFucntionOnButtonCategorySubClick callBackFucntionOnButtonCategorySubClick, CategoryModel.DataEntity item, CategoryModel.DataEntity item_parent) {
        this.callBackFucntionOnButtonCategorySubClick = callBackFucntionOnButtonCategorySubClick;
        this.item = item;
        this.item_parent = item_parent;
    }

    public static CategorySubDialog display(CallBackFucntionOnButtonCategorySubClick callBackFucntionOnButtonCategorySubClick, FragmentManager fragmentManager, CategoryModel.DataEntity item, CategoryModel.DataEntity item_parent) {
        CategorySubDialog exampleDialog = new CategorySubDialog(callBackFucntionOnButtonCategorySubClick, item, item_parent);
        exampleDialog.show(fragmentManager, TAG);
        return exampleDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_recycler, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        getData(item.getId(), item_parent.getId());
        return view;
    }

    private void getData(int item_id, int itemparent_id) {
        ApiHelper.getService().getCategorySub(item_id, itemparent_id).enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                if (response.body().getData() != null) {
                    list = response.body().getData();
                    setView();
                }
            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void setView() {
        categorySubAdapter = new CategorySubAdapter(getActivity(), list, this);
        recyclerView.setAdapter(categorySubAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onButtonCategorySubClick(CategoryModel.DataEntity item_sub) {
        callBackFucntionOnButtonCategorySubClick.onButtonCategorySubClick(item, item_parent, item_sub);
        dismiss();
    }
}