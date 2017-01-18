import orautil.OraFile;

import org.junit.Test;

import java.sql.SQLException;
import static org.junit.Assert.*;

/**
 * If we open file in read mode
 * and file doesn't exist in oracle directory
 * we get an error
 * java.sql.SQLException: ORA-29283: invalid file operation
 */
public class TestOraConnect {

    @Test
    public void fopen_rb_OCI() throws SQLException {
        OraFile oraFile = new OraFile();
        oraFile.fopen(AuxConfFile.getString("ociDrv"), AuxConfFile.getString("ociUserId"),
            AuxConfFile.getString("oraDumpdir"), AuxConfFile.getString("ociOraFile"), "rb", 32767);
        assertNotNull(oraFile.getConnection());
        oraFile.fclose();
    }

    @Test
    public void fopen_wb_OCI() throws SQLException {
        OraFile oraFile = new OraFile();
        oraFile.fopen(AuxConfFile.getString("ociDrv"), AuxConfFile.getString("ociUserId"),
            AuxConfFile.getString("oraDumpdir"), AuxConfFile.getString("ociOraFile"), "wb", 32767);
        assertNotNull(oraFile.getConnection());
        oraFile.fclose();
    }

    @Test
    public void fopen_rb_Thin() throws SQLException {
        OraFile oraFile = new OraFile();
        oraFile.fopen(AuxConfFile.getString("thinDrv"), AuxConfFile.getString("thinUserId"),
            AuxConfFile.getString("oraDumpdir"), AuxConfFile.getString("thinOraFile"), "rb", 32767);
        assertNotNull(oraFile.getConnection());
        oraFile.fclose();
    }

    @Test
    public void fopen_wb_Thin() throws SQLException {
        OraFile oraFile = new OraFile();
        oraFile.fopen(AuxConfFile.getString("thinDrv"), AuxConfFile.getString("thinUserId"),
            AuxConfFile.getString("oraDumpdir"), AuxConfFile.getString("thinOraFile"), "wb", 32767);
        assertNotNull(oraFile.getConnection());
        oraFile.fclose();
    }
}
