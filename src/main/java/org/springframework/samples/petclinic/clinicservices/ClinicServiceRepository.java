package org.springframework.samples.petclinic.clinicservices;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClinicServiceRepository extends Repository<ClinicService, Integer> {

    @Query("SELECT description FROM ClinicService clinicService ORDER BY clinicService.description")
    @Transactional(readOnly = true)
    List<String> findClinicServices();

    @Transactional(readOnly = true)
    ClinicService findById(Integer id);

    @Transactional(readOnly = true)
    List<ClinicService> findAll();

    void save(ClinicService clinicService);

    void deleteById(Integer id);
}
