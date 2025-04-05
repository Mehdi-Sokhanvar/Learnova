package org.learnova.lms.dto.request;

import java.util.List;

public class QuestionRequestDto {

    private Long courseId;

    private Long questionId;

    private Long examId;

    private Double score;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    //    private List<QuestionId> questionIdList;
//
//    public Long getCourseId() {
//        return courseId;
//    }
//
//    public void setCourseId(Long courseId) {
//        this.courseId = courseId;
//    }
//
//    public List<QuestionId> getQuestionIdList() {
//        return questionIdList;
//    }
//
//    public void setQuestionIdList(List<QuestionId> questionIdList) {
//        this.questionIdList = questionIdList;
//    }
}
