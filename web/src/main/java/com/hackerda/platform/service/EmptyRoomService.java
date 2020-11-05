package com.hackerda.platform.service;

import com.hackerda.platform.infrastructure.database.dao.EmptyRoomDao;
import com.hackerda.platform.infrastructure.database.model.EmptyRoom;
import com.hackerda.platform.controller.vo.EmptyRoomVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hackerda.platform.infrastructure.database.model.UrpClassroom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author Xie
 * @date 2019/9/18
 */
@Slf4j
@Service
public class EmptyRoomService {

    @Autowired
    private EmptyRoomDao emptyRoomDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    private Map<String,ReentrantLock>lockMap=new HashMap<>();

    /**
     * 对数据进行楼层筛选,如果楼层为0，则不进行筛选
     *
     * @param week      星期数
     * @param teaNum    教学楼编号
     * @param dayOfWeek 星期天数
     * @param order     节次
     * @param floor     楼层
     * @return 返回经过筛选楼层后的空教室数据的list
     */
    public List<UrpClassroom> getEmptyRoomReply(String week, String teaNum, int dayOfWeek, int order, int floor) {
        // 注解@Cacheable是使用AOP代理实现的 ，通过创建内部类来代理缓存方法
        // 类内部的方法调用类内部的缓存方法不会走代理，使得cacheable注解失效，
        // 所以就不能正常创建缓存，因此需要一个代理对象来调用
        String wSection = dayOfWeek + "/" + order;
        List<UrpClassroom> emptyRoom = emptyRoomDao.getEmptyRoomReply(week, teaNum, wSection);
        String key = "empty_Room_data::" + week + teaNum + wSection;
        //对数据缓存24小时，重复查询会更新这个数据的过期时间
        stringRedisTemplate.expire(key, 24L, TimeUnit.HOURS);

        return emptyRoom;
    }


    /**
     * 提供不分节次的全量查询
     *
     * @param week
     * @param teaNum
     * @param dayOfWeek
     * @param floor
     * @return
     */
	public List<EmptyRoomVo> getFullEmptyRoomReply(String week, String teaNum, int dayOfWeek, int floor) {
	  try { 
			ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
			Map<String, EmptyRoom> classRoomMap = new HashMap<>();
			List<Integer> orderList = Lists.newArrayList(1, 3, 5, 7, 9);
			//加锁部分。
			if (stringRedisTemplate.keys("empty_Room_data::" + week + teaNum + "*").size() == 0) {
				if (!lockMap.containsKey(week + teaNum)) {
					synchronized (this) {
						if (!lockMap.containsKey(week + teaNum))
							lockMap.put(week + teaNum, new ReentrantLock());
					}
				}
				lockMap.get(week + teaNum).lock();
			}
			for (int order : orderList) {
				List<UrpClassroom> emptyRoomReply = getEmptyRoomReply(week, teaNum, dayOfWeek, order, floor);
				for (UrpClassroom room : emptyRoomReply) {
					if (classRoomMap.containsKey(room.getNumber())) {
						classRoomMap.get(room.getNumber()).addOrder(order);
					} else {
						classRoomMap.put(room.getNumber(), new EmptyRoom(room));
					}
				}
			}
			return classRoomMap.values().stream()
					.map(emptyRoom -> new EmptyRoomVo(emptyRoom.getRoom(), emptyRoom.getOrderList()))
					.sorted(Comparator.comparing(o -> o.getUrpClassroom().getNumber())).collect(Collectors.toList());

		} finally {
			//finally语句块解锁
			if (lockMap.get(week + teaNum) != null && lockMap.get(week + teaNum).isLocked())
				lockMap.get(week + teaNum).unlock();
		}
	}

}

