package com.hackerda.platform.service;

import com.hackerda.platform.application.CommunityPostApp;
import com.hackerda.platform.controller.request.CreatePostRequest;
import com.hackerda.platform.controller.vo.*;
import com.hackerda.platform.domain.community.*;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.wechat.WechatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommunityPostService {

    @Autowired
    private PosterRepository posterRepository;
    @Autowired
    private CommunityPostApp communityPostApp;
    @Autowired
    private LikeCountService likeCountService;
    @Autowired
    private CommentCountService commentCountService;
    @Autowired
    private IdentityCategoryFilter identityCategoryFilter;
    @Autowired
    private PostViewCounter postViewCounter;

    public PostIdentityVO getPostIdentityByStudent(String account) {

        StudentAccount studentAccount = new StudentAccount(account);

        PostIdentityVO vo = new PostIdentityVO();

        StudentPoster studentPoster = posterRepository.findByStudentAccount(studentAccount);

        if(studentPoster != null) {
            for (IdentityCategory value : IdentityCategory.values()) {
                if(identityCategoryFilter.userChooseFilter(value)) {
                    continue;
                }
                boolean checked = value == IdentityCategory.Community;
                vo.add(value.getCode(), studentPoster.getShowName(value), studentPoster.getShowAvatarUrl(value), checked);
            }
        }
        return vo;
    }

    public CreatePostResultVO cratePost(String userName, CreatePostRequest createPostRequest) {
        List<ImageInfo> imageInfoList = createPostRequest.getImageInfoRequestList().stream()
                .map(x -> new ImageInfo(x.getTempFileURL(), x.getFileID()))
                .collect(Collectors.toList());


        PostBO postBO = new PostBO(userName, createPostRequest.getContent(), imageInfoList,
                IdentityCategory.getByCode(createPostRequest.getIdentityCode()), createPostRequest.getModel());

        postBO.setStatus(RecordStatus.Create);

        communityPostApp.createPost(postBO);

        CreatePostResultVO createPostResultVO = new CreatePostResultVO();

        createPostResultVO.setRelease(postBO.isRelease());
        createPostResultVO.setErrMsg(postBO.getUnReleaseReason());

        return createPostResultVO;
    }


    public PostVO getPostById(String userName, int postId, String appId, String openid) {

        PostDetailBO post = posterRepository.findByPostById(postId);
        long viewCount = postViewCounter.increment(post.getId(), post.getUserName(), post.isAnonymous(),
                appId == null ? null :
                new WechatUser(appId, openid));

        post.setViewCount(Long.valueOf(viewCount).intValue());
        PostVO postVO = getPostVO(post);

        postVO.setAuthor(userName.equals(postVO.getUserName()));
        setLikeCount(userName, postVO);
        return postVO;
    }

    private void setLikeCount(String userName, PostVO postVO) {
        if(!userName.equals("guest")) {
            boolean hasLike = likeCountService.hasLike(LikeType.Post, postVO.getId(), userName);
            postVO.setHasLike(hasLike);
        }
        long size = likeCountService.likeCount(LikeType.Post, postVO.getId());
        postVO.setLikeCount(size);
    }

    private PostVO getPostVO(PostDetailBO post) {
        PostVO postVO = new PostVO();

        postVO.setId(post.getId())
                .setContent(post.getContent())
                .setAllowComment(post.isAllowComment())
                .setAvatar(post.getShowAvatar())
                .setUserName(post.getUserName())
                .setViewCount(post.getViewCount())
                .setCommentCount(commentCountService.count(post.getId()))
                .setLikeCount(post.getLikeCount())
                .setShowUserName(post.getShowUserName())
                .setPostTime(post.getPostTime())
                .setIdentityCode(post.getIdentityCategory().getCode())
                .setAnonymous(post.getIdentityCategory().isAnonymous())
                .setHasDelete(post.isDelete())
                .setLastReplyTime(post.getLastReplyTime());

        List<ImageInfoVO> imageInfoVOList = post.getImageInfoList().stream().map(imageInfo -> {
            ImageInfoVO infoVO = new ImageInfoVO();
            infoVO.setFileId(imageInfo.getFileId());
            infoVO.setUrl(imageInfo.getPath());
            return infoVO;
        }).collect(Collectors.toList());

        postVO.setImageInfoList(imageInfoVOList);

        return postVO;
    }

    public PostDetailVO getPostDetail(String userName, Integer startId, int count, String appId, String openid) {
        List<PostDetailBO> detailBOList = posterRepository.findShowPost(startId, count);
        return getPostDetailVO(userName, appId, openid, detailBOList);
    }

    public PostDetailVO getRecentlyPost(String userName, Long timestamp, int count, String appId, String openid) {

        List<PostDetailBO> detailBOList =
                posterRepository.findShowPostByLastReply(timestamp == null ? null : new Date(timestamp), count);
        return getPostDetailVO(userName, appId, openid, detailBOList);
    }

    public PostDetailVO getRecommendPost(String userName, String appId, String openid) {
        List<PostDetailBO> detailBOList = communityPostApp.getRecommendPost();
        return getPostDetailVO(userName, appId, openid, detailBOList);
    }

    public CheckPostUpdateVO checkUpdate(long maxPostId) {
        long maxReleasePostId = posterRepository.findMaxReleasePostId();

        CheckPostUpdateVO vo = new CheckPostUpdateVO();
        vo.setMaxReleasePostId(maxReleasePostId);
        return vo;
    }


    private PostDetailVO getPostDetailVO(String userName, String appId, String openid, List<PostDetailBO> detailBOList) {
        for (PostDetailBO postDetailBO : detailBOList) {

            long viewCount = postViewCounter.increment(postDetailBO.getId(), postDetailBO.getUserName(),
                    postDetailBO.isAnonymous(), appId == null ? null : new WechatUser(appId, openid));

            postDetailBO.setViewCount(Long.valueOf(viewCount).intValue());
        }
        return getPostDetailVO(userName, detailBOList);
    }

    /**
     *
     * @param userName 贴子所属的用户名
     * @param postUserName 帖子所属的用户名
     * @param startId 帖子开始id
     * @param count 请求的数量
     * @return
     */
    public PostDetailVO getPostByUserName(String userName, String postUserName, Integer startId, int count) {
        List<PostDetailBO> detailBOList = posterRepository.findPostByUser(postUserName, startId, count);

        return getPostDetailVO(userName, detailBOList);

    }

    private PostDetailVO getPostDetailVO(String userName, List<PostDetailBO> detailBOList) {
        List<PostVO> postVOList = detailBOList.stream()
                .map(this::getPostVO)
                .collect(Collectors.toList());

        for (PostVO postVO : postVOList) {
            setLikeCount(userName, postVO);
            postVO.setAuthor(userName.equals(postVO.getUserName()));
        }

        PostDetailVO postDetailVO = new PostDetailVO();

        postDetailVO.setPostList(postVOList);

        postDetailVO.setCount(postVOList.size());

        long nextMaxId = postVOList.stream()
                .map(PostVO::getId)
                .min(Long::compareUnsigned)
                .orElse(-1L);

        long nextTimestamp = postVOList.stream()
                .map(x-> x.getLastReplyTime().getTime())
                .min(Long::compareUnsigned)
                .orElse(-1L);

        postDetailVO.setNextMaxId(nextMaxId);
        postDetailVO.setNextTimestamp(nextTimestamp);

        return postDetailVO;
    }

    public CreateCommentResultVO deletePostById(String userName, int postId) {
        PostDetailBO post = posterRepository.findByPostById(postId);
        CreateCommentResultVO resultVO = new CreateCommentResultVO();
        resultVO.setRelease(communityPostApp.deletePost(post, userName));
        return resultVO;

    }
}
