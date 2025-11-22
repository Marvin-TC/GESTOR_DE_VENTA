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
import com.kobux.pdvabarroteria.models.ProveedorModel;
import com.kobux.pdvabarroteria.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProveedorGestionFragment extends Fragment {

    private boolean esNuevo;
    private long proveedorId;
    private EditText txtNombreEmpresa;
    private EditText txtDireccion;
    private EditText txtTelefono;
    private EditText txtNombreVendedor;
    private EditText txtTelefonoVendedor;
    private Button buttonGuardar;
    private Button buttonCancelar;
    private RetrofitClient retrofit;
    private ImageView buttonRefrescar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_proveedor_gestion, container, false);

        if (getArguments() != null) {
            esNuevo = getArguments().getBoolean("esNuevo", true);
            proveedorId = getArguments().getLong("idProveedor", 0);
        }

        retrofit = RetrofitClient.getInstance();
        txtNombreEmpresa = view.findViewById(R.id.txt_nombreEmpresa);
        txtDireccion = view.findViewById(R.id.txt_direccionProveedor);
        txtTelefono = view.findViewById(R.id.txt_telefono);
        txtNombreVendedor = view.findViewById(R.id.txt_nombreVendedor);
        txtTelefonoVendedor = view.findViewById(R.id.txt_telefonoVendedor);
        buttonGuardar = view.findViewById(R.id.buttonGuardarProveedor);
        buttonCancelar = view.findViewById(R.id.buttonCancelarProveedor);
        buttonRefrescar = view.findViewById(R.id.buttonRefrescarProveedor);
        TextView titulo = view.findViewById(R.id.txt_titulo_proveedor);

        if (esNuevo){
            titulo.setText("CREAR PROVEEDOR");
            buttonRefrescar.setVisibility(view.GONE);
        }
        else {
            titulo.setText("EDITAR PROVEEDOR");
            cargarProveedor();
        }

        buttonGuardar.setOnClickListener(v -> guardarProveedor());
        buttonCancelar.setOnClickListener(v -> requireActivity().finish());
        buttonRefrescar.setOnClickListener(v -> {
            buttonRefrescar.setVisibility(View.GONE);
            cargarProveedor();
        });

        return view;
    }

    private void cargarProveedor() {
        retrofit.getApi().getProveedorById(proveedorId)
                .enqueue(new Callback<ProveedorModel>() {
                    @Override
                    public void onResponse(Call<ProveedorModel> call, Response<ProveedorModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ProveedorModel p = response.body();
                            txtNombreEmpresa.setText(p.getNombreEmpresa());
                            txtDireccion.setText(p.getDireccion());
                            txtTelefono.setText(p.getTelefono());
                            txtNombreVendedor.setText(p.getNombreVendedor());
                            txtTelefonoVendedor.setText(p.getTelefonoVendedor());
                            buttonGuardar.setEnabled(true);
                            buttonCancelar.setEnabled(true);
                            Toast.makeText(getContext(), " datos cargado correctamente", Toast.LENGTH_SHORT).show();
                            buttonRefrescar.setVisibility(View.VISIBLE);

                        } else {
                            try {
                                String error = response.errorBody().string();
                                Log.e("ProveedorGestion", "Error cargarProveedor: " + error);
                                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Log.e("ProveedorGestion", "Excepción leyendo errorBody", e);
                                Toast.makeText(getContext(), "Error al cargar proveedor", Toast.LENGTH_SHORT).show();
                            }
                            requireActivity().finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProveedorModel> call, Throwable t) {
                        Log.e("ProveedorGestion", "Fallo cargarProveedor: " + t.getMessage(), t);
                        Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        requireActivity().finish();
                    }
                });
    }

    private void guardarProveedor() {

        String nombreEmpresa = txtNombreEmpresa.getText().toString().trim();
        String direccion = txtDireccion.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();
        String nombreVendedor = txtNombreVendedor.getText().toString().trim();
        String telefonoVendedor = txtTelefonoVendedor.getText().toString().trim();

        if (nombreEmpresa.isEmpty()) {
            txtNombreEmpresa.setError("Ingrese el nombre de la empresa");
            txtNombreEmpresa.requestFocus();
            return;
        }

        if (direccion.isEmpty()) {
            txtDireccion.setError("Ingrese la dirección");
            txtDireccion.requestFocus();
            return;
        }

        if (telefono.isEmpty()) {
            txtTelefono.setError("Ingrese el teléfono de la empresa");
            txtTelefono.requestFocus();
            return;
        }

        if (nombreVendedor.isEmpty()) {
            txtNombreVendedor.setError("Ingrese el nombre del vendedor");
            txtNombreVendedor.requestFocus();
            return;
        }

        if (telefonoVendedor.isEmpty()) {
            txtTelefonoVendedor.setError("Ingrese el teléfono del vendedor");
            txtTelefonoVendedor.requestFocus();
            return;
        }

        ProveedorModel req = new ProveedorModel();
        req.setNombreEmpresa(nombreEmpresa);
        req.setDireccion(direccion);
        req.setTelefono(telefono);
        req.setNombreVendedor(nombreVendedor);
        req.setTelefonoVendedor(telefonoVendedor);

        buttonGuardar.setEnabled(false);
        buttonCancelar.setEnabled(false);

        if (esNuevo) {
            retrofit.getApi().createProveedor(req)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Proveedor creado correctamente", Toast.LENGTH_SHORT).show();
                                requireActivity().setResult(Activity.RESULT_OK);
                                requireActivity().finish();
                            } else {
                                try {
                                    String error = response.errorBody().string();
                                    Log.e("ProveedorGestion", "Error createProveedor: " + error);
                                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Log.e("ProveedorGestion", "Excepción leer errorBody createProveedor", e);
                                    Toast.makeText(getContext(), "Error desconocido del servidor", Toast.LENGTH_SHORT).show();
                                }
                                buttonGuardar.setEnabled(true);
                                buttonCancelar.setEnabled(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("ProveedorGestion", "Fallo createProveedor: " + t.getMessage(), t);
                            Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            buttonGuardar.setEnabled(true);
                            buttonCancelar.setEnabled(true);
                        }
                    });
        } else {
            retrofit.getApi().updateProveedor(proveedorId, req)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Proveedor actualizado correctamente", Toast.LENGTH_SHORT).show();
                                requireActivity().setResult(Activity.RESULT_OK);
                                requireActivity().finish();
                            } else {
                                try {
                                    String error = response.errorBody().string();
                                    Log.e("ProveedorGestion", "Error updateProveedor: " + error);
                                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Log.e("ProveedorGestion", "Excepción leer errorBody updateProveedor", e);
                                    Toast.makeText(getContext(), "Error desconocido del servidor", Toast.LENGTH_SHORT).show();
                                }
                                buttonGuardar.setEnabled(true);
                                buttonCancelar.setEnabled(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("ProveedorGestion", "Fallo updateProveedor: " + t.getMessage(), t);
                            Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            buttonGuardar.setEnabled(true);
                            buttonCancelar.setEnabled(true);
                        }
                    });
        }
    }
}