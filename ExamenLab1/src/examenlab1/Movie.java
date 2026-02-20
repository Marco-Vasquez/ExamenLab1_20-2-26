/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examenlab1;

import java.util.Calendar;

/**
 *
 * @author andres
 */
public class Movie extends RentItem{
    public Calendar fechaEstreno;
    public Movie(String codigo, String nombreItem, double baseRenta){
        super(codigo, nombreItem, baseRenta);
        this.fechaEstreno=Calendar.getInstance();
    }
    public Calendar getFechaEstreno(){
        return fechaEstreno;
    }
    public void setFechaEstreno(Calendar fechaEstreno){
        this.fechaEstreno=fechaEstreno;
    }
    public String getEstado(){
        Calendar hoy=Calendar.getInstance();
        Calendar limite=(Calendar) fechaEstreno.clone();
        limite.add(Calendar.MONTH, 3);
        if(!hoy.after(limite)){
            return "ESTRENO";
        }
        return "NORMAL";
    }
    @Override
    public double pagoRenta(int dias) {
        double total=getBaseRenta()*dias;
        String estado=getEstado();
        if(estado.equals("ESTRENO") && dias>2){
            total+=(dias-2)*50;
        }
        else if(estado.equals("NORMAL") && dias>5){
            total+=(dias-5)*30;
        }
        return total;
    }
    @Override
    public String toString(){
        return super.toString()+" | Estado: "+getEstado()+" - Movie";
    }
    
}
