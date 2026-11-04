# EcoSim: Simulador de Ecosistema (versión Swing)

Aplicación de escritorio en Java con interfaz gráfica Swing. Transforma el simulador de consola de la primera instancia en una aplicación con arquitectura MVC: el jugador configura el ecosistema, avanza turno a turno, interviene cada 3 turnos y al final ve un reporte.

## Integrantes y roles

| Integrante     | Rol                                                                                |
| -------------- | ---------------------------------------------------------------------------------- |
| Matías Colomba | Configuración inicial, controladores de configuración y navegación, estructura MVC |
| Nicolás Busso  | Procesamiento de turnos y pantalla de simulación                                   |
| Máximo Paz     | DAO: guardado y recuperación de la simulación                                      |
| Bruno Ariza    | Intervenciones, estadísticas y reporte final                                       |
| Todos          | Figma (pantallas repartidas entre los cuatro)                                      |

## Requisitos y ejecución

- **NetBeans:** 28
- **JDK:** 25 (configurado en `EcoSim/nbproject/project.properties`, `javac.source=25`)
- **Librerías externas:** ninguna. Solo la biblioteca estándar de Java y Swing.

Pasos:

1. Clonar el repositorio: `git clone https://github.com/MColomba/Interfaces-Graficas-Parcial-1.git`
2. En NetBeans, elegir **File → Open Project** y seleccionar la carpeta `EcoSim` (la que tiene el icono de proyecto).
3. Presionar **Run Project** (F6). La clase principal es `principal.EcoSim`.

Las rutas son relativas al proyecto: no hace falta configurar nada en la máquina.

## Estructura del proyecto

El código está en `EcoSim/src`, separado en paquetes según la capa:

| Paquete | Responsabilidad |
|---|---|
| `modelo` | Entidades (`Entidad`, `Animal`, `Conejo`, `Lobo`, `Planta`, `PlantaVenenosa`), interfaces (`Mortal`, `Peligroso`, `Reproducible`), `Clima`, `Ecosistema` (reglas y turnos), `ReporteFinal`, `Estadisticas` y `ResultadoIntervencion`. No usa Swing ni consola. |
| `controlador` | Recibe las acciones de la vista, valida, coordina al modelo y le indica a la vista qué mostrar. `ControladorPrincipal` (navegación), `ControladorConfiguracion`, `ControladorIntervencion`, `ControladorReporte` y `ControladorSimulacionAPI`. No tiene componentes de Swing. |
| `vista` | Pantallas diseñadas con el editor visual de NetBeans: `fraVentanaPrincipal` (ventana con CardLayout), `PanelConfigIni`, `PanelSimulacio`, `PanelIntervencion` y `PanelReporte`. Solo muestra datos y envía las acciones al controlador. `VistaPrincipal` es el contrato que cumple la ventana. |
| `dao` | Guardado y carga de la simulación en archivos de texto. [Pendiente: completar con el trabajo de Máximo.] |
| `principal` | `EcoSim`, el `main`: crea las vistas y los controladores, los conecta y muestra la ventana. |

Las pruebas están en `EcoSim/test` (`controlador` y `modelo`). Son programas con `main` que lanzan un error si alguna comprobación falla. Para correrlas: clic derecho sobre el archivo → **Run File** (Shift+F6).

## Reglas de la simulación

- **Configuración:** plantas de 5 a 30, conejos de 2 a 15, lobos de 1 a 5, clima inicial y turnos de 10 a 50.
- **Cada turno:** las plantas se reproducen, los conejos comen, los lobos cazan, todas las entidades envejecen y pierden energía, y se eliminan las que quedan sin energía.
- **Intervención:** cada 3 turnos se puede cambiar el clima, agregar una entidad o seguir sin intervenir. Máximo 5 lobos en toda la simulación.
- **Fin:** al completar los turnos configurados o cuando se extingue una población.
- **Reporte final:** causa de fin, turno de mayor actividad, entidades más longevas, lobo con más cacerías, y nacimientos y muertes por tipo.

## Diseño (Figma)

https://www.figma.com/files/team/1699432198787047201/folder/683414875?fuid=1647083042890684552

Las capturas de las pantallas están en /Documentacion/Prompts. Cada uno de los integrantes tiene una carpeta con sus archivos respectivos.

## Video explicativo

[Completar con el link al video de la segunda instancia]

## Uso de IA y fuentes

Cada integrante documenta por separado las consultas a IA en `Documentacion/Promps/<nombre>/`.

## Desafíos encontrados

- **Tamaño de la ventana:** con CardLayout, la ventana tomaba el tamaño de la pantalla más grande. Se resolvió con un `BorderLayout` y ajustando el tamaño de cada pantalla.
- **Look and feel:** el diseñador usa el estilo de Windows y Java usa Metal por defecto, lo que cambiaba fuentes y tamaños. Se fijó el estilo del sistema en `EcoSim.main`.
