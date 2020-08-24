package configuration;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

@Component
public class JpaConfig {

    public EntityManager getEntityMenager() {
        return Persistence
                .createEntityManagerFactory("jpa-configuration")
                .createEntityManager();
    }

}
