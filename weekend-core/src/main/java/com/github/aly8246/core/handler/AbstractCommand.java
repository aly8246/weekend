package com.github.aly8246.core.handler;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 12:54
 * @description：
 * @version: ：V
 */
public abstract class AbstractCommand implements Command {

@Override
public void validCommand() {
	System.out.println("1-AbstractCommand running");
}

}
