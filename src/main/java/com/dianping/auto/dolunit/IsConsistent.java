package com.dianping.auto.dolunit;

import org.apache.commons.lang.StringUtils;
import org.hamcrest.*;

import java.io.IOException;

/**
* Created with IntelliJ IDEA.
* User: wang.jing
* Date: 14-3-21
* Time: 下午4:21
* To change this template use File | Settings | File Templates.
*/
public class IsConsistent<T> extends DiagnosingMatcher<T> {
    private DolTestCaseParser tcParser;
    private HqlExecutor testHqlExecutor;
    private HqlExecutor onlineHqlExecutor;

    public IsConsistent(DolTestCaseParser tcParser, HqlExecutor testHqlExecutor, HqlExecutor onlineHqlExecutor) {
        this.tcParser = tcParser;
        this.testHqlExecutor= testHqlExecutor;
        this.onlineHqlExecutor = onlineHqlExecutor;
    }

    @Override
    public boolean matches(Object doltestcase, Description mismatch) {
        String hql = tcParser.parse((String) doltestcase);
        testHqlExecutor.setHql(hql);
        onlineHqlExecutor.setHql(hql);

        Boolean match = false;

        if (Description.NONE == mismatch) {
            try {
                testHqlExecutor.start();
                onlineHqlExecutor.start();
                testHqlExecutor.join();
                onlineHqlExecutor.join();
                if ((0 == testHqlExecutor.isSuccess) && (0 == onlineHqlExecutor.isSuccess)) {
                    match = StringUtils.equals(testHqlExecutor.result, onlineHqlExecutor.result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!match) {
            mismatch.appendText("7.32 is ").appendValue(testHqlExecutor.result).appendText(" while online is ").appendValue(onlineHqlExecutor.result);
        }

        return match;

    }

    @Override
    public void describeTo(Description description) {
        description.appendText("is consistent across 7.32 and online.");
    }

    @Factory
    public static <T> Matcher<T> isConsistent(DolTestCaseParser tcParser, HqlExecutor testHqlExecutor, HqlExecutor onlineHqlExecutor) {
        return new IsConsistent<T>(tcParser, testHqlExecutor, onlineHqlExecutor);
    }
}
