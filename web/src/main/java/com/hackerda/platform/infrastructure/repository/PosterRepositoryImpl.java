package com.hackerda.platform.infrastructure.repository;

import com.github.pagehelper.PageHelper;
import com.hackerda.platform.domain.community.*;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.user.Gender;
import com.hackerda.platform.infrastructure.database.dao.ImageDao;
import com.hackerda.platform.infrastructure.database.dao.user.UserDao;
import com.hackerda.platform.infrastructure.database.mapper.ext.PostExtMapper;
import com.hackerda.platform.infrastructure.database.model.ImageInfoDO;
import com.hackerda.platform.infrastructure.database.model.Post;
import com.hackerda.platform.infrastructure.database.model.PostExample;
import com.hackerda.platform.infrastructure.database.model.StudentPosterDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PosterRepositoryImpl implements PosterRepository {

    @Autowired
    private UserDao userDao;
    @Autowired
    private ImageDao imageDao;
    @Autowired
    private PostExtMapper postExtMapper;

    @Override
    public StudentPoster findByStudentAccount(StudentAccount studentAccount) {

        StudentPosterDO studentPosterDO = userDao.selectByStudentPoster(studentAccount.getAccount());

        return getStudentPoster(studentPosterDO, studentAccount);
    }

    @Override
    public StudentPoster findStudentPosterByUserName(String userName) {


        return findStudentPosterByUserName(Collections.singletonList(userName)).stream().findFirst().orElse(null);
    }

    @Override
    public List<StudentPoster> findStudentPosterByUserName(Collection<String> userName) {

        return userDao.selectStudentPosterByUserName(userName).stream()
                .map(x-> getStudentPoster(x, new StudentAccount(x.getAccount())))
                .collect(Collectors.toList());
    }

    private StudentPoster getStudentPoster(StudentPosterDO studentPosterDO, StudentAccount studentAccount) {
        StudentPoster.StudentPosterBuilder builder = StudentPoster.builder(studentPosterDO.getUserName(),
                studentPosterDO.getNickName(), studentPosterDO.getAvatarUrl(), Gender.formCode(studentPosterDO.getGender()));

        builder.academyName(studentPosterDO.getAcademyName());
        builder.className(studentPosterDO.getClassName());
        builder.name(studentPosterDO.getName());
        builder.sex(studentPosterDO.getSex());
        builder.subjectName(studentPosterDO.getSubjectName());
        builder.studentAccount(studentAccount);
        builder.urpClassNum(studentPosterDO.getUrpClassNum());

        return builder.build();
    }

    @Override
    @Transactional
    public void save(PostBO postBO) {

        Post post = adapter(postBO);
        postExtMapper.insertSelective(post);
        postBO.setId(post.getId());

        List<ImageInfoDO> imageInfoDOList = postBO.getImageInfoList().stream().map(x -> {
            ImageInfoDO imageInfoDO = new ImageInfoDO();
            imageInfoDO.setFileId(x.getFileId());
            imageInfoDO.setUrl(x.getPath());
            imageInfoDO.setRecordStatus(x.getRecordStatus().getCode());
            return imageInfoDO;
        }).collect(Collectors.toList());

        imageDao.insertBatch(imageInfoDOList);

        imageDao.insertPostImageRelative(post.getId(), imageInfoDOList.stream()
                .map(ImageInfoDO::getId).collect(Collectors.toList()));


    }

    @Override
    public void update(PostBO postBO) {
        Post post = adapter(postBO);

        post.setId(postBO.getId());

        postExtMapper.updateByPrimaryKeySelective(post);
    }

    private Post adapter(PostBO postBO) {
        Post post = new Post();

        post.setAllowComment(postBO.isAllowComment() ? (byte) 1 : (byte) 0);
        post.setContent(postBO.getContent());
        post.setIdentityCode(postBO.getIdentityCategory().getCode());
        post.setRecordStatus(postBO.getStatus().getCode());
        post.setUserName(postBO.getUserName());
        post.setPostTime(postBO.getPostTime());
        post.setEquipment(postBO.getEquipment());
        return post;
    }

    @Override
    public PostDetailBO findByPostById(long id) {
        Post post = postExtMapper.selectByPrimaryKey(id);

        return getPostDetailBO(post);
    }

    @Override
    public List<PostDetailBO> findByIdList(List<Long> idList) {

        if(idList.isEmpty()) {
            return Collections.emptyList();
        }
        PostExample example = new PostExample();

        example.createCriteria().andIdIn(idList);

        return postExtMapper.selectByExample(example).stream().map(this::getPostDetailBO).collect(Collectors.toList());
    }


    @Override
    public List<PostDetailBO> findShowPost(Integer startId, int limit) {

        PostExample example = new PostExample();
        example.setOrderByClause("id desc");
        PostExample.Criteria criteria = example.createCriteria();
        if(startId != null) {
            criteria.andIdLessThan(startId.longValue());
        }
        criteria.andRecordStatusEqualTo(RecordStatus.Release.getCode());
        PageHelper.startPage(0, limit);
        List<Post> postList = postExtMapper.selectByExample(example);

        return postList.stream().map(this::getPostDetailBO).collect(Collectors.toList());

    }

    @Override
    public List<PostDetailBO> findShowPostByLastReply(Date lastReplyTime, int count) {
        PostExample example = new PostExample();
        example.setOrderByClause("last_reply_time desc");
        PostExample.Criteria criteria = example.createCriteria();
        if(lastReplyTime != null) {
            criteria.andLastReplyTimeLessThan(lastReplyTime);
        }
        criteria.andRecordStatusEqualTo(RecordStatus.Release.getCode());
        PageHelper.startPage(0, count);
        List<Post> postList = postExtMapper.selectByExample(example);

        return postList.stream().map(this::getPostDetailBO).collect(Collectors.toList());
    }

    @Override
    public List<PostDetailBO> findPostByUser(String userName, Integer startId, int count) {
        PostExample example = new PostExample();
        example.setOrderByClause("id desc");
        PostExample.Criteria criteria = example.createCriteria();

        if(startId != null) {
            criteria.andIdLessThan(startId.longValue());
        }
        criteria.andRecordStatusEqualTo(RecordStatus.Release.getCode());
        criteria.andUserNameEqualTo(userName);
        criteria.andIdentityCodeEqualTo(IdentityCategory.Community.getCode());
        PageHelper.startPage(0, count);
        List<Post> postList = postExtMapper.selectByExample(example);

        return postList.stream().map(this::getPostDetailBO).collect(Collectors.toList());
    }

    @Override
    public long countShowPost(String userName, List<IdentityCategory> identityCategoryList) {
        PostExample example = new PostExample();
        example.createCriteria().andUserNameEqualTo(userName)
                .andIdentityCodeIn(identityCategoryList.stream().map(IdentityCategory::getCode).collect(Collectors.toList()))
                .andRecordStatusEqualTo(RecordStatus.Release.getCode());
        return postExtMapper.countByExample(example);
    }

    @Override
    public long findMaxReleasePostId() {
        PostExample example = new PostExample();
        example.setOrderByClause("id desc");
        example.createCriteria()
                .andRecordStatusEqualTo(RecordStatus.Release.getCode());

        PageHelper.startPage(0, 1);

        return postExtMapper.selectByExample(example).stream().findFirst().get().getId();
    }

    @Override
    public void updateLastReplyTime(long id, Date lastReplyTime) {
        Post post = new Post();
        post.setId(id);
        post.setLastReplyTime(lastReplyTime);
        postExtMapper.updateByPrimaryKeySelective(post);
    }


    private PostDetailBO getPostDetailBO(Post post) {
        List<ImageInfo> imageInfoList = imageDao.selectByPostId(post.getId()).stream()
                .map(imageInfoDO -> new ImageInfo(imageInfoDO.getUrl(), imageInfoDO.getFileId(),
                        RecordStatus.getByCode(imageInfoDO.getRecordStatus()))).collect(Collectors.toList());

        PostDetailBO postDetailBO = new PostDetailBO(post.getId(), post.getUserName(), post.getContent(), imageInfoList,
                IdentityCategory.getByCode(post.getIdentityCode()), post.getPostTime(), post.getEquipment());

        StudentPoster poster = this.findStudentPosterByUserName(post.getUserName());

        postDetailBO.setCommentCount(post.getCommentCount());
        postDetailBO.setViewCount(post.getViewCount());
        postDetailBO.setLikeCount(post.getLikeCount());
        postDetailBO.setPostUser(poster);
        postDetailBO.setStatus(RecordStatus.getByCode(post.getRecordStatus()));
        postDetailBO.setLastReplyTime(post.getLastReplyTime());
        return postDetailBO;
    }
}
