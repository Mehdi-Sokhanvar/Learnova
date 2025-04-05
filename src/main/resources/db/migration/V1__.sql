CREATE TABLE app_user
(
    id         BIGINT       NOT NULL,
    dtype      VARCHAR(31),
    created    TIMESTAMP WITHOUT TIME ZONE,
    updated    TIMESTAMP WITHOUT TIME ZONE,
    user_name  VARCHAR(100) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    phone      VARCHAR(255) NOT NULL,
    role_id    BIGINT,
    street     VARCHAR(255),
    city       VARCHAR(255),
    state      VARCHAR(255),
    zip        VARCHAR(255),
    CONSTRAINT pk_appuser PRIMARY KEY (id)
);

CREATE TABLE course
(
    id          BIGINT       NOT NULL,
    created     TIMESTAMP WITHOUT TIME ZONE,
    updated     TIMESTAMP WITHOUT TIME ZONE,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    unique_id   UUID,
    start_date  date         NOT NULL,
    end_date    date         NOT NULL,
    teacher_id  BIGINT,
    CONSTRAINT pk_course PRIMARY KEY (id)
);

CREATE TABLE course_student_list
(
    course_id       BIGINT NOT NULL,
    student_list_id BIGINT NOT NULL
);

CREATE TABLE exam
(
    id          BIGINT       NOT NULL,
    created     TIMESTAMP WITHOUT TIME ZONE,
    updated     TIMESTAMP WITHOUT TIME ZONE,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    type        VARCHAR(255) NOT NULL,
    status      VARCHAR(255) NOT NULL,
    date        date         NOT NULL,
    start_time  time WITHOUT TIME ZONE NOT NULL,
    duration    INTEGER      NOT NULL,
    course_id   BIGINT,
    CONSTRAINT pk_exam PRIMARY KEY (id)
);

CREATE TABLE role
(
    id          BIGINT       NOT NULL,
    created     TIMESTAMP WITHOUT TIME ZONE,
    updated     TIMESTAMP WITHOUT TIME ZONE,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE student_courses
(
    course_id  BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    CONSTRAINT pk_student_courses PRIMARY KEY (course_id, student_id)
);

CREATE INDEX idx_course_end_date ON course (end_date);

CREATE INDEX idx_course_start_date ON course (start_date);

CREATE UNIQUE INDEX idx_course_unique_id ON course (unique_id);

CREATE INDEX idx_exam_date ON exam (date);

CREATE INDEX idx_user_email ON app_user (email);

CREATE INDEX idx_user_username ON app_user (user_name);

ALTER TABLE app_user
    ADD CONSTRAINT FK_APPUSER_ON_ROLE FOREIGN KEY (role_id) REFERENCES role (id);

ALTER TABLE course
    ADD CONSTRAINT FK_COURSE_ON_TEACHER FOREIGN KEY (teacher_id) REFERENCES app_user (id);

ALTER TABLE exam
    ADD CONSTRAINT FK_EXAM_ON_COURSE FOREIGN KEY (course_id) REFERENCES course (id);

CREATE INDEX idx_exam_course ON exam (course_id);

ALTER TABLE course_student_list
    ADD CONSTRAINT fk_coustulis_on_course FOREIGN KEY (course_id) REFERENCES course (id);

ALTER TABLE course_student_list
    ADD CONSTRAINT fk_coustulis_on_student FOREIGN KEY (student_list_id) REFERENCES app_user (id);

ALTER TABLE student_courses
    ADD CONSTRAINT fk_stucou_on_course FOREIGN KEY (course_id) REFERENCES course (id);

ALTER TABLE student_courses
    ADD CONSTRAINT fk_stucou_on_student FOREIGN KEY (student_id) REFERENCES app_user (id);