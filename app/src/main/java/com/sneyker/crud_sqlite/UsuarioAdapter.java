package com.sneyker.crud_sqlite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private List<Usuario> usuarios;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Usuario usuario);
        void onDeleteClick(Usuario usuario);
    }

    public UsuarioAdapter(List<Usuario> usuarios, OnItemClickListener listener) {
        this.usuarios = usuarios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);
        holder.bind(usuario, listener);
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre, tvEmail, tvEdad;
        private Button btnEliminar;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvEdad = itemView.findViewById(R.id.tvEdad);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }

        public void bind(Usuario usuario, OnItemClickListener listener) {
            tvNombre.setText(usuario.getNombre());
            tvEmail.setText(usuario.getEmail());
            tvEdad.setText("Edad: " + usuario.getEdad());

            itemView.setOnClickListener(v -> listener.onItemClick(usuario));
            btnEliminar.setOnClickListener(v -> listener.onDeleteClick(usuario));
        }
    }
}