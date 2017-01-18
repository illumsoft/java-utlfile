/**
 * Class represents a file on a database server.
 */
package orautil;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.pool.OracleDataSource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class OraFile {
    private static final String sqlFopen =
        "declare fh utl_file.file_type;"
            + "begin fh := utl_file.fopen(upper(:location), :filename, :open_mode, :max_linesize);"
            + ":id := fh.id; :datatype := fh.datatype; :byte_mode := case fh.byte_mode when true then 1 else 0 end; end;";
    private static final String sqlGetRaw =
        "declare fh utl_file.file_type;"
            + "begin fh.id := :id; fh.datatype := :datatype; fh.byte_mode := case :byte_mode when 1 then true else false end;"
            + "utl_file.get_raw(fh, :buffer); exception when no_data_found then null; end;";
    private static final String sqlPutRaw =
        "declare fh utl_file.file_type; begin fh.id := :id; fh.datatype := :datatype; fh.byte_mode := case :byte_mode when 1 then true else false end;"
            + "utl_file.put_raw(fh, :buffer, true); end;";
    private static final String sqlFclose =
        "declare fh utl_file.file_type;"
            + "begin fh.id := :id; fh.datatype := :datatype; fh.byte_mode := case :byte_mode when 1 then true else false end;"
            + "utl_file.fclose(fh); end;";
    private Connection conn;
    // map to utl_file.file_type fields
    private int id;
    private int datatype;
    private int byte_mode; // 0 - false; 1 - true
    // fopen, put_raw, get_raw
    private int maxBufferSize;

    private CallableStatement createStatement(String statementText) throws SQLException {
        CallableStatement stmt = null;
        stmt = conn.prepareCall(statementText);
        stmt.setInt("id", id);
        stmt.setInt("datatype", datatype);
        stmt.setInt("byte_mode", byte_mode);
        return stmt;
    }

    private void connect(String driver, String userId) throws SQLException {
        OracleDataSource ds = new OracleDataSource();
        ds.setURL(driver + ":" + userId);
        conn = ds.getConnection();
        conn.setAutoCommit(false);
    }

    @Override
    protected void finalize() throws Throwable {
        fclose();
        super.finalize();
    }

    public void fclose() throws SQLException {
        CallableStatement stmt = null;
        try {
            stmt = createStatement(sqlFclose);
            stmt.execute();
        }
        finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
            conn = null;
            id = 0;
            datatype = 0;
            byte_mode = 0;
        }
    }
    public void fopen(String driver,
                      String userId,
                      String oraDir,
                      String oraFile,
                      String openMode,
                      int maxBuffSize)
        throws SQLException
    {
        maxBufferSize = maxBuffSize;
        connect(driver, userId);
        CallableStatement stmt;

        stmt = conn.prepareCall(sqlFopen);
        stmt.setString("location", oraDir);
        stmt.setString("filename", oraFile);
        stmt.setString("open_mode", openMode);
        stmt.setInt("max_linesize", maxBufferSize);
        stmt.registerOutParameter("id", OracleTypes.INTEGER);
        stmt.registerOutParameter("datatype", OracleTypes.INTEGER);
        stmt.registerOutParameter("byte_mode", OracleTypes.INTEGER);
        stmt.execute();
        id = stmt.getInt("id");
        datatype = stmt.getInt("datatype");
        byte_mode = stmt.getInt("byte_mode");
    }

    public void put_raw(String fromFile)
        throws SQLException,/* FileNotFoundException,*/ IOException
    {
        CallableStatement stmt  = null;
        InputStream       input = null;
        int               bytesRead = 0;
        byte[]            buffer = null;

        try {
            buffer = new byte[maxBufferSize];
            input = new BufferedInputStream(new FileInputStream(fromFile)); // FileNotFoundException
            stmt = createStatement(sqlPutRaw);

            while ((bytesRead = input.read(buffer)) > 0) {
                byte[] rawBuff = new byte[bytesRead];
                for (int i = 0; i < rawBuff.length; i++) {
                    rawBuff[i] = buffer[i];
                }
                stmt.setBytes("buffer", rawBuff);
                stmt.execute();
            }
        }
        finally {
            if (input != null) input.close();
            if (stmt != null) stmt.close();
        }
    }

    public void get_raw(String toFile)
        throws SQLException, IOException
    {
        CallableStatement stmt  = null;
        OutputStream      output = null;
        byte[]            buffer = null;

        try {
            output = new BufferedOutputStream(new FileOutputStream(toFile));

            stmt = createStatement(sqlGetRaw);
            stmt.registerOutParameter("buffer", OracleTypes.RAW);
            do {
                stmt.execute();
                buffer = stmt.getBytes("buffer");
                if (buffer != null) {
                    output.write(buffer);
                }
            } while (buffer != null);
        }
        finally {
            if (output != null) output.close();
            if (stmt != null) stmt.close();
        }
    }

    public Connection getConnection() {return conn;}
}
