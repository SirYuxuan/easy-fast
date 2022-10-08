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
package com.yuxuan66.ef.core.beans.factory.support;

import com.yuxuan66.ef.core.beans.BeansException;
import com.yuxuan66.ef.core.beans.factory.BeanFactory;
import com.yuxuan66.ef.core.beans.factory.config.BeanDefinition;
import com.yuxuan66.ef.core.context.support.BeanDefinitionRegistry;
import com.yuxuan66.ef.core.io.ClassScanner;
import com.yuxuan66.ef.core.io.DefaultClassScanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认Bean工厂，维护所有Bean的数据
 * @author Sir丶雨轩
 * @since 2022/10/8
 */
public class EfDefaultBeanFactory implements BeanFactory, BeanDefinitionRegistry {


    private final ClassScanner classScanner;

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    private volatile List<String> beanDefinitionNames = new ArrayList<>(256);

    public EfDefaultBeanFactory() {
        this.classScanner = new DefaultClassScanner();
    }

    public EfDefaultBeanFactory(ClassScanner scanner){
        this.classScanner = scanner;
    }


    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeansException {

    }

    @Override
    public Object getBean(String name) throws BeansException {
        return null;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return null;
    }
}
