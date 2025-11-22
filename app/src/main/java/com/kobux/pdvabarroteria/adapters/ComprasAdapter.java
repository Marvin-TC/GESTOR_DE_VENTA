package com.kobux.pdvabarroteria.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kobux.pdvabarroteria.R;
import com.kobux.pdvabarroteria.models.CompraListModel;

import java.util.List;

public class ComprasAdapter extends RecyclerView.Adapter<ComprasAdapter.CompraViewHolder> {

    private final List<CompraListModel> lista;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onOpcionesClick(CompraListModel compra);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ComprasAdapter(Context context, List<CompraListModel> lista) {
        this.lista = lista;
        this.context = context;
    }

    public static class CompraViewHolder extends RecyclerView.ViewHolder {

        TextView id, fecha, proveedor, factura, total, descuento;
        ImageButton opciones;

        public CompraViewHolder(View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.txt_id);
            fecha = itemView.findViewById(R.id.txt_fecha);
            proveedor = itemView.findViewById(R.id.txt_proveedor);
            factura = itemView.findViewById(R.id.txt_factura);
            total = itemView.findViewById(R.id.txt_total);
            descuento = itemView.findViewById(R.id.txt_descuento);
            opciones = itemView.findViewById(R.id.butonOpciones);
        }
    }

    @Override
    public CompraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_compra, parent, false);
        return new CompraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompraViewHolder h, int position) {
        CompraListModel c = lista.get(position);

        h.id.setText("#" + c.getId());
        h.fecha.setText(c.getFechaCompra());
        h.proveedor.setText(c.getNombreProveedor());
        h.factura.setText("Factura: " + c.getNumeroFactura() +
                " | Serie: " + c.getSerieFactura());
        h.total.setText("Q " + c.getTotal());
        h.descuento.setText("(Desc: Q " + c.getDescuentoAplicado() + ")");
        h.opciones.setOnClickListener(v -> {
            if (listener != null) listener.onOpcionesClick(c);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}