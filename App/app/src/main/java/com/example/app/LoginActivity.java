package com.example.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.example.app.api.AuthAPI;
import com.example.app.model.CaptchaResponse;
import com.example.app.model.LoginRequest;
import com.example.app.remote.RetrofitClient;
import com.example.app.remote.SharedPrefManager;
import com.example.app.model.ResponseDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnLogin;
    ImageView imgViewCaptcha;
    EditText edtCaptchaInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imageView = findViewById(R.id.imgLogo);
        RequestOptions options = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .override(200, 200)
                .fitCenter();

        Glide.with(this)
                .load(R.drawable.coanen)
                .apply(options)
                .into(imageView);

        onCreateAccountClicked();


        // Login
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        imgViewCaptcha = findViewById(R.id.imageViewCaptcha);
        edtCaptchaInput = findViewById(R.id.etCaptchaInput);

        loadCaptcha();
        btnLogin.setOnClickListener(v -> performLogin());
        imgViewCaptcha.setOnClickListener(v -> loadCaptcha());
    }

    private void loadCaptcha() {
        RetrofitClient.getCaptchaAPI(this).getCaptcha().enqueue(new Callback<CaptchaResponse>() {
            @Override
            public void onResponse(Call<CaptchaResponse> call, Response<CaptchaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String base64Image = response.body().getImage();
                    Log.d("CAPTCHA_BASE64", base64Image);

                    if (base64Image.startsWith("data:image")) {
                        base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
                    }

                    try {
                        byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        imgViewCaptcha.setImageBitmap(decodedBitmap);
                    } catch (Exception e) {
                        Log.e("CAPTCHA", "Fail decoding captcha: " + e.getMessage());
                        Toast.makeText(LoginActivity.this, "Cannot display captcha", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CaptchaResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Cannot load captcha!", Toast.LENGTH_SHORT).show();
                Log.e("CAPTCHA", "Error: " + t.getMessage());
            }
        });
    }



    public void onCreateAccountClicked() {
        TextView textView = findViewById(R.id.tvRegister);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }

    private void performLogin() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String captchaInput = edtCaptchaInput.getText().toString().trim();

        LoginRequest loginRequest = new LoginRequest(username, password, "USER", captchaInput);
        AuthAPI authAPI = RetrofitClient.getAuthAPI(this);

        authAPI.login(loginRequest).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, retrofit2.Response<ResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseDTO res = response.body();
                    String message = res.getMessage() != null ? res.getMessage() : "";

                    if ("Invalid captcha".equalsIgnoreCase(message)) {
                        Toast.makeText(LoginActivity.this, "Wrong captcha!", Toast.LENGTH_SHORT).show();
                        loadCaptcha();
                        return;
                    }

                    if ("Invalid username or password".equalsIgnoreCase(message)) {
                        Toast.makeText(LoginActivity.this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (res.getToken() != null) {
                        SharedPrefManager.getInstance(LoginActivity.this)
                                .saveUser(res.getToken(), res.getUserName(), res.getUserRole());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error response from server!", Toast.LENGTH_SHORT).show();
                    Log.e("Login", "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Log.e("Login", "Error: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Cannot connect to server!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}