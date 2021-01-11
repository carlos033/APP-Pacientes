package com.example.myapplication.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.api.MiApi;
import com.example.myapplication.api.NetworkClient;
import com.example.myapplication.constantes.Constantes;
import com.example.myapplication.dto.jwt.JwtRequestDTO;
import com.example.myapplication.dto.jwt.JwtResponseDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressBar loadingProgressBar;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEditText = findViewById(R.id.username);
        usernameEditText.setText("ES");
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        sharedPreferences = getSharedPreferences(Constantes.PREFERENCE_NAME, MODE_PRIVATE);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                Retrofit retrofit = NetworkClient.getRetrofitClient();
                MiApi myAPI = retrofit.create(MiApi.class);
                JwtRequestDTO jwtRequestDTO = new JwtRequestDTO();
                jwtRequestDTO.setIdentificador(usernameEditText.getText().toString());
                jwtRequestDTO.setPassword(passwordEditText.getText().toString());
                Call<JwtResponseDTO> llamadaLogin = myAPI.login(jwtRequestDTO);
                llamadaLogin.enqueue(new Callback<JwtResponseDTO>() {
                    @Override
                    public void onResponse(Call<JwtResponseDTO> call, Response<JwtResponseDTO> response) {
                        loadingProgressBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            JwtResponseDTO responseDTO = response.body();
                            guardarInfoEnPreferences(responseDTO.getJwttoken(), jwtRequestDTO.getIdentificador());
                            irMainActivity();
                        } else {
                            Toast.makeText(LoginActivity.this, "Credenciales invalidas", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JwtResponseDTO> call, Throwable t) {
                        loadingProgressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Fallo de comunicacion", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }

    private void guardarInfoEnPreferences(String jwttoken, String idUsuario) {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(Constantes.PREFERENCES_KEY_TOKEN, jwttoken);
        e.putString(Constantes.PREFERENCES_KEY_ID_USUARIO, idUsuario);
        e.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tengoToken()) {
            irMainActivity();
        }
    }

    private void irMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private boolean tengoToken() {
        return sharedPreferences.getString(Constantes.PREFERENCES_KEY_TOKEN, null) != null;
    }
}