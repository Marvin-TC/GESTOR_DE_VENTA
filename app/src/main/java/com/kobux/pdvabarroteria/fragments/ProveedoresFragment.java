package com.kobux.pdvabarroteria.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kobux.pdvabarroteria.GestorVistasActivity;
import com.kobux.pdvabarroteria.R;
import com.kobux.pdvabarroteria.adapters.ProductoAdapter;
import com.kobux.pdvabarroteria.adapters.ProveedorAdapter;
import com.kobux.pdvabarroteria.models.ProveedorModel;
import com.kobux.pdvabarroteria.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProveedoresFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProveedorAdapter adapter;
    private List<ProveedorModel> lista = new ArrayList<>();
    RetrofitClient retrofit;
    Context context;
    private ActivityResultLauncher<Intent> editarProveedorLauncher;
    private FloatingActionButton buttonNuevo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_proveedores, container, false);
        recyclerView = vista.findViewById(R.id.recyclerProveedores);
        swipeRefreshLayout = vista.findViewById(R.id.swipeRefresh);
        buttonNuevo = vista.findViewById(R.id.fabAgregarProveedor);

        retrofit = RetrofitClient.getInstance();
        context = getActivity().getApplicationContext();
        DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ProveedorAdapter(context,lista);
        adapter.setOnItemClickListener(new ProveedorAdapter.OnItemClickListener() {
            @Override
            public void onEditar(ProveedorModel proveedor) {
                editarProveedor(proveedor);
            }
            @Override
            public void onEliminar(ProveedorModel proveedor) {
                eliminarProveedor(proveedor);
            }
        });
        recyclerView.setAdapter(adapter);


        //para escuchar la respuesta de edicion correcta del proveedor o guardado exitoso
        editarProveedorLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        cargarProveedores();
                    }
                }
        );
        swipeRefreshLayout.setOnRefreshListener(() -> {
            cargarProveedores();
        });
        buttonNuevo.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putBoolean("esNuevo", true);
            b.putString("tipo","PROVEEDOR");
            Intent intent = new Intent(getContext(), GestorVistasActivity.class);
            intent.putExtras(b);
            editarProveedorLauncher.launch(intent);});

        cargarProveedores();
        return vista;
    }

    private void editarProveedor(ProveedorModel proveedor) {
        Bundle b = new Bundle();
        b.putBoolean("esNuevo", false);
        b.putLong("idProveedor", proveedor.getId());
        b.putString("tipo","PROVEEDOR");
        Intent intent = new Intent(getContext(), GestorVistasActivity.class);
        intent.putExtras(b);
        editarProveedorLauncher.launch(intent);
    }

    private void eliminarProveedor(ProveedorModel proveedor) {
        retrofit.getApi().deleteProveedor(proveedor.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Proveedor eliminado correctamente", Toast.LENGTH_SHORT).show();
                    cargarProveedores();
                } else {
                    try {
                        String error = response.errorBody().string();
                        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error desconocido del servidor", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarProveedores() {
        swipeRefreshLayout.setRefreshing(true);
        retrofit.getApi().getProveedores().enqueue(new Callback<List<ProveedorModel>>() {
            @Override
            public void onResponse(Call<List<ProveedorModel>> call, Response<List<ProveedorModel>> response) {
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<ProveedorModel> productos = response.body();
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
            public void onFailure(Call<List<ProveedorModel>> call, Throwable throwable) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, "Error de conexión: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error de conexión", throwable.getMessage());
            }
        });
    }


}