package com.madret.icardadmin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.madret.icardadmin.Retrofit.ApiDeleteCard;
import com.madret.icardadmin.Retrofit.ApiServicesChangeProfileImage;
import com.madret.icardadmin.Retrofit.ChangeProfilePhoto;
import com.madret.icardadmin.Retrofit.Client;
import com.madret.icardadmin.Retrofit.DeleteResponse;
import com.madret.icardadmin.Utilites.PreferenceManger;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.madret.icardadmin.Retrofit.Client.BASE_URL;

public class CardDetailesActivity extends AppCompatActivity {

    @BindView(R.id.editText_id)AppCompatEditText EditTextId;
    @BindView(R.id.editText_fullname)AppCompatEditText EditTextFullName;
    @BindView(R.id.editText_profession)AppCompatEditText EditTextProfession;
    @BindView(R.id.editText_age)AppCompatEditText EditTextAge;
    @BindView(R.id.editText_address)AppCompatEditText EditTextAddress;
    @BindView(R.id.editText_companyname)AppCompatEditText EditTextCompanyName;
    @BindView(R.id.editText_link)AppCompatEditText EditTextLink;
    @BindView(R.id.editText_expiredate)AppCompatEditText EditTextExpireDate;
    @BindView(R.id.editText_issuedate)AppCompatEditText EditTextIssueDate;

    @BindView(R.id.button_issuedate)AppCompatButton ButtonIssueDate;
    @BindView(R.id.button_expiredate)AppCompatButton ButtonExpireDate;
    @BindView(R.id.button_update)AppCompatButton ButtonUpdate;
    @BindView(R.id.button_delete)AppCompatButton ButtonDelete;

    @BindView(R.id.imageView_person)AppCompatImageView imageView_person;
    @BindView(R.id.imageView_camera)AppCompatImageView imageView_camera;

