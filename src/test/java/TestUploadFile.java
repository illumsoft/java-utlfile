import orautil.OraFile;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

public class TestUploadFile {

    // TODO need to compare checksums on local and remote file
    @Test
    public void uploadFileOCI() throws SQLException, IOException {
        OraFile oraFile = new OraFile();
        oraFile.fopen(AuxConfFile.getString("ociDrv"), AuxConfFile.getString("ociUserId"),
            AuxConfFile.getString("oraDumpdir"), AuxConfFile.getString("ociOraFile"), "wb", 32767);
        oraFile.put_raw(AuxConfFile.getString("ociLocalFileUpload"));
    }

    @Test
    public void uploadFileThin() throws SQLException, IOException {
        OraFile oraFile = new OraFile();
        oraFile.fopen(AuxConfFile.getString("thinDrv"), AuxConfFile.getString("thinUserId"),
            AuxConfFile.getString("oraDumpdir"), AuxConfFile.getString("thinOraFile"), "wb", 32767);
        oraFile.put_raw(AuxConfFile.getString("thinLocalFileUpload"));
    }
}
