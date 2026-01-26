package com.asur.modelos;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int id;
    private String nombre;
    private String apellido;
    private String documento;
    private int idTipoDocumento;
    private String correo;
    private boolean correoConfirmado;
    private String contrasena;
    private String calle;
    private String nroPuerta;
    private String apto;
    private int idEstadoUsuario;
    private Date fecNacimiento;
    private int idPerfil;
    private Timestamp fechaRegistro;
    private List<Telefono> telefonos;

    public Usuario() {
        this.telefonos = new ArrayList<>();
    }

    public Usuario(int id, String nombre, String apellido, String documento, int idTipoDocumento,
                   String correo, boolean correoConfirmado, String contrasena, String calle,
                   String nroPuerta, String apto, int idEstadoUsuario, Date fecNacimiento, int idPerfil) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.idTipoDocumento = idTipoDocumento;
        this.correo = correo;
        this.correoConfirmado = correoConfirmado;
        this.contrasena = contrasena;
        this.calle = calle;
        this.nroPuerta = nroPuerta;
        this.apto = apto;
        this.idEstadoUsuario = idEstadoUsuario;
        this.fecNacimiento = fecNacimiento;
        this.idPerfil = idPerfil;
        this.telefonos = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public int getIdTipoDocumento() { return idTipoDocumento; }
    public void setIdTipoDocumento(int idTipoDocumento) { this.idTipoDocumento = idTipoDocumento; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public boolean isCorreoConfirmado() { return correoConfirmado; }
    public void setCorreoConfirmado(boolean correoConfirmado) { this.correoConfirmado = correoConfirmado; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }
    public String getNroPuerta() { return nroPuerta; }
    public void setNroPuerta(String nroPuerta) { this.nroPuerta = nroPuerta; }
    public String getApto() { return apto; }
    public void setApto(String apto) { this.apto = apto; }
    public int getIdEstadoUsuario() { return idEstadoUsuario; }
    public void setIdEstadoUsuario(int idEstadoUsuario) { this.idEstadoUsuario = idEstadoUsuario; }
    public Date getFecNacimiento() { return fecNacimiento; }
    public void setFecNacimiento(Date fecNacimiento) { this.fecNacimiento = fecNacimiento; }
    public int getIdPerfil() { return idPerfil; }
    public void setIdPerfil(int idPerfil) { this.idPerfil = idPerfil; }
    public Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public List<Telefono> getTelefonos() { return telefonos; }
    public void setTelefonos(List<Telefono> telefonos) { this.telefonos = telefonos; }

    public void agregarTelefono(Telefono t) {
        this.telefonos.add(t);
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return String.format("Usuario[id=%d, %s %s, %s]", id, nombre, apellido, correo);
    }
}
