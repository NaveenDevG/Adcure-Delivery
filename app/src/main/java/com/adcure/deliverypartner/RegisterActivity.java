package com.adcure.deliverypartner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {
    private static  boolean IMAGE_FRONT =false,IMAGE_BACK=false ;
    private static  int GALLERYINTENT2 =3,GALLERYINTENT1=2 ;
    private EditText edtname, edtemail, edtpass, edtcnfpass, edtnumber;
    private String check, name, email, password, mobile, profile;
    private String productRandomKey, DownloadUri,DownloadUri2,DownloadUri1;

    CircleImageView image;
    private FirebaseUser currentUser;
    long timerForOTP;
    CountDownTimer countDownTimer;
    private static int GALLERYINTENT = 1;
    KProgressHUD progressDialog;
    private StorageTask uploadTask,uploadTask1,uploadTask2;
    private Uri imageUri,imageUri1,imageUri2;
    private StorageReference storageReference;
    long timerForCountDown = 30000; //300000 for 5 minshouse_verification_customers/post_customer_loan_hv_status
    boolean isCountDownTimerIsRunning = false;
    ImageView upload;
    private FirebaseAuth mAuth;
    //  RequestQueue requestQueue;
    boolean IMAGE_STATUS = false;
    boolean STATUS = false;
    private DatabaseReference rootRef;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerification;
    Button otpbtn;
    Bitmap profilePicture;
    private String doctorRandomKey;
     public static final String TAG = "MyTag";
    private Button bt_resend_OTP, but_OTP_cancel;
    private String str_CustomerOTP;
    private TextView tv_timer;
    private Button bt_verify_OTP;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String pic="";Button button;
private ImageButton frontABtn,backABtn;
ImageView frontView,backView;
boolean otpStatus=false;
    private String currentUserId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      try{  super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        rootRef = FirebaseDatabase.getInstance().getReference();
frontABtn=findViewById(R.id.imgfrontbtn);
backABtn=findViewById(R.id.imgbackUpload);
frontView=findViewById(R.id.imgFrontView);
backView=findViewById(R.id.imgBackView);
frontABtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
OpenGallery1();
    }
});
backABtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
OpenGallery2();
              }
          });
        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        TextView appname = findViewById(R.id.appname);
        appname.setTypeface(typeface);
        upload = findViewById(R.id.uploadpic);
        storageReference = FirebaseStorage.getInstance().getReference().child("Deliver Agent info");

        image = findViewById(R.id.profilepic);
        edtname = findViewById(R.id.name);
        edtemail = findViewById(R.id.email);
        edtpass = findViewById(R.id.password);
        edtcnfpass = findViewById(R.id.confirmpassword);
        edtnumber = findViewById(R.id.number);
