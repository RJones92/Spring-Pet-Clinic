package com.example.springpetclinic.services.map;

import com.example.springpetclinic.model.Owner;
import com.example.springpetclinic.services.OwnerService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
public class OwnerServiceMap extends AbstractMapService<Owner, Long> implements OwnerService {

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
    public Set<Owner> findAll() {
        return super.findAll();
    }

    @Override
    public Owner findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Owner save(Owner object) {
        return super.save(object);
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
