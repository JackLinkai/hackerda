package com.hackerda.platform.controller.auth;

import com.hackerda.platform.aggregator.UserInfoAggregator;
import com.hackerda.platform.controller.WebResponse;
import com.hackerda.platform.controller.vo.StudentUserDetailVO;
import com.hackerda.platform.controller.vo.UserInfoVO;
import com.hackerda.platform.service.rbac.UserAuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author JR Chan
 */
@RestController
@RequestMapping("/authz")
public class UserAuthorizeController {

    @Autowired
    private UserAuthorizeService userAuthorizeService;
    @Autowired
    private UserInfoAggregator userInfoAggregator;

    @RequestMapping(value = "/student", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse<StudentUserDetailVO> studentAuthz(@RequestParam("account") String account,
                                                         @RequestParam("password") String password,
                                                         @RequestParam(value = "appid", required = false) String appid,
                                                         @RequestParam(value = "openid", required = false) String openid){


        return WebResponse.success(userAuthorizeService.studentAuthorize(account, password, appid, openid));
    }


    @RequestMapping(value = "/user", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse<UserInfoVO> userAuthz(@RequestParam("account") String account,
                                             @RequestParam("password") String password,
                                             @RequestParam(value = "appid", required = false) String appid,
                                             @RequestParam(value = "openid", required = false) String openid){


        return WebResponse.success(userInfoAggregator.studentAuthorize(account, password, appid, openid));
    }

    @RequestMapping(value = "/commonUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse<UserInfoVO> authCommonWechatUser(@RequestParam("account") String account,
                                                                 @RequestParam("phoneNumber") String phoneNumber,
                                                                 @RequestParam(value = "appid", required = false) String appId,
                                                                 @RequestParam(value = "openid", required = false) String openid){


        return WebResponse.success(userInfoAggregator.bindCommonWechatUser(account, phoneNumber, appId, openid));
    }

    @RequestMapping(value = "/commonStudent", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse<StudentUserDetailVO> bindCommonWechatUser(@RequestParam("account") String account,
                                                         @RequestParam("phoneNumber") String phoneNumber,
                                                         @RequestParam(value = "appid", required = false) String appId,
                                                         @RequestParam(value = "openid", required = false) String openid){


        return WebResponse.success(userAuthorizeService.bindCommonWechatUser(account, phoneNumber, appId, openid));
    }


    @RequestMapping(value = "/app/student", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse<StudentUserDetailVO> appStudentAuthz(@RequestParam("account") String account,
                                                            @RequestParam("password") String password,
                                                            @RequestParam(value = "code") String code,
                                                            @RequestParam(value = "appId") String appId){


        return WebResponse.success(userAuthorizeService.appStudentAuthorize(account, password, appId, code));
    }

}
