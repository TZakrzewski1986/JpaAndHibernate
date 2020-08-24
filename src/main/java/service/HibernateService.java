package service;

import configuration.HibernateConfig;
import model.jpa.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class HibernateService {

    private SessionFactory sessionFactory;

    private HibernateService() {
        this.sessionFactory = HibernateConfig.sessionFactory();
    }

    public void lazyLoading() {
        Session session = sessionFactory.openSession();
        CriteriaQuery<Student> query = session.getCriteriaBuilder()
                .createQuery(Student.class);

        Root<Student> studentRoot = query.from(Student.class);
        query.select(studentRoot);
        List<Student> students = session.createQuery(query)
                .getResultList();
        System.out.println(students.get(0).getBooks().size());
        session.close();
    }

    public void testCache() {
        Session session1 = sessionFactory.openSession();

        Student student_1 = session1.byId(Student.class).load(3);
        Student student_2 = session1.byId(Student.class).load(19);

        session1.clear();
        Student student_3 = session1.byId(Student.class).load(39);

        Session session2 = sessionFactory.openSession();
        Student student_4 = session2.byId(Student.class).load(16);
    }
}
