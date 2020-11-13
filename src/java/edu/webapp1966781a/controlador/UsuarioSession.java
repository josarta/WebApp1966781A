/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.webapp1966781a.controlador;

import edu.webapp1966781a.entity.Usuario;
import edu.webapp1966781a.facade.UsuarioFacadeLocal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Usuario
 */
@Named(value = "usuarioSession")
@SessionScoped
public class UsuarioSession implements Serializable {

    @EJB
    UsuarioFacadeLocal usuarioFacadeLocal;

    private String correIn = "";
    private String claveIn = "";
    private Usuario usuLogin = new Usuario();

    /**
     * Creates a new instance of UsuarioSession
     */
    public UsuarioSession() {
    }

    public void inicioSesion() {
        String mensajeSw = "";
        try {
            usuLogin = usuarioFacadeLocal.loginUsuario(correIn, claveIn);

            if (usuLogin.getId() == null) {
                mensajeSw = "swal('El usuario' , ' No se encuentra registrado  ', 'error')";
            } else {
                FacesContext fc = FacesContext.getCurrentInstance();
                fc.getExternalContext().redirect("miperfil/index.xhtml");
            }

        } catch (Exception e) {
            mensajeSw = "swal('El usuario' , ' No se encuentra registrado  ', 'error')";
        }
        PrimeFaces.current().executeScript(mensajeSw);
    }

    public void cerrarSesion() {
        usuLogin = null;
        try {
            
            
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
            String ctx = ext.getRequestContextPath();
            
            ((HttpSession) ext.getSession(false)).invalidate();
            
            fc.getExternalContext().redirect(ctx + "/index.xhtml");
        } catch (Exception e) {
            System.out.println(" Error cerrando sesion UsuarioSession:cerrarSesion " + e.getMessage());
        }

    }

    public void actualizarMisDatos() {
        String mensajeSw = "";
        try {
            usuarioFacadeLocal.edit(usuLogin);
            mensajeSw = "swal('Se actualizaron ' , 'Sus datos exitosamente  ', 'success')";
        } catch (Exception e) {
            mensajeSw = "swal('No se puede' , ' Actualizar mis datos  ', 'error')";
        }
        PrimeFaces.current().executeScript(mensajeSw);
    }

    public String getCorreIn() {
        return correIn;
    }

    public void setCorreIn(String correIn) {
        this.correIn = correIn;
    }

    public String getClaveIn() {
        return claveIn;
    }

    public void setClaveIn(String claveIn) {
        this.claveIn = claveIn;
    }

    public Usuario getUsuLogin() {
        return usuLogin;
    }

    public void setUsuLogin(Usuario usuLogin) {
        this.usuLogin = usuLogin;
    }

}
