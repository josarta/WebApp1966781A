/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.webapp1966781a.controlador;

import edu.webapp1966781a.entity.Usuario;
import edu.webapp1966781a.facade.UsuarioFacadeLocal;
import edu.webapp1966781a.utilidades.Email;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Usuario
 */
@Named(value = "administradorView")
@ViewScoped
public class AdministradorView implements Serializable {

    @EJB
    UsuarioFacadeLocal usuarioFacadeLocal;
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    private Usuario usReg = new Usuario();

    /**
     * Creates a new instance of AdministradorView
     */
    @PostConstruct
    public void leerListaUsuarios() {
        listaUsuarios.addAll(usuarioFacadeLocal.findAll());
    }

    public void crearUsuario() {
        String mensajeSw = "";
        try {
            usReg.setFechaRegistro(new Date());
            usuarioFacadeLocal.create(usReg);
            listaUsuarios.add(usReg);
            mensajeSw = "swal('Usuario registrado' , ' con exito ', 'success')";
        } catch (Exception e) {
            mensajeSw = "swal('El usuario' , ' Ya se encuentra registrado  ', 'error')";
        }
        usReg = new Usuario();
        PrimeFaces.current().executeScript(mensajeSw);
    }

    public void removerUsuario(Usuario usuRem) {
        String mensajeSw = "";
        try {
            usuarioFacadeLocal.remove(usuRem);
            listaUsuarios.remove(usuRem);
            mensajeSw = "swal('Usuario removido' , ' con exito ', 'success')";
        } catch (Exception e) {
            mensajeSw = "swal('Problemas removiendo' , ' al usuario  ', 'error')";
        }
        PrimeFaces.current().executeScript(mensajeSw);
    }

    public void cargaUsuarioEditar(Usuario usuEditar) {
        this.usReg = usuEditar;
    }

    public void editarUsuario() {
        String mensajeSw = "";
        try {
            usuarioFacadeLocal.edit(usReg);
            listaUsuarios.clear();
            listaUsuarios.addAll(usuarioFacadeLocal.findAll());
            mensajeSw = "swal('Usuario modificado' , ' con exito ', 'success')";
        } catch (Exception e) {
            mensajeSw = "swal('Problemas modificando' , ' al usuario  ', 'error')";
        }
        PrimeFaces.current().executeScript(mensajeSw);
    }
    
    public void correoMasivo(){
        try {
            for (Usuario lUsuario : listaUsuarios) {
                Email.sendBienvenido(lUsuario.getCorreo(), 
                        lUsuario.getNombres() + " " +lUsuario.getApellidos(),
                        lUsuario.getCorreo(), lUsuario.getClave());
            }
        } catch (Exception e) {
        }
        
    }
    

    public AdministradorView() {
    }

    public ArrayList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(ArrayList<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public Usuario getUsReg() {
        return usReg;
    }

    public void setUsReg(Usuario usReg) {
        this.usReg = usReg;
    }

}
