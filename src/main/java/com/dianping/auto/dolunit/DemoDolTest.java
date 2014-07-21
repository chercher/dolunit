package com.dianping.auto.dolunit;

import static com.dianping.auto.dolunit.IsConsistent.isConsistent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;

import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: wang.jing
 * Date: 14-3-21
 * Time: 下午5:18
 * To change this template use File | Settings | File Templates.
 */
public class DemoDolTest extends DolTestCase {

//    @Test
//    public void dolTest1() {
//        assertConsistent("use bi; select date_add('2014-05-08', 1) from dual");
//    }
//
//    @Test
//    public void dolTest2() {
//        assertConsistent("use bi; select dp_date_add('2014-05-09', 1) from dual");
//    }

    @Test
    public void pvmvdayweekrelativeratio() {
        assertTestenvOk("use bi; select case when abs(a.total/b.total-1)>0.25 and abs(a.total/c.total-1)>0.25 then 'fail' else 'ok' end from\n" +
                "(\n" +
                "select count(*) as total from bi.dpdw_hippo_log where hp_cal_dt = '${env.YYYYMMDD}'\n" +
                ") a\n" +
                "join\n" +
                "(\n" +
                "select count(*) as total from bi.dpdw_hippo_log where hp_cal_dt = '${env.YYYYMMDD_P1D}'\n" +
                ") b\n" +
                "on 1=1\n" +
                "join\n" +
                "(\n" +
                "select count(*) as total from bi.dpdw_hippo_log where hp_cal_dt = '${env.YYYYMMDD_P7D}'\n" +
                ") c\n" +
                "on 1=1;");

//        assertTestenvFail("use bi; select case when abs(a.total/b.total-1)>0.25 and abs(a.total/c.total-1)>0.25 then 'fail' else 'ok' end from" +
//               "(select count(*) as total from bi.dpdim_op_push where hp_stat_date = '${env.YYYYMMDD}'" +
//                ") a" +
//                " join" +
//                "(" +
//                "select count(*) as total from bi.dpdim_op_push where hp_stat_date = '${env.YYYYMMDD_P1D}'" +
//                ") b" +
//                "on 1=1" +
//                "join" +
//                "(" +
//                "select count(*) as total from bi.dpdim_op_push where hp_stat_date = '${env.YYYYMMDD_P7D}'" +
//                ") c" +
//                "on 1=1;");
    }


}