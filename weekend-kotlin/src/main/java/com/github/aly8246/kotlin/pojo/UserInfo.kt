package com.github.aly8246.kotlin.pojo

import com.github.aly8246.core.annotation.Mapping
import com.github.aly8246.core.annotation.WeekendId

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/10/30 下午 03:13
 * @description：
 * @version:   ：V
 */
class UserInfo {
    @Mapping(name = ["id"], type = String::class)
    @WeekendId
    var userId: String? = null

    @Mapping(name = ["name"], type = String::class)
    var userName: String? = null
    var userMoney: Double? = null

    @Mapping(name = ["age"], type = Long::class)
    var userAge: Int? = null


    constructor()

    override fun toString(): String {
        return "UserInfo(id='$userId', name='$userName', userMoney=$userMoney, age=$userAge)"
    }

}