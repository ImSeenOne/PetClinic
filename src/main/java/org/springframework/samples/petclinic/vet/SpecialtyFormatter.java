package org.springframework.samples.petclinic.vet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PC1
 */


import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
/**
 *
 * @author 
 */
@Component
public class SpecialtyFormatter implements Formatter<Specialty>{
    
    private final VetRepository vets;


    @Autowired
    public SpecialtyFormatter(VetRepository vets) {
        this.vets = vets;
    }

    @Override
    public String print(Specialty specialties, Locale locale) {
        return specialties.getName();
    }

    @Override
    public Specialty parse(String text, Locale locale) throws ParseException {
        Collection<Specialty> findSpecialtiesTypes = this.vets.findSpecialtiesTypes();
        for (Specialty type : findSpecialtiesTypes) {
            if (type.getName().equals(text)) {
                return type;
            }
        }
        throw new ParseException("type not found: " + text, 0);
    }

}