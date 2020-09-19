package top.henton.utils;

import com.google.common.collect.Sets;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Test;

public class TimeUtilTest {

    @Test
    public void testGetResetTimes() {
        // 周一
        HashSet<Integer> day = Sets.newHashSet(1);
        // 五点
        HashSet<Integer> hours = Sets.newHashSet(5);
        // 2019-02-20 16:58:01 周三  2019-03-28 16:58:01 周四
        int resetTimes = TimeUtil.getResetTimes(1550653081000L, 1553763481000L, day, hours);
        Assert.assertEquals(5, resetTimes);
        // 2019-02-20 16:58:01 周三  2019-03-25 04:59:59 周一
        resetTimes = TimeUtil.getResetTimes(1550653081000L, 1553461199000L, day, hours);
        Assert.assertEquals(4, resetTimes);
        // 2019-02-17 16:58:01 周日  2019-03-25 04:59:59 周一
        resetTimes = TimeUtil.getResetTimes(1550393881000L, 1553461199000L, day, hours);
        Assert.assertEquals(5, resetTimes);
        // 2019-02-17 16:58:01 周日  2019-03-26 04:59:59 周二
        resetTimes = TimeUtil.getResetTimes(1550393881000L, 1553547599000L, day, hours);
        Assert.assertEquals(6, resetTimes);
        // 2019-02-18 06:58:01 周一  2019-03-26 04:59:59 周二
        resetTimes = TimeUtil.getResetTimes(1550444281000L, 1553547599000L, day, hours);
        Assert.assertEquals(5, resetTimes);
        // 2019-02-18 06:58:01 周一  2019-03-26 04:59:59 周二
        resetTimes = TimeUtil.getResetTimes(1550444281000L, 1553547599000L, day, hours);
        Assert.assertEquals(5, resetTimes);
        // 2019-03-28 16:58:01 周四  2019-02-20 16:58:01 周三
        resetTimes = TimeUtil.getResetTimes(1553763481000L, 1550653081000L, day, hours);
        Assert.assertEquals(0, resetTimes);
        // 2019-02-17 16:58:01 周日  2019-02-18 16:58:01 周一
        resetTimes = TimeUtil.getResetTimes(1550393881000L, 1550480281000L, day, hours);
        Assert.assertEquals(1, resetTimes);
        // 2019-02-18 04:59:00 周一  2019-02-18 05:00:00 周一
        resetTimes = TimeUtil.getResetTimes(1550437140000L, 1550437200000L, day, hours);
        Assert.assertEquals(1, resetTimes);
        // 2019-02-18 05:00:00 周一  2019-02-18 05:00:00 周一
        resetTimes = TimeUtil.getResetTimes(1550437200000L, 1550437200000L, day, hours);
        Assert.assertEquals(0, resetTimes);
        // 2019-02-18 05:00:00 周一  2019-02-18 05:30:00 周一
        resetTimes = TimeUtil.getResetTimes(1550437200000L, 1550439000000L, day, hours);
        Assert.assertEquals(0, resetTimes);
        // 2020-09-19 10:29:14 周六  2020-09-26 10:29:14 周六
        resetTimes = TimeUtil.getResetTimes(1600482554000L, 1601087354000L, day, hours);
        Assert.assertEquals(1, resetTimes);
        // 2020-09-21 05:00:00 周一  2020-09-28 05:00:00 周六
        resetTimes = TimeUtil.getResetTimes(1600635600000L, 1601240400000L, day, hours);
        Assert.assertEquals(1, resetTimes);
        // 2020-09-21 05:00:00 周一  2020-09-28 04:59:59 周一
        resetTimes = TimeUtil.getResetTimes(1600635600000L, 1601240399000L, day, hours);
        Assert.assertEquals(0, resetTimes);
        // 2020-09-21 06:00:00 周一  2020-09-28 05:00:00 周一
        resetTimes = TimeUtil.getResetTimes(1600639200000L, 1601240400000L, day, hours);
        Assert.assertEquals(1, resetTimes);
        // 2020-09-21 05:00:00 周一  2020-10-05 04:59:59 周一
        resetTimes = TimeUtil.getResetTimes(1600635600000L, 1601845199000L, day, hours);
        Assert.assertEquals(1, resetTimes);
    }

    @Test
    public void testGetCrossWeeks() {
        // 2019-07-11 15:33:36 周四  2019-07-14 15:33:36 周日
        int crossWeeks = TimeUtil.getCrossWeeks(1562830416000L, 1563089616000L);
        Assert.assertEquals(0, crossWeeks);
        // 2019-07-11 15:33:36 周四  2019-07-15 15:33:36 周一
        crossWeeks = TimeUtil.getCrossWeeks(1562830416000L, 1563176016000L);
        Assert.assertEquals(1, crossWeeks);
        // 2019-07-11 15:33:36 周四  2019-07-22 15:33:36 周一
        crossWeeks = TimeUtil.getCrossWeeks(1562830416000L, 1563780816000L);
        Assert.assertEquals(2, crossWeeks);
    }

}
