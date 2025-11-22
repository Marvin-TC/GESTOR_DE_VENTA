package com.kobux.pdvabarroteria.services;

import com.kobux.pdvabarroteria.models.ClienteModel;
import com.kobux.pdvabarroteria.models.CompraListModel;
import com.kobux.pdvabarroteria.models.CompraModel;
import com.kobux.pdvabarroteria.models.ProductoModel;
import com.kobux.pdvabarroteria.models.ProveedorModel;
import com.kobux.pdvabarroteria.models.VentaModel;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

  // CLIENTES
    @GET("cliente")
    Call<List<ClienteModel>> getClientes();

    @GET("cliente/{id}")
    Call<ClienteModel> getClienteById(@Path("id") Long id);

    @POST("cliente")
    Call<ResponseBody> createCliente(@Body ClienteModel cliente);

    @PUT("cliente/{id}")
    Call<ResponseBody> updateCliente(@Path("id") Long id, @Body ClienteModel cliente);

    @DELETE("cliente/{id}")
    Call<ResponseBody> deleteCliente(@Path("id") Long id);

    // PRODUCTOS
    @GET("productos")
    Call<List<ProductoModel>> getProductos();

    @GET("productos/{id}")
    Call<ProductoModel> getProductoById(@Path("id") Long id);

    @POST("productos")
    Call<ResponseBody> createProducto(@Body ProductoModel producto);

    @PUT("productos/{id}")
    Call<ResponseBody> updateProducto(@Path("id") Long id, @Body ProductoModel producto);

    @DELETE("productos/{id}")
    Call<ResponseBody> deleteProducto(@Path("id") Long id);

    // PROVEEDORES
    @GET("proveedor")
    Call<List<ProveedorModel>> getProveedores();

    @GET("proveedor/{id}")
    Call<ProveedorModel> getProveedorById(@Path("id") Long id);

    @POST("proveedor")
    Call<ResponseBody> createProveedor(@Body ProveedorModel proveedor);

    @PUT("proveedor/{id}")
    Call<ResponseBody> updateProveedor(@Path("id") Long id, @Body ProveedorModel proveedor);

    @DELETE("proveedor/{id}")
    Call<ResponseBody> deleteProveedor(@Path("id") Long id);


    // COMPRAS
    @GET("compras")
    Call<List<CompraListModel>> getCompras();

    @GET("compras/{id}")
    Call<CompraModel> getCompraById(@Path("id") Long id);

    @POST("compras")
    Call<CompraModel> createCompra(@Body CompraModel compra);

    @PUT("compras/{id}")
    Call<CompraModel> updateCompra(@Path("id") Long id, @Body CompraModel compra);

    @DELETE("compras/{id}")
    Call<ResponseBody> deleteCompra(@Path("id") Long id);

    //VENTAS
    @GET("ventas")
    Call<List<VentaModel>> getVentas();

    @GET("ventas/{id}")
    Call<VentaModel> getVentaById(@Path("id") Long id);

    @POST("ventas")
    Call<VentaModel> createVenta(@Body VentaModel venta);

    @PUT("ventas/{id}")
    Call<VentaModel> updateVenta(@Path("id") Long id, @Body VentaModel venta);

    @DELETE("ventas/{id}")
    Call<ResponseBody> deleteVenta(@Path("id") Long id);

    // ------- ENDPOINT RESUMEN DE VENTAS -------
    @GET("ventas/resumen-ventas")
    Call<List<Map<String, Object>>> resumenVentas(
            @Query("inicio") String inicio,   // formato yyyy-MM-dd
            @Query("fin") String fin          // formato yyyy-MM-dd
    );

    // ------- ENDPOINT TOP PRODUCTOS -------
    @GET("ventas/top-productos")
    Call<List<Map<String, Object>>> topProductos(
            @Query("inicio") String inicio,   // formato yyyy-MM-dd
            @Query("fin") String fin,         // formato yyyy-MM-dd
            @Query("limite") int limite       // opcional
    );


}