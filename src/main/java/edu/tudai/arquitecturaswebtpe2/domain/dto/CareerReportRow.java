package edu.tudai.arquitecturaswebtpe2.domain.dto;

import lombok.*;

/**
 * @author
 *  <ul>
 *    <li>Guillermina Lauge</li>
 *    <li>Pablo Mendoza</li>
 *    <li>Ricardo Gentil</li>
 *  </ul>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CareerReportRow {
    private Long careerId;
    private String careerName;
    private Integer year;
    private Long inscripted;
    private Long graduated;
}
