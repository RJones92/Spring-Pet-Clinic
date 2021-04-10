package com.example.springpetclinic.controllers;

import com.example.springpetclinic.model.Owner;
import com.example.springpetclinic.services.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/owners")
public class OwnerController {
    private static final String VIEW_FIND_OWNERS_FORM = "owners/findOwners";
    private static final String VIEW_LIST_OWNERS = "owners/ownersList";
    private static final String VIEW_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder webDataBinder) {
        webDataBinder.setDisallowedFields("id");
    }

    @RequestMapping("/find")
    public String findOwnerForm(Model model) {
        model.addAttribute("owner", Owner.builder().build());
        return VIEW_FIND_OWNERS_FORM;
    }

    @GetMapping({"", "/"})
    public String processFindOwnerForm(Owner owner, BindingResult bindingResult, Model model) {

        if (owner.getLastName() == null) {
            owner.setLastName("");
        }

        List<Owner> results = ownerService.findAllByLastNameLike("%" + owner.getLastName() + "%");
        if (results.isEmpty()) {
            bindingResult.rejectValue("lastName", "not found", "not found");
            return VIEW_FIND_OWNERS_FORM;
        } else if (results.size() == 1) {
            owner = results.get(0);
            return "redirect:/owners/" + owner.getId();
        } else {
            model.addAttribute("selections", results);
            return VIEW_LIST_OWNERS;
        }
    }

    @GetMapping("/{ownerId}")
    public ModelAndView showOwner(@PathVariable Long ownerId) {
        ModelAndView mav = new ModelAndView("owners/ownerDetails");
        mav.addObject(ownerService.findById(ownerId));
        return mav;
    }

    @GetMapping("/new")
    public String createOwnerForm(Model model) {
        model.addAttribute("owner", Owner.builder().build());
        return VIEW_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/new")
    public String processCreateOwnerForm(Owner owner, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return VIEW_CREATE_OR_UPDATE_FORM;
        } else {
            Owner savedOwner = ownerService.save(owner);
            return "redirect:/owners/" + savedOwner.getId();
        }
    }

    @GetMapping("/{ownerId}/edit")
    public String updateOwnerForm(@PathVariable Long ownerId, Model model) {
        Owner ownerToUpdate = ownerService.findById(ownerId);
        model.addAttribute("owner", ownerToUpdate);
        return VIEW_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/{ownerId}/edit")
    public String processUpdateOwnerForm(Owner owner, BindingResult bindingResult, @PathVariable Long ownerId) {
        if (bindingResult.hasErrors()) {
            return VIEW_CREATE_OR_UPDATE_FORM;
        } else {
            owner.setId(ownerId);
            Owner savedOwner = ownerService.save(owner);
            return "redirect:/owners/" + savedOwner.getId();
        }
    }
}
