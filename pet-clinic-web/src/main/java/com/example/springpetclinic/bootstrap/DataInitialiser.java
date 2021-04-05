package com.example.springpetclinic.bootstrap;

import com.example.springpetclinic.model.*;
import com.example.springpetclinic.services.OwnerService;
import com.example.springpetclinic.services.PetTypeService;
import com.example.springpetclinic.services.SpecialityService;
import com.example.springpetclinic.services.VetService;
import com.example.springpetclinic.services.VisitService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitialiser implements CommandLineRunner {

    private final OwnerService ownerService;
    private final VetService vetService;
    private final PetTypeService petTypeService;
    private final SpecialityService specialityService;
    private final VisitService visitService;

    public DataInitialiser(OwnerService ownerService, VetService vetService, PetTypeService petTypeService,
                           SpecialityService specialityService, VisitService visitService) {
        this.ownerService = ownerService;
        this.vetService = vetService;
        this.petTypeService = petTypeService;
        this.specialityService = specialityService;
        this.visitService = visitService;
    }

    @Override
    public void run(String... args) throws Exception {

        int countPetTypesStored = petTypeService.findAll().size();
        if (countPetTypesStored == 0) loadData();

    }

    private void loadData() {
        //------- Pet Types --------
        PetType dog = new PetType();
        dog.setName("Dog");
        PetType savedDogPetType = petTypeService.save(dog);
        
        PetType cat = new PetType();
        cat.setName("Cat");
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
        rhysPet.setBirthDate(LocalDate.of(2018, 8, 26));
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
        rickysDog.setBirthDate(LocalDate.of(2020, 1, 1));
        rickysDog.setName("Jean Girard");

        owner2.getPets().add(rickysDog);

        ownerService.save(owner2);

        System.out.println("Loaded Owners...");

        //------- Visits --------
        Visit dogVisit = new Visit();
        dogVisit.setPet(rickysDog);
        dogVisit.setDate(LocalDate.of(2021, 9, 2));
        dogVisit.setDescription("Sneezy dog");

        visitService.save(dogVisit);


        //------- Specialities --------
        Speciality dentistry = new Speciality();
        dentistry.setDescription("Dentistry");
        Speciality savedDentistry = specialityService.save(dentistry);

        Speciality surgery = new Speciality();
        surgery.setDescription("Surgery");
        Speciality savedSurgery = specialityService.save(surgery);

        Speciality cardiologist = new Speciality();
        cardiologist.setDescription("Cardiologist");
        Speciality savedCardiologist = specialityService.save(cardiologist);

        //------- Vets --------
        Vet vet1 = new Vet();
        vet1.setFirstName("Jack");
        vet1.setLastName("Black");
        vet1.getSpecialities().add(savedDentistry);

        vetService.save(vet1);

        Vet vet2 = new Vet();
        vet2.setFirstName("Helena");
        vet2.setLastName("Bonham-Carter");
        vet2.getSpecialities().add(savedSurgery);
        vet2.getSpecialities().add(savedCardiologist);

        vetService.save(vet2);

        System.out.println("Loaded Vets...");
    }
}
