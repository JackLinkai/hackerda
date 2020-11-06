package com.hackerda.platform.infrastructure.database.dao;

import com.hackerda.platform.infrastructure.database.model.UrpClassroom;
import com.hackerda.platform.utils.DateUtils;
import com.hackerda.platform.utils.Term;
import com.hackerda.spider.UrpSearchSpider;
import com.hackerda.spider.support.search.SearchResult;
import com.hackerda.spider.support.search.classroom.SearchResultWrapper;
import com.hackerda.spider.support.search.emptyroom.EmptyRoomRecord;
import com.hackerda.spider.support.search.emptyroom.SearchEmptyRoomPost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JR Chan
 */
@Service
@CacheConfig(cacheNames = "empty_Room_data")
@Slf4j
public class EmptyRoomDao {

    @Autowired
    private UrpSearchSpider urpSearchSpider;

    /**
     * 从缓存中获取空教室信息，若redis中没有相关缓存，则爬取
     *
     * @param week     星期数
     * @param teaNum   教学楼编号
     * @param wSection 星期天数/节次
     * @return 返回包含空教室数据的list
     */
    @Cacheable(key = "#p0+#p1+#p2", unless = "#result == null")
    public List<UrpClassroom> getEmptyRoomReply(String week, String teaNum, String wSection) {

        log.info("爬取空教室缓存{} {} {}", week, teaNum, wSection);
        Term term = DateUtils.getCurrentSchoolTime().getTerm();
        SearchEmptyRoomPost emptyRoomPost = new SearchEmptyRoomPost(week, teaNum, wSection, "1", "200");
        emptyRoomPost.setExecutiveEducationPlanNumber(term.getExecutiveEducationPlanNum());
        List<SearchResult<EmptyRoomRecord>> resultList = urpSearchSpider.searchEmptyRoom(emptyRoomPost);
        SearchResult<EmptyRoomRecord> result = resultList.stream().findFirst().orElse(null);

        if(result == null) {
            return Collections.emptyList();
        }

        return result.getRecords().stream().map(x -> {
            UrpClassroom urpClassroom = new UrpClassroom();
            urpClassroom.setName(x.getClassroomName());
            urpClassroom.setNumber(x.getId().getClassroomNumber());
            return urpClassroom;

        }).collect(Collectors.toList());

    }
}
