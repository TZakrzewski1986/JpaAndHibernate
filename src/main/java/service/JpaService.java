package service;

import configuration.JpaConfig;
import model.jpa.*;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;

@Service
public class JpaService {

    private EntityManager entityManager;

    public JpaService(JpaConfig jpaConfig) {
        entityManager = jpaConfig.getEntityMenager();
    }

    public void test() {
        System.out.println("JpaService test");
    }

    public void savePersonInDb() {
        entityManager.getTransaction().begin();
        Person person = new Person();
        person.setAddress(new Address("Kraków", "Grodzka", "10-100"));
        person.setBillingAddress(new Address("Warszawa", "Wiejska", "10-300"));
        person.setName("Andrzej");
        person.setLastname("Duda");
        entityManager.persist(person);
        entityManager.getTransaction().commit();
    }

    public Person getPersonFromDb(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> query = cb.createQuery(Person.class);
        Root<Person> personRoot = query.from(Person.class);
        query.select(personRoot).where(cb.equal(personRoot.get(Person_.name), name));
        return entityManager.createQuery(query).getSingleResult();
    }

    public Person getPersonFromDbUsingNativeSql(String name) {
        Query query = entityManager.createNativeQuery("select * from  person p where p.name = ?1", Person.class);
        query.setParameter(1, name);
        return (Person) query.getSingleResult();
    }

    public Person getPersonFromDbUsingJpql(String name) {
        TypedQuery<Person> query = entityManager.createQuery(
                "from Person p where p.name = :name", Person.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    public void modifyPerson(String name) {
        Person person = getPersonFromDb(name);
        entityManager.getTransaction().begin();
        person.setLastname("Zmiana");
        entityManager.merge(person);
        entityManager.getTransaction().commit();
    }

    public void addStudentsInDb(List<Student> students) {
        entityManager.getTransaction().begin();
        students.forEach(entityManager::persist);
        entityManager.getTransaction().commit();
    }

    public List<Student> prepareStudentData() {
        Computer computer = new Computer();
        computer.setSerialNumber("XYZ123");
        computer.setSerialNumber("Asus #1");
        computer.setLocalization("Krakow");
        Computer computer2 = new Computer();
        computer2.setSerialNumber("ABC123");
        computer2.setSerialNumber("Asus #2");
        computer2.setLocalization("Warszawa");
        Author author = new Author();
        author.setName("Julian");
        author.setSurname("Tuwim");
        Author author2 = new Author();
        author2.setName("Jan");
        author2.setSurname("Brzechwa");
        Author author3 = new Author();
        author3.setName("Paulo");
        author3.setSurname("Coelho");
        Book book = new Book();
        book.setTitle("Wiersze");
        book.setAuthor(author);
        Book book2 = new Book();
        book2.setTitle("Akademia Pana kleksa");
        book2.setAuthor(author2);
        Book book3 = new Book();
        book3.setTitle("Lepszy nóż w plecy niż...");
        book3.setAuthor(author3);
        Student student = new Student();
        student.setName("Andrzej");
        student.setSurname("Duda");
        student.getBooks().add(book);
        book.setStudent(student);
        student.getComputers().add(computer);
        student.getComputers().add(computer2);
        computer.getStudents().add(student);
        computer2.getStudents().add(student);
        Student student2 = new Student();
        student2.setName("Aleksander");
        student2.setSurname("Kwasniewski");
        student2.getBooks().add(book2);
        book2.setStudent(student2);
        student2.getComputers().add(computer);
        computer.getStudents().add(student2);
        Student student3 = new Student();
        student3.setName("Krzysztof");
        student3.setSurname("Kononowicz");
        student3.getBooks().add(book3);
        book3.setStudent(student3);
        student3.getComputers().add(computer2);
        computer2.getStudents().add(student3);
        return Arrays.asList(student, student2, student3);
    }

    public List<Book> getBooksTakenByStudentsFromLocationNativeQuery(String location) {
        Query query = entityManager.createNativeQuery("SELECT b.* from book b\n" +
                "join student s on b.student_id = s.id\n" +
                "join computer_student cs on s.id = cs.student_id\n" +
                "join computer co on cs.computer_id = co.id\n" +
                "WHERE co.localization = ?1", Book.class);
        query.setParameter(1, location);
        return query.getResultList();
    }

    public List<Book> getBooksTakenByStudentsFromLocationJpql(String location) {
        TypedQuery<Book> query = entityManager.createQuery(
                "from Book b "
                        + "Join b.student s "
                        + "Join s.computers c "
                        + "Where c.localization = :localization ", Book.class
        );
        query.setParameter("localization", location);
        return query.getResultList();
    }

    public List<BookInfo> getBooksInfoProjectionWithJpql(String location) {
        TypedQuery<BookInfo> query = entityManager.createQuery(
                "select new model.jpa.BookInfo(b.title, s.name, s.surname) "
                        + "from Book b "
                        + "join b.student s "
                        + "join s.computers c "
                        + "where c.localization = :localization", BookInfo.class
        );
        query.setParameter("localization", location);
        return query.getResultList();
    }

    public List<BookInfo> getBooksInfoProjectionWithCriteriaApi(String location) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookInfo> query = cb.createQuery(BookInfo.class);
        Root<Book> bookRoot = query.from(Book.class);
        Join<Book, Student> studentJoin = bookRoot.join("student");
        Join<Student, Computer> computerJoin = studentJoin.join("computers");

        query
                .select(cb.construct(BookInfo.class,
                        bookRoot.get("title"),
                        studentJoin.get("name"),
                        studentJoin.get("surname")))
                .where(cb.equal(computerJoin.get("localization"), location))
                .groupBy(bookRoot.get("title"),
                        studentJoin.get("name"), studentJoin.get("surname"));

        return entityManager.createQuery(query).getResultList();
    }

    public void lazyLoading() {
        entityManager.getTransaction().begin();
        CriteriaQuery<Student> query = entityManager.getCriteriaBuilder()
                .createQuery(Student.class);

        Root<Student> studentRoot = query.from(Student.class);
        query.select(studentRoot);
        List<Student> students = entityManager.createQuery(query)
                .getResultList();
        System.out.println(students.get(0).getBooks().size());
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
