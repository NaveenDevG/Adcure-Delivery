package com.adcure.deliverypartner;


import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adcure.deliverypartner.Constants.AllConstants;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ShipOrder extends AppCompatActivity {
    private long count;
    private int orderfina;
    Button shipbtn;
    RecyclerView recyclerView;
    ImageView toprof;
    String add, nme, num, pid, uid, state = "";
    RecyclerView.LayoutManager layoutManager;
    private TextView paid, addr, gnme, gnum, pin;
    private String pinOrder = "";
    private ProgressDialog progressDialog;
private String invId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_ship_order);
            recyclerView = findViewById(R.id.orderlistitems);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            paid = findViewById(R.id.paid);
            addr = findViewById(R.id.addres);
            gnme = findViewById(R.id.name);
            gnum = findViewById(R.id.num);
            shipbtn = findViewById(R.id.shipbtn);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            nme = getIntent().getStringExtra("nme").toString();
            num = getIntent().getStringExtra("num").toString();
            uid = getIntent().getStringExtra("uid").toString();
            pid = getIntent().getStringExtra("pid").toString();
            invId=getIntent().getStringExtra("inv").toString();
            databaseReference.child("Order Pin").child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        Toast.makeText(ShipOrder.this, "sent", Toast.LENGTH_SHORT).show();
                        pinOrder = snapshot.child("pin").getValue().toString();

                    } else {
                        Toast.makeText(ShipOrder.this, "Seems like this order is not shipped yet..", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

//        DatabaseReference  databaseReference4 = FirebaseDatabase.getInstance().getReference().child("All Payments").child(uid);
//                            DatabaseReference db5=FirebaseDatabase.getInstance().getReference().child("Products in Sub-Category").child(model.getCategory()).child(model.getSub_category()).child(model.getPid());
//                            DatabaseReference db6=FirebaseDatabase.getInstance().getReference().child("Products in Category").child(model.getCategory()).child(model.getPid());
            DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Pharmacy").child("Orders").child(pid);
            databaseReference4.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("shipped").getValue().equals("y") && snapshot.child("delivered").getValue().equals("y")) {
                        shipbtn.setVisibility(View.GONE);
                        state = "delivered";
                    } else if (snapshot.child("shipped").getValue().equals("y")) {
                        shipbtn.setText("Delivered ?");
                        state = "shipped";
                    } else {
                        shipbtn.setVisibility(View.GONE);

                    }
//                else if(snapshot.child("shipped").equals("n") || snapshot.child("delivered").equals("n")){
//                    shipbtn.setVisibility(View.GONE);
//                }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            cartList();
            shipbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(ShipOrder.this)
                            .setMessage("Have you Delivered the product ?")
                            .setPositiveButton("Yes",
                                    (dialog, id) -> {
                                        // User wants to try giving the permissions again.
                                        databaseReference.child("Order Pin").child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    Toast.makeText(ShipOrder.this, "sent", Toast.LENGTH_SHORT).show();
                                                    if(pinOrder.equals("")){
                                                        pinOrder = snapshot.child("pin").getValue().toString();
                                                     }

                                                     getToken("Order", "Your Order pin was." + pinOrder);
                                                    otpRequest1();

                                                } else {
                                                    Toast.makeText(ShipOrder.this, "Seems like this order is not shipped yet..", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

//                                        if(state.equals("shipped")){
//
//                                            DatabaseReference  databaseReference4 = FirebaseDatabase.getInstance().getReference().child("Pharmacy").child("All Payments").child(uid);
//                                            DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Pharmacy").child("Orders").child(pid);
//                                            Calendar calendar=Calendar.getInstance();
//                                            SimpleDateFormat currentDate=new  SimpleDateFormat("MMM dd,yyyy");
//                                            String saveCurrentdate=currentDate.format(calendar.getTime());
//                                            DatabaseReference  databaseReference5= FirebaseDatabase.getInstance().getReference().child("Pharmacy").child("Today Payments");
////                            DatabaseReference db5=FirebaseDatabase.getInstance().getReference().child("Products in Sub-Category").child(model.getCategory()).child(model.getSub_category()).child(model.getPid());
////                            DatabaseReference db6=FirebaseDatabase.getInstance().getReference().child("Products in Category").child(model.getCategory()).child(model.getPid());
//                                            databaseReference4.child("delivered").setValue("y");
//                                            productRef.child("delivered").setValue("y");
//                                            databaseReference5.addValueEventListener(
//                                                    new ValueEventListener() {
//                                                        @Override
//                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                            if(snapshot.child(saveCurrentdate).child(uid).exists()){
//                                                                databaseReference5.child(saveCurrentdate).child(uid).child("delivered").setValue("y");
//                                                            }
//                                                        }
//
//                                                        @Override
//                                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                                        }
//                                                    }
//                                            );
//                                            if(getIntent().getStringExtra("paid").contains("COD")){
//                                                String paid=getIntent().getStringExtra("paid").replace("COD - Not paid ","");
//
//                                                databaseReference4.child("paid").setValue(paid);
//                                                productRef.child("paid").setValue(paid);
//
//                                                databaseReference5.addValueEventListener(
//                                                        new ValueEventListener() {
//                                                            @Override
//                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                                if(snapshot.child(saveCurrentdate).child(uid).exists()){
//                                                                    databaseReference5.child(saveCurrentdate).child(uid).child("paid").setValue(paid);
//                                                                }
//                                                            }
//
//                                                            @Override
//                                                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                                            }
//                                                        }
//                                                );
//
//                                                startActivity(getIntent());
//
//
//                                            }
//
//
////                                            startActivity(getIntent());
////                                            Toast.makeText(ShipOrder.this, "Product delivered to\n"+"User Name : "+nme+"\nUser Number : "+num+"\nUser Id : "+uid, Toast.LENGTH_SHORT).show();
//                                        }
//                                        else{
//                                            DatabaseReference  databaseReference4 = FirebaseDatabase.getInstance().getReference().child("Pharmacy").child("All Payments").child(uid);
//                                            DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Pharmacy").child("Orders").child(pid);
//                                            Calendar calendar=Calendar.getInstance();
//                                            String saveCurrentdate,saveCurentTime;
//
//                                            DatabaseReference  databaseReference5= FirebaseDatabase.getInstance().getReference().child("Pharmacy").child("Today Payments");
//                                            databaseReference4.child("shipped").setValue("y");
//                                            productRef.child("shipped").setValue("y");
//
//                                            Random rand = new Random();
//
//                                            String randomPin = String.format("%04d", rand.nextInt(10000));
//
////                                System.out.println("Random double value between 0.0 and 1.0 : " + id);
//                                            SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd,yyyy HH:mm:ss a");
//                                            saveCurrentdate=currentDate.format(calendar.getTime());
//                                            HashMap hashMap=new HashMap<>();
//                                            hashMap.put("pin",randomPin);
//                                            hashMap.put("uid",uid);
//                                            hashMap.put("date",saveCurrentdate);
//                                            hashMap.put("oid",pid);
//                                            hashMap.put("name",nme);
//                                            hashMap.put("num",num);
//                                            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
//                                            databaseReference.child("Order Pin").child(pid).updateChildren(hashMap);
//                                            databaseReference.child("Users").child(uid).child("Order Pins").child(pid).updateChildren(hashMap);
//
//                                            databaseReference4.addValueEventListener(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                    if(snapshot.child("paid").getValue().toString().contains("COD")){
//
////                                            databaseReference4.child("paid").setValue(snapshot.child("paid").getValue().toString().replace("COD - Not paid ",""));
////                                            productRef.child("paid").setValue(snapshot.child("paid").getValue().toString().replace("COD - Not paid ",""));
//
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                                }
//                                            });
//                                            databaseReference5.addValueEventListener(
//                                                    new ValueEventListener() {
//                                                        @Override
//                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                            if(snapshot.child(saveCurrentdate).child(uid).exists()){
//                                                                databaseReference5.child(saveCurrentdate).child(uid).child("shipped").setValue("y");
//                                                            }
//                                                        }
//
//                                                        @Override
//                                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                                        }
//                                                    }
//                                            );
//
//                                            getToken("Order","Your Final Order Shippeed successfully..");
//
//                                            startActivity(getIntent());
//                                            Toast.makeText(ShipOrder.this, "Product shipped successfully to\n"+"User Name : "+nme+"\nUser Number : "+num+"\nUser Id : "+uid, Toast.LENGTH_SHORT).show();
//                                        }

                                    })
                            .setNegativeButton("No",
                                    (dialog, id) -> {
                                        // User doesn't want to give the permissions.
                                        dialog.cancel();
                                    })
                            .show();
                }
            });
            if (getIntent().getStringExtra("paid").contains("COD")) {
                paid.setText(getIntent().getStringExtra("paid").toString());

            } else {
                paid.setText("Paid : Rs " + getIntent().getStringExtra("paid").toString());

            }
            addr.setText("Shipping Address : " + getIntent().getStringExtra("addr").toString());
            gnme.setText("Name : " + getIntent().getStringExtra("nme").toString());
            gnum.setText("Contact Number : " + getIntent().getStringExtra("num").toString());

        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void cartList() {
        try {


            DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Pharmacy").child("Orders").child(getIntent().getStringExtra("pid"));
            Query query = productRef.orderByChild("Carts");

            productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        FirebaseRecyclerOptions<Carts> options =
                                new FirebaseRecyclerOptions.Builder<Carts>()
                                        .setQuery(productRef, Carts.class).build();
                        FirebaseRecyclerAdapter<Carts, CartViewHolder> adapter =
                                new FirebaseRecyclerAdapter<Carts, CartViewHolder>(options) {
                                    @Override
                                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Carts model) {

                                        holder.productname.setText(model.getPname());
                                        holder.dc.setText("delivery charge : " + model.getDeliverycharge());

                                        Picasso.get().load(model.getPimg()).into(holder.productImage);

                                        holder.qty.setText("Qty : " + model.getQuantity());
                                        holder.price.setText("₹ " + model.getPrice());
                                        int onetypeProductpr = (Integer.valueOf(model.getPrice().replaceAll("\\D+", ""))) * Integer.valueOf(model.getQuantity());
                                        orderfina = orderfina + onetypeProductpr;
                                        if (model.getPresc().equals("yes")) {
                                            holder.ll_imgPresc.setVisibility(View.VISIBLE);
                                            Picasso.get().load(model.getImgPresc()).into(holder.img_pres);

                                        } else {
                                            holder.ll_imgPresc.setVisibility(View.GONE);

                                        }

                                    }


                                    @NonNull
                                    @Override
                                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                        View view = LayoutInflater.from(ShipOrder.this).inflate(R.layout.cart_item_layout, parent, false);
                                        CartViewHolder viewHolder = new CartViewHolder(view);
                                        return viewHolder;
                                    }
                                };
                        recyclerView.setAdapter(adapter);
                        adapter.startListening();
                    } else {
                        //lvnext.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("excc", e.getMessage());
            Toast.makeText(ShipOrder.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void goPath(final DatabaseReference fromPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fromPath.child("").setValue("y", new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            System.out.println("Copy failed");
                        } else {
                            System.out.println("Success");

                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//         toPath.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//for (DataSnapshot snapshot:dataSnapshot.getChildren()){
//
//    snapshot.getRef().child("ordered").setValue("y");
////    snapshot.getRef().child("paymentid").setValue(s);
//     toPath.child("Users").child(auth.getCurrentUser().getUid()).child("Orders").setValue(dataSnapshot.getValue());
//}
//}
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    private void getTokenAdmin(String title, String message) {
        try {


            DatabaseReference database = FirebaseDatabase.getInstance().getReference("AdminToken");

            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String token = snapshot.child("token").getValue().toString();

                    String uid = snapshot.child("auth").getValue().toString();

                    JSONObject to = new JSONObject();
                    JSONObject data = new JSONObject();
                    try {
                        data.put("title", title);
                        data.put("message", message);
                        data.put("hisID", uid);
                        //data.put("hisImage", myImage);
                        to.put("to", token);
                        to.put("data", data);

                        sendNotification(to);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     private void getToken(String title, String message) {
        try {


            DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Details");
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String token = snapshot.child("token").getValue().toString();


                    JSONObject to = new JSONObject();
                    JSONObject data = new JSONObject();
                    try {
                        data.put("title", title);
                        data.put("message", message);
                        data.put("hisID", uid);
                        //data.put("hisImage", myImage);
                        to.put("to", token);
                        to.put("data", data);

                        sendNotification(to);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(JSONObject to) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AllConstants.NOTIFICATION_URL, to, response -> {
            Log.d("notification", "sendNotification: " + response);
        }, error -> {
            Log.d("notification", "sendNotification: " + error);
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "key=" + AllConstants.SERVER_KEY);
                map.put("Content-Type", "application/json");
                return map;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        request.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ShipOrder.this, MainActivity.class));
        finish();
    }

    public void otpRequest1() {
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait..");
            progressDialog.setMessage("Please wait..While checking the PIN");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.verfication, null);
            builder.setView(view);
            builder.setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            EditText tv_OTPCode = (EditText) view.findViewById(R.id.tv_OTPCodeForVerification);
            Button verify = view.findViewById(R.id.but_Resend_OTP);

            Button cancel = view.findViewById(R.id.but_OTP_cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            // bln_anum = true;
            verify.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              progressDialog.show();
                                              String VerificationCode = tv_OTPCode.getText().toString();
                                              if (TextUtils.isEmpty(VerificationCode)) {

                                                  progressDialog.dismiss();
                                                  Adialog.showAlert("Alert", "Please write verification code..", ShipOrder.this);
                                              } else {
                                                  if (pinOrder.equals(VerificationCode)) {
                                                      progressDialog.dismiss();
                                                      alertDialog.dismiss();
                                                      updateStatus();
                                                  } else {
                                                      progressDialog.dismiss();
                                                      Adialog.showAlert("Alert", "Please Enter Correct Pin..", ShipOrder.this);

                                                  }
//                                                         progressDialog.dismiss();
////                                                         otpStatus=true;
//                                                         alertDialog.dismiss();
//                                                         Adialog.showAlert("Alert", "Verification done..", ShipOrder.this);
//                                                         edtnumber.setEnabled(false);
//                                                         edtnumber.setBackgroundResource(R.drawable.txtviewbg);
//                                                         otpbtn.setEnabled(false);
//                                                         otpbtn.setBackgroundResource(R.drawable.txtviewbg);
// Toast.makeText(RegisterActivity.this, mVerification, Toast.LENGTH_SHORT).show();
//                                                         PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerification, VerificationCode);
//                                                         signInWithPhoneauthCredential(phoneAuthCredential);


                                              }
                                          }
                                      }
            );

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void updateStatus() {
        if (state.equals("shipped")) {

            DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference().child("Pharmacy").child("All Payments").child(uid);
            DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Pharmacy").child("Orders").child(pid);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
            String saveCurrentdate = currentDate.format(calendar.getTime());
            DatabaseReference databaseReference5 = FirebaseDatabase.getInstance().getReference().child("Pharmacy").child("Today Payments");
//                            DatabaseReference db5=FirebaseDatabase.getInstance().getReference().child("Products in Sub-Category").child(model.getCategory()).child(model.getSub_category()).child(model.getPid());
//                            DatabaseReference db6=FirebaseDatabase.getInstance().getReference().child("Products in Category").child(model.getCategory()).child(model.getPid());
            databaseReference4.child("delivered").setValue("y");
            productRef.child("delivered").setValue("y");
            databaseReference5.addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(saveCurrentdate).child(uid).exists()) {
                                databaseReference5.child(saveCurrentdate).child(uid).child("delivered").setValue("y");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    }
            );
            if (getIntent().getStringExtra("paid").contains("COD")) {
                String paid = getIntent().getStringExtra("paid").replace("COD - Not paid ", "");

                databaseReference4.child("paid").setValue(paid);
                productRef.child("paid").setValue(paid);

                databaseReference5.addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.child(saveCurrentdate).child(uid).exists()) {
                                    databaseReference5.child(saveCurrentdate).child(uid).child("paid").setValue(paid);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        }
                );

                startActivity(getIntent());


            }
            getTokenAdmin("Order", "New Order Delivered.. Invoice Id : " +invId);
//                                            startActivity(getIntent());
            Toast.makeText(ShipOrder.this, "Product delivered to\n" + "User Name : " + nme + "\nUser Number : " + num + "\nUser Id : " + uid, Toast.LENGTH_SHORT).show();
        }
//                                        else{
//                                            DatabaseReference  databaseReference4 = FirebaseDatabase.getInstance().getReference().child("Pharmacy").child("All Payments").child(uid);
//                                            DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Pharmacy").child("Orders").child(pid);
//                                            Calendar calendar=Calendar.getInstance();
//                                            String saveCurrentdate,saveCurentTime;
//
//                                            DatabaseReference  databaseReference5= FirebaseDatabase.getInstance().getReference().child("Pharmacy").child("Today Payments");
//                                            databaseReference4.child("shipped").setValue("y");
//                                            productRef.child("shipped").setValue("y");
//
//                                            Random rand = new Random();
//
//                                            String randomPin = String.format("%04d", rand.nextInt(10000));
//
////                                System.out.println("Random double value between 0.0 and 1.0 : " + id);
//                                            SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd,yyyy HH:mm:ss a");
//                                            saveCurrentdate=currentDate.format(calendar.getTime());
//                                            HashMap hashMap=new HashMap<>();
//                                            hashMap.put("pin",randomPin);
//                                            hashMap.put("uid",uid);
//                                            hashMap.put("date",saveCurrentdate);
//                                            hashMap.put("oid",pid);
//                                            hashMap.put("name",nme);
//                                            hashMap.put("num",num);
//                                            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
//                                            databaseReference.child("Order Pin").child(pid).updateChildren(hashMap);
//                                            databaseReference.child("Users").child(uid).child("Order Pins").child(pid).updateChildren(hashMap);
//
//                                            databaseReference4.addValueEventListener(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                    if(snapshot.child("paid").getValue().toString().contains("COD")){
//
////                                            databaseReference4.child("paid").setValue(snapshot.child("paid").getValue().toString().replace("COD - Not paid ",""));
////                                            productRef.child("paid").setValue(snapshot.child("paid").getValue().toString().replace("COD - Not paid ",""));
//
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                                }
//                                            });
//                                            databaseReference5.addValueEventListener(
//                                                    new ValueEventListener() {
//                                                        @Override
//                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                            if(snapshot.child(saveCurrentdate).child(uid).exists()){
//                                                                databaseReference5.child(saveCurrentdate).child(uid).child("shipped").setValue("y");
//                                                            }
//                                                        }
//
//                                                        @Override
//                                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                                        }
//                                                    }
//                                            );
//
//                                            getToken("Order","Your Final Order Shippeed successfully..");
//
//                                            startActivity(getIntent());
//                                            Toast.makeText(ShipOrder.this, "Product shipped successfully to\n"+"User Name : "+nme+"\nUser Number : "+num+"\nUser Id : "+uid, Toast.LENGTH_SHORT).show();
//                                        }

    }
}