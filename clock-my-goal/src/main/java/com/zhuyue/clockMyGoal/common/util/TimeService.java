package com.zhuyue.clockMyGoal.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;

/**
 * 时间工具类
 * 提供时间相关的格式化，计算等功能
 */
public class TimeService {
  /**
   * 获取当前时间字符串
   * @return 当前时间字符串
   */
  public static String getCurrentTimeString() {
    String nowDateTimeString = getTimeStringByTime(LocalDateTime.now());
    return nowDateTimeString;
  }

  /**
   * 获取当前时间戳
   * @return 当前时间戳
   */
  public static long getCurrentTimestamp() {
    LocalDateTime now = LocalDateTime.now();
    long tsMs = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    return tsMs;
  }

  /**
   * 按格式化时间字符串转换为LocalDateTime
   * @param timeString 时间字符串
   * @return LocalDateTime
   */
  public static LocalDateTime getTimeByTimeString(String timeString) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime time = LocalDateTime.parse(timeString, dtf);
    return time;
  }

  /**
   * 按LocalDateTime转换为时间字符串
   * @param timeString 时间字符串
   * @return LocalDateTime
   */
  public static String getTimeStringByTime(LocalDateTime time) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String timeString = dtf.format(time);
    return timeString;
  }

  /**
   * 计算两个时间之间的差值
   * @param startTime 开始时间
   * @param endTime 结束时间
   * @return 差值
   */
  public static long getTimeDifference(int cycle, LocalDateTime startTime, LocalDateTime endTime) {
    switch (cycle) {
      case 1:
        return ChronoUnit.DAYS.between(startTime, endTime);
      case 2:
        return ChronoUnit.WEEKS.between(startTime, endTime);
      case 3:
        return ChronoUnit.MONTHS.between(startTime, endTime);
      default:
        return ChronoUnit.DAYS.between(startTime, endTime);
    }
  }
  
  /**
   * 判断是否是同一周
   * @param date1
   * @param date2
   * @return
   */
  public static boolean isSameWeek(LocalDateTime date1, LocalDateTime date2) {
    WeekFields ISO_WEEK = WeekFields.ISO;
    int week1 = date1.get(ISO_WEEK.weekOfWeekBasedYear());
    int week2 = date2.get(ISO_WEEK.weekOfWeekBasedYear());
    int year1 = date1.get(ISO_WEEK.weekBasedYear());
    int year2 = date2.get(ISO_WEEK.weekBasedYear());
    return week1 == week2 && year1 == year2;
  }
}
