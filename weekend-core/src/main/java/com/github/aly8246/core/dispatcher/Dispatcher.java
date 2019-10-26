package com.github.aly8246.core.dispatcher;

import java.util.function.Consumer;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 06:16
 * @description：
 * @version: ：V
 */
public interface Dispatcher<T> {
        T exec();
}
