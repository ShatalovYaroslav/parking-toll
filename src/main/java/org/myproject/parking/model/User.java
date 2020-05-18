
package org.myproject.parking.model;

import lombok.*;


@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private String name;

    private int age;

    private double salary;
}
