package com.hackerda.platform.domain.student;

public interface StudentInfoService {

    /**
     * 检查用户教务网的账号密码是否正确
     * 每次登录都会实时请求教务网验证密码是否正确
     * 如果教务网请求超时（通常为服务不可用），那么会查找数据库中的记录进行比对。
     *
     * @param account 教务网账号
     * @param enablePassword 教务网账号对应的明文密码
     * @return 密码是否正确
     */
    boolean checkPasswordValid(String account, String enablePassword);

    WechatStudentUserBO getStudentInfo(StudentAccount account, String enablePassword);

}
