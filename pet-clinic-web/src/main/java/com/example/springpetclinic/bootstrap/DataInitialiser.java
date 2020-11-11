package com.example.springpetclinic.bootstrap;

import com.example.springpetclinic.model.Owner;
import com.example.springpetclinic.model.Pet;
import com.example.springpetclinic.model.PetType;
import com.example.springpetclinic.model.Vet;
import com.example.springpetclinic.services.OwnerService;
import com.example.springpetclinic.services.PetTypeService;
import com.example.springpetclinic.services.VetService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitialiser implements CommandLineRunner {

    private final OwnerService ownerService;
    private final VetService vetService;
    private final PetTypeService petTypeService;

    public DataInitialiser(OwnerService ownerService, VetService vetService, PetTypeService petTypeService) {
        this.ownerService = ownerService;
        this.vetService = vetService;
        this.petTypeService = petTypeService;
    }

    @Override
    public void run(String... args) throws Exception {

        //------- Pet Types --------
        PetType dog = new PetType();
        dog.setName("Dog");
        PetType savedDogPetType = petTypeService.save(dog);

        PetType cat = new PetType();
        dog.setName("Cat");
        PetType savedCatPetType = petTypeService.save(cat);

        //------- Owners and their Pets --------
        Owner owner1 = new Owner();
        owner1.setFirstName("Rhys");
        owner1.setLastName("Jones");
        owner1.setAddress("101 Dulwich Street");
        owner1.setCity("London");
        owner1.setTelephone("01234111222");

        Pet rhysPet = new Pet();
        rhysPet.setPetType(savedCatPetType);
        rhysPet.setOwner(owner1);
        rhysPet.setBirthdate(LocalDate.of(2018, 8, 26));
        rhysPet.setName("Milo");

        owner1.getPets().add(rhysPet);
        ownerService.save(owner1);

        Owner owner2 = new Owner();
        owner2.setFirstName("Ricky");
        owner2.setLastName("Bobby");
        owner2.setAddress("101 Dulwich Street");
        owner2.setCity("London");
        owner2.setTelephone("01234111222");

        Pet rickysDog = new Pet();
        rickysDog.setPetType(savedDogPetType);
        rickysDog.setOwner(owner2);
        rickysDog.setBirthdate(LocalDate.of(2020, 1, 1));
        rickysDog.setName("Jean Girard");

        owner2.getPets().add(rickysDog);
        ownerService.save(owner2);

        System.out.println("Loaded Owners...");

        //------- Vets --------
        Vet vet1 = new Vet();
        vet1.setFirstName("Jack");
        vet1.setLastName("Black");

        vetService.save(vet1);

        Vet vet2 = new Vet();
        vet2.setFirstName("Helena");
        vet2.setLastName("Bonham-Carter");

        vetService.save(vet2);

        System.out.println("Loaded Vets...");

    }
}
