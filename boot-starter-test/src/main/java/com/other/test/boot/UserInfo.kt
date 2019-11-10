package com.other.test.boot

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/10/30 下午 03:13
 * @description：
 * @version:   ：V
 */
class UserInfo {
    var id: String? = null
    var name: String? = null
    var userMoney: Double? = null
    var age: Int? = null

    constructor(id: String, name: String, userMoney: Double, age: Int) {
        this.id = id
        this.name = name
        this.userMoney = userMoney
        this.age = age
    }

    constructor()

    override fun toString(): String {
        return "UserInfo(id='$id', name='$name', userMoney=$userMoney, age=$age)"
    }

}