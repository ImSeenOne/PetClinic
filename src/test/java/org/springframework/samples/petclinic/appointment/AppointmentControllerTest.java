package org.springframework.samples.petclinic.appointment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.owner.*;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {

    private static final int TEST_APPOINTMENT_ID = 1;
    private static final int TEST_APPOINTMENT_PET_ID = 1;
    private static final String TEST_APPOINTMENT_SPECIALTY_ID = "surgery";

    private static final int TEST_OWNER_ID = 1;
    private static final int TEST_PET_ID = 1;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AppointmentRepository appointmentRepository;

    @MockBean
    private PetRepository petRepository;

    @MockBean
    private VetRepository vetRepository;

    private Appointment appointment;

    private Owner owner;

    private Pet pet;

    @Before
    public void setup() {
        appointment = new Appointment();
        appointment.setId(TEST_APPOINTMENT_ID);
        appointment.setMatter("Gripa de chucho");
        appointment.setDate("2019-06-28 08:30");
        appointment.setPetId(TEST_APPOINTMENT_PET_ID);
        appointment.setSpecialty_id(TEST_APPOINTMENT_SPECIALTY_ID);
        given(this.appointmentRepository.findById(TEST_APPOINTMENT_ID)).willReturn(appointment);//

        PetType petTypeCat = new PetType();
        petTypeCat.setId(1);
        petTypeCat.setName("cat");

        pet = new Pet();
        pet.setId(TEST_PET_ID);
        pet.setName("Clodimira");
        pet.setType(petTypeCat);
        given(this.petRepository.findById(TEST_PET_ID)).willReturn(pet);

        owner = new Owner();
        owner.setFirstName("Cristian");
        owner.setLastName("Ramirez Fonseca");
        owner.setId(TEST_OWNER_ID);
        owner.setTelephone("9611750541");
        owner.setAddress("Calle Olmo Sur #513 Col. Patria nueva");
        owner.setCity("TUXTLA GUTIÉRREZ");
        owner.setZipCode("29045");
        owner.addPet(pet);
    }

    @WithMockUser(value = "user")
    @Test
    public void testInitCreationFormTest() throws Exception {
        mockMvc.perform(get("/pets/{petId}/appointments/new",TEST_APPOINTMENT_PET_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("appointment", "owner", "pet"))
            .andExpect(view().name("appointments/createOrUpdateAppointmentForm"));
    }



    @WithMockUser(value = "user")
    @Test
    public void initEditFormTest() throws Exception {
        mockMvc.perform(get(
            "/pets/{petId}/appointments/{appointmentId}/edit",
            TEST_PET_ID,TEST_APPOINTMENT_ID))

            .andExpect(model().attributeExists("appointment"))
            .andExpect(model().attribute("appointment", hasProperty("date", is("2019-06-28 08:30"))))
            .andExpect(model().attribute("appointment", hasProperty("matter", is("Gripa de chucho"))))
            .andExpect(model().attribute("appointment", hasProperty("specialty_id", is("surgery"))))
            .andExpect(view().name("appointments/createOrUpdateAppointmentForm"))
            .andExpect(status().isOk());
    }

    @WithMockUser(value = "user")
    @Test
    public void testProcessEditForm() throws Exception {
        mockMvc.perform(post(
            "/pets/{petId}/appointments/{appointmentId}/edit",
            TEST_PET_ID, TEST_APPOINTMENT_ID)
            .param("date", "2019-06-28 08:30")
            .param("matter", "Infección de chucho")
            .param("specialty_id", "radiology")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/appointments/"+TEST_APPOINTMENT_ID));
    }

    @WithMockUser(value = "user")
    @Test
    public void testShowAppointment() throws Exception {
        mockMvc.perform(get(
            "/appointments/{appointmentId}"
            ,TEST_APPOINTMENT_ID))
            .andExpect(status().isOk())
            .andExpect(model().attribute("date","2019-06-28 08:30"))
            .andExpect(model().attribute("matter", "Gripa de chucho"))
            .andExpect(model().attribute("specialty_id", "surgery"))
            .andExpect(view().name("appointments/appointmentDetails"));
    }
}
