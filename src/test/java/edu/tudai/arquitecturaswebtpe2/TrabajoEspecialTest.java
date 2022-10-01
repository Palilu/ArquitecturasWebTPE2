package edu.tudai.arquitecturaswebtpe2;

import edu.tudai.arquitecturaswebtpe2.domain.dto.CareerCount;
import edu.tudai.arquitecturaswebtpe2.domain.dto.CareerReportRow;
import edu.tudai.arquitecturaswebtpe2.domain.entity.*;
import edu.tudai.arquitecturaswebtpe2.domain.repository.CareerRepository;
import edu.tudai.arquitecturaswebtpe2.domain.repository.InscriptionRepository;
import edu.tudai.arquitecturaswebtpe2.domain.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author
 *  <ul>
 *    <li>Guillermina Lauge</li>
 *    <li>Pablo Mendoza</li>
 *    <li>Ricardo Gentil</li>
 *  </ul>
 *
 * Enunciado:
 *
 * 1) Considere el diseño de un registro de estudiantes, con la siguiente información: nombres,
 * apellido, edad, género, número de documento, ciudad de residencia, número de libreta
 * universitaria, carrera(s) en la que está inscripto, antigüedad en cada una de esas carreras, y
 * si se graduó o no. Diseñar el diagrama de objetos y el diagrama DER correspondiente.
 *
 * 2) Implementar consultas para:
 *  a) dar de alta un estudiante X
 *  b) matricular un estudiante en una carrera X
 *  c) recuperar todos los estudiantes, y especificar algún criterio de ordenamiento simple. X
 *  d) recuperar un estudiante, en base a su número de libreta universitaria. X
 *  e) recuperar todos los estudiantes, en base a su género. X
 *  f) recuperar las carreras con estudiantes inscriptos, y ordenar por cantidad de inscriptos. X
 *  g) recuperar los estudiantes de una determinada carrera, filtrado por ciudad de residencia. X
 *
 * 3) Generar un reporte de las carreras, que para cada carrera incluya información de los
 * inscriptos y egresados por año. Se deben ordenar las carreras alfabéticamente, y presentar
 * los años de manera cronológica.
 * Nota: las consultas deben ser resueltas mayormente en JPQL, y no en código Java.
 */
