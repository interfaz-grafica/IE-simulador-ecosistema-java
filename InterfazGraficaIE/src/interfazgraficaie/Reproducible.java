package interfazgraficaie;

public interface Reproducible {
    
   void reproducirse(Ecosistema eco);
   
   void puedeReproducirse();
   
   default void intentarReproduccion(Ecosistema eco) {
       
   }
    
}
