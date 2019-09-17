package com.phsartech.onlinegetseller.callback;

import com.phsartech.onlinegetseller.model.OrderModel;

public interface CallBackFucntionAcceptShipping {
    void acceptShipping(OrderModel.Data item);
}
