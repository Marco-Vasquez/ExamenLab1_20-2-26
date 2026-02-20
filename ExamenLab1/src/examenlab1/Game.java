/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package examenlab1;
import java.util.Calendar;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author janinadiaz
 */
public class Game extends RentItem implements MenuActions{ 
    private Calendar fechaPublicacion;
    private ArrayList<String> especificaciones;
    private static final double PRECIO_FIJO=20;
   
    public Game(String codigo, String nombre){
        super(codigo, nombre, PRECIO_FIJO);
        this.fechaPublicacion=Calendar.getInstance();
        this.especificaciones=new ArrayList<>();
        
    }
    public ArrayList<String> getEspecificaciones(){
    return especificaciones;
    }
    
    public Calendar getFechaPublicacion(){
        return fechaPublicacion;
    }
    
    public void addEspecificaciones(String especificacion){
        especificaciones.add(especificacion);
        
    }
    public void setFechaPublicacion(int year, int mes, int dia){
        this.fechaPublicacion=Calendar.getInstance();
        fechaPublicacion.set(year, mes - 1, dia);
    }
    
    public void listarEspecificaciones(){
        listEspecificacionesRec(0);
    }

    private void listEspecificacionesRec(int i) {
        if (i>=especificaciones.size())
            return;
        System.out.println((i+1)+". "+especificaciones.get(i));
        listEspecificacionesRec(i+1);
        
        
    }
 
    public String getEspecificacionesTexto(){
    if(especificaciones.isEmpty())
        return "Sin especificaciones";
    
    return getEspecificacionesRec(0);
    }

    private String getEspecificacionesRec(int i) {
        if(i>=especificaciones.size()) 
            return "";
        
        return (i+1)+". "+especificaciones.get(i)+"\n"+getEspecificacionesRec(i+1);
    }
    
    

   
    @Override
    public String toString(){
        return super.toString()+"\nPublicacion: "+fechaPublicacion+"– PS3 Game";
    }
    @Override
    public double pagoRenta(int dias){
        return PRECIO_FIJO*dias;
    }

    @Override
    public void submenu() {
    }

    @Override
    public void ejecutarOpcion(int opcion){
    
    }
    
}
