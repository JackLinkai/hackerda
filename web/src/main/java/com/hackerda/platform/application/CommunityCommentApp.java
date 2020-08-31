package com.hackerda.platform.application;

import com.hackerda.platform.application.event.EventPublisher;
import com.hackerda.platform.domain.community.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EventListener;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommunityCommentApp {

    @Autowired
    private ContentSecurityCheckService contentSecurityCheckService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PosterRepository posterRepository;
    @Autowired
    private CommentCountService commentCountService;
    @Autowired
    private LikeCountService likeCountService;
    @Autowired
    private EventPublisher eventPublisher;


    public void addComment(CommentBO commentBO) {

        if (!contentSecurityCheckService.isSecurityContent(commentBO.getContent())) {
            commentBO.setStatus(RecordStatus.UnderReview);
            eventPublisher.addCommentEvent(commentBO, true);
        } else {
            commentBO.setStatus(RecordStatus.Release);
        }

        commentRepository.save(commentBO);
    }

    public void addLike(LikeBO likeBO) {
        LikeBO like = likeRepository.find(likeBO.getUserName(), likeBO.getLikeType(), likeBO.getTypeContentId());

        if (like != null) {
            like.setShow(likeBO.isShow());
            like.setLikeTime(likeBO.getLikeTime());
            likeRepository.update(like);
        } else if (likeBO.isShow()) {
            likeRepository.save(likeBO);
        }

        eventPublisher.addLikeEvent(likeBO);
    }

    public void countSynchronize() {

        long maxId = posterRepository.findShowPost(null, 1).stream().findFirst().get().getId() + 1;

        while (true) {
            List<PostDetailBO> showPost = posterRepository.findShowPost(Math.toIntExact(maxId), 100);
            if(showPost.size() == 0) {
                break;
            }

            maxId = showPost.stream()
                    .map(PostDetailBO::getId)
                    .min(Long::compareUnsigned)
                    .orElse(-1L);


            for (PostDetailBO detailBO : showPost) {
                List<CommentDetailBO> detailBOList = commentRepository.find(RecordStatus.Release, detailBO.getId());
                commentCountService.setCount(detailBO.getId(), detailBOList.size());

                List<LikeBO> show = likeRepository.findShow(LikeType.Post, detailBO.getId());

                likeCountService.setCount(LikeType.Post, detailBO.getId(),
                        show.stream().map(LikeBO::getUserName).collect(Collectors.toSet()));

                detailBO.setLikeCount(show.size());
                detailBO.setCommentCount(detailBOList.size());

                posterRepository.update(detailBO, detailBO.getId());

                for (CommentDetailBO commentDetailBO : detailBOList) {
                    List<LikeBO> commentLike = likeRepository.findShow(LikeType.Comment, commentDetailBO.getId());

                    likeCountService.setCount(LikeType.Comment, detailBO.getId(),
                            commentLike.stream().map(LikeBO::getUserName).collect(Collectors.toSet()));
                }
            }
        }
    }


}
