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
import com.phsartech.onlinegetseller.adapter.BrandAdapter;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnButtonBrandClick;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnButtonBrandDialogClick;
import com.phsartech.onlinegetseller.model.BrandModel;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrandDialog extends DialogFragment implements CallBackFunctionOnButtonBrandDialogClick {

    private CallBackFunctionOnButtonBrandClick callBackFunctionOnButtonBrandClick;
    private RecyclerView recyclerView;
    private List<BrandModel.DataEntity> list;
    private BrandAdapter brandAdapter;
    private static String TAG = "BrandDialog";
    private Toolbar toolbar;

    public BrandDialog(CallBackFunctionOnButtonBrandClick callBackFunctionOnButtonBrandClick) {
        this.callBackFunctionOnButtonBrandClick = callBackFunctionOnButtonBrandClick;
    }

    public static BrandDialog display(FragmentManager fragmentManager, CallBackFunctionOnButtonBrandClick callBackFunctionOnButtonBrandClick) {
        BrandDialog exampleDialog = new BrandDialog(callBackFunctionOnButtonBrandClick);
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
        getData();
        return view;
    }

    private void getData() {
        ApiHelper.getService().getBrand().enqueue(new Callback<BrandModel>() {
            @Override
            public void onResponse(Call<BrandModel> call, Response<BrandModel> response) {
                if (response.body().getData() != null) {
                    list = response.body().getData();
                    setView();
                }
            }

            @Override
            public void onFailure(Call<BrandModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void setupToolbar() {
        if (toolbar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setTitle("Brand");
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
        }
    }

    private void setView() {
        brandAdapter = new BrandAdapter(getActivity(), list, this);
        recyclerView.setAdapter(brandAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onButtonBrandClick(BrandModel.DataEntity item) {
        callBackFunctionOnButtonBrandClick.onButtonBrandClick(item);
        dismiss();
    }
}
