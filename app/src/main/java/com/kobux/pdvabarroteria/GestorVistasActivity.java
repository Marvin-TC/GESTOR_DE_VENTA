package com.kobux.pdvabarroteria;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.kobux.pdvabarroteria.fragments.ClientesFragment;
import com.kobux.pdvabarroteria.fragments.ClientesGestionFragment;
import com.kobux.pdvabarroteria.fragments.ProductosGestionFragment;
import com.kobux.pdvabarroteria.fragments.ProveedorGestionFragment;

public class GestorVistasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestor_vistas);

        Bundle extras = getIntent().getExtras();
        if (extras == null) return;

        String tipo = extras.getString("tipo", "");
        Fragment fragment = null;

        if (tipo.equals("PROVEEDOR")) {
            fragment = new ProveedorGestionFragment();
            fragment.setArguments(extras);
        }else if (tipo.equals("CLIENTE")){
            fragment = new ClientesGestionFragment();
            fragment.setArguments(extras);
        }else if (tipo.equals("PRODUCTO")){
            fragment = new ProductosGestionFragment();
            fragment.setArguments(extras);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedorFragments, fragment)
                .commit();
    }
}