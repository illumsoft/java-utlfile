import orautil.OraFile;

import org.junit.Test;
import java.io.IOException;
import java.sql.SQLException;

public class TestDownloadFile {

    // TODO need to compare checksums on local and remote file
    @Test
    public void downloadFileOCI() throws SQLException, IOException {
        OraFile oraFile = new OraFile();
        oraFile.fopen(AuxConfFile.getString("ociDrv"), AuxConfFile.getString("ociUserId"),
            AuxConfFile.getString("oraDumpdir"), AuxConfFile.getString("ociOraFile"), "rb", 32767);
        oraFile.get_raw(AuxConfFile.getString("ociLocalFileDownload"));
    }

    @Test
    public void downloadFileThin() throws SQLException, IOException {
        OraFile oraFile = new OraFile();
        oraFile.fopen(AuxConfFile.getString("thinDrv"), AuxConfFile.getString("thinUserId"),
            AuxConfFile.getString("oraDumpdir"), AuxConfFile.getString("thinOraFile"), "rb", 32767);
        oraFile.get_raw(AuxConfFile.getString("thinLocalFileDownload"));
    }
}
