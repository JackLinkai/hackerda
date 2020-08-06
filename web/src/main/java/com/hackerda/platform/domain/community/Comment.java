package com.hackerda.platform.domain.community;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Comment {

    private Long id;

    /** 根评论的id **/
    private Long rootId;

    /** 回复的评论id **/
    private Long replyCommentId;

    private boolean isRoot;

    private PostUser replyUser;

    private ImageInfo imageInfo;

    /** 话题Id **/
    private Long postId;
    /** 状态 10.待审核 20.已发布 **/
    private Integer status = 10;

    /** 评论内容 **/
    private String content;
    /** 评论时间 **/
    private Date postTime = new Date();


    /** 用户角色名称集合 **/
    private List<String> userRoleNameList = new ArrayList<>();
    /** 总回复数 **/
    private Integer totalReply = 0;
    /** 回复集合 **/
    private List<Comment> replyList = new ArrayList<>();

    /** 点赞数目 **/
    private int likeCount;

}