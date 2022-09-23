package edunhnil.project.forum.api.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataSourceConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.main")
    public DataSourceProperties MainPostgresDataSource() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.main.hikari")
    public DataSource MainDatasource() {
        return MainPostgresDataSource().initializeDataSourceBuilder().build();
    }

    @Bean
    public JdbcTemplate MainJdbcTemplate(@Qualifier("MainDatasource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
