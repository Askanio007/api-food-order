package config.hibernateConfig;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

    /*camel
    private final String dbUrl = "jdbc:postgresql://localhost:5432/food";
    private final String dbUsername = "fooduser";
    private final String dbPassword = "eew9jaekuamiePeibo6x";
    */

    //local
    /*private final String dbUrl = "jdbc:postgresql://localhost:5432/food_order";
    private final String dbUsername = "postgres";
    private final String dbPassword = "12332155";*/


    //hiroku
    private final String dbUrl = "jdbc:postgresql://ec2-54-217-208-52.eu-west-1.compute.amazonaws.com:5432/d8i30hqsab72ta";
    private final String dbUsername = "pragasinetdiej";
    private final String dbPassword = "711f550315ca3d32e1decd0b42b7239b3f94079ad0a7ddb70a59616847142a5e";

        @Bean(name = "dataSource")
           public DataSource getDataSource() {
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setUrl(dbUrl);
            dataSource.setUsername(dbUsername);
            dataSource.setPassword(dbPassword);
            return dataSource;
        }

    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource){
        LocalSessionFactoryBuilder sessionFactoryBuilder = new LocalSessionFactoryBuilder(dataSource);
        sessionFactoryBuilder.scanPackages("entity");
        sessionFactoryBuilder.addProperties(getHibernateProperties());
        return sessionFactoryBuilder.buildSessionFactory();
    }

    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory){
        return new HibernateTransactionManager(sessionFactory);
    }

    private Properties getHibernateProperties(){
        Properties properties = new Properties();
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL94Dialect");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.format_sql", "true");
        return properties;
    }
}
