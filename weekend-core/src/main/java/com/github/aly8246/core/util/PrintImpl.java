package com.github.aly8246.core.util;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/28 下午 02:43
 * @description：
 * @version: ：V
 */
public class PrintImpl implements Print {
@Override
public void info(String msg) {
	System.out.println("Weekend  >> ： " + msg);
}

@Override
public void debug(String msg) {
	System.err.println("Weekend  >> ： " + msg);
}
}
