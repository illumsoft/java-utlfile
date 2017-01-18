/**
 * export ORACLE_HOME=/path/to/oracle/home
 * export LD_LIBRARY_PATH=$ORACLE_HOME/lib:$LD_LIBRARY_PATH
 *
 * Compile:
 * javac -classpath $ORACLE_HOME/jdbc/lib/ojdbc6.jar OraFileTransfer.java
 * Run:
 * java -classpath .:$ORACLE_HOME/jdbc/lib/ojdbc6.jar \
 * OraFileTransfer jdbc:oracle:thin \
 * "sys as sysdba/sys_pass@host:1521/sid" download dumpdir file_thin.dmp
 *
 * java -classpath .:$ORACLE_HOME/jdbc/lib/ojdbc6.jar \
 * OraFileTransfer jdbc:oracle:oci \
 * "sys as sysdba/sys_pass@tnsname" download dumpdir file_OCI.dmp
 *
 * OraFileTransfer jdbc:oracle:thin user/pass@db   upload directory file
 * OraFileTransfer jdbc:oracle:oci  user/pass@db download directory file
 *
 * dbURL = oraDriver:userId
 * oraDriver = [ jdbc:oracle:thin | jdbc:oracle:oci ]
 *
 * userId { [username/password[ as sysdba]]@tnsname
 *        | [username/password[ as sysdba]]@host:port/sid }
 *
 * create directory dumpdir as '/tmp';
 * grant read, write on directory dumpdir to oraUser;
 * grant execute on utl_file to oraUser;
 */

package orautil;
import java.nio.file.Paths;

public class UtlFile {

    private static final int bufferSize = 32767; // max raw parameter for fopen, get_raw, put_raw
    private static final String UPLOAD = "upload";
    private static final String DOWNLOAD = "download";

    public static void usage() {
        System.out.println("\nStore (or get) local file to (from) Oracle directory.");
        System.out.println("\nUsage:");
        System.out.println("OraFileTransfer driver user/pass@database_specifier op directory localfile");
        System.out.println("driver := jdbc:oracle:{ oci | thin }");
        System.out.println("op := { download | upload }");
    }

    public static void main(String[] args) {
        String oradrv;
        String userId;
        String cmd;
        String oradir;
        String file;
        try {
            // TODO: for now parameters are position dependent
            oradrv = args[0];
            userId = args[1];
            cmd    = args[2];
            oradir = args[3];
            file   = args[4];
            // TODO: Is there a shorter way to get filename?
            String oraFileName = (Paths.get(file)).getFileName().toString();

            if (UPLOAD.equals(cmd)) {
                OraFile oraFile = new OraFile();
                oraFile.fopen(oradrv, userId, oradir, oraFileName, "wb", bufferSize);
                oraFile.put_raw(file);
            } else if (DOWNLOAD.equals(cmd)) {
                OraFile oraFile = new OraFile();
                oraFile.fopen(oradrv, userId, oradir, oraFileName, "rb", bufferSize);
                oraFile.get_raw(file);
            } else {
                usage();
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            usage();
            System.exit(10);
        }
    }
}
