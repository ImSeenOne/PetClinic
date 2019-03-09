package org.springframework.samples.petclinic.medicine;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
public interface MedicineRepository extends Repository<Medicine, Integer>{
    
    
    @Query("SELECT DISTINCT medicine FROM Medicine medicine WHERE medicine.name LIKE :name%")
    @Transactional(readOnly = true)
    Collection<Medicine> findByName(@Param("name") String name);
    
    
    
    @Query("SELECT medicine FROM Medicine medicine WHERE medicine.id =:id")
    @Transactional(readOnly = true)
    Medicine findById(@Param("id") Integer id);
    

    /**
     *
     * @param medicine the {@link Medicine} to delete
     */
    
    void delete(Medicine medicine);
    
    
    
    /**
     *
     * @param medicine the {@link Medicine} to save
     */
    void save(Medicine medicine);
    
    
    
}
