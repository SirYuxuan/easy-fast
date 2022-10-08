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
package com.yuxuan66.ef.util;

import com.yuxuan66.ef.consts.EfConst;
import com.yuxuan66.ef.core.io.ProtocolTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * @author Sir丶雨轩
 * @since 2022/10/8
 */
public class ScannerUtil {

    private static final Logger logger = LoggerFactory.getLogger(ScannerUtil.class);

    /**
     * 扫描某个包下所有Class类
     *
     * @param pkg 扫描报名
     * @return Class列表
     */
    public static List<Class<?>> scan(String pkg) {
        List<Class<?>> classList = new LinkedList<>();
        try {
            //包名转化为路径名
            String pathName = ClassUtils.package2Path(pkg);
            //获取路径下URL
            Enumeration<URL> urls = ClassUtils.getDefaultClassLoader().getResources(pathName);
            //循环扫描路径
            classList = scanUrls(pkg, urls);
        } catch (Exception e) {
            logger.error("扫描包路径出错：{}", pkg, e);
        }
        return classList;
    }

    /**
     * 扫描多个Url路径，找出符合包名的Class类
     *
     * @param pkg  包名
     * @param urls 路径
     * @return Class列表
     */
    private static List<Class<?>> scanUrls(String pkg, Enumeration<URL> urls) {
        List<Class<?>> classList = new LinkedList<>();

        //Enumeration转list
        List<URL> urlList = new LinkedList<>();
        while (urls.hasMoreElements()) {
            urlList.add(urls.nextElement());
        }

        //创建异步任务
        List<FutureTask<List<Class<?>>>> tasks = new LinkedList<>();
        urlList.forEach(url -> {
            UrlScanCallable call = new UrlScanCallable(pkg, url);
            FutureTask<List<Class<?>>> task = new FutureTask<>(call);
            ExecutorUtil.executeInPool(new Thread(task));
            tasks.add(task);
        });

        //等待并处理返回结果
        tasks.parallelStream().forEach(task -> {
            try {
                classList.addAll(task.get());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });

        return classList;
    }

    /**
     * 递归扫描指定文件路径下的Class文件
     *
     * @param pkg      包名
     * @param filePath 文件路径
     * @return Class列表
     */
    private static List<Class<?>> recursiveScan4Path(String pkg, String filePath) {
        List<Class<?>> classList = new LinkedList<>();

        //读取文件
        File file = new File(filePath);
        if (!file.exists() || !file.isDirectory()) {
            return classList;
        }

        //处理类文件
        File[] classes = file.listFiles(child -> ClassUtils.isClass(child.getName()));
        assert classes != null;
        Arrays.asList(classes).forEach(child -> {
            String className = ClassUtils.classFile2SimpleClass(
                    pkg + ClassUtils.PACKAGE_SEPARATOR + child.getName()
            );

            try {
                Class<?> clz = ClassUtils.getDefaultClassLoader().loadClass(className);
                classList.add(clz);
            } catch (ClassNotFoundException | LinkageError e) {
                logger.warn("can load class:{}", className);
            }
        });

        //处理目录
        File[] dirs = file.listFiles(File::isDirectory);
        assert dirs != null;
        Arrays.asList(dirs).forEach(child -> {
            String childPackageName = pkg +
                    ClassUtils.PACKAGE_SEPARATOR +
                    child.getName();
            String childPath = filePath +
                    ClassUtils.PATH_SEPARATOR +
                    child.getName();
            classList.addAll(
                    recursiveScan4Path(childPackageName, childPath)
            );
        });

        return classList;
    }

    /**
     * 递归扫描Jar文件内的Class类
     *
     * @param pkg     包名
     * @param jarPath Jar文件路径
     * @return Class列表
     * @throws IOException IO异常
     */
    private static List<Class<?>> recursiveScan4Jar(String pkg, String jarPath) throws IOException {
        List<Class<?>> classList = new LinkedList<>();

        //读取Jar文件
        JarInputStream jin = new JarInputStream(new FileInputStream(jarPath));
        JarEntry entry = jin.getNextJarEntry();
        while (entry != null) {
            String name = entry.getName();
            entry = jin.getNextJarEntry();

            //包名不匹配，跳过
            if (!name.contains(ClassUtils.package2Path(pkg))) {
                continue;
            }

            //判断是否类文件
            if (ClassUtils.isClass(name)) {
                if (ClassUtils.isAnonymousInnerClass(name)) {
                    //是匿名内部类，跳过
                    continue;
                }

                //文件名转类名
                String className = ClassUtils.classFile2SimpleClass(ClassUtils.path2Package(name));
                try {
                    //加载类文件
                    Class<?> clz = ClassUtils.getDefaultClassLoader().loadClass(className);
                    classList.add(clz);
                } catch (ClassNotFoundException | LinkageError e) {
                    logger.debug("can load class:{}", name);
                }
            }
        }

        return classList;
    }


    /**
     * URL扫描线程类
     */
    static class UrlScanCallable implements Callable<List<Class<?>>> {

        /**
         * 需匹配的包名
         */
        private final String pkg;
        /**
         * 需扫描的URL
         */
        private final URL url;

        public UrlScanCallable(String pkg, URL url) {
            this.pkg = pkg;
            this.url = url;
        }

        @Override
        public List<Class<?>> call() {
            //获取协议
            String protocol = url.getProtocol();

            List<Class<?>> classList = new LinkedList<>();
            try {
                if (ProtocolTypes.file.name().equals(protocol)) {
                    //文件
                    String path = URLDecoder.decode(url.getFile(), EfConst.DEFAULT_CHARSET);
                    classList.addAll(recursiveScan4Path(pkg, path));

                } else if (ProtocolTypes.jar.name().equals(protocol)) {
                    //jar包
                    String jarPath = URLUtil.getJarPathFormUrl(url);
                    classList.addAll(recursiveScan4Jar(pkg, jarPath));
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            return classList;
        }
    }
}
