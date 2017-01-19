/**
 * Read configuration
 * AuxConfFile.getString();
 * if value not exists save it in config file and return default
 */
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ConfigurationBuilder;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.IOException;

public class AuxConfFile {

    static enum DefaultConfigValues {
        oraDumpdir("dumpdir"),

        ociDrv("jdbc:oracle:oci"),
        ociUserId("user/password@tns_alias"),
        ociOraFile("dump_file_on_oracle_server_oci.dmp"),
        ociLocalFileUpload("dir/to/upload/dump_file_local.dmp"),
        ociLocalFileDownload("dir/to/download/dump_file_local_oci.dmp"),

        thinDrv("jdbc:oracle:thin"),
        thinUserId("user/password@database_host:1521/oracle_sid"),
        thinOraFile("dump_file_on_oracle_server_thin.dmp"),
        thinLocalFileUpload("dir/to/upload/dump_file_local.dmp"),
        thinLocalFileDownload("dir/to/download/dump_file_local_thin.dmp");

        private final String value;
        private DefaultConfigValues(String pValue) { value = pValue; }
        public String getValue() {return value;}
    }

    private static String configFileName = "config.properties";
    private static Configuration config;// =  configs.properties(configFile);

    private static Configuration init() throws ConfigurationException, IOException {
        File configFile;
        Configuration cfg;
        configFile = new File(configFileName);
        Configurations configs = new Configurations();
        FileBasedConfigurationBuilder<PropertiesConfiguration> configBuilder
                = configs.propertiesBuilder(configFile);

        if (configFile.exists()) {
            System.out.println("Using config file: " + configFile.getName());
            cfg = configBuilder.getConfiguration();
        } else {

            System.out.println("Config file does not exists. Create default.");
            configFile.createNewFile();
            cfg = configBuilder.getConfiguration();
            for (DefaultConfigValues c : DefaultConfigValues.values()) {
                cfg.addProperty(c.name(), c.getValue());
            }
            configBuilder.save();
        }
        return cfg;
    }

    public static String getString(String pName) {
        String ret = null;
        try {
            if (config == null) { config = init(); }

            ret = config.getString(pName);
            if (ret == null) {
                ret = DefaultConfigValues.valueOf(pName).getValue();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }
}
