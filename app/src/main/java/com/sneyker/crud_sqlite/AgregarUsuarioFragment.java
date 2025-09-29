package com.sneyker.crud_sqlite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class AgregarUsuarioFragment extends Fragment {

    private EditText etNombre, etEmail, etEdad;
    private Button btnGuardar, btnCancelar;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_usuario, container, false);

        dbHelper = new DatabaseHelper(getContext());

        etNombre = view.findViewById(R.id.etNombre);
        etEmail = view.findViewById(R.id.etEmail);
        etEdad = view.findViewById(R.id.etEdad);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnCancelar = view.findViewById(R.id.btnCancelar);

        btnGuardar.setOnClickListener(v -> guardarUsuario());
        btnCancelar.setOnClickListener(v -> cancelar());

        return view;
    }

    private void guardarUsuario() {
        String nombre = etNombre.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String edadStr = etEdad.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty() || edadStr.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int edad = Integer.parseInt(edadStr);
            Usuario usuario = new Usuario(nombre, email, edad);
            long id = dbHelper.agregarUsuario(usuario);

            if (id != -1) {
                Toast.makeText(getContext(), "Usuario agregado exitosamente", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigateUp();
            } else {
                Toast.makeText(getContext(), "Error al agregar usuario", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Edad debe ser un número válido", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelar() {
        Navigation.findNavController(requireView()).navigateUp();
    }
}