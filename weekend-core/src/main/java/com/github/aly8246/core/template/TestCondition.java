package com.github.aly8246.core.template;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/11/12 下午 05:37
 * @description：
 * @version: ：V
 */
public class TestCondition {
    private final static String aa = "select * from user_info where userId=#{userId} and " +
            "when(moneyType){" +
            "is 1 -> money_type_name = '人民币}}'" +
            "   is 2 -> money_type_name = '美元'" +
            "} or age < 35 and" +
            "when(moneyType){" +
            "is 1 -> money_type_name = '人民币}}'" +
            "   is 2 -> money_type_name = '美元 when(moneyType){" +
            "is 1 -> money_type_name = '人民币'" +
            "   is 2 -> money_type_name = '美元'" +
            "}'" +
            "} or age < 35 and" +
            "when(moneyType){" +
            "is 1 -> money_type_name = '人民币}}'" +
            "   is 2 -> money_type_name = '美元'" +
            "} ";

    public static void main(String[] args) {
        Matcher matcher = Pattern.compile("when\\([a-zA-Z0-9]+\\)\\{*.[\\s\\S]+?\\}\\s+(?!.?')").matcher(aa);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }
}
