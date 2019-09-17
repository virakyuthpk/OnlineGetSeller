package com.phsartech.onlinegetseller.dialog;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.adapter.CategoryParentAdapter;
import com.phsartech.onlinegetseller.callback.CallBackFucntionOnButtonCategoryParentClick;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnButtonCategoryParentDialogClick;
import com.phsartech.onlinegetseller.model.CategoryModel;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryParentDialog extends DialogFragment implements CallBackFunctionOnButtonCategoryParentDialogClick {

    private static String TAG = "CategoryParentDialog";
    private RecyclerView recyclerView;
    private List<CategoryModel.DataEntity> list;
    private CategoryParentAdapter categoryParentAdapter;
    private CategoryModel.DataEntity item;
    private CallBackFucntionOnButtonCategoryParentClick callBackFucntionOnButtonCategoryParentClick;
    private Toolbar toolbar;

    public CategoryParentDialog(CategoryModel.DataEntity item, CallBackFucntionOnButtonCategoryParentClick callBackFucntionOnButtonCategoryParentClick) {
        this.item = item;
        this.callBackFucntionOnButtonCategoryParentClick = callBackFucntionOnButtonCategoryParentClick;
    }

    public static CategoryParentDialog display(CallBackFucntionOnButtonCategoryParentClick callBackFucntionOnButtonCategoryParentClick, FragmentManager fragmentManager, CategoryModel.DataEntity item) {
        CategoryParentDialog exampleDialog = new CategoryParentDialog(item, callBackFucntionOnButtonCategoryParentClick);
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
        toolbar = view.findViewById(R.id.toolbar);
        setupToolbar();
        getData(item.getId());
        return view;
    }

    private void getData(int item_id) {
        ApiHelper.getService().getCategoryParent(item_id).enqueue(new Callback<CategoryModel>() {
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

    private void setupToolbar() {
        if (toolbar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setTitle("Category Parent");
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
        }
    }

    private void setView() {
        categoryParentAdapter = new CategoryParentAdapter(getActivity(), list, this);
        recyclerView.setAdapter(categoryParentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onButtonCategoryParentClick(CategoryModel.DataEntity item_parent) {
        callBackFucntionOnButtonCategoryParentClick.onButtonCategoryParentClick(item, item_parent);
        dismiss();
    }
}
