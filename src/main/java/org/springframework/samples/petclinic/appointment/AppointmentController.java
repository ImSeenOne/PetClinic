package org.springframework.samples.petclinic.appointment;

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
public class AppointmentController {

    private static final String VIEWS_APPOINTMENTS_CREATE_OR_UPDATE_FORM = "appointments/createOrUpdateAppointmentForm";

    private final AppointmentRepository appointmentRepository;
    private final PetRepository petRepository;
    private final VetRepository vetRepository;

    @Autowired
    public AppointmentController(AppointmentRepository appointmentRepository, PetRepository petRepository, VetRepository vetRepository) {
        this.appointmentRepository = appointmentRepository;
        this.petRepository = petRepository;
        this.vetRepository = vetRepository;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("specialties")
    public Collection<Specialty> populatSpecialties(){
        return this.vetRepository.findSpecialtiesTypes();
    }

    @GetMapping("pets/{petId}/appointments/new")
    public String initCreationForm(Map<String, Object> model, @PathVariable("petId") int petId) {
        Pet pet = this.petRepository.findById(petId);

        if(pet == null) {
            return this.VIEWS_APPOINTMENTS_CREATE_OR_UPDATE_FORM;
        }

        Appointment appointment = new Appointment();

        appointment.setPetId(petId);
        model.put("appointment", appointment);
        model.put("pet", pet);
        model.put("owner", pet.getOwner());
        return VIEWS_APPOINTMENTS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("pets/{petId}/appointments/new")
    public String processCreationForm(@Valid Appointment appointment, BindingResult result) {
        if(result.hasErrors()) {
            return VIEWS_APPOINTMENTS_CREATE_OR_UPDATE_FORM;
        } else {
            this.appointmentRepository.save(appointment);
            Owner owner = this.petRepository.findById(appointment.getPetId()).getOwner();
            return "redirect:/owners/"+owner.getId();
        }
    }

    @GetMapping("pets/{petId}/appointments/{appointmentId}/edit")
    public String initEditForm(Map<String, Object> model, @PathVariable("petId") int petId, @PathVariable("appointmentId") int appointmentId) {

        Appointment appointment = this.appointmentRepository.findById(appointmentId);
        Pet pet = this.petRepository.findById(appointment.getPetId());

        model.put("appointment", appointment);
        model.put("owner", pet.getOwner());
        model.put("pet", pet);

        return VIEWS_APPOINTMENTS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("pets/{petId}/appointments/{appointmentId}/edit")
    public String processEditForm(@Valid Appointment appointment, BindingResult result, @PathVariable("appointmentId") int appointmentId, @PathVariable("petId") int petId) {
        if(result.hasErrors()) {
            return VIEWS_APPOINTMENTS_CREATE_OR_UPDATE_FORM;
        } else {
            appointment.setId(appointmentId);
            appointment.setPetId(petId);
            this.appointmentRepository.save(appointment);
            return "redirect:/appointments/" + appointment.getId();
        }
    }

    @GetMapping("/appointments")
    public ModelAndView showAppointments() {
        ModelAndView mav = new ModelAndView("appointments/showAppointments");
        List<Appointment> appointments = this.appointmentRepository.findAll();
        List<Owner> owners = new LinkedList<>();
        for (Appointment appointment : appointments) {
            Pet pet = this.petRepository.findById(appointment.getPetId());
            if(!owners.contains( pet.getOwner() ) ) {
                owners.add(pet.getOwner());
            }
        }

        mav.addObject("appointments", appointments);
        mav.addObject("owners", owners);

        return mav;
    }

    @GetMapping("/appointments/{appointmentId}")
    public ModelAndView showAppointment(@PathVariable("appointmentId") int appointmentId) {
        ModelAndView mav = new ModelAndView("appointments/appointmentDetails");

        Pet pet = this.petRepository.findById(this.appointmentRepository.findById(appointmentId).getPetId());
        Owner owner = pet.getOwner();

        mav.addObject("appointment",this.appointmentRepository.findById(appointmentId));
        mav.addObject("owner",owner);
        mav.addObject("pet", pet);
        return mav;
    }
}
