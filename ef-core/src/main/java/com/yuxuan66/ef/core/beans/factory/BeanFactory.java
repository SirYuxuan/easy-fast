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
package com.yuxuan66.ef.core.beans.factory;

import com.yuxuan66.ef.core.beans.BeansException;

/**
 * Bean的创建工程
 * @author Sir丶雨轩
 * @since 2022/9/30
 */
public interface BeanFactory {

    /**
     * 根据名称获取Bean的实例
     * @param name 名称
     * @return 实例
     * @throws BeansException  找不到Bean等错误
     */
    Object getBean(String name) throws BeansException;


    /**
     * 根据名称获取Bean的实例,转换为指定类型
     * @param name 名称
     * @param requiredType 类型
     * @return 实例
     * @throws BeansException  找不到Bean等错误
     */
    <T> T getBean(String name, Class<T> requiredType) throws BeansException;

    /**
     * 根据类型获取Bean的实例
     * @param requiredType 类型
     * @return 实例
     * @param <T> 类型
     * @throws BeansException 找不到Bean等错误
     */
    <T> T getBean(Class<T> requiredType) throws BeansException;

}
