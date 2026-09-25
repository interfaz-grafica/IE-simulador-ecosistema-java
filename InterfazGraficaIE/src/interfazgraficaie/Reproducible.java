package interfazgraficaie;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

/**
 *
 * @author GZ TIENDA
 */
public interface Reproducible {
    
   void reproducirse(Ecosistema eco);
   
   void puedeReproducirse();
   
   default void intentarReproduccion(Ecosistema eco)
   {
       
   }
    
}
