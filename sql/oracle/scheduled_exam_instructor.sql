-- 강사 정기시험 관리 기능용 Oracle 스키마 변경
-- 애플리케이션 실행 전 TEAMDB 계정에서 1회 실행

DECLARE
	v_count NUMBER;
BEGIN
	SELECT COUNT(*) INTO v_count
	FROM user_tab_columns
	WHERE table_name='SCHEDULED_EXAM'
	  AND column_name='INSTRUCTOR_ID';

	IF v_count=0 THEN
		EXECUTE IMMEDIATE 'ALTER TABLE scheduled_exam ADD instructor_id NUMBER';
	END IF;
END;
/

DECLARE
	v_count NUMBER;
BEGIN
	SELECT COUNT(*) INTO v_count
	FROM user_tab_columns
	WHERE table_name='SCHEDULED_EXAM'
	  AND column_name='TIME_LIMIT';

	IF v_count=0 THEN
		EXECUTE IMMEDIATE 'ALTER TABLE scheduled_exam ADD time_limit NUMBER DEFAULT 120';
	END IF;
END;
/

UPDATE scheduled_exam
SET time_limit=120
WHERE time_limit IS NULL;

COMMIT;
