package org.springframework.samples.petclinic.medicine;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author USER
 */

@Controller
 class MedicineController {
    private static final String VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM ="medicines/createOrUpdateMedicineForm";
    private final MedicineRepository medicines;
    


    public MedicineController(MedicineRepository clinicService) {
        this.medicines = clinicService;
    }
    
     @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping("/medicines/new")
    public String initCreationForm(Map<String, Object> model) {
        Medicine medicine = new Medicine();
        model.put("medicine", medicine);
        return VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM;
    }
    
    @PostMapping("/medicines/new")
    public String processCreationForm(@Valid Medicine medicine, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM;
        } else {
            this.medicines.save(medicine);
            return "redirect:/medicines/" + medicine.getId();
        }
    }

    @GetMapping("/medicines/find")
    public String initFindForm(Map<String, Object> model) {
        model.put("medicine", new Medicine());
        return "medicines/findMedicines";
    }
    
    @GetMapping("/medicines")
    public String processFindForm(Medicine medicine, BindingResult result, Map<String, Object> model) {
  
        if (medicine.getName() == null) {
            medicine.setName(""); 
        }
       
        Collection<Medicine> results = this.medicines.findByName(medicine.getName());
        if (results.isEmpty()) {  
          result.rejectValue("Name", "notFound", "not found");
            return "medicines/findMedicines";
        } else if (results.size() == 1) {
            medicine = results.iterator().next();
            return "redirect:/medicines/" + medicine.getId();
        } else {
            
            model.put("selections", results);
            return "medicines/medicinesList";
        }
    }
    
    @GetMapping("/medicines/{medicineId}/edit")
    public String initUpdateMedicineForm(@PathVariable("medicineId") int medicineId, Model model) {
        Medicine medicine = this.medicines.findById(medicineId);
        model.addAttribute(medicine);
        return VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/medicines/{medicineId}/edit")
    public String processUpdateMedicineForm(@Valid Medicine medicine, BindingResult result, @PathVariable("medicineId") int medicineId) {
        if (result.hasErrors()) {
            return VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM;
        } else {
            medicine.setId(medicineId);
            this.medicines.save(medicine);
            return "redirect:/medicines/{medicineId}";
        }
    }
    
   
  @GetMapping("/medicines/{medicineId}/remove")
    public String processDelete(@PathVariable("medicineId") int medicineId, Model model,Medicine medicine)  {
            medicine=this.medicines.findById(medicineId);
            this.medicines.delete(medicine);
            return "redirect:/medicines/find";
    }

    @GetMapping("/medicines/{medicineId}")
    public ModelAndView showMedicine(@PathVariable("medicineId") int medicineId) {
        ModelAndView mav = new ModelAndView("medicines/medicineDetails");
        mav.addObject(this.medicines.findById(medicineId));
        return mav;
    }
     
     
     
}
