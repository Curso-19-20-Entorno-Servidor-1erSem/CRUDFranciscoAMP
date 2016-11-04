package es.albarregas.conexion;

import java.sql.Connection;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

public class Conexion {

    final static Logger LOGGER = Logger.getRootLogger();

    DataSource datasource = null;

    public static DataSource getDataSource() {

        DataSource datasource = null;
        try {
            Context contextoInicial = new InitialContext();
            datasource = (DataSource) contextoInicial.lookup("java:comp/env/jdbc/CRUDPool");
        } catch (NamingException ex) {
            LOGGER.fatal("Problemas en el acceso al pool de conexiones", ex);

        }
        return datasource;
    }

    public static void closeConexion(Connection conexion, ResultSet resultado) {
        try {
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException ex) {
            LOGGER.fatal("Problemas al cerrar la conexión", ex);
        }

        try {
            if (resultado != null) {
                resultado.close();
            }
        } catch (SQLException ex) {
            LOGGER.fatal("Problemas al cerrar el ResultSet", ex);
        }
    }

}
