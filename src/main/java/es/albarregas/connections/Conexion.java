package es.albarregas.connections;

import es.albarregas.utils.MyLogger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class Conexion {

    DataSource datasource;

    public static DataSource getDataSource() {
        

        DataSource datasource = null;
        try {
            Context contextoInicial = new InitialContext();
            datasource = (DataSource) contextoInicial.lookup("java:comp/env/jdbc/CRUDPool");
        } catch (NamingException ex) {
            
            MyLogger.doLog(ex, Conexion.class, "fatal");
            
        }
        return datasource;
    }

    public static void closeConexion(Connection conexion, ResultSet resultado) {
        try {
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException ex) {
            MyLogger.doLog(ex, Conexion.class, "error");
        }

        try {
            if (resultado != null) {
                resultado.close();
            }
        } catch (SQLException ex) {
            MyLogger.doLog(ex, Conexion.class, "error");
        }
    }

}
