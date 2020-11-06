package com.hackerda.platform.infrastructure.community;

import com.google.common.collect.Lists;
import com.hackerda.platform.domain.community.IdentityCategory;
import com.hackerda.platform.domain.community.RecordStatus;
import com.hackerda.platform.infrastructure.database.mapper.ext.PostExtMapper;
import com.hackerda.platform.infrastructure.database.model.Post;
import com.hackerda.platform.infrastructure.database.model.PostExample;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("prod")
@SpringBootTest
public class IdentityCategoryRedisFilterScript {

    @Autowired
    private IdentityCategoryRedisFilter identityCategoryRedisFilter;
    @Autowired
    private PostExtMapper postExtMapper;


    @Test
    public void add() {

        identityCategoryRedisFilter.add(IdentityCategory.Anonymous);
        identityCategoryRedisFilter.add(IdentityCategory.College);
        identityCategoryRedisFilter.add(IdentityCategory.Grade);


        PostExample example = new PostExample();
        example.createCriteria()
                .andRecordStatusEqualTo(RecordStatus.Release.getCode())
                .andIdentityCodeIn(Lists.newArrayList(IdentityCategory.Anonymous.getCode(),
                        IdentityCategory.College.getCode(), IdentityCategory.Grade.getCode()));


        List<Post> posts = postExtMapper.selectByExample(example);

        for (Post post : posts) {
            post.setRecordStatus(RecordStatus.Hide.getCode());
            postExtMapper.updateByPrimaryKeySelective(post);

        }

        System.out.println(identityCategoryRedisFilter.userChooseFilter(IdentityCategory.Anonymous));
    }

    @Test
    public void remove() {

        identityCategoryRedisFilter.remove(IdentityCategory.Anonymous);
        identityCategoryRedisFilter.remove(IdentityCategory.College);
        identityCategoryRedisFilter.remove(IdentityCategory.Grade);
        System.out.println(identityCategoryRedisFilter.userChooseFilter(IdentityCategory.Anonymous));


        PostExample example = new PostExample();
        example.createCriteria()
                .andRecordStatusEqualTo(RecordStatus.Hide.getCode())
                .andIdentityCodeIn(Lists.newArrayList(IdentityCategory.Anonymous.getCode(),
                        IdentityCategory.College.getCode(), IdentityCategory.Grade.getCode()));



        List<Post> posts = postExtMapper.selectByExample(example);

        for (Post post : posts) {
            post.setRecordStatus(RecordStatus.Release.getCode());
            postExtMapper.updateByPrimaryKeySelective(post);

        }
    }

}