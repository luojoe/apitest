package com.apitest.action.utils;

import java.util.List;

/**
 * @author joe
 * @data 2019/7/4 21:51
 * @email joeluo520@gmail.com
 */
public interface ServiceResolver {
    /* 获取module 中所有的服务列表 */
    List<RestServiceItem> findAllSupportedServiceItemsInModule();

    /* 获取project 中所有的服务列表 */
    List<RestServiceItem> findAllSupportedServiceItemsInProject();
}
