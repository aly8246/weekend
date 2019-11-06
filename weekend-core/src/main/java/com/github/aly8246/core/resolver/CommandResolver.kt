package com.github.aly8246.core.resolver

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/11/06 下午 03:08
 * @description：
 * @version:   ：V
 */
interface CommandResolver {
    fun run(baseCommand: String): Operation
}