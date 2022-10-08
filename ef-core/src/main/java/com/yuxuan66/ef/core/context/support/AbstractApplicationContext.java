/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yuxuan66.ef.core.context.support;

import com.yuxuan66.ef.core.beans.BeansException;
import com.yuxuan66.ef.core.beans.factory.BeanFactory;
import com.yuxuan66.ef.core.context.ApplicationContext;
import com.yuxuan66.ef.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Sir丶雨轩
 * @since 2022/9/30
 */
public abstract class AbstractApplicationContext implements BeanFactory, ApplicationContext {

    /**
     * log对象
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 上下文的唯一id
     */
    private String id = ObjectUtils.identityToString(this);
    /**
     * 应用启动时间
     */
    private long startupDate;

    /**
     * Display Name
     */
    private String displayName = ObjectUtils.identityToString(this);

    /**
     * 用于刷新和销毁的锁
     */
    private final Object startupShutdownLock = new Object();

    /**
     * 标识此上下文是否处于活动状态
     */
    private final AtomicBoolean active = new AtomicBoolean();

    /**
     * 标识此上下文是否已经关闭
     */
    private final AtomicBoolean closed = new AtomicBoolean();


    /**
     * 刷新创建Bean
     *
     * @throws BeansException Bean相关异常
     */
    public void refresh() throws BeansException {
        synchronized (startupShutdownLock) {
            // 初始化状态，准备开始刷新
            prepareRefresh();

            // 创建BeanFactory

            // 加载Bean定义

            // 创建Bean实例

            // 依赖注入属性

            // 通知监听Bean创建完毕
        }
    }

    /**
     * 准备刷新设置一些数据状态
     */
    protected void prepareRefresh() {
        // 切换状态
        this.startupDate = System.currentTimeMillis();
        this.closed.set(false);
        this.active.set(true);

        if (logger.isDebugEnabled()) {
            if (logger.isTraceEnabled()) {
                logger.trace("Refreshing " + this);
            } else {
                logger.debug("Refreshing " + getDisplayName());
            }
        }
    }

    /**
     * 获取上下文启动时间
     *
     * @return 启动时间
     */
    public long getStartupDate() {
        return this.startupDate;
    }

    /**
     * 获取上下文唯一ID
     *
     * @return ID
     */
    public String getId() {
        return this.id;
    }

    /**
     * 获取上下文显示的名称
     *
     * @return 名称
     */
    public String getDisplayName() {
        return this.displayName;
    }
}
