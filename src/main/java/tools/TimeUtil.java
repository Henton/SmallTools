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
        if (oldTime >= nowTime) {
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
        long crossMs = nowTime - oldTime;
        // 跨过了多少个24小时
        int crossDays = (int) (crossMs / DAY);
        // 跨过了多少周，若oldTime和nowTime同一天（如同是周一、周二）且oldTime的当前小时数大于nowTime则周数加一方便计算，后面进行修正
        int crossWeeks = ((i == j && k > l) ? crossDays / 7 + 1 : crossDays / 7);
        // 根据跨过多少周计算有多少个指定时间点（周几几点）,因为是经过了7*24小时，因此不要考虑是周几几点，每周必定会有day.size() * hours.size()个时间点
        result += crossWeeks * day.size() * hours.size();
        // 不是同一天则追到同一天并累计时间点个数（因不一定每天都包含时间点，因此需要循环判断）
        while (i != j) {
            if (day.contains(i)) {
                result += hours.size();
            }
            i = i % 7 + 1;
        }
        // 重置时间点不包含今天则直接返回结果
        if (!day.contains(i) && !day.contains(originI)) {
            return result;
        }
        // 重置时间点包含今天，且oldTime的当前小时数大于nowTime，追加今天的重置次数，后边再修正
        if (day.contains(i) && originI != originJ && k > l) {
            result += hours.size();
        }
        // 开始修正
        // oldTime的天数为重置天数，且nowTime与oldTime初始天数不同，则减去多计算的重置次数
        if (day.contains(originI) && originI != originJ) {
            for (int temp = 0; temp <= k; ++temp) {
                if (hours.contains(temp)) {
                    result = result - 1;
                }
            }
        }
        // 重置时间点包含今天，进行修正
        // 到此都在同一天如同是周一、周二）、且oldTime的当前小时数大于nowTime则之前的计算多算了n个小时的时间，需要减去
        if (day.contains(i) && k > l) {
            // l为nowTime当前小时，当前已经达到该时间不需要减去
            for (l = l + 1; l <= k; ++l) {
                if (hours.contains(l)) {
                    result--;
                }
            }
        }
        // oldTime的当前小时数小于nowTime则之前的计算少算了n个小时的时间，需要加上
        if (day.contains(i) && k <= l) {
            // k为oldTime当前小时，是过去的时间已经计算过不需要再加
            for (k = k + 1; k <= l; ++k) {
                if (hours.contains(k)) {
                    result++;
                }
            }
        }
        return result;
    }

}