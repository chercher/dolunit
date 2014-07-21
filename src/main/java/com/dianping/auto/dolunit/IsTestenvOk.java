package com.dianping.auto.dolunit;

import org.apache.commons.lang.StringUtils;
import org.hamcrest.*;

/**
* Created with IntelliJ IDEA.
* User: wang.jing
* Date: 14-3-21
* Time: 下午4:21
* To change this template use File | Settings | File Templates.
*/
public class IsTestenvOk<T> extends DiagnosingMatcher<T> {
    private DolTestCaseParser tcParser;
    private HqlExecutor testHqlExecutor;

    public IsTestenvOk(DolTestCaseParser tcParser, HqlExecutor testHqlExecutor) {
        this.tcParser = tcParser;
        this.testHqlExecutor= testHqlExecutor;
    }

    @Override
    public boolean matches(Object doltestcase, Description mismatch) {
        String hql = tcParser.parse((String) doltestcase);
        testHqlExecutor.setHql(hql);

        Boolean match = false;

        if (Description.NONE == mismatch) {
            try {
                testHqlExecutor.start();
                testHqlExecutor.join();
                if (0 == testHqlExecutor.isSuccess) {
                    match = StringUtils.contains(testHqlExecutor.result, "ok");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!match) {
            mismatch.appendText("Actual result is ").appendValue(testHqlExecutor.result);
        }

        return match;

    }

    @Override
    public void describeTo(Description description) {
        description.appendText("ok");
    }

    @Factory
    public static <T> Matcher<T> isTestenvOk(DolTestCaseParser tcParser, HqlExecutor testHqlExecutor) {
        return new IsTestenvOk<T>(tcParser, testHqlExecutor);
    }
}
