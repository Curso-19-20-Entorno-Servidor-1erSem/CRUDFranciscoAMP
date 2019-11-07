package es.albarregas.utils;

/**
 *
 * @author Jesus
 */
public class MyLogger {

    public static void doLog(Exception excepcion, Class clase, String nivel) {

        /*
        * Realizamos las siguientes asignaciones:
        *   - A la propiedad "logPath" el nombre de la clase donde se produjo el error
        *   - A la propiedad "projectName" el nombre del proyecto.
        * Estas prpiedades del sistema se leerán en el fichero de propiedades de log4j.properties 
        */
        System.setProperty("logPath", clase.getSimpleName());
        System.setProperty("projectName", "CRUDPool");
        /*
        * Creamos un objeto logger a través del método getLogger de la clase Logger
        */
        org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(clase);
        /*
        * Configuramos la línea de error con la línea donde se ha producido y la causa
        */
        String out = "Línea: " + excepcion.getStackTrace()[0].getLineNumber() + " Causa: " + excepcion;

        /*
        * Dependiendo del nivel de logger asignaremos la salida a un logger determinado
        */
        switch (nivel) {

            case "fatal":
                logger.fatal(out);
                break;
            case "warn":
                logger.warn(out);
                break;
            case "error":
                logger.error(out);
                break;
            default:
                logger.info(out);
                
        }
        
    }
    
}
