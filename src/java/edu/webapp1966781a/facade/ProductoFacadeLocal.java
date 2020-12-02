/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.webapp1966781a.facade;

import edu.webapp1966781a.entity.Producto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Usuario
 */
@Local
public interface ProductoFacadeLocal {

    void create(Producto producto);

    void edit(Producto producto);

    void remove(Producto producto);

    Producto find(Object id);

    List<Producto> findAll();

    List<Producto> findRange(int[] range);

    int count();

    public int contarPorCategoria(int fk_categoria);

    public List<Producto> listaPorCategoria(int fk_categoria);
    
}
