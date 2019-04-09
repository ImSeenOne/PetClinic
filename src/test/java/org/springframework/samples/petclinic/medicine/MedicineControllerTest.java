package org.springframework.samples.petclinic.medicine;


import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.samples.petclinic.medicine.Medicine;
import org.springframework.samples.petclinic.medicine.MedicineController;
import org.springframework.samples.petclinic.medicine.MedicineRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 *
 * @author USER
 */


@RunWith(SpringRunner.class)
@WebMvcTest(MedicineController.class)

public class MedicineControllerTest {
    
    private static final int TEST_MEDICINE_ID = 2;
    
  
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicineRepository medicines;

    private Medicine canidryl;
    
     @Before
    public void setup() {
        canidryl= new Medicine();
        canidryl.setId(TEST_MEDICINE_ID);
        canidryl.setName("Aclomast");
        canidryl.setActive_ingredient("Acolan");
        canidryl.setPresentation("Tabletas");
        given(this.medicines.findById(TEST_MEDICINE_ID)).willReturn(canidryl);
    }
    @WithMockUser(value = "user")
     @Test
    public void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/medicines/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("medicine"))
            .andExpect(view().name("medicines/createOrUpdateMedicineForm"));
    }
    
    @WithMockUser(value="user")
     @Test
    public void testProcessCreationFormSuccess() throws Exception {
        mockMvc.perform(post("/medicines/new")
            .param("name", "Aspirina")
            .param("active_ingredient", "Acido acetilsalicilico")
            .param("presentation", "Oral 500mg")
        )
            .andExpect(status().is3xxRedirection());
    }
    
    ///ver si aqui no hay error por esto
    @WithMockUser(value="user")
    @Test
    public void testProcessCreationFormHasErrors() throws Exception {
        mockMvc.perform(post("/medicines/new")
            .param("name", "Aspirina")
            .param("active_ingredient", "Acido acetilsalicilico")
            //.param("presentation", "Oral 500mg")
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("medicine"))
            //.andExpect(model().attributeHasFieldErrors("medicine", "active_ingredient"))
            .andExpect(model().attributeHasFieldErrors("medicine", "presentation"))
            .andExpect(view().name("medicines/createOrUpdateMedicineForm"));
    }
    
    ///000001*
    @WithMockUser(value="user")
     @Test
    public void testInitFindForm() throws Exception {
        mockMvc.perform(get("/medicines/find"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("medicine"))
            .andExpect(view().name("medicines/findMedicines"));
    }

    @WithMockUser(value="user")
    @Test
    public void testProcessFindFormSuccess() throws Exception {
        given(this.medicines.findByName("")).willReturn(Lists.newArrayList(canidryl, new Medicine()));
        mockMvc.perform(get("/medicines"))
            .andExpect(status().isOk())
            .andExpect(view().name("medicines/medicinesList"));
    }
    
    ///000001*
    @WithMockUser(value="user")
    @Test
    public void testProcessFindFormByName() throws Exception {
        given(this.medicines.findByName(canidryl.getName())).willReturn(Lists.newArrayList(canidryl));
        mockMvc.perform(get("/medicines")
            .param("name", "CANIDRYL")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/medicines/" + TEST_MEDICINE_ID));
    }

    @WithMockUser(value="user")
     @Test
    public void testProcessFindFormNoMedicinesFound() throws Exception {
        mockMvc.perform(get("/medicines")
            .param("name", "Unknown Surname")
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("medicine", "Name"))
            .andExpect(model().attributeHasFieldErrorCode("medicine", "Name", "notFound"))
            .andExpect(view().name("medicines/findMedicines"));
    }

    @WithMockUser(value="user")
    @Test
    public void testInitUpdateMedicineForm() throws Exception {
        mockMvc.perform(get("/medicines/{medicineId}/edit", TEST_MEDICINE_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("medicine"))
            .andExpect(model().attribute("medicine", hasProperty("name", is("CANIDRYL"))))
            .andExpect(model().attribute("medicine", hasProperty("active_ingredient", is("Carprofeno"))))
            .andExpect(model().attribute("medicine", hasProperty("presentation", is("Comprimido Oral 500mg."))))
            .andExpect(view().name("medicines/createOrUpdateMedicineForm"));
    }
    ///00001*
    @WithMockUser(value="user")
    @Test
    public void testProcessUpdateMedicineFormSuccess() throws Exception {
        mockMvc.perform(post("/medicines/{medicineId}/edit", TEST_MEDICINE_ID)
            .param("name", "Aspirina")
            .param("active_ingredient", "Acido acetilsalicilico")
            .param("presentation", "Oral 500mg")         
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/medicines/{medicineId}"));
    }
    @WithMockUser(value="user")
     @Test
    public void testProcessUpdateMedicineFormHasErrors() throws Exception {
        mockMvc.perform(post("/medicines/{medicineId}/edit", TEST_MEDICINE_ID)
            .param("name", "Aspirina ")
            .param("active_ingredient", "Acido acetilsalicilico")
            //.param("presentation", "Oral 500mg.")
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("medicine"))
            //.andExpect(model().attributeHasFieldErrors("medicine", "active_ingredient"))
            .andExpect(model().attributeHasFieldErrors("medicine", "presentation"))
            .andExpect(view().name("medicines/createOrUpdateMedicineForm"));
    }




    @WithMockUser(value="user")
    @Test
    public void testShowMedicine() throws Exception {
        mockMvc.perform(get("/medicines/{medicineId}", TEST_MEDICINE_ID))
            .andExpect(status().isOk())
            .andExpect(model().attribute("medicine", hasProperty("name", is("CANIDRYL"))))
            .andExpect(model().attribute("medicine", hasProperty("active_ingredient", is("Carprofeno"))))
            .andExpect(model().attribute("medicine", hasProperty("presentation", is("Comprimido Oral 500mg."))))
            
            .andExpect(view().name("medicines/medicineDetails"));
    }
    
    
}
