package com.kobux.pdvabarroteria;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.kobux.pdvabarroteria.dto.LoginResponse;
import com.kobux.pdvabarroteria.network.RetrofitClient;
import com.kobux.pdvabarroteria.utils.SessionManager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmail, txtPassword;
    private Button btnLogin;
    private SessionManager session;
    private RetrofitClient retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);

        // Si ya hay sesión → enviar al MainActivity
        if (session.haySesionActiva()) {
            irAlMain();
            return;
        }

        setContentView(R.layout.activity_login);

        retrofit = RetrofitClient.getInstance();
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> hacerLogin());
    }

    private void hacerLogin() {
        String user = txtEmail.getText().toString().trim();
        String pass = txtPassword.getText().toString().trim();

        if (user.isEmpty()) {
            txtEmail.setError("Ingresa tu usuario");
            return;
        }
        if (pass.isEmpty()) {
            txtPassword.setError("Ingresa tu contraseña");
            return;
        }
        bloquearBoton(btnLogin);
        retrofit.getApi().login(user, pass)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        Log.d("LOGIN", "Código HTTP: " + response.code());
                        String usernName = "";
                        long userId = 1;
                        if (response.isSuccessful()) {
                            try {
                                String json = response.body().string();
                                LoginResponse login = new Gson().fromJson(json, LoginResponse.class);
                                session.guardarSesion(
                                        login.getId(),
                                        login.getEmail(),
                                        login.getNombre()
                                );
                                Log.d("LOGIN", "Respuesta exitosa: ");
                            } catch (Exception ex) {
                                Log.e("LOGIN", "Error leyendo response.body(): " + ex.getMessage());
                            }
                            Toast.makeText(LoginActivity.this,
                                    "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            irAlMain();
                        } else {
                            try {
                                String error = response.errorBody() != null ?
                                        response.errorBody().string() : "null";
                                Log.e("LOGIN", "Error del servidor (" + response.code() + "): " + error);
                                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();

                            } catch (Exception e) {
                                Log.e("LOGIN", "Error leyendo errorBody(): " + e.getMessage());
                                Toast.makeText(LoginActivity.this,
                                        "Error desconocido del servidor", Toast.LENGTH_SHORT).show();
                            }
                            activarBoton(btnLogin);
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("LOGIN", "Error de conexión: " + t.getMessage(), t);
                        Toast.makeText(LoginActivity.this,
                                "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        activarBoton(btnLogin);
                    }
                });
    }

    private void irAlMain() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void bloquearBoton(Button btn) {
        btn.setEnabled(false);
        btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
    }

    private void activarBoton(Button btn) {
        btn.setEnabled(true);
        btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(btn.getContext(), R.color.colorPrimary)));
    }
}