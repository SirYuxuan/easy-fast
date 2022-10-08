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
package com.yuxuan66.ef.core.io;

import com.yuxuan66.ef.util.ClassUtils;
import com.yuxuan66.ef.util.ExecutorUtil;
import com.yuxuan66.ef.util.ObjectUtils;
import com.yuxuan66.ef.util.ScannerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author Sir丶雨轩
 * @since 2022/10/8
 */
public class DefaultClassScanner implements ClassScanner {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<Class<?>> scan(String... scanBasePackages) {
        return scan(Arrays.asList(scanBasePackages));
    }

    @Override
    public List<Class<?>> scan(List<String> scanBasePackages) {
        List<Class<?>> classList = new LinkedList<>();

        //没有需要扫描的包，返回空列表
        if (ObjectUtils.isEmpty(scanBasePackages)) {
            return classList;
        }

        //去除重复包
        List<String> realScanBasePackages = ClassUtils.distinctPackages(scanBasePackages);

        //创建异步线程
        List<FutureTask<List<Class<?>>>> tasks = new LinkedList<>();
        realScanBasePackages
                .forEach(pkg -> {
                    ScannerCallable call = new ScannerCallable(pkg);
                    FutureTask<List<Class<?>>> task = new FutureTask<>(call);
                    ExecutorUtil.executeInPool(new Thread(task));
                    tasks.add(task);
                });

        //等待返回结果
        tasks.parallelStream().forEach(task -> {
            try {
                classList.addAll(task.get());
            } catch (InterruptedException | ExecutionException e) {
                logger.error(e.getMessage(), e);
            }
        });

        return classList;
    }


    /**
     * 扫描器线程类
     */
    static class ScannerCallable implements Callable<List<Class<?>>> {

        /**
         * 扫描的包名称
         */
        private String pkg;

        public ScannerCallable(String pkg) {
            this.pkg = pkg;
        }

        @Override
        public List<Class<?>> call() {
            return ScannerUtil.scan(pkg);
        }
    }
}
