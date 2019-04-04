package org.springframework.samples.petclinic.appointment;

import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.vet.Specialty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {

    @Column(name = "pet_id")
    private Integer petId;

    @Column(name = "date")
    private String date;

    @Column(name = "matter")
    private String matter;

    @Column(name="specialty_id")
    @NotEmpty
    private String specialty_id;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "vet_specialties", joinColumns = @JoinColumn(name = "vet_id"), inverseJoinColumns = @JoinColumn(name = "specialty_id"))
    private Set<Specialty> specialties;

    public String getSpecialty_id() {
        return specialty_id;
    }

    public void setSpecialty_id(String specialty_id) {
        this.specialty_id = specialty_id;
    }

    protected Set<Specialty> getSpecialtiesInternal() {
        if (this.specialties == null) {
            this.specialties = new HashSet<>();
        }
        return this.specialties;
    }

    protected void setSpecialtiesInternal(Set<Specialty> specialties) {
        this.specialties = specialties;
    }

    public void setSpecialties(List<Specialty> specialties){
        addSpecialty(specialties.get(0));
    }

    public int getNrOfSpecialties() {
        return getSpecialtiesInternal().size();
    }

    public void addSpecialty(Specialty specialty) {
        getSpecialtiesInternal().add(specialty);
    }

    public int getPetId() {
        return petId;
    }

    public Appointment setPetId(Integer petId) {
        this.petId = petId;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Appointment setDate(String date) {
        this.date = date;
        return this;
    }

    public String getMatter() {
        return matter;
    }

    public Appointment setMatter(String matter) {
        this.matter = matter;
        return this;
    }
}
