
package examenlab1;

import javax.swing.ImageIcon;

public abstract class RentItem {
    private String codigo;
    private String nombreItem;
    private double baseRenta;
    private int copiasDisponibles;
    private ImageIcon imagen;
    public RentItem(String codigo, String nombreItem, double baseRenta){
        this.codigo=codigo;
        this.nombreItem=nombreItem;
        this.baseRenta=baseRenta;
        this.copiasDisponibles=0;
    }
    
    public abstract double pagoRenta(int dias);
    
    public String getCodigo(){
        return codigo;
    }
    public String getNombreItem(){
        return nombreItem;
    }
    public double getBaseRenta(){
        return baseRenta;
    }
    public int getCopiasDisponibles(){
        return copiasDisponibles;
    }
    public void setCopiasDisponibles(int copias){
        this.copiasDisponibles=copias;
    }
    public ImageIcon getImagen(){
        return imagen;
    }
    public void setImagen(ImageIcon imagen){
        this.imagen=imagen;
    }
    public String toString(){
        return "Código: "+codigo+" | Nombre: "+nombreItem+" | Precio Base: "+baseRenta+" Lps. | Copias: "+copiasDisponibles;
    }
}
