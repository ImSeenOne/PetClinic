/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.vet;

import java.util.Collection;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;
import javax.validation.Valid;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
class VetController {
    
    private static final String VIEWS_VETS_CREATE_OR_UPDATE_FORM = "vets/createOrUpdateVetForm";
    private final VetRepository vets;

    public VetController(VetRepository clinicService) {
        this.vets = clinicService;
    }

    @GetMapping("/vets.html")
    public String showVetList(Map<String, Object> model) {
        // Here we are returning an object of type 'Vets' rather than a collection of Vet
        // objects so it is simpler for Object-Xml mapping
        Vets vets = new Vets();
        vets.getVetList().addAll(this.vets.findAll());
        model.put("vets", vets);
        return "vets/vetList";
    }
    
      @GetMapping("/vets/new")
    public String initCreationForm(Map<String, Object> model) {
        Vet vet = new Vet();
        model.put("vet", vet);
        return VIEWS_VETS_CREATE_OR_UPDATE_FORM ;
    }

    @PostMapping("/vets/new")
    public String processCreationForm(@Valid Vet vet, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_VETS_CREATE_OR_UPDATE_FORM ;
        } else {
            this.vets.save(vet);
            return "redirect:/vets/" + vet.getId();
           
        }
    }
    
    @ModelAttribute("specialties")
    public Collection<Specialty> populatSpecialties(){
                return this.vets.findSpecialtiesTypes();
    }
    
     @GetMapping("/vets/find")
    public String initFindForm(Map<String, Object> model) {
        model.put("vet", new Vet());
        return "vets/findVets";
    }

    
    
    @GetMapping("/vets")
    public String processFindForm(Vet vet, BindingResult result, Map<String, Object> model) {

        if (vet.getLastName() == null) {
            vet.setLastName("");
        }
        Collection<Vet> results = this.vets.findByLastName(vet.getLastName());
        if (results.isEmpty()) {
           
            result.rejectValue("lastName", "notFound", "not found");
            return "vets/findVets";
        } else if (results.size() == 1) {
            
            vet = results.iterator().next();
            return "redirect:/vets/" + vet.getId();
        } else {
           
            model.put("selections", results);
            return "vets/vetsList";
        }
    }

    @GetMapping("/vets/{vetId}/edit")
    public String initUpdateVetForm(@PathVariable("vetId") int ownerId, Model model) {
        Vet owner = this.vets.findById(ownerId);
        model.addAttribute(owner);
        return VIEWS_VETS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/vets/{vetId}/edit")
    public String processUpdateOwnerForm(@Valid Vet vet, BindingResult result, @PathVariable("vetId") int vetId) {
        if (result.hasErrors()) {
            return VIEWS_VETS_CREATE_OR_UPDATE_FORM;
        } else {
            vet.setId(vetId);
            this.vets.save(vet);
            return "redirect:/vets/{vetId}";
        }
    }
    
    
  
    @GetMapping("/vets/{vetId}")
    public ModelAndView showOwner(@PathVariable("vetId") int ownerId) {
        ModelAndView mav = new ModelAndView("vets/vetDetails");
        mav.addObject(this.vets.findById(ownerId));
        return mav;
    }
    
     @GetMapping("/vets/{vetId}/remove")
    public String processDelete(@PathVariable("vetId") int vetId, Model model,Vet vet)  {
            vet=this.vets.findById(vetId);
            this.vets.delete(vet);
            return "redirect:/vets/find";
    }

}
