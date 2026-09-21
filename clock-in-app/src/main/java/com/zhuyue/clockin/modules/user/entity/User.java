package com.zhuyue.clockin.modules.user.entity;

import lombok.Builder;
import lombok.Data;

/* 
 * 用户实体类 
*/
@Data
@Builder
public class User {
    /** 用户ID */
    private long id;
    /** 用户名 */
    private String userName;
    /** 密码 */
    private String password;
    /** 性别：1 男，2 女，3 保密 */
    private Integer sex;
    /** 年龄 */
    private int age;
    /** 创建时间 */
    private String createTime;
    /** 更新时间 */
    private String updateTime;
    /** 手机号 */
    private String phone;
    /** 头像url */
    private String avatarUrl;
}