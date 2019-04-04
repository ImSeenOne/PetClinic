package org.springframework.samples.petclinic.appointment;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AppointmentRepository extends Repository<Appointment, Integer> {

    @Query("SELECT matter FROM Appointment appointment ORDER BY appointment.matter")
    @Transactional(readOnly = true)
    List<String> findAppointmentMatters();

    @Transactional(readOnly = true)
    Appointment findById(Integer id);

    @Transactional(readOnly = true)
    List<Appointment> findAll();

    void save(Appointment appointment);
}
