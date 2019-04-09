package org.springframework.samples.petclinic.clinicservices;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class ClinicServiceController {

    private static final String VIEWS_CLINIC_SERVICE_CREATE_OR_UPDATE_FORM = "clinicServices/createOrUpdateClinicServiceForm";

    private final ClinicServiceRepository clinicServiceRepository;

    @Autowired
    public ClinicServiceController(ClinicServiceRepository clinicServiceRepository) {
        this.clinicServiceRepository = clinicServiceRepository;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping("/services")
    public ModelAndView findAllServices() {
        List<ClinicService> services = this.clinicServiceRepository.findAll();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("clinicServices/showServices");
        mav.addObject("services", services);
        return mav;
    }

    @GetMapping("/services/new")
    public ModelAndView initCreateForm() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(VIEWS_CLINIC_SERVICE_CREATE_OR_UPDATE_FORM);
        ClinicService service = new ClinicService();
        mav.addObject("service", service);
        return mav;
    }

    @PostMapping("/services/new")
    public String processCreateForm(@Valid ClinicService clinicService, BindingResult result) {
        if(result.hasErrors()) {
            return VIEWS_CLINIC_SERVICE_CREATE_OR_UPDATE_FORM;
        } else {
            this.clinicServiceRepository.save(clinicService);
            return "redirect:/services";
        }
    }

    @GetMapping("/services/{serviceId}/edit")
    public ModelAndView initEditForm(@PathVariable("serviceId") int serviceId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(VIEWS_CLINIC_SERVICE_CREATE_OR_UPDATE_FORM);
        ClinicService service = this.clinicServiceRepository.findById(serviceId);

        mav.addObject("service", service);
        return mav;
    }

    @PostMapping("/services/{serviceId}/edit")
    public String processEditForm(@PathVariable("serviceId") int serviceId, @Valid ClinicService clinicService, BindingResult result) {
        if(result.hasErrors()) {
            return VIEWS_CLINIC_SERVICE_CREATE_OR_UPDATE_FORM;
        } else {
            clinicService.setId(serviceId);
            this.clinicServiceRepository.save(clinicService);
            return "redirect:/services";
        }
    }

    @GetMapping("/services/{serviceId}/delete")
    public String deleteForm(@PathVariable("serviceId") int serviceId) {
        ClinicService clinicService = this.clinicServiceRepository.findById(serviceId);
        if(clinicService == null) {
            return "/error";
        } else {
            this.clinicServiceRepository.deleteById(serviceId);
            return "redirect:/services";
        }
    }

}
