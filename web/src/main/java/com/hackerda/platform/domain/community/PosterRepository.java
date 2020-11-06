package com.hackerda.platform.domain.community;

import com.hackerda.platform.domain.student.StudentAccount;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface PosterRepository {

    StudentPoster findByStudentAccount(StudentAccount studentAccount);

    StudentPoster findStudentPosterByUserName(String userName);

    List<StudentPoster> findStudentPosterByUserName(Collection<String> userName);

    void save(PostBO postBO);

    void update(PostBO postBO);

    PostDetailBO findByPostById(long id);

    List<PostDetailBO> findByIdList(List<Long> idList);

    List<PostDetailBO> findShowPost(Integer start, int count);

    List<PostDetailBO> findShowPostByLastReply(Date lastReplyTime, int count);

    List<PostDetailBO> findPostByUser(String userName, Integer start, int count);

    long countShowPost(String userName, List<IdentityCategory> identityCategoryList);

    long findMaxReleasePostId();

    void updateLastReplyTime(long id, Date lastReplyTime);

}
