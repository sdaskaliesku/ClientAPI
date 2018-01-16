package com.client.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.net.URI;
import java.util.Objects;

/**
 * @author sdaskaliesku
 */
public class DBConfig {
    private String driver;
    private String dialect;
    private String username;
    private String password;
    private String url;

    public DBConfig(String driver, String dialect, String username, String password, String url) {
        this.driver = driver;
        this.dialect = dialect;
        this.username = username;
        this.password = password;
        this.url = url;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static boolean isHeroku() {
        return StringUtils.isNotEmpty(System.getenv("DATABASE_URL"));
    }

    private static URI getHerokuUrl() {
        try {
            return new URI(System.getenv("DATABASE_URL"));
        } catch (Exception e) {
            return null;
        }
    }

    public static DBConfig getHerokuConfig() {
        if (isHeroku()) {
            URI dbUri = getHerokuUrl();
            if (Objects.nonNull(dbUri)) {
                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String url = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?useUnicode=true&characterEncoding=utf8";
                return new DBConfig("org.postgresql.Driver", "org.hibernate.dialect.PostgreSQLDialect", username, password, url);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("driver", driver)
                .append("dialect", dialect)
                .append("username", username)
                .append("password", password)
                .append("url", url)
                .toString();
    }
}
