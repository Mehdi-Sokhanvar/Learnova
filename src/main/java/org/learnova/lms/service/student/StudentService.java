package org.learnova.lms.service.student;

import org.learnova.lms.dto.request.RegisterDTO;

public interface StudentService {
    void register( RegisterDTO user);
}
