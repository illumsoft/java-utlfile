import orautil.UtlFile;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

public class TestParseCmdArgs {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void mainOnlyThreeArgs() {
        String[] args = new String[3];
        args[0] = "user/password@tns_alias";
        args[1] = "upload";
        args[2] = "dumpdir";

        exit.expectSystemExitWithStatus(10);
        UtlFile c = new UtlFile();
        c.main(args);
    }

    @Test
    public void mainOnlyFourArgs() {
        String[] args = new String[4];
        args[0] = "driver";
        args[1] = "user/password@tns_alias";
        args[2] = "upload";
        args[3] = "dumpdir";

        exit.expectSystemExitWithStatus(10);
        UtlFile c = new UtlFile();
        c.main(args);
    }

    @Test
    public void mainWrongCmd() {
        String[] args = new String[5];
        args[0] = "driver";
        args[1] = "user/password@tns_alias";
        args[2] = "wrond_cmd";
        args[3] = "dumpdir";
        args[4] = "user_file.dmp";

        exit.expectSystemExitWithStatus(1);
        UtlFile c = new UtlFile();
        c.main(args);
    }
}
