/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package examenlab1;
import java.util.Calendar;
import java.util.ArrayList;

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
    
 
    

   
    @Override
    public String toString(){
        return super.toString()+"\nPublicacion: "+fechaPublicacion+"– PS3 Game";
    }
    @Override
    public double pagoRenta(int dias) {
        return 0;
    }

    @Override
    public void submenu() {
    }

    @Override
    public void ejecutarOpcion(int opcion) {
    
    }
    
}
