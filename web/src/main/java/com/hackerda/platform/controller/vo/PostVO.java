package com.hackerda.platform.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class PostVO {

    private long id;

    /**
     * 用户id
     */
    private String userName;
    /**
     * 展示的用户名称
     */
    private String showUserName;
    /**
     * 展示的用户头像
     */
    private String avatar;

    private String content;

    private int identityCode;

    private List<ImageInfoVO> imageInfoList;

    /** 评论总数 **/
    private long commentCount = 0;

    /** 点赞数 **/
    private long likeCount = 0;

    /** 查看总数 **/
    private int viewCount = 0;

    /** 发表时间 **/
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private Date postTime;

    /** 是否允许评论 **/
    private boolean allowComment = true;

    private boolean hasLike;

    /**
     * 是否是圈子作者
     */
    private boolean isAuthor;

    /**
     * 是否已经删除
     */
    private boolean hasDelete;

    /**
     * 是否是匿名贴
     */
    private boolean isAnonymous;

    /**
     * 帖子最后被回复时间
     */
    private Date lastReplyTime;
}
