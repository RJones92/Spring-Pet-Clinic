package com.example.springpetclinic.services.springdatajpa;

import com.example.springpetclinic.model.Owner;
import com.example.springpetclinic.repositories.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OwnerServiceSDJpaTest {

    @Mock
    OwnerRepository ownerRepository;
    @InjectMocks
    OwnerServiceSDJpa service;

    Owner returnOwner;

    public static final String LAST_NAME = "Smith";

    @BeforeEach
    void setUp() {
        returnOwner = Owner.builder().id(1L).lastName(LAST_NAME).build();
    }

    @Test
    void findByLastName() {
        when(ownerRepository.findByLastName(any())).thenReturn(returnOwner);

        Owner foundOwner = service.findByLastName(LAST_NAME);

        assertThat(foundOwner.getLastName(), is(LAST_NAME));
        verify(ownerRepository, times(1)).findByLastName(any());
    }

    @Test
    void findAll() {
        Set<Owner> returnOwners = new HashSet<>();
        returnOwners.add(returnOwner);
        returnOwners.add(Owner.builder().id(2L).lastName("Jones").build());

        when(ownerRepository.findAll()).thenReturn(returnOwners);

        Set<Owner> foundOwners = service.findAll();

        assertThat(foundOwners, hasSize(2));
    }

    @Test
    void findById() {
        when(ownerRepository.findById(1L)).thenReturn(Optional.of(returnOwner));

        Owner foundOwner = service.findById(1L);

        assertThat(foundOwner.getLastName(), is(LAST_NAME));
    }

    @Test
    void findByIdThatIsNotFound() {
        when(ownerRepository.findById(anyLong())).thenReturn(Optional.empty());

        Owner foundOwner = service.findById(1L);

        assertNull(foundOwner);
    }

    @Test
    void save() {
        Owner ownerToSave = Owner.builder().id(2L).lastName("SaveMe").build();

        when(ownerRepository.save(any())).thenReturn(ownerToSave);

        Owner savedOwner = service.save(ownerToSave);

        assertThat(savedOwner.getLastName(), is("SaveMe"));
        verify(ownerRepository, times(1)).save(any());
    }

    @Test
    void delete() {
        service.delete(returnOwner);

        verify(ownerRepository, times(1)).delete(any());
    }

    @Test
    void deleteById() {
        service.deleteById(returnOwner.getId());

        verify(ownerRepository, times(1)).deleteById(anyLong());
    }
}