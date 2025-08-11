CREATE TABLE answer_option
(
    id                          BIGINT NOT NULL,
    created                     TIMESTAMP,
    updated                     TIMESTAMP,
    multiple_option_question_id BIGINT,
    option                      VARCHAR(255),
    is_correct                  BOOLEAN,
    CONSTRAINT pk_answeroption PRIMARY KEY (id)
);

CREATE TABLE app_user
(
    id         BIGINT       NOT NULL,
    dtype      VARCHAR(31),
    created    TIMESTAMP,
    updated    TIMESTAMP,
    user_name  VARCHAR(100) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    phone      VARCHAR(255),
    status     VARCHAR(255),
    role_id    BIGINT,
    street     VARCHAR(255),
    city       VARCHAR(255),
    state      VARCHAR(255),
    zip        VARCHAR(255),
    CONSTRAINT pk_appuser PRIMARY KEY (id)
);

CREATE TABLE category
(
    id      BIGINT NOT NULL,
    created TIMESTAMP,
    updated TIMESTAMP,
    name    VARCHAR(255),
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE course
(
    id          BIGINT NOT NULL,
    created     TIMESTAMP,
    updated     TIMESTAMP,
    title       VARCHAR(255),
    description VARCHAR(255),
    unique_id   UUID,
    start_date  date,
    end_date    date,
    teacher_id  BIGINT,
    CONSTRAINT pk_course PRIMARY KEY (id)
);

CREATE TABLE essay_question
(
    id         BIGINT       NOT NULL,
    answer     VARCHAR(255) NOT NULL,
    max_length INT,
    CONSTRAINT pk_essayquestion PRIMARY KEY (id)
);

CREATE TABLE exam
(
    id             BIGINT                   NOT NULL,
    created        TIMESTAMP,
    updated        TIMESTAMP,
    title          VARCHAR(255)             NOT NULL,
    description    VARCHAR(255)             NOT NULL,
    status         VARCHAR(255),
    date           date                     NOT NULL,
    start_time     TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time       TIMESTAMP WITH TIME ZONE NOT NULL,
    course_id      BIGINT,
    teacher_id     BIGINT,
    exam_time_zone VARCHAR(255),
    shuffled       BOOLEAN,
    mark           DOUBLE PRECISION,
    passing_mark   DOUBLE PRECISION,
    CONSTRAINT pk_exam PRIMARY KEY (id)
);

CREATE TABLE exam_question
(
    id          BIGINT NOT NULL,
    created     TIMESTAMP,
    updated     TIMESTAMP,
    exam_id     BIGINT,
    question_id BIGINT,
    score       DOUBLE PRECISION,
    CONSTRAINT pk_examquestion PRIMARY KEY (id)
);

CREATE TABLE exam_session
(
    id               BIGINT NOT NULL,
    created          TIMESTAMP,
    updated          TIMESTAMP,
    exam_id          BIGINT,
    student_id       BIGINT,
    start_time       TIMESTAMP,
    end_time         TIMESTAMP,
    status           SMALLINT,
    total_score      DOUBLE PRECISION,
    current_question INT,
    CONSTRAINT pk_examsession PRIMARY KEY (id)
);

CREATE TABLE exam_session_question_order
(
    exam_session_id BIGINT NOT NULL,
    question_order  BIGINT
);

CREATE TABLE multiple_option_question
(
    id       BIGINT NOT NULL,
    shuffled BOOLEAN,
    CONSTRAINT pk_multipleoptionquestion PRIMARY KEY (id)
);

CREATE TABLE null_question_list
(
    teacher_id       BIGINT NOT NULL,
    question_list_id BIGINT NOT NULL
);

CREATE TABLE question
(
    id            BIGINT NOT NULL,
    created       TIMESTAMP,
    updated       TIMESTAMP,
    title         VARCHAR(255),
    description   CLOB,
    identifier    VARCHAR(255),
    default_score DOUBLE PRECISION,
    level         VARCHAR(255),
    category_id   BIGINT,
    teacher_id    BIGINT,
    course_id     BIGINT,
    type          VARCHAR(255),
    is_deleted    BOOLEAN,
    CONSTRAINT pk_question PRIMARY KEY (id)
);

CREATE TABLE role
(
    id          BIGINT       NOT NULL,
    created     TIMESTAMP,
    updated     TIMESTAMP,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE student_answer
(
    id               BIGINT NOT NULL,
    created          TIMESTAMP,
    updated          TIMESTAMP,
    exam_session_id  BIGINT,
    exam_question_id BIGINT,
    answer           VARCHAR(255),
    score            DOUBLE PRECISION,
    feedback         VARCHAR(255),
    CONSTRAINT pk_studentanswer PRIMARY KEY (id)
);

CREATE TABLE student_courses
(
    course_id  BIGINT NOT NULL,
    student_id BIGINT NOT NULL
);

CREATE TABLE true_false_question
(
    id         BIGINT NOT NULL,
    is_correct BOOLEAN,
    CONSTRAINT pk_truefalsequestion PRIMARY KEY (id)
);

ALTER TABLE null_question_list
    ADD CONSTRAINT uc_null_question_list_questionlist UNIQUE (question_list_id);

ALTER TABLE question
    ADD CONSTRAINT uc_question_identifier UNIQUE (identifier);

CREATE INDEX idx_course_end_date ON course (end_date);

CREATE INDEX idx_course_start_date ON course (start_date);

CREATE UNIQUE INDEX idx_course_unique_id ON course (unique_id);

CREATE INDEX idx_exam_date ON exam (date);

CREATE INDEX idx_question_identifier ON question (identifier);

CREATE INDEX idx_user_email ON app_user (email);

CREATE INDEX idx_user_username ON app_user (user_name);

ALTER TABLE answer_option
    ADD CONSTRAINT FK_ANSWEROPTION_ON_MULTIPLEOPTIONQUESTION FOREIGN KEY (multiple_option_question_id) REFERENCES multiple_option_question (id);

ALTER TABLE app_user
    ADD CONSTRAINT FK_APPUSER_ON_ROLE FOREIGN KEY (role_id) REFERENCES role (id);

ALTER TABLE course
    ADD CONSTRAINT FK_COURSE_ON_TEACHER FOREIGN KEY (teacher_id) REFERENCES app_user (id);

ALTER TABLE essay_question
    ADD CONSTRAINT FK_ESSAYQUESTION_ON_ID FOREIGN KEY (id) REFERENCES question (id);

ALTER TABLE exam_question
    ADD CONSTRAINT FK_EXAMQUESTION_ON_EXAM FOREIGN KEY (exam_id) REFERENCES exam (id);

ALTER TABLE exam_question
    ADD CONSTRAINT FK_EXAMQUESTION_ON_QUESTION FOREIGN KEY (question_id) REFERENCES question (id);

ALTER TABLE exam_session
    ADD CONSTRAINT FK_EXAMSESSION_ON_EXAM FOREIGN KEY (exam_id) REFERENCES exam (id);

ALTER TABLE exam_session
    ADD CONSTRAINT FK_EXAMSESSION_ON_STUDENT FOREIGN KEY (student_id) REFERENCES app_user (id);

ALTER TABLE exam
    ADD CONSTRAINT FK_EXAM_ON_COURSE FOREIGN KEY (course_id) REFERENCES course (id);

CREATE INDEX idx_exam_course ON exam (course_id);

ALTER TABLE exam
    ADD CONSTRAINT FK_EXAM_ON_TEACHER FOREIGN KEY (teacher_id) REFERENCES app_user (id);

ALTER TABLE multiple_option_question
    ADD CONSTRAINT FK_MULTIPLEOPTIONQUESTION_ON_ID FOREIGN KEY (id) REFERENCES question (id);

ALTER TABLE question
    ADD CONSTRAINT FK_QUESTION_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE question
    ADD CONSTRAINT FK_QUESTION_ON_COURSE FOREIGN KEY (course_id) REFERENCES course (id);

CREATE INDEX idx_question_course ON question (course_id);

ALTER TABLE question
    ADD CONSTRAINT FK_QUESTION_ON_TEACHER FOREIGN KEY (teacher_id) REFERENCES app_user (id);

ALTER TABLE student_answer
    ADD CONSTRAINT FK_STUDENTANSWER_ON_EXAMQUESTION FOREIGN KEY (exam_question_id) REFERENCES exam_question (id);

ALTER TABLE student_answer
    ADD CONSTRAINT FK_STUDENTANSWER_ON_EXAMSESSION FOREIGN KEY (exam_session_id) REFERENCES exam_session (id);

ALTER TABLE true_false_question
    ADD CONSTRAINT FK_TRUEFALSEQUESTION_ON_ID FOREIGN KEY (id) REFERENCES question (id);

ALTER TABLE exam_session_question_order
    ADD CONSTRAINT fk_examsession_questionorder_on_exam_session FOREIGN KEY (exam_session_id) REFERENCES exam_session (id);

ALTER TABLE null_question_list
    ADD CONSTRAINT fk_nulquelis_on_question FOREIGN KEY (question_list_id) REFERENCES question (id);

ALTER TABLE null_question_list
    ADD CONSTRAINT fk_nulquelis_on_teacher FOREIGN KEY (teacher_id) REFERENCES app_user (id);

ALTER TABLE student_courses
    ADD CONSTRAINT fk_stucou_on_course FOREIGN KEY (course_id) REFERENCES course (id);

ALTER TABLE student_courses
    ADD CONSTRAINT fk_stucou_on_student FOREIGN KEY (student_id) REFERENCES app_user (id);