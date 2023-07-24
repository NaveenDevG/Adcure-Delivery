package com.adcure.deliverypartner;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView productname,qty, price,dc;
    public ImageView productImage,delete;
//    public ItemClickListener listener;
    public   LinearLayout ll_imgPresc;
  public   ImageView img_pres;
    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        productImage=(ImageView)itemView.findViewById(R.id.image_cartlist);
        productname=(TextView)itemView.findViewById(R.id.cart_prtitle);
        qty=(TextView)itemView.findViewById(R.id.cart_prcount);
        price=(TextView)itemView.findViewById(R.id.cart_prprice);
          dc=(TextView)itemView.findViewById(R.id.dccart);
          ll_imgPresc=itemView.findViewById(R.id.ll_img_pres);
          img_pres=itemView.findViewById(R.id.img_pres);
      }

    @Override
    public void onClick(View v) {

    }
}
//    public void setItemClickListener(ItemClickListener listener){
//        this.listener=listener;
//    }
//    @Override
//    public void onClick(View view) {
//        listener.onClick(view,getAdapterPosition(),false);
//    }



