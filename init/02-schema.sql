CREATE TABLE course_vector (
                               id BIGSERIAL PRIMARY KEY,
                               course_no BIGINT NOT NULL,
                               chunk_no INTEGER NOT NULL,
                               title VARCHAR(1000) NOT NULL,
                               instructor_no BIGINT NOT NULL,
                               content TEXT NOT NULL,
                               embedding VECTOR(768) NOT NULL,
                               create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE question_vector (
                                 id BIGSERIAL PRIMARY KEY,
                                 question_no BIGINT NOT NULL,
                                 course_no BIGINT,
                                 title VARCHAR(4000) NOT NULL,
                                 description TEXT,
                                 theme INTEGER,
                                 type INTEGER,
                                 difficulty INTEGER NOT NULL,
                                 answer VARCHAR(2000) NOT NULL,
                                 option1 VARCHAR(2000),
                                 option2 VARCHAR(2000),
                                 option3 VARCHAR(2000),
                                 option4 VARCHAR(2000),
                                 content TEXT,
                                 embedding VECTOR(768),
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE course_question_map (
                                     id BIGSERIAL PRIMARY KEY,
                                     course_no BIGINT NOT NULL,
                                     question_no BIGINT NOT NULL,
                                     similarity DOUBLE PRECISION NOT NULL,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     CONSTRAINT uq_course_question_map UNIQUE(course_no,question_no)
);

CREATE INDEX idx_course_vector_course_no
    ON course_vector(course_no);

CREATE INDEX idx_question_vector_question_no
    ON question_vector(question_no);

CREATE INDEX idx_question_vector_course_no
    ON question_vector(course_no);

CREATE INDEX idx_course_question_map_question_no
    ON course_question_map(question_no);

CREATE INDEX idx_course_question_map_course_no
    ON course_question_map(course_no);

CREATE INDEX idx_course_vector_embedding
    ON course_vector
    USING hnsw (embedding vector_cosine_ops);

CREATE INDEX idx_question_vector_embedding
    ON question_vector
    USING hnsw (embedding vector_cosine_ops);