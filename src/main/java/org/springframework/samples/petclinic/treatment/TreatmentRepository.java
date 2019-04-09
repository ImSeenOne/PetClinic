package org.springframework.samples.petclinic.treatment;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TreatmentRepository extends Repository<Treatment, Integer> {

    @Transactional(readOnly = true)
    Treatment findById(Integer id);

    @Transactional(readOnly = true)
    List<Treatment> findAll();

    void save(Treatment treatment);

    void delete(Treatment treatment);
}
