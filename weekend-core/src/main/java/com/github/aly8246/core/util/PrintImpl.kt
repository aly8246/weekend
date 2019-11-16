package com.github.aly8246.core.util

import com.github.aly8246.core.configuration.Configurations
import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/28 下午 02:43
 * @description：
 * @version: ：V
 */
class PrintImpl : Print {
    private fun thisTime(): String {
        try {
            return SimpleDateFormat(configuration.dataFormat!!).format(Date())
        } catch (e: Exception) {
        }
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
    }

    override fun info(msg: String) {
        println("Weekend INFO [${thisTime()}] >> ： $msg")
    }

    override fun debug(msg: String) {
        System.err.println("Weekend DEBUG [${thisTime()}] >> ： $msg")
    }

    override fun error(msg: String) {
        System.err.println("Weekend ERROR [${thisTime()}] >> ： $msg")
    }

    override fun warning(msg: String) {
        System.err.println("Weekend WARNING [${thisTime()}] >> ： $msg")
    }
}
