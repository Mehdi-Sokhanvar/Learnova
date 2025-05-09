package org.learnova.lms.service.register;


import jakarta.validation.Valid;
import org.learnova.lms.dto.request.RegisterDTO;

public interface RegisterService {

    void registerStudent(@Valid RegisterDTO user);


    void registerTeacher(@Valid RegisterDTO user);
}
