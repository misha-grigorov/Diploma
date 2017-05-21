package com.rx.controllers;

import com.rx.dao.Discipline;
import com.rx.dto.DisciplineFormDto;
import com.rx.services.DisciplineService;
import com.rx.validators.DisciplineFormDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/discipline")
public class DisciplineController {

    private DisciplineService disciplineService;
    private DisciplineFormDtoValidator validator;

    @Autowired
    public DisciplineController(DisciplineService disciplineService, DisciplineFormDtoValidator disciplineFormDtoValidator) {
        this.disciplineService = disciplineService;
        this.validator = disciplineFormDtoValidator;
    }

    @InitBinder("disciplineFormDto")
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @GetMapping(name = "add-discipline", value = "add-discipline")
    public ModelAndView getAddingDisciplineForm() {
        return addingDisciplineForm();
    }

    @PostMapping(name = "add-discipline", value = "add-discipline")
    public String addDiscipline(@Valid DisciplineFormDto disciplineFormDto,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-discipline";
        } else if (disciplineService.existsByName(disciplineFormDto.getName())) {
            bindingResult.rejectValue("name", "discipline.isPresent");
            return "add-discipline";
        } else {
            Long id = disciplineService.insertOrUpdateDiscipline(Discipline.builder()
                    .withName(disciplineFormDto.getName())
                    .build());
            return "redirect:/discipline/" + id + "/admin";
        }
    }

    @GetMapping(name = "{id}", value = "{id}")
    public ModelAndView getDiscipline(@PathVariable("id") Long id) {
        ModelAndView modelAndView = disciplineForm();
        modelAndView.getModel().put("id", id);
        modelAndView.getModel().put("discipline", disciplineService.getDisciplineById(id));

        return modelAndView;
    }

    @PostMapping(name = "{id}", value = "{id}")
    public String updateDiscipline(@PathVariable("id") Long id,
                                   @Valid DisciplineFormDto disciplineFormDto,
                                   BindingResult bindingResult,
                                   Model model) {
        Discipline discipline = disciplineService.getDisciplineById(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("disciplineFormDto", disciplineFormDto);
        } else if (disciplineService.existsByName(disciplineFormDto.getName())) {
            model.addAttribute("disciplineFormDto", disciplineFormDto);
            bindingResult.rejectValue("name", "discipline.isPresent");
        } else {
            discipline.setName(disciplineFormDto.getName());
            disciplineService.insertOrUpdateDiscipline(discipline);
        }

        model.addAttribute("discipline", discipline);
        return "discipline";
    }

    @GetMapping(name = "{id}/admin", value = "{id}/admin")
    public ModelAndView getDisciplineAdmin(@PathVariable("id") Long id) {
        ModelAndView modelAndView = disciplineAdminForm();
        modelAndView.getModel().put("id", id);
        modelAndView.getModel().put("discipline", disciplineService.getDisciplineById(id));

        return modelAndView;
    }

    @PostMapping(name = "{id}/admin", value = "{id}/admin")
    public String updateDisciplineAdmin(@PathVariable("id") Long id,
                                   @Valid DisciplineFormDto disciplineFormDto,
                                   BindingResult bindingResult,
                                   Model model) {
        Discipline discipline = disciplineService.getDisciplineById(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("disciplineFormDto", disciplineFormDto);
        } else if (disciplineService.existsByName(disciplineFormDto.getName())) {
            model.addAttribute("disciplineFormDto", disciplineFormDto);
            bindingResult.rejectValue("name", "discipline.isPresent");
        } else {
            discipline.setName(disciplineFormDto.getName());
            disciplineService.insertOrUpdateDiscipline(discipline);
        }


        model.addAttribute("discipline", discipline);
        return "discipline-admin";
    }

    @GetMapping(name = "disciplines", value = "disciplines")
    public String getDisciplines(Model model) {
        Iterable<Discipline> disciplines = disciplineService.getAllDisciplines();

        model.addAttribute("disciplines", disciplines);
        return "disciplines";
    }

    @GetMapping(name = "delete-discipline/{id}", value = "delete-discipline/{id}")
    public String deleteDiscipline(@PathVariable("id") Long id) {
        disciplineService.deleteById(id);
        return "redirect:/discipline/" + id + "/disciplines";
    }

    private ModelAndView addingDisciplineForm() {
        return new ModelAndView("add-discipline", "disciplineFormDto", new DisciplineFormDto());
    }

    private ModelAndView disciplineForm() {
        return new ModelAndView("discipline", "disciplineFormDto", new DisciplineFormDto());
    }

    private ModelAndView disciplineAdminForm() {
        return new ModelAndView("discipline-admin", "disciplineFormDto", new DisciplineFormDto());
    }
}
