package org.learnova.lms.service.register;


import javax.validation.Valid;
import javax.validation.constraints.*;
import org.learnova.lms.dto.request.RegisterDTO;

public interface RegisterService {

    void registerStudent(@Valid RegisterDTO user);


    void registerTeacher(@Valid RegisterDTO user);
}
