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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kobux.pdvabarroteria.R;
import com.kobux.pdvabarroteria.models.ProductoModel;
import com.kobux.pdvabarroteria.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductosGestionFragment extends Fragment {

    private boolean esNuevo;
    private long productoId;
    private EditText txtNombreProducto;
    private EditText txtNombreFacturacion;
    private EditText txtCodigoInterno;
    private EditText txtCodigoBarra;
    private EditText txtUrlImagen;
    private EditText txtPrecioUnidad;
    private EditText txtPrecioMayorista;
    private EditText txtStock;
    private Switch switchEstado;
    private Button buttonGuardar;
    private Button buttonCancelar;
    private ImageView buttonRefrescar;
    private RetrofitClient retrofit;
    private TextView txtEtiqueta;
    private TextView txtxEtiquetaStock;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_productos_gestion, container, false);

        if (getArguments() != null) {
            esNuevo = getArguments().getBoolean("esNuevo", true);
            productoId = getArguments().getLong("idProducto", 0);
        }

        retrofit = RetrofitClient.getInstance();
        txtNombreProducto = view.findViewById(R.id.txt_nombreProducto);
        txtNombreFacturacion = view.findViewById(R.id.txt_nombreFacturacion);
        txtCodigoInterno = view.findViewById(R.id.txt_codigoInterno);
        txtCodigoBarra = view.findViewById(R.id.txt_codigoBarra);
        txtUrlImagen = view.findViewById(R.id.txt_urlImagen);
        txtPrecioUnidad = view.findViewById(R.id.txt_precioUnidad);
        txtPrecioMayorista = view.findViewById(R.id.txt_precioMayorista);
        txtStock = view.findViewById(R.id.txt_stock);
        txtEtiqueta = view.findViewById(R.id.etiqueta_estado);
        txtxEtiquetaStock = view.findViewById(R.id.etiqueta_stock);
        switchEstado = view.findViewById(R.id.switch_estadoProducto);
        buttonGuardar = view.findViewById(R.id.buttonGuardarProducto);
        buttonCancelar = view.findViewById(R.id.buttonCancelarProducto);
        buttonRefrescar = view.findViewById(R.id.buttonRefrescarProducto);
        TextView titulo = view.findViewById(R.id.txt_titulo_producto);

        if (esNuevo){
            titulo.setText("CREAR PRODUCTO");
            buttonRefrescar.setVisibility(View.GONE);
            switchEstado.setVisibility(view.GONE);
            txtStock.setVisibility(view.GONE);
            txtEtiqueta.setVisibility(view.GONE);
        }
        else {
            titulo.setText("EDITAR PRODUCTO");
            txtStock.setEnabled(false);
            cargarProducto();
        }

        buttonGuardar.setOnClickListener(v -> guardarProducto());
        buttonCancelar.setOnClickListener(v -> requireActivity().finish());
        buttonRefrescar.setOnClickListener(v -> {
            buttonRefrescar.setVisibility(View.GONE);
            cargarProducto();
        });

        return view;
    }

    private void cargarProducto() {
        retrofit.getApi().getProductoById(productoId)
                .enqueue(new Callback<ProductoModel>() {
                    @Override
                    public void onResponse(Call<ProductoModel> call, Response<ProductoModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ProductoModel p = response.body();
                            txtNombreProducto.setText(p.getNombre());
                            txtNombreFacturacion.setText(p.getNombreFacturacion());
                            txtCodigoInterno.setText(p.getCodigoInterno());
                            txtCodigoBarra.setText(p.getCodigoBarra());
                            txtUrlImagen.setText(p.getUrlImagen());
                            txtPrecioUnidad.setText(String.valueOf(p.getPrecioUnidad()));
                            txtPrecioMayorista.setText(String.valueOf(p.getPrecioMayorista()));
                            txtStock.setText(String.valueOf(p.getStock()));
                            switchEstado.setChecked(p.isEstado());
                            buttonGuardar.setEnabled(true);
                            buttonCancelar.setEnabled(true);
                            Toast.makeText(getContext(), "Datos cargados correctamente", Toast.LENGTH_SHORT).show();
                            buttonRefrescar.setVisibility(View.VISIBLE);
                        } else {
                            try {
                                String error = response.errorBody().string();
                                Log.e("ProductosGestion", "Error cargarProducto: " + error);
                                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Log.e("ProductosGestion", "Excepción leyendo errorBody", e);
                                Toast.makeText(getContext(), "Error al cargar producto", Toast.LENGTH_SHORT).show();
                            }
                            requireActivity().finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductoModel> call, Throwable t) {
                        Log.e("ProductosGestion", "Fallo cargarProducto: " + t.getMessage(), t);
                        Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        requireActivity().finish();
                    }
                });
    }

    private void guardarProducto() {

        String nombre = txtNombreProducto.getText().toString().trim();
        String nombreFact = txtNombreFacturacion.getText().toString().trim();
        String codigoInterno = txtCodigoInterno.getText().toString().trim();
        String codigoBarra = txtCodigoBarra.getText().toString().trim();
        String urlImagen = txtUrlImagen.getText().toString().trim();
        String precioUnidadStr = txtPrecioUnidad.getText().toString().trim();
        String precioMayoristaStr = txtPrecioMayorista.getText().toString().trim();
        String stockStr = txtStock.getText().toString().trim();
        boolean estado = switchEstado.isChecked();

        if (nombre.isEmpty()) {
            txtNombreProducto.setError("Ingrese el nombre del producto");
            txtNombreProducto.requestFocus();
            return;
        }

        if (codigoInterno.isEmpty()) {
            txtCodigoInterno.setError("Ingrese el código interno");
            txtCodigoInterno.requestFocus();
            return;
        }

        double precioUnidad = 0;
        if (!precioUnidadStr.isEmpty()) {
            try {
                precioUnidad = Double.parseDouble(precioUnidadStr);
                if (precioUnidad < 0) {
                    txtPrecioUnidad.setError("El precio no puede ser negativo");
                    txtPrecioUnidad.requestFocus();
                    return;
                }
                if (precioUnidad == 0) {
                    txtPrecioUnidad.setError("El precio no puede ser cero");
                    txtPrecioUnidad.requestFocus();
                    return;
                }
            } catch (Exception e) {
                txtPrecioUnidad.setError("Ingrese un precio válido");
                txtPrecioUnidad.requestFocus();
                return;
            }
        }

        double precioMayorista = 0;
        if (!precioMayoristaStr.isEmpty()) {
            try {
                precioMayorista = Double.parseDouble(precioMayoristaStr);
            } catch (Exception e) {
                txtPrecioMayorista.setError("Ingrese un precio válido");
                txtPrecioMayorista.requestFocus();
                return;
            }
        }

        if (precioMayorista == 0) {
            txtPrecioMayorista.setError("El precio mayorista debe ser mayor a cero");
            txtPrecioMayorista.requestFocus();
            return;
        }

        if (precioMayorista  > precioUnidad) {
            txtPrecioMayorista.setError("El precio mayorista no puede ser mayor al precio normal");
            precioMayorista = precioUnidad;
            txtPrecioMayorista.setText(String.valueOf(precioMayorista));
            txtPrecioMayorista.requestFocus();
            return;
        }

        int stock = 0;
        if (!stockStr.isEmpty()) {
            try {
                stock = Integer.parseInt(stockStr);
                if (stock < 0) {
                    txtStock.setError("Stock inválido");
                    txtStock.requestFocus();
                    return;
                }
            } catch (Exception e) {
                txtStock.setError("Ingrese un número válido");
                txtStock.requestFocus();
                return;
            }
        }

        ProductoModel req = new ProductoModel();
        req.setNombre(nombre);
        req.setNombreFacturacion(nombreFact);
        req.setCodigoInterno(codigoInterno);
        req.setCodigoBarra(codigoBarra);
        req.setUrlImagen(urlImagen);
        req.setPrecioUnidad(precioUnidad);
        req.setPrecioMayorista(precioMayorista);
        req.setStock(stock);
        req.setEstado(estado);

        buttonGuardar.setEnabled(false);
        buttonCancelar.setEnabled(false);

        if (esNuevo) {

            retrofit.getApi().createProducto(req)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Producto creado correctamente", Toast.LENGTH_SHORT).show();
                                requireActivity().setResult(Activity.RESULT_OK);
                                requireActivity().finish();
                            } else {
                                try {
                                    String error = response.errorBody().string();
                                    Log.e("ProductosGestion", "Error createProducto: " + error);
                                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Log.e("ProductosGestion", "Excepción leer errorBody createProducto", e);
                                    Toast.makeText(getContext(), "Error desconocido del servidor", Toast.LENGTH_SHORT).show();
                                }
                                buttonGuardar.setEnabled(true);
                                buttonCancelar.setEnabled(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("ProductosGestion", "Fallo createProducto: " + t.getMessage(), t);
                            Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            buttonGuardar.setEnabled(true);
                            buttonCancelar.setEnabled(true);
                        }
                    });

        } else {

            retrofit.getApi().updateProducto(productoId, req)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();
                                requireActivity().setResult(Activity.RESULT_OK);
                                requireActivity().finish();
                            } else {
                                try {
                                    String error = response.errorBody().string();
                                    Log.e("ProductosGestion", "Error updateProducto: " + error);
                                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Log.e("ProductosGestion", "Excepción leer errorBody updateProducto", e);
                                    Toast.makeText(getContext(), "Error desconocido del servidor", Toast.LENGTH_SHORT).show();
                                }
                                buttonGuardar.setEnabled(true);
                                buttonCancelar.setEnabled(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("ProductosGestion", "Fallo updateProducto: " + t.getMessage(), t);
                            Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            buttonGuardar.setEnabled(true);
                            buttonCancelar.setEnabled(true);
                        }
                    });
        }
    }
}