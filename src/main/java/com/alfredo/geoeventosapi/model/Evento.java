package com.alfredo.geoeventosapi.model;


public class Evento {

    private Integer eventId;
    private String  nombreEvento;
    private String  valorEvento;
    private String  lugarEvento;
    private String  vigenciaEvento;
    private String  descripcionEvento;
    private String  fotosEvento;

    public Evento() {}

    public Evento(Integer eventId, String nombreEvento, String valorEvento,
                  String lugarEvento, String vigenciaEvento,
                  String descripcionEvento, String fotosEvento) {
        this.eventId           = eventId;
        this.nombreEvento      = nombreEvento;
        this.valorEvento       = valorEvento;
        this.lugarEvento       = lugarEvento;
        this.vigenciaEvento    = vigenciaEvento;
        this.descripcionEvento = descripcionEvento;
        this.fotosEvento       = fotosEvento;
    }

    public Integer getEventId()                       { return eventId; }
    public void setEventId(Integer eventId)           { this.eventId = eventId; }

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