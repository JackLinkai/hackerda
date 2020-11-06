package com.hackerda.platform.domain.community;

import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PostDetailBO extends PostBO {


    /** 发布者信息 **/
    private Poster postUser;

    /** 评论总数 **/
    private int commentCount = 0;

    /** 点赞数 **/
    private int likeCount = 0;

    /** 查看总数 **/
    private int viewCount = 0;

    private Date lastReplyTime;


    public PostDetailBO(long id, String userName, String content, List<ImageInfo> imageInfoList,
                        IdentityCategory identityCategory, String equipment) {
        super(userName, content, imageInfoList, identityCategory, equipment);
        super.setId(id);
    }

    public PostDetailBO(long id, String userName, String content, List<ImageInfo> imageInfoList,
                        IdentityCategory identityCategory, Date postTime, String equipment) {
        super(userName, content, imageInfoList, identityCategory, postTime, equipment);
        super.setId(id);
    }

    public String getShowUserName() {
        return postUser.getShowName(super.getIdentityCategory());
    }

    public String getShowAvatar() {
        return postUser.getShowAvatarUrl(super.getIdentityCategory());
    }
}
