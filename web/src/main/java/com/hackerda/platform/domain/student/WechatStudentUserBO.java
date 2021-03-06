package com.hackerda.platform.domain.student;

import com.hackerda.platform.domain.WechatPlatform;
import com.hackerda.platform.domain.constant.ErrorCode;
import com.hackerda.platform.domain.wechat.WechatUser;
import com.hackerda.platform.exception.BusinessException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@ToString(callSuper = true)
public class WechatStudentUserBO extends StudentUserBO{

    private Map<String, WechatUser> wechatUserMap = new HashMap<>(0);

    @EqualsAndHashCode.Exclude
    private Set<WechatUser> originWechatUserSet = Collections.emptySet();

    public boolean hasBindApp() {
        return getAppOpenid() != null;
    }


    public void setBindWechatUser(List<WechatUser> wechatUserList) {
        originWechatUserSet = new HashSet<>(wechatUserList);

        this.wechatUserMap = wechatUserList.stream().collect(Collectors.toMap(WechatUser::getAppId,
                wechatUser -> wechatUser));
    }


    public String getAppOpenid(){
        return "";
    }


    public String getOpenid(String appId) {
        if(wechatUserMap.isEmpty()) {
            return "";
        }
        return wechatUserMap.get(appId).getOpenId();
    }


    public boolean hasBindApp(String appId) {
        return wechatUserMap.containsKey(appId);
    }

    public boolean hasBindWechatUser(WechatUser wechatUser) {
        return originWechatUserSet.contains(wechatUser) || wechatUserMap.containsValue(wechatUser);
    }

    public boolean hasBindWechatUser() {
        return !originWechatUserSet.isEmpty() || !wechatUserMap.isEmpty();
    }

    public void bindWechatUser(WechatUser wechatUser) {
        if(wechatUserMap.containsKey(wechatUser.getAppId())) {
            throw new BusinessException(ErrorCode.ACCOUNT_HAS_BIND, "微信"+ wechatUser.toString()+ "无法绑定此学号"+super.getAccount());
        }
        wechatUserMap.put(wechatUser.getAppId(), wechatUser);
    }

    public WechatUser revokeWechatUser(String appId) {
        WechatUser wechatUser = wechatUserMap.remove(appId);
        if(wechatUser == null) {
            log.error("{} have`t bin appId {}", this, appId);
        }
        return wechatUser;
    }

    public List<WechatUser> getNewBindWechatUser() {
        return wechatUserMap.values().stream()
                .filter(x-> !originWechatUserSet.contains(x))
                .collect(Collectors.toList());
    }

    public List<WechatUser> getRevokeWechatUser() {
        return originWechatUserSet.stream()
                .filter(x-> !wechatUserMap.containsValue(x))
                .collect(Collectors.toList());
    }

}
