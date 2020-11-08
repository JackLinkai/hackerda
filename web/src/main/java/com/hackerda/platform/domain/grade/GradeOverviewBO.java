package com.hackerda.platform.domain.grade;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GradeOverviewBO {

    private List<TermGradeBO> termGradeList;

    private double gpa;

    private int gpaRank;

    private int gpaRankSize;

    /**
     * 选修课分
     */
    private double optionalCourseCredit;

    private int errorCode;

    private String errorMsg;

    public GradeOverviewBO (List<TermGradeBO> termGradeList) {
        this.termGradeList = termGradeList;

        double sumGradePoint = 0.0;
        double sumCredit = 0.0;
        double sumOptionalCredit = 0.0;

        List<GradeBO> collect = termGradeList.stream()
                .flatMap(x -> x.getGradeList().stream())
                .filter(GradeBO::hasScore)
                .collect(Collectors.toList());

        // count gpa
        for (GradeBO grade : collect) {
            double credit = grade.getCourseCredit();
            sumCredit += credit;
            sumGradePoint += grade.getCreditGradePoint();
            if(grade.isOptional()){
                sumOptionalCredit += credit;
            }
        }
        this.setOptionalCourseCredit(sumOptionalCredit);


        if(sumCredit != 0){
            double f = sumGradePoint / sumCredit;
            BigDecimal b = new BigDecimal(f);
            this.setGpa(b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
    }


    public List<GradeBO> getUpdateGrade(){
        if(!CollectionUtils.isEmpty(termGradeList)) {
            return termGradeList.stream().map(TermGradeBO::getGradeList)
                    .flatMap(Collection::stream)
                    .filter(GradeBO::isUpdate)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<GradeBO> getNewGrade(){
        if(!CollectionUtils.isEmpty(termGradeList)) {
            return termGradeList.stream().map(TermGradeBO::getGradeList)
                    .flatMap(Collection::stream)
                    .filter(GradeBO::isNewGrade)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public boolean fetchSuccess(){
        if(CollectionUtils.isEmpty(termGradeList)){
            return false;
        }

        boolean success = true;

        for (TermGradeBO gradeBO : termGradeList) {
            if(!gradeBO.isFetchSuccess() && !gradeBO.isFinishFetch()){
                success = false;
                break;
            }
        }
        return success;
    }

    public List<GradeBO> getNeedToSendGrade(){
        if(!CollectionUtils.isEmpty(termGradeList)){
            return termGradeList.get(0).getGradeList()
                    .stream()
                    .filter(GradeBO::hasScore)
                    .filter(x-> x.isUpdate() || x.isNewGrade())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * 以往的成绩是否已经完成抓取
     * @return 完成抓取则返回true
     */
    public boolean isFinishFetch(){
        if (!CollectionUtils.isEmpty(termGradeList)) {
            for (TermGradeBO grade : termGradeList) {
                if(!grade.isFinishFetch()){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
