package tools;

import java.util.Arrays;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Test;

public class TimeUtilTest {

    @Test
    public void testGetResetTimes() {
        // 周一
        HashSet<Integer> day = new HashSet(Arrays.asList(1));
        // 五点
        HashSet<Integer> hours = new HashSet(Arrays.asList(5));
        // 2019-02-20 16:58:01 周三  2019-03-28 16:58:01 周四
        int resetTimes = TimeUtil.getResetTimes(1550653081000L, 1553763481000L, day, hours);
        Assert.assertEquals(resetTimes, 5);
        // 2019-02-20 16:58:01 周三  2019-03-25 04:59:59 周一
        resetTimes = TimeUtil.getResetTimes(1550653081000L, 1553461199000L, day, hours);
        Assert.assertEquals(resetTimes, 4);
        // 2019-02-17 16:58:01 周日  2019-03-25 04:59:59 周一
        resetTimes = TimeUtil.getResetTimes(1550393881000L, 1553461199000L, day, hours);
        Assert.assertEquals(resetTimes, 5);
        // 2019-02-17 16:58:01 周日  2019-03-26 04:59:59 周二
        resetTimes = TimeUtil.getResetTimes(1550393881000L, 1553547599000L, day, hours);
        Assert.assertEquals(resetTimes, 6);
        // 2019-02-18 06:58:01 周一  2019-03-26 04:59:59 周二
        resetTimes = TimeUtil.getResetTimes(1550444281000L, 1553547599000L, day, hours);
        Assert.assertEquals(resetTimes, 5);
        // 2019-02-18 06:58:01 周一  2019-03-26 04:59:59 周二
        resetTimes = TimeUtil.getResetTimes(1550444281000L, 1553547599000L, day, hours);
        Assert.assertEquals(resetTimes, 5);
        // 2019-03-28 16:58:01 周四  2019-02-20 16:58:01 周三
        resetTimes = TimeUtil.getResetTimes(1553763481000L, 1550653081000L, day, hours);
        Assert.assertEquals(resetTimes, 0);
        // 2019-02-17 16:58:01 周日  2019-02-18 16:58:01 周一
        resetTimes = TimeUtil.getResetTimes(1550393881000L, 1550480281000L, day, hours);
        Assert.assertEquals(resetTimes, 1);
        // 2019-02-18 04:59:00 周一  2019-02-18 05:00:00 周一
        resetTimes = TimeUtil.getResetTimes(1550437140000L, 1550437200000L, day, hours);
        Assert.assertEquals(resetTimes, 1);
        // 2019-02-18 05:00:00 周一  2019-02-18 05:00:00 周一
        resetTimes = TimeUtil.getResetTimes(1550437200000L, 1550437200000L, day, hours);
        Assert.assertEquals(resetTimes, 0);
        // 2019-02-18 05:00:00 周一  2019-02-18 05:30:00 周一
        resetTimes = TimeUtil.getResetTimes(1550437200000L, 1550439000000L, day, hours);
        Assert.assertEquals(resetTimes, 0);
    }

    @Test
    public void testGetCrossWeeks() {
        // 2019-07-11 15:33:36 周四  2019-07-14 15:33:36 周日
        int crossWeeks = TimeUtil.getCrossWeeks(1562830416000L, 1563089616000L);
        Assert.assertEquals(crossWeeks, 0);
        // 2019-07-11 15:33:36 周四  2019-07-15 15:33:36 周一
        crossWeeks = TimeUtil.getCrossWeeks(1562830416000L, 1563176016000L);
        Assert.assertEquals(crossWeeks, 1);
        // 2019-07-11 15:33:36 周四  2019-07-22 15:33:36 周一
        crossWeeks = TimeUtil.getCrossWeeks(1562830416000L, 1563780816000L);
        Assert.assertEquals(crossWeeks, 2);
    }

}
