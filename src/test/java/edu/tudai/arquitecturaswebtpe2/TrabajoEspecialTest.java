package edu.tudai.arquitecturaswebtpe2;

import edu.tudai.arquitecturaswebtpe2.domain.dto.CareerCount;
import edu.tudai.arquitecturaswebtpe2.domain.dto.CareerReportRow;
import edu.tudai.arquitecturaswebtpe2.domain.entity.*;
import edu.tudai.arquitecturaswebtpe2.domain.repository.CareerRepository;
import edu.tudai.arquitecturaswebtpe2.domain.repository.InscriptionRepository;
import edu.tudai.arquitecturaswebtpe2.domain.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
public class TrabajoEspecialTest extends ArquitecturasWebTpe2ApplicationTests {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CareerRepository careerRepository;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @BeforeEach
    public void cleanUp() {
        inscriptionRepository.deleteAll();
        careerRepository.deleteAll();
        studentRepository.deleteAll();
    }

    /**
     * Prueba el requerimiento: 2.a. dar de alta un estudiante
     */
    @Test
    public void createStudent() {
        System.out.println("2.a. Dar de alta un estudiante");
        this.createBestStudents();
        List<Student> students = studentRepository.findAll();
        assertThat(students.size()).isEqualTo(3);
        assertThat(students.get(0).getGivenNames()).isEqualTo("Guillermina");
        assertThat(students.get(1).getGivenNames()).isEqualTo("Pablo");
        assertThat(students.get(2).getGivenNames()).isEqualTo("Ricardo");
        System.out.println("2.a. Estudiante dado de alta satisfactoriamente");
    }

    /**
     * Prueba el requerimiento: 2.b. matricular un estudiante en una carrera
     */
    @Test
    public void createInscription() {
        System.out.println("2.b. Matriculando un estudiante en una carrera");
        Student cosme = studentRepository.save(Student.builder()
                .givenNames("Cosme")
                .lastName("Fulanito")
                .city("Tandil")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .studentId(1L)
                .gender(Gender.MALE)
                .dni("1")
                .build());
        System.out.println("2.b. Creado cosme");
        Career teclismo = careerRepository.save(Career.builder()
                .name("Teclismo")
                .build());
        System.out.println("2.b. Creado teclismo");
        Inscription inscriptionCosmeTeclismo = inscriptionRepository.save(Inscription.builder()
                .student(cosme)
                .career(teclismo)
                .since(LocalDateTime.now())
                .status(InscriptionStatus.INSCRIPTED)
                .build());
        assertThat(inscriptionCosmeTeclismo.getId()).isNotNull();
        assertThat(inscriptionCosmeTeclismo.getStudent()).isEqualTo(cosme);
        assertThat(inscriptionCosmeTeclismo.getCareer()).isEqualTo(teclismo);
        System.out.println("2.b. Cosme inscripto al teclismo");

    }

    /**
     * Prueba el requerimiento: 2.c. recuperar todos los estudiantes, y especificar algún criterio de ordenamiento simple.
     */
    @Test
    public void findStudentsSorted() {
        System.out.println("2.c. Recuperando todos los estudiantes, y especificar algún criterio de ordenamiento simple.");
        this.createBestStudents();
        System.out.println("2.c. Estudiantes ordenados por ciudad asc:");
        List<Student> studentsByCityAsc = studentRepository.findAll(Sort.by(Sort.Direction.ASC, "city"));
        assertThat(studentsByCityAsc.size()).isEqualTo(3);
        assertThat(studentsByCityAsc.get(0).getCity()).isEqualTo("Ayacucho");
        assertThat(studentsByCityAsc.get(1).getCity()).isEqualTo("Sierras Bayas");
        assertThat(studentsByCityAsc.get(2).getCity()).isEqualTo("Tandil");
        System.out.println("2.c. Estudiantes ordenados por nombres desc:");
        List<Student> studentsByNameDesc = studentRepository.findAll(Sort.by(Sort.Direction.DESC, "givenNames"));
        assertThat(studentsByNameDesc.size()).isEqualTo(3);
        assertThat(studentsByNameDesc.get(0).getGivenNames()).isEqualTo("Ricardo");
        assertThat(studentsByNameDesc.get(1).getGivenNames()).isEqualTo("Pablo");
        assertThat(studentsByNameDesc.get(2).getGivenNames()).isEqualTo("Guillermina");
        System.out.println("2.c. Estudiantes ordenados.");
    }

