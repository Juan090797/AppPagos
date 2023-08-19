package com.example.dnisearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dnisearch.model.Movimiento;
import java.util.List;

public class MovimientoAdapter extends RecyclerView.Adapter<MovimientoAdapter.MovimientoViewHolder> {
    private List<Movimiento> movimientos;
    public MovimientoAdapter(List<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }

    @NonNull
    @Override
    public MovimientoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new MovimientoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovimientoViewHolder holder, int position) {
        Movimiento movimiento = movimientos.get(position);
        holder.textViewId.setText(String.valueOf((position+1)));
        holder.textViewDetalle.setText(movimiento.getDetalle());
        holder.textViewTotal.setText(String.valueOf(movimiento.getTotal()));
        // Configura otros elementos de la tarjeta aquí si es necesario
    }

    @Override
    public int getItemCount() {
        return movimientos.size();
    }
    static class MovimientoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDetalle;
        TextView textViewTotal;
        TextView textViewId;
        public MovimientoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDetalle = itemView.findViewById(R.id.textViewDetalle);
            textViewTotal = itemView.findViewById(R.id.textViewTotal);
            textViewId = itemView.findViewById(R.id.textViewId);
        }
    }
}