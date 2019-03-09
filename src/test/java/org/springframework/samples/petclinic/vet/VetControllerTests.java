package org.springframework.samples.petclinic.vet;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Test class for the {@link VetController}
 */
@RunWith(SpringRunner.class)
@WebMvcTest(VetController.class)
public class VetControllerTests {
     private static final int TEST_VET_ID = 1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VetRepository vets;
    
     private Vet vet;
      
    

    @Before
    public void setup() { 
        vet = new Vet();
        vet.setId(TEST_VET_ID);
        vet.setFirstName( "Helen");
        vet.setLastName("Leary");
        vet.setTelephone("86229373");
        vet.setWorkdays("lunes,viernes");
        vet.setBusiness_hours("08:00-12:00");
        vet.setSpecialty_id("radiology");
        given(this.vets.findById(TEST_VET_ID)).willReturn(vet);
    }
    
      @Test
    public void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/vets/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("vet"))
            .andExpect(view().name("vets/createOrUpdateVetsForm"));
    }

    @Test
    public void testProcessCreationFormSuccess() throws Exception {
        mockMvc.perform(post("/vets/new")
            .param("firstName", "Jame")
            .param("lastName", "Carter")
            .param("telephone", "86229373")
            .param("workdays", "lunes,viernes")
            .param("business_hours", "08:00-12:00")
            .param("specialty_id", "surgery")
        )
            .andExpect(status().is3xxRedirection());
    }
    


    @Test
    public void testProcessCreationFormHasErrors() throws Exception {
        mockMvc.perform(post("/vets/new")
            .param("firstName", "Jame")
            .param("lastName", "Carter")
            
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("vet"))
            .andExpect(model().attributeHasFieldErrors("vet","telephone"))
            .andExpect(model().attributeHasFieldErrors("vet", "workdays"))
            .andExpect(model().attributeHasFieldErrors("vet", "business_hours"))
            .andExpect(view().name("vets/createOrUpdateVetForm"));
    }
    
    @Test
    public void testInitFindForm() throws Exception {
        mockMvc.perform(get("/vets/find"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("vet"))
            .andExpect(view().name("vets/findVets"));
    }
    
     @Test
    public void testProcessFindFormSuccess() throws Exception {
        given(this.vets.findByLastName("")).willReturn(Lists.newArrayList(vet, new Vet()));
        mockMvc.perform(get("/vets"))
            .andExpect(status().isOk())
            .andExpect(view().name("vets/vetsList"));
    }

    @Test
    public void testProcessFindFormByLastName() throws Exception {
        given(this.vets.findByLastName(vet.getLastName())).willReturn(Lists.newArrayList(vet));
        mockMvc.perform(get("/vets")
            .param("lastName", "Carter")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/vets/" + TEST_VET_ID));
    }
    
     @Test
    public void testProcessFindFormNoOwnersFound() throws Exception {
        mockMvc.perform(get("/vets")
            .param("lastName", "Unknown Surname")
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("vet", "lastName"))
            .andExpect(model().attributeHasFieldErrorCode("vet", "lastName", "notFound"))
            .andExpect(view().name("vets/findVets"));
    }
    
    @Test
    public void testInitUpdateOwnerForm() throws Exception {
        mockMvc.perform(get("/vets/{vetId}/edit", TEST_VET_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("vet"))
            .andExpect(model().attribute("vet", hasProperty("lastName", is("Leary"))))
            .andExpect(model().attribute("vet", hasProperty("firstName", is("Helen"))))
            .andExpect(model().attribute("vet", hasProperty("telephone", is("86229373"))))
            .andExpect(model().attribute("vet", hasProperty("workdays", is("lunes,viernes"))))
            .andExpect(model().attribute("vet", hasProperty("business_hours", is("08:00-12:00"))))
            .andExpect(model().attribute("vet", hasProperty("specialty_id", is("radiology"))))
            .andExpect(view().name("vets/createOrUpdateVetForm"));
    }
    
    @Test
    public void testProcessUpdateOwnerFormSuccess() throws Exception {
        mockMvc.perform(post("/vets/{vetId}/edit", TEST_VET_ID)
            .param("firstName", "Jame")
            .param("lastName", "Carter")
            .param("telephone", "86229373")
            .param("workdays", "lunes,viernes")
            .param("business_hours", "08:00-12:00")
            .param("specialty_id", "surgery")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/vets/{vetId}"));
    }
    
    @Test
    public void testProcessUpdateOwnerFormHasErrors() throws Exception {
        mockMvc.perform(post("/vets/{vetId}/edit", TEST_VET_ID)
            .param("firstName", "Joe")
            .param("lastName", "Bloggs")
            .param("city", "London")
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("owner"))
            .andExpect(model().attributeHasFieldErrors("owner", "address"))
            .andExpect(model().attributeHasFieldErrors("owner", "telephone"))
            .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }
    
     @Test
    public void testShowOwner() throws Exception {
        mockMvc.perform(get("/vets/{vetId}", TEST_VET_ID))
            .andExpect(status().isOk())
            .andExpect(model().attribute("vet", hasProperty("lastName", is("Leary"))))
            .andExpect(model().attribute("vet", hasProperty("firstName", is("Helen"))))
            .andExpect(model().attribute("vet", hasProperty("telephone", is("86229373"))))
            .andExpect(model().attribute("vet", hasProperty("workdays", is("lunes,viernes"))))
            .andExpect(model().attribute("vet", hasProperty("business_hours", is("08:00-12:00"))))
            .andExpect(model().attribute("vet", hasProperty("specialty_id", is("radiology"))))
            .andExpect(view().name("vets/vetDetails"));
    }
}
