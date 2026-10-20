
package com.example.gymlog_cloud.modelo;

/**
 * Representa la información personal de un usuario.
 *
 * Responsabilidad:
 * Almacenar y proporcionar acceso a los datos
 * recibidos desde nuestro servicio Flask.
 */
public class Usuario {

    // Atributos privados para aplicar encapsulamiento.
    private String nombre;
    private String correo;
    private String rut;

    /**
     * Construye un usuario con sus datos personales.
     *
     * @param nombre Nombre del usuario.
     * @param correo Correo electrónico del usuario.
     * @param rut RUT del usuario.
     */
    public Usuario(String nombre, String correo, String rut) {
        this.nombre = nombre;
        this.correo = correo;
        this.rut = rut;
    }

    /**
     * Devuelve el nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve el correo electrónico del usuario.
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Devuelve el RUT del usuario.
     */
    public String getRut() {
        return rut;
    }
}
