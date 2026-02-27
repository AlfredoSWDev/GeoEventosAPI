package com.alfredo.geoeventosapi.dto;

public class ImagenResponseDTO {

    private boolean success;
    private String  url;
    private String  mensaje;

    // ── Constructor vacío ──
    public ImagenResponseDTO() {}

    // ── Constructor completo ──
    public ImagenResponseDTO(boolean success, String url, String mensaje) {
        this.success = success;
        this.url     = url;
        this.mensaje = mensaje;
    }

    // ── Getters y Setters ──
    public boolean isSuccess()              { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getUrl()          { return url; }
    public void setUrl(String url)  { this.url = url; }

    public String getMensaje()              { return mensaje; }
    public void setMensaje(String mensaje)  { this.mensaje = mensaje; }
}