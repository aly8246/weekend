package com.github.aly8246.kotlin.pojo

import com.github.aly8246.core.annotation.Mapping

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/10/30 下午 03:13
 * @description：
 * @version:   ：V
 */
class UserInfo {
    @Mapping(name = ["userId"], dbName = ["id"])
    var userId: String? = null

    @Mapping(name = ["userName"], dbName = ["name"])
    var userName: String? = null
    var userMoney: Double? = null
    var age: Int? = null


    constructor()

    override fun toString(): String {
        return "UserInfo(id='$userId', name='$userName', userMoney=$userMoney, age=$age)"
    }

}