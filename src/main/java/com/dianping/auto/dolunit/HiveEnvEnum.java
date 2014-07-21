package com.dianping.auto.dolunit;

/**
 * Created with IntelliJ IDEA.
 * User: wang.jing
 * Date: 14-3-20
 * Time: 下午4:15
 * To change this template use File | Settings | File Templates.
 */
public enum HiveEnvEnum {
    TEST("test"), ONLINE("online");
    private String str;
    private HiveEnvEnum(String str) {
        this.str = str;
    }
    public String toString() {
        return str;
    }
}
