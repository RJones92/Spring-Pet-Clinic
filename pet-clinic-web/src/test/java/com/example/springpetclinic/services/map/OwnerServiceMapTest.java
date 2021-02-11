package com.example.springpetclinic.services.map;

import com.example.springpetclinic.model.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class OwnerServiceMapTest {

    OwnerServiceMap ownerServiceMap;
    final Long ownerId_One = 1L;
    final String ownerLastName_One = "Smith";

    @BeforeEach
    void setUp() {
        ownerServiceMap = new OwnerServiceMap(new PetTypeServiceMap(), new PetServiceMap());

        ownerServiceMap.save(Owner.builder()
                .id(ownerId_One)
                .lastName(ownerLastName_One)
                .build());
    }

    @Test
    void findByLastName() {
        Owner owner = ownerServiceMap.findByLastName(ownerLastName_One);

        assertNotNull(owner);
        assertEquals(ownerId_One, owner.getId());
    }

    @Test
    void findByLastNameNotFound() {
        Owner owner = ownerServiceMap.findByLastName("foo");

        assertNull(owner);
    }

    @Test
    void findAll() {
        Set<Owner> owners = ownerServiceMap.findAll();

        assertEquals(1, owners.size());
    }

    @Test
    void findById() {
        Owner owner = ownerServiceMap.findById(ownerId_One);

        assertEquals(ownerId_One, owner.getId());
    }

    @Test
    void saveOwnerWithId() {
        Long ownerId_two = 2L;
        Owner owner2 = Owner.builder().id(ownerId_two).build();

        Owner savedOwner = ownerServiceMap.save(owner2);

        assertEquals(ownerId_two, savedOwner.getId());
    }

    @Test
    void saveOwnerWithoutId() {
        Owner savedOwner = ownerServiceMap.save(Owner.builder().build());

        assertNotNull(savedOwner);
        assertNotNull(savedOwner.getId());
    }

    @Test
    void delete() {
        ownerServiceMap.delete(ownerServiceMap.findById(ownerId_One));

        assertEquals(0, ownerServiceMap.findAll().size());
    }

    @Test
    void deleteById() {
        ownerServiceMap.deleteById(ownerId_One);

        assertEquals(0, ownerServiceMap.findAll().size());
    }
}