package com.dianping.auto.dolunit;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import static com.dianping.auto.dolunit.IsConsistent.isConsistent;
import static com.dianping.auto.dolunit.IsTestenvOk.isTestenvOk;
import static com.dianping.auto.dolunit.IsTestenvFail.isTestenvFail;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;

/**
* Created with IntelliJ IDEA.
* User: wang.jing
* Date: 14-3-20
* Time: 下午3:47
* To change this template use File | Settings | File Templates.
*/
public class DolTestCase {
    protected DolTestCaseParser tcParser;
    protected HqlExecutor testHqlExecutor;
    protected HqlExecutor onlineHqlExecutor;

    @BeforeMethod
    public void setup() {
        tcParser = new DolTestCaseParser();
        testHqlExecutor = new HqlExecutor("test", HiveEnvEnum.TEST);
        onlineHqlExecutor = new HqlExecutor("online", HiveEnvEnum.ONLINE);
    }

    public void assertConsistent(String doltestcase) {
        assertThat(doltestcase, isConsistent(tcParser, testHqlExecutor, onlineHqlExecutor));
    }

    public void assertTestenvOk(String doltestcase) {
        assertThat(doltestcase, isTestenvOk(tcParser, testHqlExecutor));
    }

    public void assertTestenvFail(String doltestcase) {
        assertThat(doltestcase, isTestenvFail(tcParser, testHqlExecutor));
    }

    @AfterMethod
    public void teardown() {
        if (null != testHqlExecutor) {
            if (null != testHqlExecutor.result) {
                testHqlExecutor.close();
            }
        }
        if (null != onlineHqlExecutor) {
            if (null != onlineHqlExecutor.result)
                onlineHqlExecutor.close();
        }
    }
}
