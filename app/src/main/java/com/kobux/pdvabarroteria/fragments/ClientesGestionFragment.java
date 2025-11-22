package com.kobux.pdvabarroteria.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kobux.pdvabarroteria.R;
import com.kobux.pdvabarroteria.models.ClienteModel;
import com.kobux.pdvabarroteria.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientesGestionFragment extends Fragment {

    private boolean esNuevo;
    private long clienteId;
    private EditText txtNombres;
    private EditText txtApellidos;
    private EditText txtDireccion;
    private EditText txtNit;
    private EditText txtCorreoElectronico;
    private Button buttonGuardar;
    private Button buttonCancelar;
    private ImageView buttonRefrescar;
    private RetrofitClient retrofit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_clientes_gestion, container, false);

        if (getArguments() != null) {
            esNuevo = getArguments().getBoolean("esNuevo", true);
            clienteId = getArguments().getLong("idCliente", 0);
        }

        retrofit = RetrofitClient.getInstance();
        txtNombres = view.findViewById(R.id.txt_nombres);
        txtApellidos = view.findViewById(R.id.txt_apellidos);
        txtDireccion = view.findViewById(R.id.txt_direccion);
        txtNit = view.findViewById(R.id.txt_nit);
        txtCorreoElectronico = view.findViewById(R.id.txt_correoElectronico);
        buttonGuardar = view.findViewById(R.id.buttonGuardarCambios);
        buttonCancelar = view.findViewById(R.id.buttonCancelar);
        buttonRefrescar = view.findViewById(R.id.buttonRefrescarCliente);

        TextView titulo = view.findViewById(R.id.txt_titulo);

        if (esNuevo){
            titulo.setText("CREAR NUEVO CLIENTE");
            buttonRefrescar.setVisibility(View.GONE);
        }
        else {
            titulo.setText("EDITAR CLIENTE");
            cargarCliente();
        }

        buttonGuardar.setOnClickListener(v -> guardarCliente());
        buttonCancelar.setOnClickListener(v -> requireActivity().finish());
        buttonRefrescar.setOnClickListener(v -> {
            buttonRefrescar.setVisibility(View.GONE);
            cargarCliente();
        });

        return view;
    }

    private void cargarCliente() {
        retrofit.getApi().getClienteById(clienteId)
                .enqueue(new Callback<ClienteModel>() {
                    @Override
                    public void onResponse(Call<ClienteModel> call, Response<ClienteModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ClienteModel c = response.body();
                            txtNombres.setText(c.getNombres());
                            txtApellidos.setText(c.getApellidos());
                            txtDireccion.setText(c.getDireccion());
                            txtNit.setText(c.getNit());
                            txtCorreoElectronico.setText(c.getCorreoElectronico());
                            buttonGuardar.setEnabled(true);
                            buttonCancelar.setEnabled(true);
                            Toast.makeText(getContext(), " datos cargado correctamente", Toast.LENGTH_SHORT).show();
                            buttonRefrescar.setVisibility(View.VISIBLE);
                        } else {
                            try {
                                String error = response.errorBody().string();
                                Log.e("ClientesGestion", "Error cargarCliente: " + error);
                                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Log.e("ClientesGestion", "Excepción leyendo errorBody", e);
                                Toast.makeText(getContext(), "Error al cargar cliente", Toast.LENGTH_SHORT).show();
                            }
                            requireActivity().finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ClienteModel> call, Throwable t) {
                        Log.e("ClientesGestion", "Fallo cargarCliente: " + t.getMessage(), t);
                        Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        requireActivity().finish();
                    }
                });
    }

    private void guardarCliente() {

        String nombres = txtNombres.getText().toString().trim();
        String apellidos = txtApellidos.getText().toString().trim();
        String direccion = txtDireccion.getText().toString().trim();
        String nit = txtNit.getText().toString().trim();
        String correo = txtCorreoElectronico.getText().toString().trim();

        if (nombres.isEmpty()) {
            txtNombres.setError("Ingrese los nombres");
            txtNombres.requestFocus();
            return;
        }

        if (apellidos.isEmpty()) {
            txtApellidos.setError("Ingrese los apellidos");
            txtApellidos.requestFocus();
            return;
        }

        if (direccion.isEmpty()) {
            txtDireccion.setError("Ingrese la dirección");
            txtDireccion.requestFocus();
            return;
        }

        if (nit.isEmpty()) {
            nit = "CF";
            txtNit.setText("CF");
        }

        ClienteModel req = new ClienteModel();
        req.setNombres(nombres);
        req.setApellidos(apellidos);
        req.setDireccion(direccion);
        req.setNit(nit);
        req.setCorreoElectronico(correo);

        buttonGuardar.setEnabled(false);
        buttonCancelar.setEnabled(false);

        if (esNuevo) {

            retrofit.getApi().createCliente(req)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Cliente creado correctamente", Toast.LENGTH_SHORT).show();
                                requireActivity().setResult(Activity.RESULT_OK);
                                requireActivity().finish();
                            } else {
                                try {
                                    String error = response.errorBody().string();
                                    Log.e("ClientesGestion", "Error createCliente: " + error);
                                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Log.e("ClientesGestion", "Excepción leer errorBody createCliente", e);
                                    Toast.makeText(getContext(), "Error desconocido del servidor", Toast.LENGTH_SHORT).show();
                                }
                                buttonGuardar.setEnabled(true);
                                buttonCancelar.setEnabled(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("ClientesGestion", "Fallo createCliente: " + t.getMessage(), t);
                            Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(),	Toast.LENGTH_SHORT).show();
                            buttonGuardar.setEnabled(true);
                            buttonCancelar.setEnabled(true);
                        }
                    });

        } else {

            retrofit.getApi().updateCliente(clienteId, req)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Cliente actualizado correctamente", Toast.LENGTH_SHORT).show();
                                requireActivity().setResult(Activity.RESULT_OK);
                                requireActivity().finish();
                            } else {
                                try {
                                    String error = response.errorBody().string();
                                    Log.e("ClientesGestion", "Error updateCliente: " + error);
                                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Log.e("ClientesGestion", "Excepción leer errorBody updateCliente", e);
                                    Toast.makeText(getContext(), "Error desconocido del servidor", Toast.LENGTH_SHORT).show();
                                }
                                buttonGuardar.setEnabled(true);
                                buttonCancelar.setEnabled(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("ClientesGestion", "Fallo updateCliente: " + t.getMessage(), t);
                            Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(),	Toast.LENGTH_SHORT).show();
                            buttonGuardar.setEnabled(true);
                            buttonCancelar.setEnabled(true);
                        }
                    });
        }
    }
}