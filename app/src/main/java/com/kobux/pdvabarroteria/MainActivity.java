package com.kobux.pdvabarroteria;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kobux.pdvabarroteria.adapters.ClienteAdapter;
import com.kobux.pdvabarroteria.fragments.ChatFragment;
import com.kobux.pdvabarroteria.fragments.ClientesFragment;
import com.kobux.pdvabarroteria.fragments.ComprasFragment;
import com.kobux.pdvabarroteria.fragments.ProductosFragment;
import com.kobux.pdvabarroteria.fragments.ProveedoresFragment;
import com.kobux.pdvabarroteria.fragments.VentasFragment;
import com.kobux.pdvabarroteria.models.ClienteModel;
import com.kobux.pdvabarroteria.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        session = new SessionManager(this);
        if (!session.haySesionActiva()) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor_fragmentos, new ChatFragment())
                    .commit();
            getSupportActionBar().setTitle("Chat de Tienda");
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            String titulo = "";

            if (item.getItemId() == R.id.nav_chat) {
                selectedFragment = new ChatFragment();
                titulo = "Chat de Tienda";
            }
            else if (item.getItemId() == R.id.nav_venta) {
                selectedFragment = new VentasFragment();
                titulo = "Ventas";
            }
            else if (item.getItemId() == R.id.nav_clientes) {
                selectedFragment = new ClientesFragment();
                titulo = "Clientes";
            }
            else if (item.getItemId() == R.id.nav_productos) {
                selectedFragment = new ProductosFragment();
                titulo = "Productos";
            }
            else if (item.getItemId() == R.id.nav_proveedores) {
                selectedFragment = new ProveedoresFragment();
                titulo = "Proveedores";
            }
            else if (item.getItemId() == R.id.nav_compras) {
                selectedFragment = new ComprasFragment();
                titulo = "Compras";
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contenedor_fragmentos, selectedFragment)
                        .commit();

                getSupportActionBar().setTitle(titulo);
            }
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_logout) {
            cerrarSesion();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cerrarSesion() {
        session.cerrarSesion();
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish(); // evita volver con back
    }
}
