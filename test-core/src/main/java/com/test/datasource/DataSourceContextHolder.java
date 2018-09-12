package com.test.datasource;

import com.test.pagehelper.PageHelperHolder;

/**
 * @author : wuhengzhen
 * @Description : 动态切换数据源支持器
 * @date : 2018/09/11 16:33
 * @system name:
 * @copyright:
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    /**
     * @param dataSourceType 数据库类型
     * @return void
     * @throws
     * @Description: 设置数据源类型
     */
    public static void setDataSourceType(String dataSourceType) {
        // 根据数据源定义数据库分页方言
        DataSourceType dataSourceTypeDialect = DataSourceType.getType(dataSourceType);
        if (dataSourceType != null) {
            PageHelperHolder.setPagerType(dataSourceTypeDialect.getDialect());
        }
        contextHolder.set(dataSourceType);
    }

    /**
     * @param
     * @return String
     * @throws
     * @Description: 获取数据源类型
     */
    public static String getDataSourceType() {
        return contextHolder.get();
    }

    /**
     * @param
     * @return void
     * @throws
     * @Description: 清除数据源类型
     */
    public static void clearDataSourceType() {
        contextHolder.remove();
    }

}
