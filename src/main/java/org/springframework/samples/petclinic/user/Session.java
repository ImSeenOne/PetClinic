package org.springframework.samples.petclinic.user;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.format.datetime.DateTimeFormatAnnotationFormatterFactory;
import org.springframework.samples.petclinic.model.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "sessions")
public class Session extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date dateTime;
}
