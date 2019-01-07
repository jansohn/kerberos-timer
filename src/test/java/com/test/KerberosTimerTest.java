package com.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.utils.DiagnosticCommand;

public class KerberosTimerTest {
    private static final Logger log = LoggerFactory.getLogger(KerberosTimerTest.class);

    private String serverName = "";
    private int port = 1433;
    private String dbName = "";
    private String userName = "";
    private String password = "";

    @BeforeClass
    public static void setUpOnce() throws URISyntaxException {
        log.info("Setting up Kerberos configuration...");

        System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");

        URL configUrl = KerberosTimerTest.class.getClassLoader().getResource("krb5.conf");
        assertThat(configUrl).isNotNull();
        System.setProperty("java.security.krb5.conf", Paths.get(configUrl.toURI()).toAbsolutePath().toString());
    }

    @Test
    public void testSelect() throws URISyntaxException, SQLServerException, SQLException {
        // thread dump before opening DB connection
        DiagnosticCommand.dump();

        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName(this.serverName);
        ds.setPortNumber(this.port);
        ds.setDatabaseName(this.dbName);
        ds.setUser(this.userName);
        ds.setPassword(this.password);
        ds.setIntegratedSecurity(true);
        ds.setAuthenticationScheme("JavaKerberos");

        try (Connection con = ds.getConnection();
                Statement stmt = con.createStatement();)
        {
            log.info("Connection established successfully.");

            // Create and execute an SQL statement that returns user name.
            String SQL = "SELECT SUSER_SNAME()";

            try (ResultSet rs = stmt.executeQuery(SQL)) {

                // Iterate through the data in the result set and display it.
                while (rs.next()) {
                    log.info("user name: {}", rs.getString(1));
                }
            }
        }

        // thread dump after opening DB connection
        DiagnosticCommand.dump();
    }
}
