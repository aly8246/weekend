package com.github.aly8246.core.util

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/28 下午 02:43
 * @description：
 * @version: ：V
 */
class PrintImpl : Print {
    override fun info(msg: String) {
        println("Weekend  >> ： $msg")
    }

    override fun debug(msg: String) {
        System.err.println("Weekend  >> ： $msg")
    }
}
