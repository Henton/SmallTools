package tools;

import java.util.Set;
import org.joda.time.DateTime;

public class TimeUtil {

    public static final int SECOND = 1000;

    public static final int MINUTE = SECOND * 60;

    public static final int HOUR = MINUTE * 60;

    public static final int DAY = HOUR * 24;

    public static final int WEEK = DAY * 7;

    /**
     * 获取两个时间戳之间经过了几个时间点（时间点为周几几点）
     * 
     * @param oldTime
     * @param nowTime
     * @param day
     * @param hours
     * @return
     */
    public static int getResetTimes(long oldTime, long nowTime, Set<Integer> day, Set<Integer> hours) {
        long crossMs = nowTime - oldTime;
        if (crossMs <= 0) {
            return 0;
        }
        int result = 0;
        DateTime old = new DateTime(oldTime);
        DateTime now = new DateTime(nowTime);
        int i = old.getDayOfWeek();
        int j = now.getDayOfWeek();
        int k = old.getHourOfDay();
        int l = now.getHourOfDay();
        int originI = i;
        int originJ = j;
        // 跨过了多少个24小时
        int crossDays = (int) (crossMs / DAY);
        // 跨过了多少周，若oldTime和nowTime同一天（如同是周一、周二）且oldTime的当前小时数大于nowTime则周数加一方便计算，后面进行修正
        int crossWeeks = ((i == j && k >= l && crossDays > 0) ? crossDays / 7 + 1 : crossDays / 7);
        // 根据跨过多少周计算有多少个指定时间点（周几几点）,因为是经过了7*24小时，因此不要考虑是周几几点，每周必定会有day.size() * hours.size()个时间点
        result += crossWeeks * day.size() * hours.size();
        // 不是同一天则追到同一天并累计时间点个数（因不一定每天都包含时间点，因此需要循环判断）
        while (i != j) {
            if (day.contains(i)) {
                if(i == originI){
                    for (int temp = k + 1; temp < 24; ++temp) {
                        if (hours.contains(temp)) {
                            result = result + 1;
                        }
                    }
                }else {
                    result += hours.size();
                }
            }
            i = i % 7 + 1;
        }
        // 重置时间点不包含今天则直接返回结果
        if (!day.contains(j)) {
            return result;
        }
        // 重置时间点包含今天，进行修正
        // 初始天数不同，计算0点到现在小时的重置次数即可
        if (originI != originJ) {
            for (int temp = 0; temp <= l; ++temp) {
                if (hours.contains(temp)) {
                    result = result + 1;
                }
            }
            return result;
        }
        // 初始天数相同，oldTime的当前小时数小于nowTime则之前的计算少算了n个小时的时间，需要加上
        if (k < l) {
            // k为oldTime当前小时，是过去的时间已经计算过不需要再加
            for (k = k + 1; k <= l; ++k) {
                if (hours.contains(k)) {
                    result = result + 1;
                }
            }
            return result;
        }
        // 初始天数相同，oldTime的当前小时数大于nowTime则之前的计算多算了n个小时的时间，需要减去
        if (k >= l) {
            // l为nowTime当前小时，当前已经达到该时间不需要减去
            for (l = l + 1; l <= k; ++l) {
                if (hours.contains(l)) {
                    result--;
                }
            }
        }
        return result;
    }

    /**
     * 获取两个时间戳之间经过了几周（自然周）
     *
     * @param oldTime
     * @param newTime
     * @return
     */
    public static int getCrossWeeks(long oldTime, long newTime) {
        DateTime dateTime = new DateTime(oldTime);
        DateTime withMillisOfDay = dateTime.withDayOfWeek(1).withMillisOfDay(0);
        long millis = withMillisOfDay.getMillis();
        return (int) ((newTime - millis) / WEEK);
    }

}
