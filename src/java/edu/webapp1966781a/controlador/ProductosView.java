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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.Part;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

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
    private Part archivoImagen;
    private Part archivoExcel;
    private String nombreArchivo;

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

    public void subeImagen() {
        String mensajeSw = "";
        if (archivoImagen != null) {
            if (archivoImagen.getSize() < 4000000) {
                if ("image/jpeg".equals(archivoImagen.getContentType())) {
                    File carpeta = new File("C:/imgdashio/Productos/Categorias");
                    if (!carpeta.exists()) {
                        carpeta.mkdirs();
                    }
                    try (InputStream is = archivoImagen.getInputStream()) {
                        SimpleDateFormat ffecha = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                        Calendar hoy = Calendar.getInstance();
                        String renombraArchivo = ffecha.format(hoy.getTime()) + ".";
                        renombraArchivo += FilenameUtils.getExtension(archivoImagen.getSubmittedFileName());
                        Files.copy(is, (new File(carpeta, renombraArchivo)).toPath(), StandardCopyOption.REPLACE_EXISTING);
                        nombreArchivo = renombraArchivo;
                    } catch (Exception e) {
                    }

                } else {
                    mensajeSw = "swal('El archivo' , ' no es una imagen ', 'error')";
                }
            } else {
                mensajeSw = "swal('La imagen' , ' es muy grande  ', 'error')";
            }
        } else {
            mensajeSw = "swal('No se subio' , ' Una imagen  ', 'error')";
        }

        PrimeFaces.current().executeScript(mensajeSw);
    }

    public void insertarXLS(List cellDataList) {
        try {
            int filasContador = 0;
            for (int i = 0; i < cellDataList.size(); i++) {
                List cellTemp = (List) cellDataList.get(i);
                Producto newP = new Producto();
                for (int j = 0; j < cellTemp.size(); j++) {
                    XSSFCell hssfCell = (XSSFCell) cellTemp.get(j);
                    switch (filasContador) {
                        case 0:
                            newP.setSerial(hssfCell.toString());
                            filasContador++;
                            break;
                        case 1:
                            newP.setNombre(hssfCell.toString());
                            filasContador++;
                            break;
                        case 2:
                            newP.setImagenRuta(hssfCell.toString());
                            filasContador++;
                            break;
                        case 3:
                            newP.setCantidad(hssfCell.toString());
                            filasContador++;
                            break;
                        case 4:
                            newP.setValorCompra(hssfCell.getNumericCellValue());
                            filasContador++;
                            break;
                        case 5:
                            newP.setValorVenta(hssfCell.getNumericCellValue());
                            filasContador++;
                            break;
                        case 6:
                            Categoria nueva = categoriaFacadeLocal.find((int) Math.floor(hssfCell.getNumericCellValue()));
                            newP.setFkCategoria(nueva);
                            productoFacadeLocal.create(newP);
                            filasContador = 0;
                            break;
                    }

                }
            }

        } catch (Exception e) {
        }
    }

    public void subeExcel() throws IOException {
        String mensajeSw = "";
        if (archivoExcel != null) {
            if (archivoExcel.getSize() < 4000000) {
                if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(archivoExcel.getContentType())) {
                    InputStream input = archivoExcel.getInputStream();
                    List cellData = new ArrayList();
                    try {
                        XSSFWorkbook workBook = new XSSFWorkbook(input);
                        XSSFSheet hssfSheet = workBook.getSheetAt(0);
                        Iterator rowIterator = hssfSheet.rowIterator();
                        rowIterator.next();

                        while (rowIterator.hasNext()) {
                            XSSFRow hssfRow = (XSSFRow) rowIterator.next();
                            Iterator iterator = hssfRow.cellIterator();
                            List cellTemp = new ArrayList();
                            while (iterator.hasNext()) {
                                XSSFCell hssfCell = (XSSFCell) iterator.next();
                                cellTemp.add(hssfCell);
                            }
                            cellData.add(cellTemp);
                        }
                        insertarXLS(cellData);
                    } catch (Exception e) {
                        PrimeFaces.current().executeScript("swal('Problemas ingresando el archivo' , 'error');");
                    }
                    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                    context.redirect("index.xhtml");
                } else {
                    mensajeSw = "swal('El archivo' , ' no es una XLSX ', 'error')";
                }
            } else {
                mensajeSw = "swal('El archivo' , ' es muy grande  ', 'error')";
            }
        } else {
            mensajeSw = "swal('No puede cargar ' , ' EL  archivo  ', 'error')";
        }

        PrimeFaces.current().executeScript(mensajeSw);
    }

    public void crearProducto() {
        String mensajeSw = "";
        try {
            Categoria obt = categoriaFacadeLocal.find(id_cat_selec);
            objProductoNew.setFkCategoria(obt);
            objProductoNew.setImagenRuta(nombreArchivo);
            productoFacadeLocal.create(objProductoNew);
            objProductoNew = new Producto();
            mensajeSw = "swal('Producto registrado' , ' con exito ', 'success')";
        } catch (Exception e) {
            mensajeSw = "swal('Problemas ingresando ' un nuevo producto  ', 'error')";
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

    public Part getArchivoImagen() {
        return archivoImagen;
    }

    public void setArchivoImagen(Part archivoImagen) {
        this.archivoImagen = archivoImagen;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public Part getArchivoExcel() {
        return archivoExcel;
    }

    public void setArchivoExcel(Part archivoExcel) {
        this.archivoExcel = archivoExcel;
    }

}