    /**
     * Prueba el requerimiento: 2.d. recuperar un estudiante, en base a su número de libreta universitaria.
     */
    @Test
    public void findStudentByStudentId() {
        System.out.println("2.d. recuperando un estudiante, en base a su número de libreta universitaria.");
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
        assertThat(result).isNotEmpty();
        assertThat(result.get())
                .usingRecursiveComparison()
                .ignoringFields("inscriptions")
                .isEqualTo(cosme);
        System.out.println("2.d. Estudiante recuperado en base a su número de libreta universitaria.");
    }

    /**
     * Prueba el requerimiento: 2.e. recuperar un estudiante, en base a su número de libreta universitaria.
     */
    @Test
    public void findStudentsByGender() {
        System.out.println("2.e. Recuperando un estudiante, en base a su número de libreta universitaria.");
        this.createBestStudents();
        List<Student> males = studentRepository.findByGender(Gender.MALE);
        List<Student> females = studentRepository.findByGender(Gender.FEMALE);
        List<Student> other = studentRepository.findByGender(Gender.OTHER);
        assertThat(males.get(0).getGender()).isEqualTo(Gender.MALE);
        assertThat(females.get(0).getGender()).isEqualTo(Gender.FEMALE);
        assertThat(other.get(0).getGender()).isEqualTo(Gender.OTHER);
        System.out.println("2.e. Estudiante recuperado en base a su número de libreta universitaria.");
    }

    /**
     * Prueba el requerimiento: 2.f. recuperar las carreras con estudiantes inscriptos, y ordenar por cantidad de inscriptos.
     */
    @Test
    public void findCareersByInscriptions() {
        System.out.println("2.f. Recuperando las carreras con estudiantes inscriptos, y ordenar por cantidad de inscriptos.");
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
        assertThat(careerCounts.size()).isEqualTo(3);
        assertThat(careerCounts.get(0).getCareerName()).isEqualTo("TUDAI");
        assertThat(careerCounts.get(1).getCareerName()).isEqualTo("Ingeniería de Sistemas");
        assertThat(careerCounts.get(2).getCareerName()).isEqualTo("Filosofía y Letras");
        System.out.println("2.f. Recuperadas las carreras con estudiantes inscriptos, y ordenar por cantidad de inscriptos.");
    }

    /**
     * Prueba el requerimiento: 2.g. recuperar las carreras con estudiantes inscriptos, y ordenar por cantidad de inscriptos.
     */
    @Test
    public void findStudentsByCareerAndCity() {
        System.out.println("2.g. Recuperando las carreras con estudiantes inscriptos, y ordenar por cantidad de inscriptos.");
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
        assertThat(philosophersOfTandil.size()).isEqualTo(1);
        assertThat(philosophersOfTandil.get(0).getGivenNames()).isEqualTo("Guillermina");
        System.out.println("2.g. Recuperadas las carreras con estudiantes inscriptos, y ordenar por cantidad de inscriptos.");
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
        System.out.println("3. Generar un reporte de las carreras.");
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
                .status(InscriptionStatus.INSCRIPTED)
                .build());
        inscriptionRepository.save(Inscription.builder()
                .student(richard)
                .career(ids)
                .since(LocalDateTime.now())
                .status(InscriptionStatus.INSCRIPTED)
                .build());

        List<CareerReportRow> careerReport = careerRepository.getCareersReport();
        assertThat(careerReport.size()).isEqualTo(2);
        assertThat(careerReport.get(0).getCareerName()).isEqualTo("Filosofía y Letras");
        assertThat(careerReport.get(0).getYear()).isEqualTo(2022);
        assertThat(careerReport.get(0).getGraduated()).isEqualTo(1);
        assertThat(careerReport.get(0).getInscripted()).isEqualTo(0);
        assertThat(careerReport.get(1).getCareerName()).isEqualTo("Ingeniería de Sistemas");
        assertThat(careerReport.get(1).getYear()).isEqualTo(2022);
        assertThat(careerReport.get(1).getGraduated()).isEqualTo(0);
        assertThat(careerReport.get(1).getInscripted()).isEqualTo(2);
        System.out.println("3. Reporte de las carreras generado.");
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
