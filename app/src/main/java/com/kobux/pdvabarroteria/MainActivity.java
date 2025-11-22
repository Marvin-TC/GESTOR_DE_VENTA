package com.kobux.pdvabarroteria;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kobux.pdvabarroteria.adapters.ClienteAdapter;
import com.kobux.pdvabarroteria.fragments.ClientesFragment;
import com.kobux.pdvabarroteria.fragments.ComprasFragment;
import com.kobux.pdvabarroteria.fragments.ProductosFragment;
import com.kobux.pdvabarroteria.fragments.ProveedoresFragment;
import com.kobux.pdvabarroteria.fragments.VentasFragment;
import com.kobux.pdvabarroteria.models.ClienteModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor_fragmentos, new VentasFragment())
                    .commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_venta)
                selectedFragment = new VentasFragment();
            else if (item.getItemId() == R.id.nav_clientes)
                selectedFragment = new ClientesFragment();
            else if (item.getItemId() == R.id.nav_productos)
                selectedFragment = new ProductosFragment();
            else if (item.getItemId() == R.id.nav_proveedores)
                selectedFragment = new ProveedoresFragment();
            else if (item.getItemId() == R.id.nav_compras)
                selectedFragment = new ComprasFragment();
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contenedor_fragmentos, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}
