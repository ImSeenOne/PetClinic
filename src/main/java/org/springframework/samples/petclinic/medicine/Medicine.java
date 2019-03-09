
package org.springframework.samples.petclinic.medicine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.core.style.ToStringCreator;

import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.NamedEntity;
/**
 *
 * @author USER
 */

@Entity
@Table(name = "medicines")
public class Medicine extends BaseEntity{
    
    
    @Column(name = "name")
    @NotEmpty
    private String name;
    
    @Column(name = "active_ingredient")
    @NotEmpty
    private String active_ingredient;
    
    @Column(name = "presentation")
    @NotEmpty
    private String presentation;
  
    
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getActive_ingredient(){
        return this.active_ingredient;
    }
    
    public void setActive_ingredient(String active_ingredient){
        this.active_ingredient = active_ingredient;
    }
    
    public String getPresentation(){
        return this.presentation;
    }
    
    public void setPresentation(String presentation){
        this.presentation = presentation;
    }
    
    
    @Override
    public String toString() {
        return new ToStringCreator(this)

                .append("id", this.getId()).append("new", this.isNew())
                .append("name", this.getName())
                .append("active_ingredient", this.getActive_ingredient())
                .append("presentation", this.getPresentation()).toString();
                
    }
    
   
}
