package examenlab1;
import java.util.Calendar;
import java.util.ArrayList;
import javax.swing.JOptionPane;

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
        if (i>=especificaciones.size()) return;
        System.out.println((i+1)+". "+especificaciones.get(i));
        listEspecificacionesRec(i+1);
    }
 
    public String getEspecificacionesTexto(){
        if(especificaciones.isEmpty()) return "Sin especificaciones";
        return getEspecificacionesRec(0);
    }

    private String getEspecificacionesRec(int i) {
        if(i>=especificaciones.size()) return "";
        return (i+1)+". "+especificaciones.get(i)+"\n"+getEspecificacionesRec(i+1);
    }
   
    @Override
    public String toString(){
        String fecha=(fechaPublicacion.get(Calendar.DAY_OF_MONTH))+"|"+(fechaPublicacion.get(Calendar.MONTH)+1)+"|"+fechaPublicacion.get(Calendar.YEAR);
        return super.toString()+"\nPublicacion: "+fecha+"– PS3 Game";
    }

    @Override
    public double pagoRenta(int dias){
        return PRECIO_FIJO*dias;
    }

    @Override
    public void submenu() {
        JOptionPane.showMessageDialog(null,
            "=== SUBMENU: " + getNombreItem() + " ===\n\n" +
            "1. Actualizar fecha de publicacion\n" +
            "2. Agregar especificacion tecnica\n" +
            "3. Ver especificaciones tecnicas",
            "Submenu - PS3 Game", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void ejecutarOpcion(int opcion){
        switch(opcion){
            case 1:
                try {
                    String year = JOptionPane.showInputDialog(null, "Ingrese el año (ej: 2023):", "Actualizar Fecha", JOptionPane.QUESTION_MESSAGE);
                    if(year == null) return;
                    String mes = JOptionPane.showInputDialog(null, "Ingrese el mes (1-12):", "Actualizar Fecha", JOptionPane.QUESTION_MESSAGE);
                    if(mes == null) return;
                    String dia = JOptionPane.showInputDialog(null, "Ingrese el dia:", "Actualizar Fecha", JOptionPane.QUESTION_MESSAGE);
                    if(dia == null) return;
                    setFechaPublicacion(Integer.parseInt(year.trim()), Integer.parseInt(mes.trim()), Integer.parseInt(dia.trim()));
                    JOptionPane.showMessageDialog(null, "Fecha de publicacion actualizada correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
                } catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(null, "Entrada invalida. Ingrese numeros.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case 2:
                String spec = JOptionPane.showInputDialog(null, "Ingrese la especificacion tecnica:", "Agregar Especificacion", JOptionPane.QUESTION_MESSAGE);
                if(spec != null && !spec.trim().isEmpty()){
                    addEspecificaciones(spec.trim());
                    JOptionPane.showMessageDialog(null, "Especificacion agregada correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
                }
                break;

            case 3:
                JOptionPane.showMessageDialog(null,
                    "Especificaciones de " + getNombreItem() + ":\n\n" + getEspecificacionesTexto(),
                    "Especificaciones Tecnicas", JOptionPane.INFORMATION_MESSAGE);
                break;

            default:
                JOptionPane.showMessageDialog(null, "Opcion no valida. Elija 1, 2 o 3.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}