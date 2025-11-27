package com.kobux.pdvabarroteria.fragments;

import android.content.Context;
import android.os.Bundle;

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
import com.kobux.pdvabarroteria.R;
import com.kobux.pdvabarroteria.adapters.ComprasAdapter;
import com.kobux.pdvabarroteria.models.ClienteModel;
import com.kobux.pdvabarroteria.models.CompraListModel;
import com.kobux.pdvabarroteria.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComprasFragment extends Fragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ComprasAdapter adapter;
    private List<CompraListModel> lista = new ArrayList<>();
    private RetrofitClient retrofit;
    private Context context;
    private boolean isFabVisible = true;
    private FloatingActionButton buttonNuevo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_compras, container, false);

        recyclerView = vista.findViewById(R.id.recyclerCompras);
        swipeRefreshLayout = vista.findViewById(R.id.swipeRefresh);
        buttonNuevo = vista.findViewById(R.id.fabAgregarCompras);
        retrofit = RetrofitClient.getInstance();
        context = getActivity().getApplicationContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
        adapter = new ComprasAdapter(context, lista);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            cargarCompras();
        });

        cargarCompras();

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

    private void cargarCompras() {
        swipeRefreshLayout.setRefreshing(true);

        retrofit.getApi().getCompras().enqueue(new Callback<List<CompraListModel>>() {
            @Override
            public void onResponse(Call<List<CompraListModel>> call, Response<List<CompraListModel>> response) {
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<CompraListModel> clientes = response.body();
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
            public void onFailure(Call<List<CompraListModel>> call, Throwable throwable) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, "Error de conexión: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error de conexión", throwable.getMessage());
            }
        });
    }
}