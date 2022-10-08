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

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Sir丶雨轩
 * @since 2022/10/8
 */
public class ClassUtils {

    /**
     * 文件路径分隔符
     */
    public static final String PATH_SEPARATOR = "/";
    /**
     * 包分隔符
     */
    public static final String PACKAGE_SEPARATOR = ".";

    /**
     * 匿名内部类匹配表达式
     */
    public static final Pattern ANONYMOUS_INNER_CLASS_PATTERN = Pattern.compile("^[\\s\\S]*\\${1}\\d+\\.class$");

    /**
     * 包名转换为路径名
     *
     * @param packageName 包名
     * @return 路径名
     */
    public static String package2Path(String packageName) {
        return packageName.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
    }

    /**
     * 路径名转换为包名
     *
     * @param pathName 路径名
     * @return 包名
     */
    public static String path2Package(String pathName) {
        return pathName.replaceAll(PATH_SEPARATOR, PACKAGE_SEPARATOR);
    }

    /**
     * 根据文件后缀名判断是否Class文件
     *
     * @param fileName 文件名
     * @return 是否Class文件
     */
    public static boolean isClass(String fileName) {
        if (!StringUtils.hasLength(fileName)) {
            return false;
        }
        return fileName.endsWith(".class");
    }

    /**
     * Class文件名转为类名（即去除后缀名）
     *
     * @param classFileName Class文件名
     * @return 类名
     */
    public static String classFile2SimpleClass(String classFileName) {
        return classFileName.replace(".class", "");
    }

    /**
     * 根据类名判断是否匿名内部类
     *
     * @param className 类名
     * @return 是否匿名内部类
     */
    public static boolean isAnonymousInnerClass(String className) {
        return ANONYMOUS_INNER_CLASS_PATTERN.matcher(className).matches();
    }

    /**
     * 去除重复包
     *
     * @param packages 包名集合
     * @return 去重后的包名集合
     */
    public static List<String> distinctPackages(List<String> packages) {
        List<String> distinctPackages = new LinkedList<>();

        //去除完全重复包
        List<String> minPackages = packages.stream()
                .distinct().toList();

        //去除父子包
        minPackages.forEach(srcPkg -> {
            long count = minPackages.stream()
                    .filter(comparePkg -> !comparePkg.equals(srcPkg) && srcPkg.startsWith(comparePkg))
                    .count();
            if (count == 0) {
                distinctPackages.add(srcPkg);
            }
        });


        return distinctPackages;
    }

    /**
     * 获取默认的类加载器
     *
     * @return 类加载器
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

}
