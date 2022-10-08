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

import com.yuxuan66.ef.annotation.Service;
import com.yuxuan66.ef.core.beans.BeansException;
import com.yuxuan66.ef.core.beans.factory.BeanFactory;
import com.yuxuan66.ef.core.beans.factory.config.BeanDefinition;
import com.yuxuan66.ef.core.beans.factory.support.EfDefaultBeanFactory;
import com.yuxuan66.ef.util.Assert;

/**
 * 一般应用上下文
 *
 * @author Sir丶雨轩
 * @since 2022/10/8
 */
public class GenericApplicationContext extends AbstractApplicationContext implements BeanDefinitionRegistry {

    private final BeanFactory beanFactory;

    /**
     * Create a new GenericApplicationContext.
     */
    public GenericApplicationContext() {
        this.beanFactory = new EfDefaultBeanFactory();
    }

    /**
     * Create a new GenericApplicationContext with the given beanFactory.
     *
     * @param beanFactory beanFactory
     */
    public GenericApplicationContext(BeanFactory beanFactory) {
        Assert.notNull(beanFactory, "BeanFactory must not be null");
        this.beanFactory = beanFactory;
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return this.beanFactory.getBean(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return this.beanFactory.getBean(name, requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return this.beanFactory.getBean(requiredType);
    }

    public void register(Class<?>... componentClasses) {
        for (Class<?> componentClass : componentClasses) {
            Service annotation = componentClass.getAnnotation(Service.class);
            System.out.println(annotation);
        }
    }


    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeansException {
    }
}
