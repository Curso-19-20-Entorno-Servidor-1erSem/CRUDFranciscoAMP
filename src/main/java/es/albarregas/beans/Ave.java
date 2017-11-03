package es.albarregas.beans;

import java.io.Serializable;
import java.sql.Date;

public class Ave implements Serializable{

    String anilla;
    String especie;
    String lugar;
    Date fecha;

    public String getAnilla() {
        return anilla;
    }

    public void setAnilla(String anilla) {
        this.anilla = anilla;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
}
