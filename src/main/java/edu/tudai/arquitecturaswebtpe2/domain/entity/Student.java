package edu.tudai.arquitecturaswebtpe2.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author
 *  <ul>
 *    <li>Guillermina Lauge</li>
 *    <li>Pablo Mendoza</li>
 *    <li>Ricardo Gentil</li>
 *  </ul>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"id", "givenNames", "lastName"})
@Builder
@EqualsAndHashCode
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String givenNames;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String dni;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Long studentId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.ALL)
    private List<Inscription> inscriptions;
}
