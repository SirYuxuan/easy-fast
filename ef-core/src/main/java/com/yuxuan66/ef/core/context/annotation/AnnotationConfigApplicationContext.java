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
package com.yuxuan66.ef.core.context.annotation;

import com.yuxuan66.ef.core.context.support.GenericApplicationContext;
import com.yuxuan66.ef.core.io.ClassScanner;
import com.yuxuan66.ef.core.io.DefaultClassScanner;
import com.yuxuan66.ef.util.Assert;

import java.util.List;

/**
 * 注解启动应用上下文
 *
 * @author Sir丶雨轩
 * @since 2022/10/8
 */
public class AnnotationConfigApplicationContext extends GenericApplicationContext implements AnnotationConfigRegistry {

    private final ClassScanner classScanner;

    public AnnotationConfigApplicationContext(String... basePackages) {
        super();
        this.classScanner = new DefaultClassScanner();
        scan(basePackages);
    }

    public AnnotationConfigApplicationContext(ClassScanner scanner, String... basePackages) {
        super();
        Assert.notNull(scanner, "ClassScanner must not be null");
        this.classScanner = scanner;
        scan(basePackages);
    }

    @Override
    public void scan(String... basePackages) {
        List<Class<?>> classList = classScanner.scan(basePackages);
        this.register(classList.toArray(new Class[0]));
        this.refresh();
    }
}
