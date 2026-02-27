package com.alfredo.geoeventosapi.dto;

public class EventoDTO {

    private String nombreEvento;
    private String valorEvento;
    private String lugarEvento;
    private String vigenciaEvento;
    private String descripcionEvento;
    private String fotosEvento;

    public EventoDTO() {}

    public String getNombreEvento()                   { return nombreEvento; }
    public void setNombreEvento(String nombreEvento)  { this.nombreEvento = nombreEvento; }

    public String getValorEvento()                    { return valorEvento; }
    public void setValorEvento(String valorEvento)    { this.valorEvento = valorEvento; }

    public String getLugarEvento()                    { return lugarEvento; }
    public void setLugarEvento(String lugarEvento)    { this.lugarEvento = lugarEvento; }

    public String getVigenciaEvento()                       { return vigenciaEvento; }
    public void setVigenciaEvento(String vigenciaEvento)    { this.vigenciaEvento = vigenciaEvento; }

    public String getDescripcionEvento()                          { return descripcionEvento; }
    public void setDescripcionEvento(String descripcionEvento)    { this.descripcionEvento = descripcionEvento; }

    public String getFotosEvento()                    { return fotosEvento; }
    public void setFotosEvento(String fotosEvento)    { this.fotosEvento = fotosEvento; }
}