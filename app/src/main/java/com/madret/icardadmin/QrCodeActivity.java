package com.madret.icardadmin;

import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.madret.icardadmin.Retrofit.ApiCreateQrcode;
import com.madret.icardadmin.Retrofit.ApiServiceCreateCard;
import com.madret.icardadmin.Retrofit.Client;
import com.madret.icardadmin.Retrofit.CreateQrcodePojo;
import com.madret.icardadmin.Retrofit.NewCardPojo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class QrCodeActivity extends AppCompatActivity {

    @BindView(R.id.EditText_identificationcode)
    EditText EditTextIdCode;
    @BindView(R.id.EditText_Name)
    EditText EditTextName;
    @BindView(R.id.EditText_Profession)
    EditText EditTextProfession;
    @BindView(R.id.EditText_companyname)
    EditText EditTextCompanyName;
    @BindView(R.id.EditText_link)
    EditText EditTextLink;
    @BindView(R.id.EditText_email)
    EditText EditTextEmail;
    @BindView(R.id.EditText_phonenumber)
    EditText EditTextPhoneNumber;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.create_button)
    Button generatebutton;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.progressBar_changeimage)
    ProgressBar progressBar2;

    String IdCode, Name, Profession, CompanyName, Link, Email, PhoneNumber, Path;
    Bitmap bitmap;
    private static final String IMAGE_DIRECTORY = "/QRImages";
    boolean automaticChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        generatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdCode = EditTextIdCode.getText().toString().trim();
                Name = EditTextName.getText().toString().trim();
                Profession = EditTextProfession.getText().toString().trim();
                CompanyName = EditTextCompanyName.getText().toString().trim();
                Link = EditTextLink.getText().toString().trim();
                Email = EditTextEmail.getText().toString().trim();
                PhoneNumber = EditTextPhoneNumber.getText().toString().trim();
                if (IdCode.isEmpty() || Name.isEmpty() || Profession.isEmpty() || CompanyName.isEmpty() || Link.isEmpty() || Email.isEmpty() || PhoneNumber.isEmpty()) {
                    Toast.makeText(QrCodeActivity.this, "Fill Empty Field", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(Name + ":" + Profession + ":" + CompanyName + ":" + Link + ":" + Email + ":" + PhoneNumber, BarcodeFormat.QR_CODE, 200, 200);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        Path = saveImage(bitmap);
                        if (!Path.isEmpty()) {
                            CreateQR(Path);
                        } else {
                            Toast.makeText(QrCodeActivity.this, "not genertrate correctly", Toast.LENGTH_SHORT).show();
                        }
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                automaticChanged = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                automaticChanged = false;

            }

            @Override
            public void afterTextChanged(Editable s) {
                // do stuff
                automaticChanged = true;
            }
        };

        EditTextIdCode.addTextChangedListener(textWatcher);
        EditTextIdCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && automaticChanged == true) {
                    String code = EditTextIdCode.getText().toString();
                    if (!code.isEmpty()) {
//                        GetQRCode(code);
                    } else {
                        Toast.makeText(QrCodeActivity.this, "Empty filed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public String saveImage(Bitmap myBitmap)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public void CreateQR(String path) {
        progressBar.setVisibility(View.VISIBLE);
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(path);
        // Parsing any Media type file
        final RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        ApiCreateQrcode getResponse = Client.getClient().create(ApiCreateQrcode.class);


        RequestBody identificationcode = RequestBody.create(okhttp3.MultipartBody.FORM, IdCode);
        RequestBody persname = RequestBody.create(okhttp3.MultipartBody.FORM, Name);
        RequestBody job = RequestBody.create(okhttp3.MultipartBody.FORM, Profession);
        RequestBody companyname = RequestBody.create(okhttp3.MultipartBody.FORM, CompanyName);
        RequestBody link = RequestBody.create(okhttp3.MultipartBody.FORM, Link);
        RequestBody email = RequestBody.create(okhttp3.MultipartBody.FORM, Email);
        RequestBody phone = RequestBody.create(okhttp3.MultipartBody.FORM, PhoneNumber);

        Call<CreateQrcodePojo> call = getResponse.uploadFile(fileToUpload, filename, identificationcode,persname,job,companyname,link,email,phone);
        call.enqueue(new Callback<CreateQrcodePojo>() {
            @Override
            public void onResponse(Call<CreateQrcodePojo> call, Response<CreateQrcodePojo> response) {
                if (response.body() != null) {
                    if (response.body().isSuccess()) {
                        Log.d("tag", "onResponse: " + response.body().getMessage());
                        Toast.makeText(QrCodeActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                    } else {
                        Log.d("tag", "onResponse: " + response.body().getMessage());
                        Toast.makeText(QrCodeActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("tag", "response" + response.body());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CreateQrcodePojo> call, Throwable t) {
                Log.d("tag", "onResponse: " + t.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

//    public void GetCardData(String code)
//    {
//        progressBar2.setVisibility(View.VISIBLE);
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        apiserver3 Response = retrofit.create(apiserver3.class);
//        Call<ServerResponse2> call = Response.getData(code);
//        call.enqueue(new Callback<ServerResponse2>() {
//            @Override
//            public void onResponse(Call<ServerResponse2> call, retrofit2.Response<ServerResponse2> response) {
//                progressBar2.setVisibility(View.GONE);
//                if (response.body().isResponse())
//                {
//                    fullname.setText(response.body().getInformation().getFullName());
//                    profession.setText(response.body().getInformation().getProfession());
//                    companyname.setText(response.body().getInformation().getCompanyName());
//                    age.setText(response.body().getInformation().getAge());
//                    address.setText(response.body().getInformation().getAddress());
//                    issuedate.setText(response.body().getInformation().getIssueDate());
//                    expiredate.setText(response.body().getInformation().getExpireDate());
//                    update.setVisibility(View.VISIBLE);
//                    CreateNewCard.setVisibility(View.GONE);
//                } else {
//                    fullname.setText("");
//                    profession.setText("");
//                    companyname.setText("");
//                    age.setText\("");
//                    address.setText("");
//                    issuedate.setText("");
//                    expiredate.setText("");
//                    update.setVisibility(View.GONE);
//                    CreateNewCard.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ServerResponse2> call, Throwable t) {
//                progressBar2.setVisibility(View.GONE);
//                Toast.makeText(QrCodeActivity.this, "Internet Access Very Poor" + "\n" + t.toString(), Toast.LENGTH_LONG).show();
//                Log.d("tag", "onFailure: " + t.toString());
//            }
//        });
//    }
}