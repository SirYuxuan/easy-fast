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

import java.net.URL;

/**
 * @author Sir丶雨轩
 * @since 2022/10/8
 */
public class URLUtil {


    /**
     * url中文件类型前缀
     */
    public static final String JAR_URL_FILE_PREFIX = "file:";

    /**
     * 从url中获取jar文件真实路径
     * <p>
     * jar文件url示例如下：<p>
     * jar:file:/Users/cent/.gradle/caches/modules-2/files-2.1/org.projectlombok/lombok/1.18.4/7103ab519b1cdbb0642ad4eaf1db209d905d0f96/lombok-1.18.4.jar!/org
     *
     * @param url url
     * @return jar文件真实路径
     */
    public static String getJarPathFormUrl(URL url) {
        String file = url.getFile();
        return file.substring(0, file.lastIndexOf("!"))
                .replaceFirst(JAR_URL_FILE_PREFIX, "");
    }
}
