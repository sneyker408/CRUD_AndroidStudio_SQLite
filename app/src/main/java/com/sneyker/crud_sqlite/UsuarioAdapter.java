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
        void onEditClick(Usuario usuario);  // AGREGADO
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
        return usuarios != null ? usuarios.size() : 0;
    }

    public void actualizarLista(List<Usuario> nuevaLista) {
        this.usuarios = nuevaLista;
        notifyDataSetChanged();
    }

    static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre, tvEmail, tvEdad;
        private Button btnEditar, btnEliminar;  // AGREGADO btnEditar

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvEmail = itemView.findViewById(R.id.etEmail);
            tvEdad = itemView.findViewById(R.id.tvEdad);
            btnEditar = itemView.findViewById(R.id.btnEditar);      // AGREGADO
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }

        public void bind(Usuario usuario, OnItemClickListener listener) {
            tvNombre.setText(usuario.getNombre());
            tvEmail.setText(usuario.getEmail());
            tvEdad.setText("Edad: " + usuario.getEdad());

            itemView.setOnClickListener(v -> listener.onItemClick(usuario));

            // AGREGADO: Click en editar
            btnEditar.setOnClickListener(v -> listener.onEditClick(usuario));

            btnEliminar.setOnClickListener(v -> listener.onDeleteClick(usuario));
        }
    }
}
// Desarrollado por: Erick Francisco Diaz Serrano
// Proyecto: CRUD SQLite Android
// Fecha: 2024