//          edtnumber.addTextChangedListener(TextWatcher1);

        otpbtn = (Button) findViewById(R.id.bt_primaryMobile_verify);

        otpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtpass.setVisibility(View.GONE);
                edtcnfpass.setVisibility(View.GONE);
                mobile = "+91" + edtnumber.getText().toString();

                if (STATUS){
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            mobile, 60, TimeUnit.SECONDS, RegisterActivity.this, callbacks
                    );
                }
                else if (validateName() && validateEmail()  ) {
                    rootRef.child("Agents").child(edtnumber.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!snapshot.exists()){
                                if (IMAGE_STATUS){
                                    if(IMAGE_FRONT && IMAGE_BACK) {
                                        name = edtname.getText().toString();
                                        email = edtemail.getText().toString();
                                        password = edtcnfpass.getText().toString();
                                        mobile = "+91" + edtnumber.getText().toString();


                                        progressDialog.show();
                                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                                mobile, 60, TimeUnit.SECONDS, RegisterActivity.this, callbacks
                                        );
                                    }else{
                                        Adialog.showAlert("Alert", "Make sure that you uploaded AAdhaar photos..", RegisterActivity.this);

                                    }
                                }else{
                                    Adialog.showAlert("Alert", "Please Upload your photo", RegisterActivity.this);
                                }
                                }
                            else{
                                Adialog.showAlert("Alert","Please use another mobile number.. This number is already regestered..",RegisterActivity.this);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    })  ;

                } else {
                    Adialog.showAlert("Alert", "please fill all the details", RegisterActivity.this);
                    // Toasty.info(RegisterActivity.this,"please fill all the details ",Toast.LENGTH_LONG).show();

                }

            }
        });
        progressDialog = KProgressHUD.create(RegisterActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        edtname.addTextChangedListener(nameWatcher);
        edtemail.addTextChangedListener(emailWatcher);
        edtpass.addTextChangedListener(passWatcher);
        edtcnfpass.addTextChangedListener(cnfpassWatcher);
        edtnumber.addTextChangedListener(numberWatcher);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // requestQueue = Volley.newRequestQueue(RegisterActivity.this);

        //validate user details and register user

        button = findViewById(R.id.register);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    progressDialog.show();
                    //TODO AFTER VALDATION

                    if ( validateName() && validateEmail() && validateNumber() ) {
                        if(validatePhoto()){
                            if(otpStatus){


                            name = edtname.getText().toString();
                            email = edtemail.getText().toString();
                            password = edtcnfpass.getText().toString();
                            mobile = edtnumber.getText().toString();


                            rootRef.child("Agents").child(mobile).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(!snapshot.exists()){
                                   uploadPhotos(); }
                                    else{
                                        Adialog.showAlert("Alert","Please use another mobile number.. This number is already regestered..",RegisterActivity.this);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            })  ;
                            //Validation Success


                            progressDialog.show();
                        }
                        else{
                                progressDialog.dismiss();
                                Adialog.showAlert("Alert","Please verify the mobile number..",RegisterActivity.this);
                        }           }
                        else{
                            progressDialog.dismiss();
                            Adialog.showAlert("Alert","Please upload all photos..",RegisterActivity.this);
                        }
                    } else {
                        progressDialog.dismiss();
                        Adialog.showAlert("Alert", "Please fill all details correctly", RegisterActivity.this);
                    }
                }
                catch (Exception e){
                    e.getMessage();
                }
            }
        });

        //Take already registered user to Register page


        //take user to reset password


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CropImage.activity()
//                        .setAspectRatio(1, 1)
//                        .start(RegisterActivity.this);
                OpenGallery();
//                Dexter.withActivity(RegisterActivity.this)
//                        .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        .withListener(new MultiplePermissionsListener() {
//                            @Override
//                            public void onPermissionsChecked(MultiplePermissionsReport report) {
//                                // check if all permissions are granted
//                                if (report.areAllPermissionsGranted()) {
//                                    progressDialog.show();
//                                    // do you work now
//                                       }
//
//                                // check for permanent denial of any permission
//                                if (report.isAnyPermissionPermanentlyDenied()) {
//                                    // permission is denied permenantly, navigate user to app settings
//                                    Snackbar.make(view, "Kindly grant Required Permission", Snackbar.LENGTH_LONG)
//                                            .setAction("Allow", null).show();
//                                }
//                            }
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                                token.continuePermissionRequest();
//                            }
//                        })
//                        .onSameThread()
//                        .check();


                //result will be available in onActivityResult which is overridden
            }
        });


        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
 otpStatus=true;
 signInWithPhoneauthCredential(phoneAuthCredential);
            }


            public void onCodeSent(String VerificationId, PhoneAuthProvider.ForceResendingToken token) {

                mVerification = VerificationId;
                mResendToken = token;
                progressDialog.dismiss();
                otpRequest1();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        };}
      catch (Exception e){
        e.printStackTrace();
    }

}

    private boolean validatePhoto() {
        if(!IMAGE_FRONT || !IMAGE_BACK || !IMAGE_STATUS){
            return false;
        }
        else{
            return true;
        }
    }

    private void sendUserToLogin() {
    }

    private void OpenGallery() {

        Intent galleryintent = new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent, GALLERYINTENT);
    }
    private void OpenGallery1() {

        Intent galleryintent1 = new Intent();
        galleryintent1.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent1.setType("image/*");
        startActivityForResult(galleryintent1, GALLERYINTENT1);
    }
    private void OpenGallery2() {

        Intent galleryintent2 = new Intent();
        galleryintent2.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent2.setType("image/*");
        startActivityForResult(galleryintent2, GALLERYINTENT2);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERYINTENT && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            image.setImageURI(imageUri);
            IMAGE_STATUS = true;
        }    if (requestCode == GALLERYINTENT1 && resultCode == RESULT_OK && data != null) {
            imageUri1 = data.getData();
            frontView.setImageURI(imageUri1);
            IMAGE_FRONT = true;
        }   if (requestCode == GALLERYINTENT2 && resultCode == RESULT_OK && data != null) {
            imageUri2 = data.getData();
            backView.setImageURI(imageUri2);
            IMAGE_BACK = true;
        }
    }

