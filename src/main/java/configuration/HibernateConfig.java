package configuration;

import model.jpa.Author;
import model.jpa.Book;
import model.jpa.Computer;
import model.jpa.Student;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.Properties;

public class HibernateConfig {

    private static SessionFactory sessionFactory = sessionFactory();

    public static SessionFactory sessionFactory() {
        if (sessionFactory != null) {
            return sessionFactory;
        }

        StandardServiceRegistryBuilder registryBuilder =
                new StandardServiceRegistryBuilder();
        registryBuilder.applySettings(readProperties());

        StandardServiceRegistry registry = registryBuilder.build();

        Configuration cfg = new Configuration();
        cfg.addAnnotatedClass(Student.class);
        cfg.addAnnotatedClass(Author.class);
        cfg.addAnnotatedClass(Book.class);
        cfg.addAnnotatedClass(Computer.class);

        sessionFactory = cfg.buildSessionFactory(registry);
        return sessionFactory;
    }

    private static Properties readProperties() {
        try {
            Properties prop = new Properties();
            prop.load(HibernateConfig.class
            .getResourceAsStream("/hibernate.properties"));
            return prop;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
