package com.adcure.deliverypartner;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference productRef;
    RecyclerView.LayoutManager layoutManager;
    private TextView txt;
    FirebaseAuth mAuth;
MaterialSearchBar materialSearchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            recyclerView = findViewById(R.id.totalorders_list);
            recyclerView.setHasFixedSize(true);
            mAuth = FirebaseAuth.getInstance();
            layoutManager = new LinearLayoutManager(this);
            materialSearchBar=findViewById(R.id.searchBar);

             materialSearchBar.addTextChangeListener(TextWatcher1);
            recyclerView.setLayoutManager(layoutManager);
            txt = (TextView) findViewById(R.id.no);
            String uid = mAuth.getCurrentUser().getUid();
            productRef = FirebaseDatabase.getInstance().getReference().child("Pharmacy").child("All Payments");
//
            onStart();
        } catch (Exception e) {
            e.getMessage();
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
    private final android.text.TextWatcher TextWatcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            if(materialSearchBar.getText().toString().equals("")){
                onStart();
            }else {
                onSearch();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() ) {
                    Query query = snapshot.getRef();
                    FirebaseRecyclerOptions<Orders> options =
                            new FirebaseRecyclerOptions.Builder<Orders>()
                                    .setQuery(productRef, Orders.class).build();
                    FirebaseRecyclerAdapter<Orders, OrderViewHolder> adapter =
                            new FirebaseRecyclerAdapter<Orders, OrderViewHolder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final Orders model) {                                    holder.itemcount.setText("Items : " + model.getItems());
                                    holder.payid.setText("Invoice id : " + model.getInvoiceid());
                                    String ordered;
                                    if (model.getDelivered().equals("y") && model.getShipped().equals("y") || model.getDelivered().equals("y")) {
                                        holder.shipped.setText("Delivered");
                                        holder.txtShip.setVisibility(View.GONE);
                                        ordered = "Ordered ---> Shipped ---> Delivered";

                                    } else if (model.getShipped().equals("y")) {
                                        holder.shipped.setText("(Shipped)");
                                        holder.txtShip.setVisibility(View.GONE);
                                        ordered = "Ordered ---> Shipped (Not Delievred Yet) ";
                                    } else {
                                        holder.shipped.setText("(Not shipped)");
                                        ordered = "Not shipped Yet";

                                    }
                                    if(model.getDelivered().equals("y")){
                                        holder.toViewPro.setVisibility(View.GONE);
                                    }

                                    holder.toViewPro.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(model.getDelivered().equals("y")){
                                                DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Users").child(model.getUid()).child("Pharmacy").child("Orders").child(model.getPaymentid());
                                                productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        productRef.child("paid").setValue(model.getPaid().replace("COD - Not paid ",""));
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                            Intent intent = new Intent(MainActivity.this, ShipOrder.class);
                                            intent.putExtra("pid", model.getPaymentid());
                                            intent.putExtra("addr", model.getGaddress());
                                            intent.putExtra("num", model.getGphone());
                                            intent.putExtra("nme", model.getGname());
                                            if(model.getPaid().contains("COD") && model.getDelivered().equals("y")){
                                                intent.putExtra("paid", model.getPaid().replace("COD - Not paid ",""));
                                            }
                                            else{
                                                intent.putExtra("paid", model.getPaid() );
                                            }
                                            intent.putExtra("uid", model.getUid());
                                            intent.putExtra("shipped", ordered);
                                            intent.putExtra("date", model.getDate());
                                            if (model.getCashback() == null) {
                                                intent.putExtra("cashback", "");
                                            } else {
                                                intent.putExtra("cashback", model.getCashback());
                                            }
                                            if (model.getInvoiceid() != null) {
                                                intent.putExtra("inv", model.getInvoiceid());
                                            }
                                            if(model.getFivepercentcoupon() == null){

                                                intent.putExtra("5%offer", "");
                                                intent.putExtra("couponamount", "");

                                            } else {
                                                intent.putExtra("5%offer", model.getFivepercentcoupon());
                                                intent.putExtra("couponamount", model.getCouponamount());

                                            }


//                                                else{
//                                                    intent.putExtra("inv",model.getInvoiceid());
//
//                                                }
                                            startActivity(intent);
                                        }
                                    });
                                }

                                @NonNull
                                @Override
                                public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_view, parent, false);
                                    OrderViewHolder viewHolder = new OrderViewHolder(view);
                                    return viewHolder;
                                }
                            };
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                } else {
//                        recyclerView.setVisibility(View.GONE);
                    txt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public  void onSearch(){
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() ) {
                    Query query = snapshot.getRef();
                    FirebaseRecyclerOptions<Orders> options =
                            new FirebaseRecyclerOptions.Builder<Orders>()
                                    .setQuery(productRef, Orders.class).build();
                    FirebaseRecyclerAdapter<Orders, OrderViewHolder> adapter =
                            new FirebaseRecyclerAdapter<Orders, OrderViewHolder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final Orders model) {


                                    holder.itemcount.setText("Items : " + model.getItems());
                                    holder.payid.setText("Invoice id : " + model.getInvoiceid());
                                    String ordered;
                                    if (model.getDelivered().equals("y") && model.getShipped().equals("y") || model.getDelivered().equals("y")) {
                                        holder.shipped.setText("Delivered");
                                        holder.txtShip.setVisibility(View.GONE);
                                        ordered = "Ordered ---> Shipped ---> Delivered";

                                    } else if (model.getShipped().equals("y")) {
                                        holder.shipped.setText("(Shipped)");
                                        holder.txtShip.setVisibility(View.GONE);
                                        ordered = "Ordered ---> Shipped (Not Delievred Yet) ";
                                    } else {
                                        holder.shipped.setText("(Not shipped)");
                                        ordered = "Not shipped Yet";

                                    }
                                    if(model.getDelivered().equals("y")){
                                        holder.toViewPro.setVisibility(View.GONE);
                                    }
                                    if(model.getInvoiceid().equals(materialSearchBar.getText().toString())){
                                        holder.ll_h.setVisibility(View.VISIBLE);
                                    }else{
                                        holder.ll_h.setVisibility(View.GONE);

                                    }

                                    holder.toViewPro.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(model.getDelivered().equals("y")){
                                                DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Users").child(model.getUid()).child("Pharmacy").child("Orders").child(model.getPaymentid());
                                                productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        productRef.child("paid").setValue(model.getPaid().replace("COD - Not paid ",""));
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                            Intent intent = new Intent(MainActivity.this, ShipOrder.class);
                                            intent.putExtra("pid", model.getPaymentid());
                                            intent.putExtra("addr", model.getGaddress());
                                            intent.putExtra("num", model.getGphone());
                                            intent.putExtra("nme", model.getGname());
                                            if(model.getPaid().contains("COD") && model.getDelivered().equals("y")){
                                                intent.putExtra("paid", model.getPaid().replace("COD - Not paid ",""));}
                                            else{
                                                intent.putExtra("paid", model.getPaid() );
                                            }
                                            intent.putExtra("uid", model.getUid());
                                            intent.putExtra("shipped", ordered);
                                            intent.putExtra("date", model.getDate());
                                            if (model.getCashback() == null) {
                                                intent.putExtra("cashback", "");
                                            } else {
                                                intent.putExtra("cashback", model.getCashback());
                                            }
                                            if (model.getInvoiceid() != null) {
                                                intent.putExtra("inv", model.getInvoiceid());
                                            }
                                            if(model.getFivepercentcoupon() == null){

                                                intent.putExtra("5%offer", "");
                                                intent.putExtra("couponamount", "");

                                            } else {
                                                intent.putExtra("5%offer", model.getFivepercentcoupon());
                                                intent.putExtra("couponamount", model.getCouponamount());

                                            }


//                                                else{
//                                                    intent.putExtra("inv",model.getInvoiceid());
//
//                                                }
                                            startActivity(intent);
                                        }
                                    });
                                }

                                @NonNull
                                @Override
                                public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_view, parent, false);
                                    OrderViewHolder viewHolder = new OrderViewHolder(view);
                                    return viewHolder;
                                }
                            };
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                } else {
//                        recyclerView.setVisibility(View.GONE);
                    txt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}