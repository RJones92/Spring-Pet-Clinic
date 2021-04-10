package com.example.springpetclinic.controllers;

import com.example.springpetclinic.model.Owner;
import com.example.springpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {
    private static final String VIEW_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

    @Mock
    OwnerService ownerService;
    @InjectMocks
    OwnerController controller;

    Set<Owner> owners;
    Owner owner1;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        owners = new HashSet<>();
        owner1 = Owner.builder().id(1L).lastName("Smith").build();
        owners.add(owner1);
        owners.add(Owner.builder().id(2L).lastName("Jones").build());

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void testFindOwnerForm() throws Exception {
        mockMvc.perform(get("/owners/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"))
                .andExpect(model().attributeExists("owner"));

        verifyNoInteractions(ownerService);
    }

    @Test
    void testProcessFindOwnerForm_NoResults() throws Exception {
        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/owners/"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));

        verify(ownerService, times(1)).findAllByLastNameLike(anyString());
    }

    @Test
    void testProcessFindOwnerForm_OneResult() throws Exception {
        Owner owner1 = Owner.builder().id(1L).build();
        List<Owner> owners = List.of(owner1);

        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(owners);

        mockMvc.perform(get("/owners/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));

        verify(ownerService, times(1)).findAllByLastNameLike(anyString());
    }

    @Test
    void testProcessFindOwnerForm_MultipleResults() throws Exception {
        Owner owner1 = Owner.builder().id(1L).build();
        Owner owner2 = Owner.builder().id(2L).build();
        List<Owner> owners = List.of(owner1, owner2);

        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(owners);

        mockMvc.perform(get("/owners/"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"))
                .andExpect(model().attribute("selections", hasSize(2)));

        verify(ownerService, times(1)).findAllByLastNameLike(anyString());

    }

    @Test
    void testShowOwner() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(owner1);

        mockMvc.perform(get("/owners/123/"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownerDetails"))
                .andExpect(model().attribute("owner", hasProperty("id", is(1L))));
    }

    @Test
    void testCreateOwnerForm() throws Exception {
        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_CREATE_OR_UPDATE_FORM))
                .andExpect(model().attributeExists("owner"));
        verifyNoInteractions(ownerService);
    }

    @Test
    void testProcessCreateOwnerForm() throws Exception {
        Owner returnOwner = Owner.builder().id(1L).build();
        when(ownerService.save(any(Owner.class))).thenReturn(returnOwner);

        mockMvc.perform(post("/owners/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));

        verify(ownerService, times(1)).save(any(Owner.class));
    }

    @Test
    void testUpdateOwnerForm() throws Exception {
        long ownerId = 1L;
        Owner returnOwner = Owner.builder().id(ownerId).build();
        when(ownerService.findById(anyLong())).thenReturn(returnOwner);

        mockMvc.perform(get("/owners/" + ownerId + "/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_CREATE_OR_UPDATE_FORM))
                .andExpect(model().attributeExists("owner"));
        verify(ownerService, times(1)).findById(anyLong());
    }

    @Test
    void testProcessUpdateOwnerForm() throws Exception {
        long ownerId = 1L;
        Owner returnOwner = Owner.builder().id(ownerId).build();
        when(ownerService.save(any(Owner.class))).thenReturn(returnOwner);

        mockMvc.perform(post("/owners/" + ownerId + "/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));

        verify(ownerService, times(1)).save(any(Owner.class));
    }
}