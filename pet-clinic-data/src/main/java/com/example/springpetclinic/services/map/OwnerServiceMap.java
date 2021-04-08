package com.example.springpetclinic.services.map;

import com.example.springpetclinic.model.Owner;
import com.example.springpetclinic.model.Pet;
import com.example.springpetclinic.model.PetType;
import com.example.springpetclinic.services.OwnerService;
import com.example.springpetclinic.services.PetService;
import com.example.springpetclinic.services.PetTypeService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Profile({"default", "map"})
public class OwnerServiceMap extends AbstractMapService<Owner, Long> implements OwnerService {

    private final PetTypeService petTypeService;
    private final PetService petService;

    public OwnerServiceMap(PetTypeService petTypeService, PetService petService) {
        this.petTypeService = petTypeService;
        this.petService = petService;
    }

    @Override
    public Owner findByLastName(String lastName) {
        Collection<Owner> owners = super.map.values();
        Optional<Owner> matchedOwner = owners
                .stream()
                .filter(owner -> owner.getLastName().equals(lastName))
                .findFirst();

        return matchedOwner.orElse(null);
    }

    @Override
    public List<Owner> findAllByLastNameLike(String lastName) {
        //TODO implement
        return null;
    }

    @Override
    public Set<Owner> findAll() {
        return super.findAll();
    }

    @Override
    public Owner findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Owner save(Owner owner) {

        if (owner == null) return null;

        //owning the ID generation process for the members
        if (owner.getPets() != null) {
            owner.getPets().forEach(ownersPet -> {
                if (ownersPet.getPetType() == null) throw new RuntimeException("Pet Type is required.");

                if (ownersPet.getPetType().getId() == null) {
                    PetType savedPetTypeWithId = petTypeService.save(ownersPet.getPetType());
                    ownersPet.setPetType(savedPetTypeWithId);
                }

                if (ownersPet.getId() == null) {
                    Pet savedPet = petService.save(ownersPet);
                    ownersPet.setId(savedPet.getId());
                }
            });
        }
        return super.save(owner);

    }

    @Override
    public void delete(Owner object) {
        super.delete(object);
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}
