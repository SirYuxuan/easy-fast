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
package com.yuxuan66.ef.core.context;

import com.yuxuan66.ef.core.beans.factory.BeanFactory;

/**
 * 应用上下文
 *
 * @author Sir丶雨轩
 * @since 2022/9/30
 */
public interface ApplicationContext extends BeanFactory {

    /**
     * 获取上下文的唯一ID
     *
     * @return ID
     */
    String getId();

    /**
     * 获取应用启动时间
     *
     * @return 启动时间
     */
    long getStartupDate();

    /**
     * 获取上下文显示的名称
     *
     * @return 名称
     */
    String getDisplayName();
}
