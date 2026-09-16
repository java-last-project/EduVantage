COPY course_vector (
	id,
	course_no,
	chunk_no,
	title,
	instructor_no,
	content,
	embedding,
	create_at
)
FROM '/seed-data/course_vector.csv'
WITH (
    FORMAT CSV,
    HEADER TRUE,
    ENCODING 'UTF8'
);

COPY question_vector (
	id,
	question_no,
	course_no,
	title,
	description,
	theme,
	type,
	difficulty,
	answer,
	option1,
	option2,
	option3,
	option4,
	content,
	embedding,
	created_at,
	updated_at
)
FROM '/seed-data/question_vector.csv'
WITH (
    FORMAT CSV,
    HEADER TRUE,
    ENCODING 'UTF8'
);

COPY course_question_map (
	id,
	course_no,
	question_no,
	similarity,
	created_at
)
FROM '/seed-data/course_question_map.csv'
WITH (
    FORMAT CSV,
    HEADER TRUE,
    ENCODING 'UTF8'
);

SELECT setval(
               pg_get_serial_sequence('course_vector','id'),
               COALESCE((SELECT MAX(id) FROM course_vector),1),
               true
       );

SELECT setval(
               pg_get_serial_sequence('question_vector','id'),
               COALESCE((SELECT MAX(id) FROM question_vector),1),
               true
       );

SELECT setval(
               pg_get_serial_sequence('course_question_map','id'),
               COALESCE((SELECT MAX(id) FROM course_question_map),1),
               true
       );