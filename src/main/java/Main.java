import configuration.SpringConfig;
import model.jpa.Book;
import model.jpa.BookInfo;
import model.jpa.Person;
import model.jpa.Student;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.HibernateService;
import service.JpaService;
import service.SpringService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SpringConfig.class);


        //HibernateService hibernateService = context.getBean(HibernateService.class);
        //hibernateService.testCache();

        SpringService springService = context.getBean(SpringService.class);
        springService.saveEmployees();

        //JpaService jpaService = context.getBean(JpaService.class);
        //jpaService.lazyLoading();
        //List<Student> students = jpaService.prepareStudentData();
        //jpaService.addStudentsInDb(students);
        //List<BookInfo> books = jpaService.getBooksInfoProjectionWithCriteriaApi("Krakow");
        //System.out.println(books);

    }
}
