/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.webapp1966781a.controlador;

import edu.webapp1966781a.entity.Categoria;
import edu.webapp1966781a.entity.Producto;
import edu.webapp1966781a.entity.Usuario;
import edu.webapp1966781a.facade.CategoriaFacadeLocal;
import edu.webapp1966781a.facade.ProductoFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Usuario
 */
@Named(value = "productosView")
@ViewScoped
public class ProductosView implements Serializable {

    @EJB
    ProductoFacadeLocal productoFacadeLocal;
    @EJB
    CategoriaFacadeLocal categoriaFacadeLocal;

    private Categoria objCategoria = new Categoria();
    private ArrayList<Categoria> listaCategorias = new ArrayList<>();
    
    private Producto objProductoNew = new Producto();
    private int id_cat_selec = 0;
    
    
    

    /**
     * Creates a new instance of ProductosView
     */
    @PostConstruct
    public void cargaCategorias() {
        try {
            objCategoria = categoriaFacadeLocal.find(1);
            listaCategorias.addAll(categoriaFacadeLocal.findAll());
        } catch (Exception e) {
            System.out.println("edu.webapp1966781a.controlador.ProductosView.cargaCategorias() " + e.getMessage());
        }
    }

    public ProductosView() {
    }

    public int contarPorCategoria() {
        return productoFacadeLocal.contarPorCategoria(objCategoria.getIdcategorias());
    }

    public List<Producto> listarPorCategoria() {
        return productoFacadeLocal.listaPorCategoria(objCategoria.getIdcategorias());
    }

    public void crearProducto() {
        String mensajeSw = "";
        try {
            Categoria obt = categoriaFacadeLocal.find(id_cat_selec);
            objProductoNew.setFkCategoria(obt);
            productoFacadeLocal.create(objProductoNew);
            objProductoNew = new Producto();
            mensajeSw = "swal('Usuario registrado' , ' con exito ', 'success')";
        } catch (Exception e) {
            mensajeSw = "swal('El usuario' , ' Ya se encuentra registrado  ', 'error')";
        }
      
        PrimeFaces.current().executeScript(mensajeSw);
    }
    
    public int catidadProductos() {
        return productoFacadeLocal.count();
    }

    public void selecionCategoria(int idCategoria) {
        objCategoria = categoriaFacadeLocal.find(idCategoria);
    }

    
    
    
    public void setObjCategoria(Categoria objCategoria) {
        this.objCategoria = objCategoria;
    }

    public Categoria getObjCategoria() {
        return objCategoria;
    }

    public void setListaCategorias(ArrayList<Categoria> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }

    public ArrayList<Categoria> getListaCategorias() {
        return listaCategorias;
    }

    public Producto getObjProductoNew() {
        return objProductoNew;
    }

    public void setObjProductoNew(Producto objProductoNew) {
        this.objProductoNew = objProductoNew;
    }

    public int getId_cat_selec() {
        return id_cat_selec;
    }

    public void setId_cat_selec(int id_cat_selec) {
        this.id_cat_selec = id_cat_selec;
    }

}
