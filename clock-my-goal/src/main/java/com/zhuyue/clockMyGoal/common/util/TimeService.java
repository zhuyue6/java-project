package com.zhuyue.clockMyGoal.common.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;

/**
 * 时间工具类
 * 提供时间相关的格式化，计算等功能
 */
public class TimeService {
  /**
   * 获取当前日期时间字符串
   * @return 当前日期时间字符串
   */
  public static String getCurrentDateTimeString() {
    String nowDateTimeString = getDateTimeStringByDateTime(LocalDateTime.now());
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
   * 按格式化日期时间字符串转换为LocalDateTime
   * @param dateTimeString 日期时间字符串
   * @return LocalDateTime
   */
  public static LocalDateTime getDateTimeByDateTimeString(String dateTimeString) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, dtf);
    return dateTime;
  }

  /**
   * 按LocalDateTime转换为日期时间字符串
   * @param dateTime 日期时间
   * @return 日期时间字符串
   */
  public static String getDateTimeStringByDateTime(LocalDateTime dateTime) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String dateTimeString = dtf.format(dateTime);
    return dateTimeString;
  }

  /**
   * 按LocalTime转换为时间字符串
   * @param time 时间
   * @return 时间字符串
   */
  public static String getTimeStringByTime(LocalTime time) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    String timeString = dtf.format(time);
    return timeString;
  }

  /**
   * 按时间字符串转换为LocalTime
   * @param timeString 时间字符串
   * @return LocalTime
   */
  public static LocalTime getTimeByTimeString(String timeString) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    LocalTime time = LocalTime.parse(timeString, dtf);
    return time;
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

  public static LocalDateTime addCycleTime(LocalDateTime createTime, int cycle, int cycleCount) {
    if (createTime == null) {
      return createTime;
    }

    switch (cycle) {
      case 1:
        return createTime.plusDays(cycleCount * 1);
      case 2:
        return createTime.plusDays(cycleCount * 7);
      case 3:
        return createTime.plusDays(cycleCount * 30);
      default:
        return createTime;
    }
  }

  /**
   * 获取当前时间 + 周期的时间段
   * @param cycle
   * @return
   */
  public static LocalDateTime[] getCycleCurrentTimeDuration (int cycle) {
    LocalDateTime[] cycleTimeList = new LocalDateTime[2];
    LocalDateTime currentDateTime = LocalDateTime.now();
    

    switch (cycle) {
      case 1:
        // 获取当天时间范围
        int year = currentDateTime.getYear();
        int month = currentDateTime.getMonthValue();
        int day = currentDateTime.getDayOfMonth();
        cycleTimeList[0] = LocalDateTime.of(year, month, day, 0, 0, 0);
        cycleTimeList[1] = LocalDateTime.of(year, month, day, 23, 59, 59);
        break;
      case 2:
        // 获取当周时间范围
        cycleTimeList[0] = currentDateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.MIN);
        cycleTimeList[1] = currentDateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX);
        break;
      case 3:
        // 获取当月时间范围
        cycleTimeList[0] = currentDateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        cycleTimeList[1] = currentDateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        break;
    }

    return cycleTimeList;
  }
}
