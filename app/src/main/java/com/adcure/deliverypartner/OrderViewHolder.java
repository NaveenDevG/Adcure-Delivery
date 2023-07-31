package com.adcure.deliverypartner;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView payid, shipped, itemcount, txtShip;
    public Button toViewPro;
    public LinearLayout ll_h;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        payid = itemView.findViewById(R.id.orderId);
        shipped = itemView.findViewById(R.id.notDeliverd);
        itemcount = itemView.findViewById(R.id.txtItemscount);
        toViewPro = itemView.findViewById(R.id.ordersItems);
        txtShip = itemView.findViewById(R.id.txtNotshipp);
        ll_h = itemView.findViewById(R.id.ll_h);
    }

    @Override
    public void onClick(View v) {

    }
}
