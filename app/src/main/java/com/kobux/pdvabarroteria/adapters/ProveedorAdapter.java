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
import com.kobux.pdvabarroteria.models.ProveedorModel;

import java.util.List;

public class ProveedorAdapter extends RecyclerView.Adapter<ProveedorAdapter.ProveedorViewHolder> {

    private final List<ProveedorModel> lista;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditar(ProveedorModel proveedor);
        void onEliminar(ProveedorModel proveedor);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ProveedorAdapter(Context context, List<ProveedorModel> lista) {
        this.lista = lista;
        this.context = context;
    }

    public static class ProveedorViewHolder extends RecyclerView.ViewHolder {

        TextView empresa, vendedor, direccion, telefonos;
        ImageButton opciones;

        public ProveedorViewHolder(View itemView, OnItemClickListener listener, List<ProveedorModel> lista) {
            super(itemView);

            empresa = itemView.findViewById(R.id.txt_empresa);
            vendedor = itemView.findViewById(R.id.txt_vendedor);
            direccion = itemView.findViewById(R.id.txt_direccion);
            telefonos = itemView.findViewById(R.id.txt_telefonos);
            opciones = itemView.findViewById(R.id.butonOpciones);

            opciones.setOnClickListener(v -> {

                ProveedorModel proveedor = lista.get(getAdapterPosition());

                PopupMenu popup = new PopupMenu(itemView.getContext(), opciones);
                popup.getMenu().add("Editar");
                popup.getMenu().add("Eliminar");

                popup.setOnMenuItemClickListener(item -> {

                    if (listener == null) return true;

                    String title = item.getTitle().toString();

                    if (title.equals("Editar")) listener.onEditar(proveedor);
                    else if (title.equals("Eliminar")) listener.onEliminar(proveedor);

                    return true;
                });

                popup.show();
            });
        }
    }

    @Override
    public ProveedorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_proveedor, parent, false);
        return new ProveedorViewHolder(view,listener,lista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProveedorViewHolder h, int position) {
        ProveedorModel p = lista.get(position);

        h.empresa.setText(p.getNombreEmpresa());
        h.vendedor.setText("Vendedor: " + p.getNombreVendedor());
        h.direccion.setText(p.getDireccion());
        h.telefonos.setText("Empresa: " + p.getTelefono() + "  /  Vendedor: " + p.getTelefonoVendedor());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}