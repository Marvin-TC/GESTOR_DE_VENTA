package com.kobux.pdvabarroteria.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.kobux.pdvabarroteria.adapters.ClienteAdapter;
import com.kobux.pdvabarroteria.models.ClienteModel;
import com.kobux.pdvabarroteria.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientesFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ClienteAdapter adapter;
    private List<ClienteModel> lista = new ArrayList<>();
    RetrofitClient retrofit;
    Context context;
    private ActivityResultLauncher<Intent> editarClienteLauncher;
    private FloatingActionButton buttonNuevo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_clientes, container, false);

        recyclerView = vista.findViewById(R.id.recyclerClientes);
        swipeRefreshLayout = vista.findViewById(R.id.swipeRefresh);
        buttonNuevo = vista.findViewById(R.id.fabAgregarCliente);

        retrofit = RetrofitClient.getInstance();
        context = getActivity().getApplicationContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
        adapter = new ClienteAdapter(context, lista);
        adapter.setOnItemClickListener(new ClienteAdapter.OnItemClickListener() {
            @Override
            public void onEditar(ClienteModel cliente) {
                editarCliente(cliente);
            }
            @Override
            public void onEliminar(ClienteModel cliente) {
                eliminarCliente(cliente);
            }
        });

        //para escuchar la respuesta de edicion correcta del proveedor o guardado exitoso
        editarClienteLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        cargarClientes();
                    }
                }
        );
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            cargarClientes();
        });

        buttonNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putBoolean("esNuevo", true);
                b.putString("tipo","CLIENTE");
                Intent intent = new Intent(getContext(), GestorVistasActivity.class);
                intent.putExtras(b);
                editarClienteLauncher.launch(intent);
            }
        });

        cargarClientes();
        return vista;
    }

    private void eliminarCliente(ClienteModel cliente) {
        retrofit.getApi().deleteCliente(cliente.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "cliente eliminado correctamente", Toast.LENGTH_SHORT).show();
                    cargarClientes();
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

    private void editarCliente(ClienteModel cliente) {
        Bundle b = new Bundle();
        b.putBoolean("esNuevo", false);
        b.putLong("idCliente", cliente.getId());
        b.putString("tipo","CLIENTE");
        Intent intent = new Intent(getContext(), GestorVistasActivity.class);
        intent.putExtras(b);
        editarClienteLauncher.launch(intent);
    }

    private void cargarClientes() {
        swipeRefreshLayout.setRefreshing(true);
        retrofit.getApi().getClientes().enqueue(new Callback<List<ClienteModel>>() {
            @Override
            public void onResponse(Call<List<ClienteModel>> call, Response<List<ClienteModel>> response) {
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<ClienteModel> clientes = response.body();
                    lista.clear();
                    lista.addAll(clientes);
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
            public void onFailure(Call<List<ClienteModel>> call, Throwable throwable) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, "Error de conexión: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error de conexión", throwable.getMessage());
            }
        });
    }
}