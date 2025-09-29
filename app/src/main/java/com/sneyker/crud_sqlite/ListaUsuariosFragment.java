package com.sneyker.crud_sqlite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ListaUsuariosFragment extends Fragment {

    private RecyclerView recyclerView;
    private UsuarioAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Usuario> listaUsuarios = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_usuarios, container, false);

        dbHelper = new DatabaseHelper(getContext());

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton fab = view.findViewById(R.id.fabAgregar);
        fab.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(
                    R.id.action_listaUsuariosFragment_to_agregarUsuarioFragment
            );
        });

        cargarUsuarios();
        return view;
    }

    private void cargarUsuarios() {
        listaUsuarios = dbHelper.obtenerTodosUsuarios();

        if (listaUsuarios.isEmpty()) {
            Toast.makeText(getContext(), "No hay usuarios registrados", Toast.LENGTH_SHORT).show();
        }

        adapter = new UsuarioAdapter(listaUsuarios, new UsuarioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Usuario usuario) {
                Toast.makeText(getContext(), "Clic en: " + usuario.getNombre(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEditClick(Usuario usuario) {
                // AGREGADO: Navegar a editar
                Bundle bundle = new Bundle();
                bundle.putInt("id", usuario.getId());
                bundle.putString("nombre", usuario.getNombre());
                bundle.putString("email", usuario.getEmail());
                bundle.putInt("edad", usuario.getEdad());

                Navigation.findNavController(requireView()).navigate(
                        R.id.action_listaUsuariosFragment_to_editarUsuarioFragment,
                        bundle
                );
            }

            @Override
            public void onDeleteClick(Usuario usuario) {
                mostrarDialogoConfirmacion(usuario);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void mostrarDialogoConfirmacion(Usuario usuario) {
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Eliminar Usuario")
                .setMessage("¿Estás seguro de que quieres eliminar a " + usuario.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    dbHelper.eliminarUsuario(usuario.getId());
                    cargarUsuarios();
                    Toast.makeText(getContext(), "Usuario eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarUsuarios();
    }
}
// Desarrollado por: Erick Francisco Diaz Serrano
// Proyecto: CRUD SQLite Android
// Fecha: 2024