    @BindView(R.id.constraint)
    ConstraintLayout constraint;

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.progressBar_changeimage)ProgressBar progressBarChangeImage;


    @BindView(R.id.progressBar)ProgressBar progressBar;
    private Uri mCropImageUri;

    String Id,Fullname,Profession,Age,Address,Companyname,Link,Expiredate,Issuedate,personimage,PhotoUri;
    private int mYear, mMonth, mDay;

    PreferenceManger preferenceManger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detailes);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        preferenceManger = new PreferenceManger(this);
        constraint.setOnClickListener(null);

        EditTextIssueDate.setEnabled(false);
        EditTextExpireDate.setEnabled(false);

        Intent intent                    = getIntent();
        Fullname                         = intent.getStringExtra("name");
        Companyname                      = intent.getStringExtra("companyname");
        personimage                      = intent.getStringExtra("personimage");
        Id                               = intent.getStringExtra("id");
        Profession                       = intent.getStringExtra("profession");
        Address                          = intent.getStringExtra("address");
        Link                             = intent.getStringExtra("link");
        Age                              = intent.getStringExtra("age");
        Issuedate                        = intent.getStringExtra("issuedate");
        Expiredate                       = intent.getStringExtra("expiredate");

        Glide.with(this).load(personimage).into(imageView_person);
        EditTextFullName.setText(Fullname);
        EditTextCompanyName.setText(Companyname);
        EditTextId.setText(Id);
        EditTextProfession.setText(Profession);
        EditTextAddress.setText(Address);
        EditTextLink.setText(Link);
        EditTextAge.setText(Age);
        EditTextIssueDate.setText(Issuedate);
        EditTextExpireDate.setText(Expiredate);



        ButtonIssueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CardDetailesActivity.this, R.style.datepicker,
                        new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                            {
                                EditTextIssueDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        },mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        ButtonExpireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CardDetailesActivity.this, R.style.datepicker,
                        new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                            {
                                EditTextExpireDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        ButtonDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DeleteCard(Id);
            }
        });

        imageView_camera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (CropImage.isExplicitCameraPermissionRequired(CardDetailesActivity.this))
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
                    }
                } else {
                    CropImage.startPickImageActivity(CardDetailesActivity.this);
                }
            }
        });


    }
    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri))
            {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            }
            else
            {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                Uri resultUri = result.getUri();
                PhotoUri = resultUri.getPath().toString();
//                Toast.makeText(this,PhotoUri, Toast.LENGTH_SHORT).show();
                UpdateImagePtofile(Id);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
                Toast.makeText(this,"Error Try again"+error,Toast.LENGTH_SHORT).show();
                Log.d("tag", "onActivityResult: "+error);
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CropImage.startPickImageActivity(this);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE)
        {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else
            {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }

    }
    private void startCropImageActivity(Uri imageUri)
    {
        CropImage.activity(imageUri)
                .setBorderLineColor(Color.WHITE)
                .setGuidelinesColor(Color.WHITE)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setBorderCornerColor(Color.WHITE)
                .setBorderCornerLength(45.0f)
                .start(this);

    }

    public void DeleteCard(String id)
    {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDeleteCard apiDeleteCard = retrofit.create(ApiDeleteCard.class);
        Call<DeleteResponse> call = apiDeleteCard.DeleteCard(id);
        call.enqueue(new Callback<DeleteResponse>()
        {
            @Override
            public void onResponse(Call<DeleteResponse>call, Response<DeleteResponse> response)
            {
                progressBar.setVisibility(View.GONE);
                Log.d("tag", "onResponse: "+response.code());
                if(response.body().isError())
                {
                    Toast.makeText(CardDetailesActivity.this, "Card Delete Successfully", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(CardDetailesActivity.this, "Not Deleted Try Later", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t)
            {
                progressBar.setVisibility(View.GONE);
                Log.d("tag", "onFailure: "+t.toString());
                Toast.makeText(CardDetailesActivity.this, "Poor Internet Connections", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void UpdateImagePtofile(String id)
    {
        progressBarChangeImage.setVisibility(View.VISIBLE);
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(PhotoUri);
        // Parsing any Media type file

        Log.d("tag", "Update"+Id);


        final RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        ApiServicesChangeProfileImage ApiServicesChangeProfileImage = Client.getClient().create(ApiServicesChangeProfileImage.class);

        RequestBody Id = RequestBody.create(okhttp3.MultipartBody.FORM, id);



        Call<ChangeProfilePhoto> call = ApiServicesChangeProfileImage.ChangeImageProfile(fileToUpload,filename,Id);
        call.enqueue(new Callback<ChangeProfilePhoto>()
        {
            @Override
            public void onResponse(Call<ChangeProfilePhoto> call, Response<ChangeProfilePhoto> response)
            {
                if(response.body() != null)
                {
                    if(response.body().isSuccess())
                    {
                        Log.d("tag", "onResponse: "+response.body().getMessage());
                        Toast.makeText(CardDetailesActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                        Glide.with(CardDetailesActivity.this).load(PhotoUri).into(imageView_person);

                    }
                    else
                    {
                        Log.d("tag", "onResponse: "+response.body().getMessage());
                        Toast.makeText(CardDetailesActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Log.d("tag", "response"+response.body());
                }
                progressBarChangeImage.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<ChangeProfilePhoto> call, Throwable t)
            {
                Log.d("tag", "onResponse: "+t.toString());
                progressBarChangeImage.setVisibility(View.GONE);
            }
        });
    }
//    @Override
//    public void onBackPressed()
//    {
//        super.onBackPressed();
//        Intent intent = new Intent(CardDetailesActivity.this,ShowAllCardActivity.class);
//        startActivity(intent);
//        finish();
//        overridePendingTransition(R.anim.leftrightin,R.anim.leftrightout);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.menu_signout:
                logoutUser();
                break;

            default:
                return false;


        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser()
    {
        preferenceManger.Clear();
        preferenceManger.setLogin(false);
        Intent intent = new Intent(CardDetailesActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
