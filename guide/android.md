# 微购推理 android sdk 文档

## 1、开发环境

- 开发工具: Android Studio 2020.3.1
- minSdkVersion: 21 
- NDK: android-ndk-r19c
- cmake: version 3.10.2

## 2、集成方式

微购图片向量解析库暂时发布到第三方渠道，集成方式如下。集成后的 demo
工程地址: [android-mnn-sample](https://github.com/wegomnn/android-mnn-sample)

### 2.1、根目录 build.gradle 文件配置

根目录 build.gradle 文件 repositories 增加配置：
> maven { url "https://raw.githubusercontent.com/wegomnn/android-maven/main" }

```
buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url "https://raw.githubusercontent.com/wegomnn/android-maven/main" }
    }
}
```

### 2.2、根目录 setting.gradle 文件配置（选配）

根目录 setting.gradle 文件如果有 `dependencyResolutionManagement` 字段，则给 repositories 增加配置：
> maven { url "https://raw.githubusercontent.com/wegomnn/android-maven/main" }

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter() 
        maven { url "https://raw.githubusercontent.com/wegomnn/android-maven/main" }
    }
}
```

### 2.3、APP 目录 build.gradle 文件配置

- APP 目录 build.gradle 文件增加 ndk 配置、sourceSets 配置
- 配置 wego 推理文件依赖 `com.wegooooo.sdk:mnn:lastversion`

```
android {
    defaultConfig {
        ndk {
            abiFilters "arm64-v8a"
        }
    }
    
    sourceSets {
        main {
            jniLibs.srcDirs = ['src/main/jniLibs', 'libs']
        }
    }
}

dependencies {
    implementation 'com.wegooooo.sdk:mnn:0.0.1'
}
```

## 3、API 介绍

### 3.1、应用初始化的同时调用 mnn 初始化方法

```
/**
 * mnn 初始化
 *
 * @param context
 */
public static void initialization(Context context)
```

### 3.2、图片向量解析之前申请模型文件资源

```
/**
 * 向量解析初始化
 *
 * @param context 上下文
 */
public static void create(Context context) 
```

### 3.3、图片向量解析之后释放模型文件资源

```
/**
 * 推理模型文件释放
 */
public static native void release()
```

### 3.4、图片向量解析

```
/**
 * 图片向量解析
 *
 * @param imagePath 图片本地地址
 * @return 图片向量
 */
public static native float[] detect(String imagePath)
```

### 3.5、图片向量保存手机本地文件缓存

```
/**
 * 图片向量保存手机本地文件缓存
 *
 * @param context    上下文
 * @param collection 图片向量集
 * @return 返回的向量 ids
 */
public static int[] vectorInsert(Context context, float[] collection) 
```

### 3.6、图片向量搜索

```
/**
 * 图片向量搜索
 *
 * @param context 上下文
 * @param target  目标图片向量集
 * @param size    预期返回结果数量
 * @return 文件索引
 */
public static float[] vectorSearch(Context context, float[] target, int size)
```

### 3.7、清空图片向量文件手机本地文件缓存

```
/**
 * 清空图片向量文件手机本地文件缓存
 *
 * @param context
 */
public static void vectorClear(Context context)
```