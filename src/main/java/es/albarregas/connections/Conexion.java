package es.albarregas.connections;

import es.albarregas.utils.MyLogger;

import java.sql.Connection;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class Conexion {

    /*
    * Declaramos el pool de conexiones
    */
    DataSource datasource;

    public static DataSource getDataSource() {
        
        DataSource datasource = null;
        try {
            /*
            * Para buscar y acceder a un recurso defnido en el Servidor de Aplicaciones
            *   - Creamos el contexto de búsqueda mediante la clase InitialContext.
            *   - Realizamos la búsqueda del recurso haciendo el casting correspondiente con la sentecia lookup
            */
            Context contextoInicial = new InitialContext();
            datasource = (DataSource) contextoInicial.lookup("java:comp/env/jdbc/CRUDPool");
        } catch (NamingException ex) {
            /*
            * Existe un error al intentar crear el pool de conexiones. Escribimos el logger y se visualiza error500.jsp
            */
            MyLogger.doLog(ex, Conexion.class, "fatal");

        }
        return datasource;
    }

    public static void closeConexion(Connection conexion) {
        try {
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException ex) {
            MyLogger.doLog(ex, Conexion.class, "error");
        }

        
    }

}
