package org.springframework.samples.petclinic.treatment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class TreatmentController {

    private static final String VIEWS_TREATMENTS_CREATE_OR_UPDATE_FORM = "treatments/createOrUpdateTreatmentsForm";

    private final TreatmentRepository treatmentRepository;

    @Autowired
    public TreatmentController(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    //AQUÍ SE ENCUENTRA TODOS LOS TRATAMIENTOS
    @GetMapping("/treatments")
    public ModelAndView findAllTreatments() {
        List<Treatment> treatments = this.treatmentRepository.findAll();  //AQUÍ LOS BUSCA EN LA BASE DE DATOS
        ModelAndView mav = new ModelAndView();
        mav.setViewName("treatments/showTreatments");
        mav.addObject("treatments", treatments);
        return mav;
    }

    //AQUÍ SE RETORNA LA VISTA PARA CREAR UN NUEVO TRATAMIENTO
    @GetMapping("/treatments/new")
    public ModelAndView initCreateForm() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(VIEWS_TREATMENTS_CREATE_OR_UPDATE_FORM);
        Treatment treatment = new Treatment();
        mav.addObject("treatment", treatment); //ACÁ SE AÑADE EL OBJETO A LA VISTA
        return mav;
    }

    //AQUÍ SE RECIBE LA VISTA CON EL OBJETO YA LLENO DE PARTE DEL USUARIO
    @PostMapping("/treatments/new")
    public String processCreateForm(@Valid Treatment treatment, BindingResult result) {
        if(result.hasErrors()) { //AQUÍ CHECA QUE NO HAYA ERRORES EN EL LLENADO
            return VIEWS_TREATMENTS_CREATE_OR_UPDATE_FORM;
        } else {
            this.treatmentRepository.save(treatment); //AQUÍ GUARDA EL TRATAMIENTO
            return "redirect:/treatments";
        }
    }

    //AQUÍ SE RETORNA LA VISTA PARA EDITAR UN USUARIO EXISTENTE, SE RECIBE EL ID PARA
    //PODER BUSCARLO EN LA bbdd
    @GetMapping("/treatments/{treatmentId}/edit")
    public ModelAndView initEditForm(@PathVariable("treatmentId") int treatmentId) { //AQUÍ ES DONDE SE RECIBE LA VARIABLE
        ModelAndView mav = new ModelAndView();
        mav.setViewName(VIEWS_TREATMENTS_CREATE_OR_UPDATE_FORM);
        Treatment treatment = this.treatmentRepository.findById(treatmentId);//AQUÍ ES DONDE SE BUSCA EL OBJETO

        mav.addObject("treatment", treatment);
        return mav; //SE RETORNA LA VISTA Y EL MODELO
    }

    //AQUÍ SE GUARDA EL OBJETO ACTUALIZADO
    @PostMapping("/treatments/{treatmentId}/edit")
    public String processEditForm(@PathVariable("treatmentId") int treatmentId, @Valid Treatment treatment, BindingResult result) {
        if(result.hasErrors()) { //SI TIENE ERRORES SE RETORNA LA VISTA CON ERROR
            return VIEWS_TREATMENTS_CREATE_OR_UPDATE_FORM;
        } else {
            treatment.setId(treatmentId);
            this.treatmentRepository.save(treatment); //AQUÍ SE GUARDA EL OBJETO ACTUALZADO
            return "redirect:/treatments";
        }
    }

    //AQUÍ SE ELIMINA EL OBJETO
    @GetMapping("/treatments/{treatmentId}/delete")
    public String deleteForm(@PathVariable("treatmentId") int treatmentId) {
        Treatment treatment = this.treatmentRepository.findById(treatmentId);
        if(treatment == null) { //CHECA SI NO ENCONTRÓ UN OBJETO CON ESE ID
            return "/error";    //SI LO ENCONTRÓ LO MANDA A LA VISTA DE ERROR
        } else {
            this.treatmentRepository.delete(treatment);//SE ELIMINA
            return "redirect:/treatments";
        }
    }

}
