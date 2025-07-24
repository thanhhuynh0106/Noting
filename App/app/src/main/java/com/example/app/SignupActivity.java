package com.example.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.app.api.AuthAPI;
import com.example.app.model.CaptchaResponse;
import com.example.app.model.RegisterRequest;
import com.example.app.model.ResponseDTO;
import com.example.app.model.User;
import com.example.app.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword, edtEmail, edtNumber, edtDob, edtCaptchaInput, edtFullName;
    Button btnSignup;
    ImageView imageViewCaptcha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView overlayImage = findViewById(R.id.overlayImage);

        Glide.with(this)
                .load(R.drawable.hongconen)
                .apply(new RequestOptions()
                        .override(1080, 1920)
                        .format(DecodeFormat.PREFER_RGB_565)
                )
                .into(overlayImage);

        onLoginHereClicked();

        // Register
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        edtNumber = findViewById(R.id.edtNumber);
        edtDob = findViewById(R.id.edtDob);
        edtFullName = findViewById(R.id.edtFullName);
        edtCaptchaInput = findViewById(R.id.edtCaptchaInput);
        btnSignup = findViewById(R.id.btnSignup);
        imageViewCaptcha = findViewById(R.id.imageViewCaptcha);


        loadCaptcha();
        btnSignup.setOnClickListener(v-> performSignup());

    }

    public void onLoginHereClicked() {
        TextView tvLogin = findViewById(R.id.tvLoginHere);
        tvLogin.setOnClickListener(v -> {
           Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
              startActivity(intent);
              overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
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
                        imageViewCaptcha.setImageBitmap(decodedBitmap);
                    } catch (Exception e) {
                        Log.e("CAPTCHA", "Fail decoding captcha: " + e.getMessage());
                        Toast.makeText(SignupActivity.this, "Cannot display captcha", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CaptchaResponse> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "Cannot load captcha!", Toast.LENGTH_SHORT).show();
                Log.e("CAPTCHA", "Error: " + t.getMessage());
            }
        });
    }

    public void performSignup() {
        String fullName = edtFullName.getText().toString().trim();
        String userName = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String number = edtNumber.getText().toString().trim();
        String dob = edtDob.getText().toString().trim();
        String captchaInput = edtCaptchaInput.getText().toString().trim();

        if (fullName.isEmpty() || userName.isEmpty() || password.isEmpty() || email.isEmpty() || number.isEmpty() || dob.isEmpty() || captchaInput.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest registerUser = new RegisterRequest(fullName, number, dob, email, userName, password, captchaInput);
        AuthAPI authAPI = RetrofitClient.getAuthAPI(this);
        authAPI.register(registerUser).enqueue(new Callback<ResponseDTO>() {

            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseDTO responseDTO = response.body();
                    String message = responseDTO.getMessage();

                    if ("Username already exists".equalsIgnoreCase(message)) {
                        Toast.makeText(SignupActivity.this, "Username already exists! Please choose another username!", Toast.LENGTH_SHORT).show();
                        edtUsername.requestFocus();
                        return;
                    }

                    if ("Registration successful".equalsIgnoreCase(message)) {
                        Toast.makeText(SignupActivity.this, "Sign up successful! Continue to login!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, "Sign up failed: " + message, Toast.LENGTH_SHORT).show();
                        Log.e("SIGNUP_ERROR", "Error message: " + message);
                    }
                } else {
                    Toast.makeText(SignupActivity.this, "Sign up failed: " + (response.body() != null ? response.body().getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
                    Log.e("SIGNUP_ERROR", "Response body is null or not successful");
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Log.e("SIGNUP_ERROR", "Error: " + t.getMessage());
                Toast.makeText(SignupActivity.this, "Cannot connect to server!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