@SpringBootTest
public class TrabajoEspecialTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CareerRepository careerRepository;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    /**
     * Prueba el requerimiento: 2.a. dar de alta un estudiante
     */
    @Test
    public void createStudent() {
        this.createBestStudents();
        List<Student> students = studentRepository.findAll();
        students.forEach(System.out::println);
    }

    /**
     * Prueba el requerimiento: 2.b. matricular un estudiante en una carrera
     */
    @Test
    public void createInscription() {
        Student cosme = studentRepository.save(Student.builder()
                .givenNames("Cosme")
                .lastName("Fulanito")
                .city("Tandil")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .studentId(1L)
                .gender(Gender.MALE)
                .dni("1")
                .build());
        Career teclismo = careerRepository.save(Career.builder()
                .name("Teclismo")
                .build());
        Inscription inscriptionCosmeTeclismo = inscriptionRepository.save(Inscription.builder()
                .student(cosme)
                .career(teclismo)
                .since(LocalDateTime.now())
                .status(InscriptionStatus.INSCRIPTED)
                .build());
        System.out.println(inscriptionCosmeTeclismo);
    }

    /**
     * Prueba el requerimiento: 2.c. recuperar todos los estudiantes, y especificar algún criterio de ordenamiento simple.
     */
    @Test
    public void findStudentsSorted() {
        this.createBestStudents();
        List<Student> studentsByCityAsc = studentRepository.findAll(Sort.by(Sort.Direction.ASC, "city"));
        studentsByCityAsc.forEach(System.out::println);
        List<Student> studentsByNameDesc = studentRepository.findAll(Sort.by(Sort.Direction.DESC, "givenNames"));
        studentsByNameDesc.forEach(System.out::println);
    }

    /**
     * Prueba el requerimiento: 2.d. recuperar un estudiante, en base a su número de libreta universitaria.
     */
    @Test
    public void findStudentByStudentId() {
        Student cosme = studentRepository.save(Student.builder()
                .givenNames("Cosme")
                .lastName("Fulanito")
                .city("Tandil")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .studentId(4L)
                .gender(Gender.MALE)
                .dni("1")
                .build());

        Optional<Student> result = studentRepository.findByStudentId(4L);
        System.out.println(result);
    }

    /**
     * Prueba el requerimiento: 2.e. recuperar un estudiante, en base a su número de libreta universitaria.
     */
    @Test
    public void findStudentsByGender() {
        this.createBestStudents();

        List<Student> males = studentRepository.findByGender(Gender.MALE);
        List<Student> females = studentRepository.findByGender(Gender.FEMALE);
        List<Student> other = studentRepository.findByGender(Gender.OTHER);
        System.out.println("Males: " + males);
        System.out.println("Females: " + females);
        System.out.println("Other: " + other);
    }

    /**
     * Prueba el requerimiento: 2.f. recuperar las carreras con estudiantes inscriptos, y ordenar por cantidad de inscriptos.
     */
    @Test
    public void findCareersByInscriptions() {
        Student guille = studentRepository.save(Student.builder()
                .givenNames("Guillermina")
                .lastName("Lauge")
                .city("Tandil")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .studentId(1L)
                .gender(Gender.FEMALE)
                .dni("1")
                .build());
        Student pali = studentRepository.save(Student.builder()
                .givenNames("Pablo")
                .lastName("Mendoza")
                .city("Tandil")
                .dateOfBirth(LocalDate.of(1884, 4, 26))
                .studentId(2L)
                .gender(Gender.OTHER)
                .dni("2")
                .build());
        Student richard = studentRepository.save(Student.builder()
                .givenNames("Ricardo")
                .lastName("Gentil")
                .city("Tandil")
                .dateOfBirth(LocalDate.of(1983, 12, 21))
                .studentId(3L)
                .gender(Gender.MALE)
                .dni("3")
                .build());
        Career ids = careerRepository.save(Career.builder()
                .name("Ingeniería de Sistemas")
                .build());
        Career fyl = careerRepository.save(Career.builder()
                .name("Filosofía y Letras")
                .build());
        Career tudai = careerRepository.save(Career.builder()
                .name("TUDAI")
                .build());
        inscriptionRepository.save(Inscription.builder()
                .student(guille)
                .career(fyl)
                .since(LocalDateTime.now())
                .status(InscriptionStatus.FINISHED)
                .build());
        inscriptionRepository.save(Inscription.builder()
                .student(pali)
                .career(ids)
                .since(LocalDateTime.now())
                .status(InscriptionStatus.ABANDONED)
                .build());
        inscriptionRepository.save(Inscription.builder()
                .student(richard)
                .career(ids)
                .since(LocalDateTime.now())
                .status(InscriptionStatus.ABANDONED)
                .build());
        inscriptionRepository.save(Inscription.builder()
                .student(guille)
                .career(tudai)
                .since(LocalDateTime.now())
                .status(InscriptionStatus.INSCRIPTED)
                .build());
        inscriptionRepository.save(Inscription.builder()
                .student(pali)
                .career(tudai)
                .since(LocalDateTime.now())
                .status(InscriptionStatus.INSCRIPTED)
                .build());
        inscriptionRepository.save(Inscription.builder()
                .student(richard)
                .career(tudai)
                .since(LocalDateTime.now())
                .status(InscriptionStatus.INSCRIPTED)
                .build());

        List<CareerCount> careerCounts = careerRepository.getCareersByInscriptions();
        careerCounts.forEach(System.out::println);
    }

    /**
     * Prueba el requerimiento: 2.g. recuperar las carreras con estudiantes inscriptos, y ordenar por cantidad de inscriptos.
     */
    @Test
    public void findStudentsByCareerAndCity() {
        Student guille = studentRepository.save(Student.builder()
                .givenNames("Guillermina")
                .lastName("Lauge")
                .city("Tandil")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .studentId(1L)
                .gender(Gender.FEMALE)
                .dni("1")
                .build());
        Student pali = studentRepository.save(Student.builder()
                .givenNames("Pablo")
                .lastName("Mendoza")
                .city("Tandil")
                .dateOfBirth(LocalDate.of(1884, 4, 26))
                .studentId(2L)
                .gender(Gender.OTHER)
                .dni("2")
                .build());
        Student richard = studentRepository.save(Student.builder()
                .givenNames("Ricardo")
                .lastName("Gentil")
                .city("Ayacucho")
                .dateOfBirth(LocalDate.of(1983, 12, 21))
                .studentId(3L)
                .gender(Gender.MALE)
                .dni("3")
                .build());
        Career ids = careerRepository.save(Career.builder()
                .name("Ingeniería de Sistemas")
                .build());
        Career fyl = careerRepository.save(Career.builder()
                .name("Filosofía y Letras")
                .build());
        inscriptionRepository.save(Inscription.builder()
                .student(guille)
                .career(fyl)
                .since(LocalDateTime.now())
                .status(InscriptionStatus.FINISHED)
                .build());
        inscriptionRepository.save(Inscription.builder()
                .student(pali)
                .career(ids)
                .since(LocalDateTime.now())
                .status(InscriptionStatus.ABANDONED)
                .build());
        inscriptionRepository.save(Inscription.builder()
                .student(richard)
                .career(ids)
                .since(LocalDateTime.now())
                .status(InscriptionStatus.ABANDONED)
                .build());

        List<Student> philosophersOfTandil = studentRepository.findByCityAndCareer(fyl, "Tandil");
        philosophersOfTandil.forEach(System.out::println);
    }

    /**
     * Prueba el requerimiento:
     *
     * 3. Generar un reporte de las carreras, que para cada carrera incluya información de los
     *    inscriptos y egresados por año. Se deben ordenar las carreras alfabéticamente, y presentar
     *    los años de manera cronológica.
     */
    @Test
    public void getCareersReport() {
        Student guille = studentRepository.save(Student.builder()
                .givenNames("Guillermina")
                .lastName("Lauge")
                .city("Tandil")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .studentId(1L)
                .gender(Gender.FEMALE)
                .dni("1")
                .build());
        Student pali = studentRepository.save(Student.builder()
                .givenNames("Pablo")
                .lastName("Mendoza")
                .city("Tandil")
                .dateOfBirth(LocalDate.of(1884, 4, 26))
                .studentId(2L)
                .gender(Gender.OTHER)
                .dni("2")
                .build());
        Student richard = studentRepository.save(Student.builder()
                .givenNames("Ricardo")
                .lastName("Gentil")
                .city("Ayacucho")
                .dateOfBirth(LocalDate.of(1983, 12, 21))
                .studentId(3L)
                .gender(Gender.MALE)
                .dni("3")
                .build());
        Career ids = careerRepository.save(Career.builder()
                .name("Ingeniería de Sistemas")
                .build());
        Career fyl = careerRepository.save(Career.builder()
                .name("Filosofía y Letras")
                .build());
        inscriptionRepository.save(Inscription.builder()
                .student(guille)
                .career(fyl)
                .since(LocalDateTime.now())
                .status(InscriptionStatus.FINISHED)
                .build());
        inscriptionRepository.save(Inscription.builder()
                .student(pali)
                .career(ids)
                .since(LocalDateTime.now())
                .status(InscriptionStatus.ABANDONED)
                .build());
        inscriptionRepository.save(Inscription.builder()
                .student(richard)
                .career(ids)
                .since(LocalDateTime.now())
                .status(InscriptionStatus.ABANDONED)
                .build());

        List<CareerReportRow> careerReport = careerRepository.getCareersReport();
        careerReport.forEach(System.out::println);
    }

    private void createBestStudents() {
        studentRepository.save(Student.builder()
                .givenNames("Guillermina")
                .lastName("Lauge")
                .city("Tandil")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .studentId(1L)
                .gender(Gender.FEMALE)
                .dni("1")
                .build());
        studentRepository.save(Student.builder()
                .givenNames("Pablo")
                .lastName("Mendoza")
                .city("Sierras Bayas")
                .dateOfBirth(LocalDate.of(1884, 4, 26))
                .studentId(2L)
                .gender(Gender.OTHER)
                .dni("2")
                .build());
        studentRepository.save(Student.builder()
                .givenNames("Ricardo")
                .lastName("Gentil")
                .city("Ayacucho")
                .dateOfBirth(LocalDate.of(1983, 12, 21))
                .studentId(3L)
                .gender(Gender.MALE)
                .dni("3")
                .build());
    }
}
