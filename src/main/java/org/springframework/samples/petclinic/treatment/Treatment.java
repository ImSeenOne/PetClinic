package org.springframework.samples.petclinic.treatment;

import org.springframework.samples.petclinic.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "treatments")
public class Treatment extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "instructions")
    private String instructions;

    @Column(name = "medicines")
    private String medicines;

    public String getName() {
        return name;
    }

    public Treatment setName(String name) {
        this.name = name;
        return this;
    }

    public String getInstructions() {
        return instructions;
    }

    public Treatment setInstructions(String instructions) {
        this.instructions = instructions;
        return this;
    }

    public String getMedicines() {
        return medicines;
    }

    public Treatment setMedicines(String medicines) {
        this.medicines = medicines;
        return this;
    }
}
