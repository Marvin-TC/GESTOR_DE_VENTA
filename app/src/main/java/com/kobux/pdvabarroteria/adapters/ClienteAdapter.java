package com.kobux.pdvabarroteria.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kobux.pdvabarroteria.R;
import com.kobux.pdvabarroteria.models.ClienteModel;

import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    private final List<ClienteModel> lista;
    private final Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditar(ClienteModel cliente);
        void onEliminar(ClienteModel cliente);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ClienteAdapter(Context context, List<ClienteModel> lista) {
        this.context = context;
        this.lista = lista;
    }

    public static class ClienteViewHolder extends RecyclerView.ViewHolder {

        TextView nombres, apellidos, direccion;
        ImageButton opciones;

        public ClienteViewHolder(View itemView, OnItemClickListener listener, List<ClienteModel> lista) {
            super(itemView);

            nombres = itemView.findViewById(R.id.txt_nombres);
            apellidos = itemView.findViewById(R.id.txt_apellidos);
            direccion = itemView.findViewById(R.id.txt_direccion);
            opciones = itemView.findViewById(R.id.butonOpciones);

            opciones.setOnClickListener(v -> {

                ClienteModel cliente = lista.get(getAdapterPosition());

                PopupMenu popup = new PopupMenu(itemView.getContext(), opciones);
                popup.getMenu().add("Editar");
                popup.getMenu().add("Eliminar");

                popup.setOnMenuItemClickListener(item -> {
                    if (listener == null) return true;

                    String title = item.getTitle().toString();
                    if (title.equals("Editar")) listener.onEditar(cliente);
                    else if (title.equals("Eliminar")) listener.onEliminar(cliente);

                    return true;
                });

                popup.show();
            });
        }
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cliente, parent, false);
        return new ClienteViewHolder(view, listener, lista);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder h, int position) {
        ClienteModel c = lista.get(position);
        h.nombres.setText(c.getNombres());
        h.apellidos.setText(c.getApellidos());
        h.direccion.setText(c.getDireccion());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}