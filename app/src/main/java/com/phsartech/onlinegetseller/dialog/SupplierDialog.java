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
import com.phsartech.onlinegetseller.adapter.SupplierAdapter;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnButtonSupplierClick;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnButtonSupplierDialogClick;
import com.phsartech.onlinegetseller.model.SupplierModel;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierDialog extends DialogFragment implements CallBackFunctionOnButtonSupplierDialogClick {

    private CallBackFunctionOnButtonSupplierClick callBackFunctionOnButtonSupplierClick;
    private RecyclerView recyclerView;
    private List<SupplierModel.DataEntity> list;
    private SupplierAdapter supplierAdapter;
    private static String TAG = "SupplierDialog";
    private Toolbar toolbar;

    public SupplierDialog(CallBackFunctionOnButtonSupplierClick callBackFunctionOnButtonSupplierClick) {
        this.callBackFunctionOnButtonSupplierClick = callBackFunctionOnButtonSupplierClick;
    }

    public static SupplierDialog display(FragmentManager fragmentManager, CallBackFunctionOnButtonSupplierClick callBackFunctionOnButtonSupplierClick) {
        SupplierDialog exampleDialog = new SupplierDialog(callBackFunctionOnButtonSupplierClick);
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

    private void setupToolbar() {
        if (toolbar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setTitle("Supplier");
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
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
        ApiHelper.getService().getSupplier().enqueue(new Callback<SupplierModel>() {
            @Override
            public void onResponse(Call<SupplierModel> call, Response<SupplierModel> response) {
                if (response.body().getData() != null) {
                    list = response.body().getData();
                    setView();
                }
            }

            @Override
            public void onFailure(Call<SupplierModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void setView() {
        supplierAdapter = new SupplierAdapter(getActivity(), list, this);
        recyclerView.setAdapter(supplierAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onButtonSupplierClick(SupplierModel.DataEntity item) {
        callBackFunctionOnButtonSupplierClick.onButtonSupplierClick(item);
        dismiss();
    }
}
