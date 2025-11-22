package com.kobux.pdvabarroteria.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kobux.pdvabarroteria.R;
import com.kobux.pdvabarroteria.models.ProductoModel;

import java.util.List;
import java.util.Locale;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private final List<ProductoModel> lista;
    private final Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditar(ProductoModel producto);
        void onEliminar(ProductoModel producto);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ProductoAdapter(Context context, List<ProductoModel> lista) {
        this.context = context;
        this.lista = lista;
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {

        ImageView imagen;
        TextView nombre, codigo, precio, stock, estado;
        ImageButton opciones;

        public ProductoViewHolder(View itemView, OnItemClickListener listener, List<ProductoModel> lista) {
            super(itemView);

            imagen = itemView.findViewById(R.id.img_producto);
            nombre = itemView.findViewById(R.id.txt_nombre_producto);
            codigo = itemView.findViewById(R.id.txt_codigo_producto);
            precio = itemView.findViewById(R.id.txt_precio_producto);
            stock = itemView.findViewById(R.id.txt_stock_producto);
            estado = itemView.findViewById(R.id.txt_estado_producto);
            opciones = itemView.findViewById(R.id.butonOpciones);

            opciones.setOnClickListener(v -> {

                ProductoModel producto = lista.get(getAdapterPosition());

                PopupMenu popup = new PopupMenu(itemView.getContext(), opciones);
                popup.getMenu().add("Editar");
                popup.getMenu().add("Eliminar");

                popup.setOnMenuItemClickListener(item -> {

                    if (listener == null) return true;

                    String title = item.getTitle().toString();

                    if (title.equals("Editar")) listener.onEditar(producto);
                    else if (title.equals("Eliminar")) listener.onEliminar(producto);

                    return true;
                });

                popup.show();
            });
        }
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_productos, parent, false);
        return new ProductoViewHolder(view, listener, lista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder h, int position) {
        ProductoModel p = lista.get(position);

        h.nombre.setText(p.getNombre());
        h.codigo.setText("Código: " + (p.getCodigoInterno() != null ? p.getCodigoInterno() : "-"));
        h.precio.setText("Q " + String.format(Locale.US, "%.2f", p.getPrecioUnidad()));
        h.stock.setText("Stock: " + p.getStock());
        h.estado.setText(p.isEstado() ? "Activo" : "Inactivo");
        h.estado.setTextColor(ContextCompat.getColor(
                h.itemView.getContext(),
                p.isEstado() ? R.color.green : R.color.red
        ));

        if (p.getUrlImagen() != null && !p.getUrlImagen().isEmpty()) {
            Glide.with(h.itemView.getContext())
                    .load(p.getUrlImagen())
                    .placeholder(R.drawable.baseline_image_24)
                    .into(h.imagen);
        } else {
            h.imagen.setImageResource(R.drawable.baseline_image_24);
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}