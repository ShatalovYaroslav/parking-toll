package org.myproject.parking.pricing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.type.SerializableToBlobType;

import javax.persistence.*;
import java.util.Map;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
@Entity(name = "POLICYCONFIG")
@Table(name = "POLICYCONFIG")
public class PricingConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @JsonIgnore
    private Long id;

    @Column(name = "POLICY_TYPE")
    @Enumerated(EnumType.STRING)
    PolicyType policyType;

    @Column(name = "PARAMETERS", length = Integer.MAX_VALUE)
    @Type(type = "org.hibernate.type.SerializableToBlobType",
            parameters = @Parameter(name = SerializableToBlobType.CLASS_NAME, value = "java.lang.Object"))
    Map<String, String> pricingParameters;
}