//    private void signInWithPhoneauthCredential(PhoneAuthCredential credential) {
//
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//
//        firebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//                        if (task.isSuccessful()) {
//
//                            final String currentUserid = mAuth.getCurrentUser().getUid();
//                            final String currentPhone = mAuth.getCurrentUser().getPhoneNumber();
//                            final String phone = edtnumber.getText().toString();
//                            if(STATUS==true){
//                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                                startActivity(intent);
//
//                                finish();
//                                Toasty.info(RegisterActivity.this, "Logged in..", Toast.LENGTH_LONG).show();
//                            }
//
//                            else if (imageUri != null) {
//                                final StorageReference filePath = storageReference.child(mAuth.getCurrentUser().getUid() + ".jpg");
//                                uploadTask = filePath.putFile(imageUri);
//                                uploadTask.addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                String msg = e.toString();
//                                                Toast.makeText(RegisterActivity.this, "Error:" + msg, Toast.LENGTH_SHORT).show();
//                                            }
//                                        })
//                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                            @Override
//                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                                Toast.makeText(RegisterActivity.this, "Profile Image Uploaded Successfully...", Toast.LENGTH_SHORT).show();
//                                                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                                                    @Override
//                                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                                                        if (!task.isSuccessful()) {
//                                                            throw task.getException();
//                                                        }
//                                                        DownloadUri = filePath.getDownloadUrl().toString();
//                                                        return filePath.getDownloadUrl();
//                                                    }
//                                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Uri> task) {
//                                                        if (task.isSuccessful()) {
//                                                            DownloadUri = task.getResult().toString();
//                                                            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                                @Override
//                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                                    if (!(snapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("Details").exists())) {
//                                                                        HashMap<String, Object> hashMap1 = new HashMap<>();
//                                                                        String token1 = FirebaseInstanceId.getInstance().getToken();
//                                                                        hashMap1.put("name", name);
//                                                                        hashMap1.put("token", token1);
//                                                                        hashMap1.put("phone", edtnumber.getText().toString());
//                                                                        hashMap1.put("email", edtemail.getText().toString());
//                                                                        hashMap1.put("image", DownloadUri);
//                                                                        hashMap1.put("otp","1");
//                                                                        String saveCurrentTime, savecurrentDate;
//                                                                        Calendar calendar = Calendar.getInstance();
//                                                                        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
//                                                                        savecurrentDate = currentDate.format(calendar.getTime());
//
//                                                                        rootRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("Details").updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                            @Override
//                                                                            public void onComplete(@NonNull Task<Void> task) {
//
//                                                                                if (task.isSuccessful()) {
//                                                                                    rootRef.child("Total Users").child(edtnumber.getText().toString()).updateChildren(hashMap1);
//                                                                                    rootRef.child("Today Users").child(savecurrentDate).child(edtnumber.getText().toString()).updateChildren(hashMap1);
//
//                                                                                    progressDialog.dismiss();
//
//                                                                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                                                                                    startActivity(intent);
//
//                                                                                    finish();
//                                                                                    Toasty.info(RegisterActivity.this, "Logged in..", Toast.LENGTH_LONG).show();
//
//                                                                                    //                                                         Intent mintent=new Intent(RegisterActivity.this,HomeActivity.class);
////                                                         mintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                                                         startActivity(mintent);
////                                                         finish();
//                                                                                } else {
//                                                                                    Adialog.showAlert("Alert", "check your internet connection", RegisterActivity.this);
//
//                                                                                    progressDialog.dismiss();
//                                                                                }
//
//                                                                            }
//                                                                        });
//                                                                        progressDialog.show();
//                                                                    } else {
//                                                                        progressDialog.dismiss();
//                                                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                                                                        startActivity(intent);
//
//                                                                        finish();
//                                                                        Toasty.info(RegisterActivity.this, "Logged in..", Toast.LENGTH_LONG).show();
//
//                                                                    }
//                                                                    progressDialog.dismiss();
//
//                                                                }
//
//                                                                @Override
//                                                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                                                }
//                                                            });
//
//                                                        } else {
//                                                            progressDialog.dismiss();
//                                                            Toast.makeText(RegisterActivity.this, "Error same", Toast.LENGTH_SHORT).show();
//
//                                                        }
//                                                    }
//                                                });
//                                            }
//                                        });
//                            }
//                            else {
//                                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        if (!(snapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("Details").exists())) {
//                                            HashMap<String, Object> hashMap1 = new HashMap<>();
//                                            String token1 = FirebaseInstanceId.getInstance().getToken();
//                                            hashMap1.put("name", name);
//                                            hashMap1.put("token", token1);
//                                            hashMap1.put("phone", edtnumber.getText().toString());
//                                            hashMap1.put("email", edtemail.getText().toString());
//                                            hashMap1.put("image", "");
//                                            hashMap1.put("otp","1");
//                                            String saveCurrentTime, savecurrentDate;
//                                            Calendar calendar = Calendar.getInstance();
//                                            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
//                                            savecurrentDate = currentDate.format(calendar.getTime());
//
//                                            rootRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("Details").updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//
//                                                    if (task.isSuccessful()) {
//                                                        rootRef.child("Total Users").child(edtnumber.getText().toString()).updateChildren(hashMap1);
//                                                        rootRef.child("Today Users").child(savecurrentDate).child(edtnumber.getText().toString()).updateChildren(hashMap1);
//
//                                                        progressDialog.dismiss();
//
//                                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                                                        startActivity(intent);
//
//                                                        finish();
//                                                        Toasty.info(RegisterActivity.this, "Logged in..", Toast.LENGTH_LONG).show();
//
//                                                        //                                                         Intent mintent=new Intent(RegisterActivity.this,HomeActivity.class);
////                                                         mintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                                                         startActivity(mintent);
////                                                         finish();
//                                                    } else {
//                                                        Adialog.showAlert("Alert", "check your internet connection", RegisterActivity.this);
//
//                                                        progressDialog.dismiss();
//                                                    }
//
//                                                }
//                                            });
//                                            progressDialog.show();
//                                        } else {
//                                            progressDialog.dismiss();
//                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                                            startActivity(intent);
//
//                                            finish();
//                                            Toasty.info(RegisterActivity.this, "Logged in..", Toast.LENGTH_LONG).show();
//
//                                        }
//                                        progressDialog.dismiss();
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//
//                            }
//                            ////Toast.makeText(OtpActivity.this, "Congratulations,you're logged in successfully..", Toast.LENGTH_SHORT).show();
//                            //   sendUserToMainActivity();
//                        } else {
//                            String msg = task.getException().toString();
//                            Toast.makeText(RegisterActivity.this, "Error:" + msg, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//        progressDialog.show();
////
//    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        RegisterActivity.this.startActivity(intent);
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
            if (validateNumber()) {
                String num = edtnumber.getText().toString();
                try {

//                    updateInfoDisplayProfile(image, edtname, edtnumber, edtemail);
                }catch (Exception e){
                    e.getMessage();
                }
            }

        }
    };

    private void updateInfoDisplayProfile(final CircleImageView changeImage,
                                          final EditText userNameChange, final EditText phonenumChange,
                                          final EditText addressChange) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Total Users").child(edtnumber.getText().toString());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String name = snapshot.child("name").getValue().toString();
                    String phone = snapshot.child("phone").getValue().toString();

                    String email = snapshot.child("email").getValue().toString();
                    if(!snapshot.child("image").getValue().toString().equals("")){

                        String image = snapshot.child("image").getValue().toString();
                        pic= snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(changeImage);

                        IMAGE_STATUS=true;

                    }else{
                        if(snapshot.child("otp").getValue().equals("0")){
                            upload.setEnabled(false);
                        }
                    }
                    edtcnfpass.setVisibility(View.GONE);
                    edtpass.setVisibility(View.GONE);
                    otpbtn.setVisibility(View.VISIBLE);
                    if(snapshot.child("otp").getValue().equals("0")){
                        otpbtn.setVisibility(View.GONE);
                        button.setEnabled(false);
                        button.setBackgroundResource(R.drawable.txtviewbg);
                        button.setVisibility(View.VISIBLE);
                        button.setText("Already Registered..Please Login");

//                       Toast.makeText(RegisterActivity.this, "User already Registered..Please Go to Login", Toast.LENGTH_SHORT).show();
                    }

                    STATUS=true;
                    //    imageUri=snapshot.child("image").getValue().toString();
                    userNameChange.setText(name);
                    phonenumChange.setText(phone);
                    addressChange.setText(email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void convertBitmapToString(Bitmap profilePicture) {
            /*
                Base64 encoding requires a byte array, the bitmap image cannot be converted directly into a byte array.
                so first convert the bitmap image into a ByteArrayOutputStream and then convert this stream into a byte array.
            */
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        profilePicture.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] array = byteArrayOutputStream.toByteArray();
        profile = Base64.encodeToString(array, Base64.DEFAULT);
    }


    private boolean validateProfile() {
        if (!IMAGE_STATUS  )
            Toasty.info(RegisterActivity.this, "Select A Profile Picture", Toast.LENGTH_LONG).show();
        return IMAGE_STATUS;
    }

    private boolean validateNumber() {

        check = edtnumber.getText().toString();
        Log.e("inside number", check.length() + " ");
        if (check.length() > 10) {
            return false;

        } else if (check.length() < 10) {
            return false;
        }
        return true;
    }

    private boolean validateCnfPass() {

        check = edtcnfpass.getText().toString();

        return check.equals(edtpass.getText().toString());
    }

    private boolean validatePass() {


        check = edtpass.getText().toString();

        if (check.length() < 4 || check.length() > 20) {
            return false;
        } else if (!check.matches("^[A-za-z0-9@]+")) {
            return false;
        }
        return true;
    }

    private boolean validateEmail() {

        check = edtemail.getText().toString();

        if (check.length() < 4 || check.length() > 40) {

            return false;
        } else if (!check.matches("^[A-za-z0-9.@]+")) {

            return false;
        } else if (!check.contains("@") || !check.contains(".")) {
            return false;
        }

        return true;
    }

    private boolean validateName() {

        check = edtname.getText().toString();

        return !(check.length() < 4 || check.length() > 20);

    }

    //TextWatcher for Name -----------------------------------------------------

    TextWatcher nameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (check.length() < 4 || check.length() > 20) {
                edtname.setError("Name Must consist of 4 to 20 characters");
            }
        }

    };

    //TextWatcher for Email -----------------------------------------------------

    TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (check.length() < 4 || check.length() > 40) {
                edtemail.setError("Email Must consist of 4 to 20 characters");
            } else if (!check.matches("^[A-za-z0-9.@]+")) {
                edtemail.setError("Only . and @ characters allowed");
            } else if (!check.contains("@") || !check.contains(".")) {
                edtemail.setError("Enter Valid Email");
            }

        }

    };

    //TextWatcher for pass -----------------------------------------------------

    TextWatcher passWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (check.length() < 4 || check.length() > 20) {
                edtpass.setError("Password Must consist of 4 to 20 characters");
            } else if (!check.matches("^[A-za-z0-9@]+")) {
                edtemail.setError("Only @ special character allowed");
            }
        }

    };

    //TextWatcher for repeat Password -----------------------------------------------------

    TextWatcher cnfpassWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (!check.equals(edtpass.getText().toString())) {
                edtcnfpass.setError("Both the passwords do not match");
            }
        }

    };

    public void otpRequest1() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.otppreview, null);
            builder.setView(view);
            builder.setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            EditText tv_OTPCode = (EditText) view.findViewById(R.id.tv_OTPCodeForVerification);
            bt_verify_OTP = (Button) view.findViewById(R.id.but_Verify_OTP);
            bt_resend_OTP = (Button) view.findViewById(R.id.but_Resend_OTP);
            but_OTP_cancel = (Button) view.findViewById(R.id.but_OTP_cancel);
            tv_timer = (TextView) view.findViewById(R.id.tv_timer);
            bt_resend_OTP.setVisibility(View.GONE);

            // bln_anum = true;
            bt_verify_OTP.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View view) {
                                                     progressDialog.show();
                                                     String VerificationCode = tv_OTPCode.getText().toString();
                                                     if (TextUtils.isEmpty(VerificationCode)) {

                                                         progressDialog.dismiss();
                                                         Adialog.showAlert("Alert", "Please write verification code..", RegisterActivity.this);
                                                     } else {

                                                         progressDialog.dismiss();
otpStatus=true;
alertDialog.dismiss();
Adialog.showAlert("Alert", "Verification done..", RegisterActivity.this);
edtnumber.setEnabled(false);
edtnumber.setBackgroundResource(R.drawable.txtviewbg);
otpbtn.setEnabled(false);
                                                         otpbtn.setBackgroundResource(R.drawable.txtviewbg);
// Toast.makeText(RegisterActivity.this, mVerification, Toast.LENGTH_SHORT).show();
//                                                         PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerification, VerificationCode);
//                                                         signInWithPhoneauthCredential(phoneAuthCredential);


                                                     }
                                                 }
                                             }
            );
            bt_resend_OTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    countDownTimer();
                    progressDialog.show();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            mobile, 60, TimeUnit.SECONDS, RegisterActivity.this, callbacks
                    );
                    //  progressDialog.dismiss();

                    //  sendOTP();
                    //   otpAlert = "";
                    // isAlternateOTPFirstTime = 1;
                    bt_resend_OTP.setVisibility(View.GONE);
                }
            });
            countDownTimer();

            but_OTP_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bt_resend_OTP.setVisibility(View.GONE);
                    //  otpAlert = "";
                    alertDialog.cancel();
                    if (isCountDownTimerIsRunning) {
                        countDownTimer.cancel();
                        countDownTimer.onFinish();
                    }
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void countDownTimer() {
        try {
            timerForOTP = System.currentTimeMillis();
            if (isCountDownTimerIsRunning) {
                countDownTimer.cancel();
                countDownTimer.onFinish();
            }
            countDownTimer = new CountDownTimer(timerForCountDown, 1000) { //Sets 5 minutes remaining
                @Override
                public void onTick(long millisUntilFinished) {
                    try {
                        isCountDownTimerIsRunning = true;
                        String time = "" + (millisUntilFinished / 1000) / 60 + ":" + (int) (millisUntilFinished / 1000) % 60;
                        tv_timer.setText(time);
                        if (millisUntilFinished <= 120000) { //60000 (1 Min)
                            try {
                                tv_timer.setTextColor(Color.RED);
                                tv_timer.setText(time);
                            } catch (Exception e) {
                                e.getMessage();
                            }
                        } else {
                            tv_timer.setText(time);
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                @Override
                public void onFinish() {
                    isCountDownTimerIsRunning = false;
                    tv_timer.setText("Time up!");
                    bt_resend_OTP.setVisibility(View.VISIBLE);
                }
            }.start();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }
    //TextWatcher for Mobile -----------------------------------------------------

    TextWatcher numberWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (check.length() > 10) {
                edtnumber.setError("Number cannot be grated than 10 digits");
                otpbtn.setBackgroundResource(R.drawable.txtviewbg);
                // otpbtn.setTextColor(R.color.white);
                otpbtn.setEnabled(false);
            } else if (check.length() < 10) {
                edtnumber.setError("Number should be 10 digits");
                otpbtn.setBackgroundResource(R.drawable.txtviewbg);
                //  otpbtn.setTextColor(R.color.white);
                otpbtn.setEnabled(false);
            } else {
                Adialog.hideKeyboard(RegisterActivity.this);
                otpbtn.setBackgroundResource(R.color.white);
                otpbtn.setTextColor(R.color.buttonColor);
                otpbtn.setEnabled(true);
                if (validateNumber()) {
                    String num = edtnumber.getText().toString();
                    try {

//                        updateInfoDisplayProfile(image, edtname, edtnumber, edtemail);
                    }catch (Exception e){
                        e.getMessage();
                    }
                }
            }
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    public void goRegister(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
    }

    public void toLogin(View view) {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    public void toForgot(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.reset,null,false);
        final EditText mail = v.findViewById(R.id.remail);
        builder.setView(v);
        builder.setCancelable(false);
        builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String umail = mail.getText().toString();
                if (TextUtils.isEmpty(umail)){
                    Toast.makeText(RegisterActivity.this, "mail cant be empty",
                            Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.sendPasswordResetEmail(umail).addOnCompleteListener(RegisterActivity.this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Mail sent",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(RegisterActivity.this, "failed to send", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    public void uploadPhotos(){
        storageReference = FirebaseStorage.getInstance().getReference().child("Deliver Agent info").child(edtnumber.getText().toString());

        final StorageReference filePath=storageReference.child(imageUri.getLastPathSegment()+" "+(edtnumber.getText().toString())+".jpg");
        final StorageReference filePath1=storageReference.child(imageUri1.getLastPathSegment()+" "+(edtnumber.getText().toString())+".jpg");
        final StorageReference filePath2=storageReference.child(imageUri2.getLastPathSegment()+" "+(edtnumber.getText().toString())+".jpg");
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] data = baos.toByteArray();
        final UploadTask uploadTask=filePath.putFile(imageUri);
//        UploadTask uploadTask = filePath.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String msg=e.toString();
                Toast.makeText(RegisterActivity.this,"Error:",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(RegisterActivity.this,"Profile Image Uploaded Successfully...",Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw  task.getException();
                        }
                        DownloadUri=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            DownloadUri=task.getResult().toString();
                            //         Toast.makeText(AddProductActivity.this,"got the Product Image saved to database...",Toast.LENGTH_SHORT).show();
                            saveInfoToDatabase();
                        }
                    }
                });
            }
        });

        final UploadTask uploadTask1=filePath1.putFile(imageUri1);
        uploadTask1.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String msg=e.toString();
                Toast.makeText(RegisterActivity.this,"Error:"+msg,Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask1=uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw  task.getException();
                        }
                        DownloadUri1=filePath1.getDownloadUrl().toString();
                        return filePath1.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            DownloadUri1=task.getResult().toString();
                            Toast.makeText(RegisterActivity.this,"got the Aadhaar front image saved to database...",Toast.LENGTH_SHORT).show();
                            saveInfoToDatabase();
                        }
                    }
                });
            }
        });
        final UploadTask uploadTask2=filePath2.putFile(imageUri2);

        uploadTask2.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String msg=e.toString();
                Toast.makeText(RegisterActivity.this,"Error:",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask2=uploadTask2.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw  task.getException();
                        }
                        DownloadUri2=filePath2.getDownloadUrl().toString();
                        return filePath2.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            DownloadUri2=task.getResult().toString();
                            Toast.makeText(RegisterActivity.this,"got the Aadhaar back image saved to database...",Toast.LENGTH_SHORT).show();
                            saveInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveInfoToDatabase() {
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                mAuth.getCurrentUser().getUid();
                if (!(snapshot.child("Agents").child(edtnumber.getText().toString()).exists())) {
                    HashMap<String, Object> hashMap1 = new HashMap<>();
                    String token1 = FirebaseInstanceId.getInstance().getToken();
                    hashMap1.put("name", name);
                    hashMap1.put("token", token1);
                    hashMap1.put("phone", edtnumber.getText().toString());
                    hashMap1.put("email", edtemail.getText().toString());
                    hashMap1.put("image", DownloadUri);
                    hashMap1.put("aadhaar1", DownloadUri1);
                    hashMap1.put("aadhaar2", DownloadUri2);
                    String saveCurrentTime, savecurrentDate;
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
                    savecurrentDate = currentDate.format(calendar.getTime());

                    rootRef.child("Delivery Agents").child(currentUserId).child("Details").updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                rootRef.child("Agents").child(edtnumber.getText().toString()).updateChildren(hashMap1);
//                                rootRef.child("Today Agents").child(savecurrentDate).child(edtnumber.getText().toString()).updateChildren(hashMap1);

                                progressDialog.dismiss();

                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);

                                finish();
                                Toasty.info(RegisterActivity.this, "Logged in..", Toast.LENGTH_LONG).show();

                                //                                                         Intent mintent=new Intent(RegisterActivity.this,HomeActivity.class);
//                                                         mintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                         startActivity(mintent);
//                                                         finish();
                            } else {
                                Adialog.showAlert("Alert", "check your internet connection", RegisterActivity.this);

                                progressDialog.dismiss();
                            }

                        }
                    });
                    progressDialog.show();
                } else {
                    progressDialog.dismiss();
//                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                    startActivity(intent);

//                    finish();
                    Toasty.info(RegisterActivity.this, "Account exists....", Toast.LENGTH_LONG).show();

                }
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void signInWithPhoneauthCredential(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            currentUserId=firebaseAuth.getCurrentUser().getUid();
                            progressDialog.dismiss();
                        } else {
                            String msg = task.getException().toString();
                            Toast.makeText(RegisterActivity.this, "Error:" + msg, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
        progressDialog.show();
//
    }

}