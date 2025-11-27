package com.kobux.pdvabarroteria.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kobux.pdvabarroteria.GestorVistasActivity;
import com.kobux.pdvabarroteria.R;
import com.kobux.pdvabarroteria.adapters.ProductoAdapter;
import com.kobux.pdvabarroteria.models.ProductoModel;
import com.kobux.pdvabarroteria.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductosFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProductoAdapter adapter;
    private List<ProductoModel> lista = new ArrayList<>();
    private RetrofitClient retrofit;
    private Context context;
    private ActivityResultLauncher<Intent> editarProductosLauncher;
    private FloatingActionButton buttonNuevo;
    private boolean isFabVisible = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_productos, container, false);
        recyclerView = vista.findViewById(R.id.recyclerProductos);
        swipeRefreshLayout = vista.findViewById(R.id.swipeRefresh);
        buttonNuevo = vista.findViewById(R.id.fabAgregarProducto);

        retrofit = RetrofitClient.getInstance();
        context = getActivity().getApplicationContext();
        DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ProductoAdapter(context,lista);
        adapter.setOnItemClickListener(new ProductoAdapter.OnItemClickListener() {
            @Override
            public void onEditar(ProductoModel producto) {
                editarProducto(producto);
            }

            @Override
            public void onEliminar(ProductoModel producto) {
                eliminarProducto(producto);
            }
        });
        recyclerView.setAdapter(adapter);


        //para escuchar la respuesta de edicion correcta del proveedor o guardado exitoso
        editarProductosLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        cargarProductos();
                    }
                }
        );
        swipeRefreshLayout.setOnRefreshListener(() -> {
            cargarProductos();
        });
        buttonNuevo.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putBoolean("esNuevo", true);
            b.putString("tipo","PRODUCTO");
            Intent intent = new Intent(getContext(), GestorVistasActivity.class);
            intent.putExtras(b);
            editarProductosLauncher.launch(intent);});

        cargarProductos();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 10 && isFabVisible) {
                    buttonNuevo.hide();
                    isFabVisible = false;
                } else if (dy < -10 && !isFabVisible) {
                    buttonNuevo.show();
                    isFabVisible = true;
                }
            }
        });
        return vista;
    }

    private void eliminarProducto(ProductoModel producto) {
    }

    private void editarProducto(ProductoModel producto) {
        Bundle b = new Bundle();
        b.putBoolean("esNuevo", false);
        b.putLong("idProducto", producto.getId());
        b.putString("tipo","PRODUCTO");
        Intent intent = new Intent(getContext(), GestorVistasActivity.class);
        intent.putExtras(b);
        editarProductosLauncher.launch(intent);
    }

    private void cargarProductos() {
        swipeRefreshLayout.setRefreshing(true);
        retrofit.getApi().getProductos().enqueue(new Callback<List<ProductoModel>>() {
            @Override
            public void onResponse(Call<List<ProductoModel>> call, Response<List<ProductoModel>> response) {
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<ProductoModel> productos = response.body();
                    lista.clear();
                    lista.addAll(productos);
                    adapter.notifyDataSetChanged();
                } else {
                    try {
                        Log.e("API_ERROR", "Código: " + response.code());
                        Log.e("API_ERROR", "ErrorBody: " + response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(context,
                            "Error del servidor (" + response.code() + ")",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<ProductoModel>> call, Throwable throwable) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, "Error de conexión: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error de conexión", throwable.getMessage());
            }
        });
    }
}