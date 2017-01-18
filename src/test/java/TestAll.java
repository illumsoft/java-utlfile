/**
 * Note: jdbc:oracle:driver_type:[username/password]@database_specifier
 * driver_type = { oci | thin }
 * database_specifier = { tnsname | host:port/sid }
 */

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestParseCmdArgs.class,
    TestOraConnect.class,
    TestUploadFile.class,
    TestDownloadFile.class
})

public class TestAll { }
