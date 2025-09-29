package com.sneyker.crud_sqlite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;

public class EditarUsuarioFragment extends Fragment {

    private TextInputEditText etNombre, etApellido, etEdad;
    private Button btnActualizar, btnCancelar;
    private DatabaseHelper dbHelper;
    private int usuarioId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_usuario, container, false);

        // Inicializar vistas
        etNombre = view.findViewById(R.id.etNombre);
        etApellido = view.findViewById(R.id.etApellido);
        etEdad = view.findViewById(R.id.etEdad);
        btnActualizar = view.findViewById(R.id.btnActualizar);
        btnCancelar = view.findViewById(R.id.btnCancelar);

        // Inicializar BD
        dbHelper = new DatabaseHelper(getContext());

        // Obtener datos del bundle
        if (getArguments() != null) {
            usuarioId = getArguments().getInt("id");
            String nombre = getArguments().getString("nombre");
            String apellido = getArguments().getString("apellido");
            int edad = getArguments().getInt("edad");

            // Llenar campos
            etNombre.setText(nombre);
            etApellido.setText(apellido);
            etEdad.setText(String.valueOf(edad));
        }

        // Botón actualizar
        btnActualizar.setOnClickListener(v -> actualizarUsuario());

        // Botón cancelar
        btnCancelar.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp()
        );

        return view;
    }

    private void actualizarUsuario() {
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String edadStr = etEdad.getText().toString().trim();

        // Validaciones
        if (nombre.isEmpty()) {
            etNombre.setError("Ingresa el nombre");
            etNombre.requestFocus();
            return;
        }

        if (apellido.isEmpty()) {
            etApellido.setError("Ingresa el apellido");
            etApellido.requestFocus();
            return;
        }

        if (edadStr.isEmpty()) {
            etEdad.setError("Ingresa la edad");
            etEdad.requestFocus();
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadStr);
            if (edad <= 0 || edad > 120) {
                etEdad.setError("Edad inválida");
                etEdad.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            etEdad.setError("Edad inválida");
            etEdad.requestFocus();
            return;
        }

        // Actualizar usuario
        Usuario usuario = new Usuario(usuarioId, nombre, apellido, edad);
        int filasActualizadas = dbHelper.actualizarUsuario(usuario); // ← CAMBIO AQUÍ

        if (filasActualizadas > 0) { // ← Y AQUÍ
            Toast.makeText(getContext(), "Usuario actualizado", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigateUp();
        } else {
            Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
// Desarrollado por: Erick Francisco Diaz Serrano
// Proyecto: CRUD SQLite Android
// Fecha: 2024