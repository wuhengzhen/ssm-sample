package com.test.utils;


import com.google.common.collect.ImmutableMap;
import jodd.datetime.JDateTime;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


/**
 * 日期工具类, 通过静态继承的方式, 扩展common-lang3中的DateUtils
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static final Map<Pattern, String> PATTERN_MAP = ImmutableMap.<Pattern, String>builder()
            .put(Pattern.compile("^\\d{8}$"), "yyyyMMdd")
            .put(Pattern.compile("^\\d{14}$"), "yyyyMMddHHmmss")
            .put(Pattern.compile("^\\d{12}$"), "yyyyMMddHHmm")
            .put(Pattern.compile("^\\d{10}$"), "yyyyMMddHH")
            .put(Pattern.compile("^\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1,3}$"), "y-M-d H:m:s.S")
            .put(Pattern.compile("^\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$"), "y-M-d H:m:s")
            .put(Pattern.compile("^\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}$"), "y-M-d H:m")
            .put(Pattern.compile("^\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}$"), "y-M-d H")
            .put(Pattern.compile("^\\d{4}\\-\\d{1,2}\\-\\d{1,2}$"), "y-M-d")
            .put(Pattern.compile("^\\d{2}:\\d{1,2}:\\d{1,2}\\.\\d{1,3}$"), "H:m:s.S")
            .put(Pattern.compile("^\\d{2}:\\d{1,2}:\\d{1,2}$"), "H:m:s")
            .put(Pattern.compile("^\\d{2}:\\d{1,2}$"), "H:m")
            .build();


    /**
     * 日期规则定义
     */
    public static final String[] DATE_PATTERN = new String[]{
            //编号0
            "yyyyMMdd",
            //编号1
            "yyyyMMddHHmmss",
            //编号2
            "yyyyMMddHHmm",
            //编号3
            "yyyy-MM-dd HH:mm:ss",
            //编号4
            "yyyyMMdd HH:mm",
            //编号5
            "yyyy-MM-dd",
            //编号6
            "mm:ss",
            //编号7
            "yyyy-MM-dd HH:mm"
    };


    /**
     * 自动解析日期/时间字符串, 此方法会自动识别日期格式.
     *
     * @param date 任意格式日期时间字符串, 具体参见{PATTERN_MAP}
     * @return 日期
     */
    public static Date autoParseDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        Date result = null;
        for (Pattern pattern : PATTERN_MAP.keySet()) {
            if (pattern.matcher(date).matches()) {
                String tmp = PATTERN_MAP.get(pattern);
                try {
                    result = parseDate(date, tmp);
                } catch (ParseException e) {
                    throw new RuntimeException("解析[" + tmp + "]格式的日期字符串[" + date + "]失败", e);
                }
            }
        }
        if (result == null) {
            throw new RuntimeException("无法识别日期字符串的格式: " + date);
        }
        return result;
    }


    /**
     * 将日期yyyy-MM-dd字符串解析为日期对象, 解析失败则返回null
     * 注意! 此方法会吞掉异常!
     *
     * @param date 日期字符串
     * @return 日期实例或null
     */
    public static Date parseDate(String date) {
        try {
            return parseDate(date, 5);
        } catch (ParseException e) {
            return null;
        }
    }


    public static Date parseDate(String date, int index) throws ParseException {
        return parseDate(date, DATE_PATTERN[index]);
    }


    /**
     * 将时间yyyy-MM-dd HH:mm:ss字符串解析为日期对象, 解析失败则返回null
     *
     * @param date 日期字符串
     * @return 日期实例或null
     */
    public static Date parseDateTime(String date) {
        Date result;
        try {
            result = parseDate(date, DATE_PATTERN[3]);
        } catch (ParseException e) {
            result = null;
        }
        return result;
    }


    /**
     * 获取今天的日期, 不包括时间. 如yyyy-MM-dd 00:00:00.000
     *
     * @return yyyy-MM-dd 00:00:00.000
     */
    public static Date todayDate() {
        Calendar result = Calendar.getInstance();
        result.set(Calendar.HOUR_OF_DAY, 0);
        result.set(Calendar.MINUTE, 0);
        result.set(Calendar.SECOND, 0);
        result.set(Calendar.MILLISECOND, 0);
        return result.getTime();
    }


    /**
     * 根据开始日期和结束日期, 计算日期差.
     * 若日期字符串格式非法, 则返回-1
     *
     * @param from 开始日期
     * @param to   结束日期
     * @return 两个日期之间的天数
     */
    public static int getDays(String from, String to) {
        Date fromDate = parseDate(from);
        Date toDate = parseDate(to);
        int days;
        if (fromDate != null && toDate != null) {
            days = getDays(fromDate, toDate);
        } else {
            days = -1;
        }
        return days;
    }


    /**
     * 根据开始日期和结束日期, 计算日期差
     *
     * @param from 开始日期
     * @param to   结束日期
     * @return 两个日期之间的天数
     */
    public static int getDays(Date from, Date to) {
        int days = 0;
        if (from.before(to)) {
            while (from.before(to)) {
                days++;
                from = addDays(from, 1);
            }
        } else {
            days = -1;
        }
        return days;
    }


    /**
     * 获取时间段(闭区间)内的每一天, 日期均以yyyy-MM-dd格式字符串表示
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 日期段内的每一天, 若日期格式不合法则返回空结果
     */
    public static Set<String> getEveryDay(String start, String end) {
        Set<String> result = new TreeSet<String>();
        Date startDate = parseDate(start);
        Date endDate = parseDate(end);
        if (startDate != null && endDate != null && startDate.getTime() <= endDate.getTime()) {
            Set<Date> allDate = getEveryDay(startDate, endDate);
            for (Date date : allDate) {
                result.add(format(date, DATE_PATTERN[5]));
            }
        }
        return result;
    }


    /**
     * 获取时间段(闭区间)内的每一天
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 日期段内的每一天
     */
    public static Set<Date> getEveryDay(Date start, Date end) {
        Set<Date> result = new HashSet<Date>();
        while (start.getTime() <= end.getTime()) {
            result.add(start);
            start = addDays(start, 1);
        }
        return result;
    }


    /**
     * 在指定日期基础上增加day
     */
    public static String addDays(String date, int days) {
        Date tmp = parseDate(date);
        if (tmp == null) {
            return null;
        }
        tmp = addDays(tmp, days);
        return DateFormatUtils.format(tmp, DATE_PATTERN[5]);
    }


    /**
     * 根据今天的时间 添加 numDay 天的时间
     *
     * @param numDay 要改变的 天数
     * @return
     */
    public static Date addDays(Date date, int numDay) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // 把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.DATE, numDay);
        Date tempDay = calendar.getTime();
        return tempDay;
    }


    /**
     * 根据今天的时间 添加 numDay 天的时间
     *
     * @param numDay 要改变的 天数
     * @return
     */
    public static Date addDays(int numDay) {
        // 取时间
        Date todayDate = DateUtils.todayDate();
        return addDays(todayDate, numDay);
    }


    /**
     * 将日期对象格式化为yyyy-MM-dd格式字符串
     *
     * @param date 日期对象
     * @return yyyy-MM-dd格式字符串
     */
    public static String format(Date date) {
        if (date == null) {
            return null;
        }
        return DateFormatUtils.format(date, DATE_PATTERN[5]);
    }


    /**
     * 将日期对象格式化为yyyy-MM-dd HH:mm:ss格式字符串
     *
     * @param date 日期对象
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return DateFormatUtils.format(date, DATE_PATTERN[3]);
    }


    /**
     * 将日期格式化为指定格式字符串
     *
     * @param date    日期对象
     * @param pattern 字符串格式
     * @return 日期字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return DateFormatUtils.format(date, pattern);
    }


    /**
     * 获得当前日期
     *
     * @return
     */
    public static String getCurrentDate() {
        JDateTime jdt = new JDateTime();
        return jdt.toString("YYYY-MM-DD");
    }

    /**
     * description : 获取当前时间（格式：YYYY-MM-DD hh:mm:ss）
     * author : wuhengzhen
     * date : 2018-11-6 16:35
     */
    public static String getCurrentDateTime() {
        JDateTime jdt = new JDateTime();
        return jdt.toString("YYYY-MM-DD hh:mm:ss");
    }

    /**
     * description :获取当前时间（格式：YYYY-MM-DD hh:mm:ss:mss）
     * author : wuhengzhen
     * date : 2018-11-6 16:35
     */
    public static String getCurrentDateTimeMillis() {
        JDateTime jdt = new JDateTime();
        return jdt.toString(JDateTime.DEFAULT_FORMAT);
    }

    /**
     * 将指定的日期转换为YYYY-MM-DD格式
     *
     * @param dateStr
     * @return
     */
    public static Date getDateByStr(String dateStr) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("wrong date format, should be " + pattern);
        }

        return date;
    }

    public static void main(String[] args) {
        System.out.println(getCurrentDateTime());
    }
}
