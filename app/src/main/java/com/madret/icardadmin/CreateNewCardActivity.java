package com.madret.icardadmin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.madret.icardadmin.Retrofit.ApiRequestQuota;
import com.madret.icardadmin.Retrofit.ApiServiceCreateCard;
import com.madret.icardadmin.Retrofit.ApiServiceRegisteration;
import com.madret.icardadmin.Retrofit.Client;
import com.madret.icardadmin.Retrofit.NewCardPojo;
import com.madret.icardadmin.Retrofit.RequestPojo;
import com.madret.icardadmin.Retrofit.ResgisterationPojo;
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

public class CreateNewCardActivity extends AppCompatActivity {
    @BindView(R.id.constraint)
    ConstraintLayout constraint;
    @BindView(R.id.EditText_identificationcode)
    AppCompatEditText EditTextIdCode;
    @BindView(R.id.EditText_fullname)
    AppCompatEditText EditTextFullName;
    @BindView(R.id.EditText_profession)
    AppCompatEditText EditTextProfession;
    @BindView(R.id.EditText_age)
    AppCompatEditText EditTextAge;
    @BindView(R.id.EditText_address)
    AppCompatEditText EditTextAddress;
    @BindView(R.id.EditText_companyname)
    AppCompatEditText EditTextCompanyName;
    @BindView(R.id.EditText_companySite)
    AppCompatEditText EditTextCompanyWebLink;
    @BindView(R.id.EditText_issuedate)
    AppCompatEditText EditTextIssuedate;
    @BindView(R.id.EditText_expiredate)
    AppCompatEditText EditTextExpiredate;
    @BindView(R.id.attachtphoto_button)
    AppCompatButton AttachButton;
    @BindView(R.id.createcard_button)
    AppCompatButton CreateButton;
    @BindView(R.id.button_issue)
    Button IssueButton;
    @BindView(R.id.button_expiredate)
    Button ExpireButton;
    private int mYear, mMonth, mDay;
    private Uri mCropImageUri;
    String IdentificationCode,FullName,Profession,Age,Address,CompanyName,CompanyLink,IssueDate,ExpireDate,PhotoUri,Adminphone;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.progressBar)ProgressBar progressBar;
    @BindView(R.id.usedcard)TextView usedcard;
    @BindView(R.id.totalcard)TextView totalcard;
    PreferenceManger preferenceManger;
    int UsedCount,TotalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_card);
        ButterKnife.bind(this);
        preferenceManger = new PreferenceManger(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        constraint.setOnClickListener(null);
        EditTextExpiredate.setEnabled(false);
        EditTextIssuedate.setEnabled(false);

        TotalCount  = preferenceManger.GetTotalcard();
        totalcard.setText("T="+TotalCount);
        UsedCount = preferenceManger.GetUsedcard();
        usedcard.setText("U="+ UsedCount);


        IssueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateNewCardActivity.this, R.style.datepicker,
                        new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                            {
                                EditTextIssuedate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        },mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        ExpireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateNewCardActivity.this, R.style.datepicker,
                        new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                            {
                                EditTextExpiredate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        AttachButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {
                if (CropImage.isExplicitCameraPermissionRequired(CreateNewCardActivity.this))
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
                } else {
                    CropImage.startPickImageActivity(CreateNewCardActivity.this);
                }
            }
        });

        CreateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

              IdentificationCode = EditTextIdCode.getText().toString().trim();
              FullName           = EditTextFullName.getText().toString().trim();
              Profession         = EditTextProfession.getText().toString().trim();
              Age                = EditTextAge.getText().toString().trim();
              Address            = EditTextAddress.getText().toString().trim();
              CompanyName        = EditTextCompanyName.getText().toString().trim();
              CompanyLink        = EditTextCompanyWebLink.getText().toString().trim();
              IssueDate          = EditTextIssuedate.getText().toString().trim();
              ExpireDate         = EditTextExpiredate.getText().toString().trim();

               if(IdentificationCode.isEmpty() || FullName.isEmpty() || Profession.isEmpty() || Age.isEmpty() || Address.isEmpty() ||CompanyName.isEmpty()||CompanyLink.isEmpty() ||IssueDate.isEmpty() || ExpireDate.isEmpty())
               {
                   Snackbar snackbar = Snackbar.make(constraint,"Please Fill Empty Field", Snackbar.LENGTH_LONG);
                   View sbView = snackbar.getView();
                   TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                   textView.setTextColor(Color.parseColor("#f20707"));
                   textView.setTextSize(18);
                   snackbar.show();
                   return;
               }
               if(IdentificationCode.length() < 4)
               {
                   EditTextIdCode.setError("Code Should Be 4 digts");
                   return;
               }
               else {
                   if(preferenceManger.GetUsedcard() != preferenceManger.GetTotalcard())
                   {
                       if(PhotoUri != null)
                       {
                           CreatCard();
                       }
                       else
                       {
                           AlertMsg("Please Select An Image For This Card");
                       }
                   }
                   else
                   {
                       RequestQuota("Cards Quota Has Been Consumed..To Get More..Click Request");
                   }
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

    public void AlertMsg(String msg)
    {
        builder = new AlertDialog.Builder(CreateNewCardActivity.this);
        builder.setMessage(msg);
        builder.setNegativeButton("close", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }
    public void RequestQuota(String msg)
    {
        builder = new AlertDialog.Builder(CreateNewCardActivity.this);
        builder.setMessage(msg);
        builder.setNegativeButton("close", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

            }
        });

        builder.setPositiveButton("Request",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Adminphone         = preferenceManger.GetPhone();
                String quotat      = "Requested";
                if(!preferenceManger.GetStatus().equals(quotat))
                {
                    RequestQuoata(quotat,Adminphone);
                }
                else
                {
                    Toast.makeText(CreateNewCardActivity.this, "You Have Pending Request", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(CreateNewCardActivity.this,DashBoardActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.leftrightin,R.anim.leftrightout);
    }


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
        Intent intent = new Intent(CreateNewCardActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void CreatCard()
    {
        progressBar.setVisibility(View.VISIBLE);
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(PhotoUri);
        // Parsing any Media type file
        final RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        ApiServiceCreateCard getResponse = Client.getClient().create(ApiServiceCreateCard.class);

        IdentificationCode = EditTextIdCode.getText().toString().trim();
        FullName           = EditTextFullName.getText().toString().trim();
        Profession         = EditTextProfession.getText().toString().trim();
        CompanyName        = EditTextCompanyName.getText().toString().trim();
        Age                = EditTextAge.getText().toString().trim();
        Address            = EditTextAddress.getText().toString().trim();
        IssueDate          = EditTextIssuedate.getText().toString().trim();
        ExpireDate         = EditTextExpiredate.getText().toString().trim();
        CompanyLink        = EditTextCompanyWebLink.getText().toString().trim();
        Adminphone         = preferenceManger.GetPhone();


        RequestBody full_name = RequestBody.create(okhttp3.MultipartBody.FORM, FullName);
        RequestBody profession = RequestBody.create(okhttp3.MultipartBody.FORM, Profession);
        RequestBody Company_name = RequestBody.create(okhttp3.MultipartBody.FORM, CompanyName);
        RequestBody age = RequestBody.create(okhttp3.MultipartBody.FORM, Age);
        RequestBody address = RequestBody.create(okhttp3.MultipartBody.FORM, Address);
        RequestBody issudate = RequestBody.create(okhttp3.MultipartBody.FORM, IssueDate);
        RequestBody expiredate = RequestBody.create(okhttp3.MultipartBody.FORM, ExpireDate);
        RequestBody identificationcode = RequestBody.create(okhttp3.MultipartBody.FORM, IdentificationCode);
        RequestBody companylink = RequestBody.create(okhttp3.MultipartBody.FORM, CompanyLink);
        RequestBody  adminphone = RequestBody.create(okhttp3.MultipartBody.FORM, Adminphone);


        Call<NewCardPojo> call = getResponse.uploadFile(fileToUpload,filename,full_name,profession,Company_name,age,address,issudate,expiredate,identificationcode,companylink,adminphone);
        call.enqueue(new Callback<NewCardPojo>()
        {
            @Override
            public void onResponse(Call<NewCardPojo> call, Response<NewCardPojo> response)
            {
                if(response.body() != null)
                {
                    if(response.body().isSuccess())
                    {
                        Log.d("tag", "onResponse: "+response.body().getMessage());
                        Toast.makeText(CreateNewCardActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                        preferenceManger.SaveUsedCard(++UsedCount);
                        usedcard.setText("U="+preferenceManger.GetUsedcard());
                    }
                    else
                    {
                        Log.d("tag", "onResponse: "+response.body().getMessage());
                        Toast.makeText(CreateNewCardActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Log.d("tag", "response"+response.body());
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<NewCardPojo> call, Throwable t)
            {
                Log.d("tag", "onResponse: "+t.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void RequestQuoata(String quoata,String phone)
    {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiRequestQuota apiRequestQuota = retrofit.create(ApiRequestQuota.class);
        Call<RequestPojo> call = apiRequestQuota.getRequest(quoata,phone);
        call.enqueue(new Callback<RequestPojo>()
        {
            @Override
            public void onResponse(Call<RequestPojo> call, retrofit2.Response<RequestPojo> response)
            {
                Log.d("tag", "onResponse: "+response.code());
                progressBar.setVisibility(View.GONE);
                if(response.body().isError())
                {
                    preferenceManger.SaveStatus("Requested");
                    Toast.makeText(CreateNewCardActivity.this,response.body().getErrorMsg(),Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(CreateNewCardActivity.this,response.body().getErrorMsg(),Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<RequestPojo> call, Throwable t)
            {
                progressBar.setVisibility(View.GONE);
                Log.d("tag", "onFailure: "+t.toString());
                AlertMsg("Check Your Internet Connection");
            }
        });
    }
}
