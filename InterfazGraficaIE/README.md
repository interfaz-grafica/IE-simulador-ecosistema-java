# Interfaz Gráfica IE 2 - Simulador de Ecosistema (MVC, Swing y Persistencia de datos)

Segunda instancia evaluativa - IES Siglo 21. Este proyecto transforma el simulador de ecosistema de consola en una aplicación de escritorio interactiva, utilizando Java Swing y aplicando el patrón de arquitectura MVC estricto.

## Equipo de desarrollo y roles

- **Sophia García Ferrer:** Diseño UX/UI (Figma) y desarrollo de la Vista (Swing).
- **Agustín Benitez:** Diseño UX/UI (Figma) y desarrollo del Modelo (principalmente `Ecosistema.java`).
- **Máximo Padula:** Estructura base del MVC, desarrollo del Controlador y de la clase `InterfazGraficaIE` (lógica de validación e intermediario).
- **Nahuel Scarone:** Implementación del DAO (persistencia de datos), controles de ventana personalizados y desarrollo de la Vista (Swing).

## Enlaces

- **Prototipo en Figma:** https://www.figma.com/design/fL2WnBpCGtvG1Tdpg7uPOh/Dise%C3%B1oFigma-Benitez--Ferrer--Padula-y-Scarone?node-id=0-1&t=p7KWe5G988HQv uds-1
- **Video explicativo:** https://drive.google.com/file/d/1GRqzNLnHft4eSFotI2GrdpaGOAqnh6qc/view?usp=sharing
- **Drive de trabajo (pestaña IE2):** https://drive.google.com/drive/u/1/folders/1sYatSq8Axe8IMQxmHA24O-1Ar_wVqHJH

## Instrucciones de ejecución

1. Clonar el repositorio.
2. Abrir el proyecto en Apache NetBeans IDE.
3. Usar JDK 26.
4. Ejecutar `InterfazGraficaIE.java` desde el paquete `principal`.

## Estructura del proyecto (arquitectura MVC)

- **modelo:** entidades del ecosistema, jerarquía (Planta, Conejo, Lobo), polimorfismo y reglas de supervivencia. Independiente de la interfaz.
- **vista:** interfaces gráficas desarrolladas con el diseñador de NetBeans. Maneja los eventos de usuario y muestra datos procesados.
- **controlador:** intermediario. Recibe acciones de la vista, coordina con el modelo y el DAO, y devuelve strings o arreglos primitivos.
- **dao:** manejo de persistencia. Guarda y carga el estado de la simulación en un archivo `.txt`.
- **principal:** contiene el método `main` que inicia la aplicación y pone en marcha la arquitectura MVC.

## Desafíos encontrados y soluciones

Durante la etapa de integración tuvimos conflictos de merge en Git, específicamente al unir la lógica de las tablas con los eventos visuales personalizados. Lo resolvimos sentándonos en equipo, compartiendo pantalla y debuggeando juntos para unificar el código sin romper el diseño de NetBeans.

## Uso de inteligencia artificial

- **Sophia García Ferrer:** https://share.gemini.google/MLwsWvCd9S5g
- **Agustín Benitez:** https://claude.ai/share/314bd0e7-21bd-43f1-b5be-ea4e9f3b674f
- **Máximo Padula:** https://share.gemini.google/vIJrWibIiRQZ
- **Nahuel Scarone:** https://share.gemini.google/l9JdvZgL2ZCr