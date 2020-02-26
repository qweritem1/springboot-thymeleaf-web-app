package net.springboot.javaguides.controller;

import net.springboot.javaguides.entity.Student;
import net.springboot.javaguides.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/signup")
    public String showSignUpForm(Student student) {
        return "add-student";
    }

    @GetMapping("/showForm")
    public String showStudentForm() {
        return "add-student";
    }

    @GetMapping("/list")
    public String students(Model model) {
        model.addAttribute("students", this.studentRepository.findAll());
        return "index";
    }

    @PostMapping("/add")
    public String addStudent(Student student, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "add-student";
        }

        this.studentRepository.save(student);
        return "redirect:list";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Student student = this.studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student"));

        model.addAttribute("student", student);
        return "update-student";
    }

    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable("id") long id, @Valid Student student,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            student.setId(id);
            return "update-student";
        }

        studentRepository.save(student);
        model.addAttribute("students", this.studentRepository.findAll());
        return "index";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable ("id") long id, Model model) {
        Student student = this.studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student"));
        this.studentRepository.delete(student);
        model.addAttribute("students", this.studentRepository.findAll());
        return "index";
    }
}
