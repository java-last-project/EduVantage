package com.sist.web.domain.exam.mapper;

import com.sist.web.domain.exam.vo.ExamOptionVO;
import com.sist.web.domain.exam.vo.ExamQuestionVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AiExamMapper {
    public int insertAiQuestion(ExamQuestionVO vo);
    public int insertAiOption(ExamOptionVO vo);
}
