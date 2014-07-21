package com.dianping.auto.dolunit;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.cli.CliDriver;
import org.apache.hadoop.hive.cli.CliSessionState;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.Driver;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created with IntelliJ IDEA.
 * User: wang.jing
 * Date: 14-3-14
 * Time: 下午12:03
 * To change this template use File | Settings | File Templates.
 */
public class HqlExecutor extends Thread {
    HiveConf conf;
    private CliSessionState ss;
    private CliDriver cliDriver;
    public String result;
    public int isSuccess = Integer.MAX_VALUE;
    private String hql;
    private HiveEnvEnum env;

    public HqlExecutor(String name, HiveEnvEnum env) {
        this(name, env, "");
    }

    public HqlExecutor(String name, HiveEnvEnum env, String hql) {
        super(name);
        this.env = env;
        this.hql = hql;
    }

    public void setHql(String hql) {
        this.hql = hql;
    }

    @Override
    public void run() {
        Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] {}) {
            @Override
            public URL getResource(String name) {
                if (StringUtils.equals("hive-site.xml", name)) {
                    return super.getResource(env + "/hive-site.xml");
                }
                return super.getResource(name);
            }
        });

        conf = new HiveConf(Driver.class);
        conf.addResource(env + "/core-site.xml");
        conf.addResource(env + "/hdfs-site.xml");
        conf.addResource(env + "/mapred-site.xml");
        conf.addResource(env + "/hive-site.xml");
        UserGroupInformation.setConfiguration(conf);

        ss = new CliSessionState(conf);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ss.out = new PrintStream(baos, true, "UTF-8");
            ss.err = System.out;

            ss.initFiles.add(System.getenv("HIVE_CONF_DIR") + File.separator + ".hiverc");

            CliSessionState.start(ss);

            this.cliDriver = new CliDriver();
            synchronized (HqlExecutor.class) {
                Thread.sleep(10000);
                this.cliDriver.processInitFiles(ss);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.isSuccess = this.cliDriver.processLine(hql,true);
        result = baos.toString();
        System.out.println(env + ": " + result);
    }

    public void close() {
        ss.close();
    }

    public static void main(String[] args) throws Exception {
        String hql1 = "use bi; select date_add('2014-03-28', 1) from dual";
        String hql2 = "use bi;select dp_date_add('2014-03-29', 1) from dual";

        HqlExecutor testHqlExecutor = new HqlExecutor("test", HiveEnvEnum.TEST, hql1);
        testHqlExecutor.start();

        HqlExecutor onlineHqlExecutor = new HqlExecutor("online", HiveEnvEnum.ONLINE, hql2);
        onlineHqlExecutor.start();

        try {
            testHqlExecutor.join();
            testHqlExecutor.close();
            onlineHqlExecutor.join();
            onlineHqlExecutor.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}