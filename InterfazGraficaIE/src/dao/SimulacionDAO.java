/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import modelo.Ecosistema;
/**
 *
 * @author Usuario
 */
public class SimulacionDAO {
    
    public boolean existePartidaGuardada() {
        return false;
    }

    public boolean borrarPartidaGuardada() {
        return true;
    }

    public boolean guardarEstado(Ecosistema eco) {
        return false; // todavía no guarda nada
    }

    public Ecosistema cargarEstado() {
        return null; // todavía no hay nada para cargar
    }
    
}
