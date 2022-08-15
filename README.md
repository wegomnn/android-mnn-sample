# 微购推理 sdk 文档

## 1、基本介绍

微购图片向量解析库采用 Android Lib 库的形式开发，集成了阿里的 mnn 图片推理库和开源的 opencv 图片处理库，利用公司的模型文件进行图片向量提取后，通过 jni 的方式提供 Android 应用上层使用。


## 2、集成方式

微购图片向量解析库暂时发布到第三方渠道，集成方式如下。集成后的 demo 工程地址: [android-mnn-sample](https://github.com/wegomnn/android-mnn-sample)

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
            abiFilters "arm64-v8a", "armeabi-v7a"
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

### 3.1、Application 初始化的同时调用 mnn 初始化方法

```
/**
 * mnn 初始化
 *
 * @param context
 */
public static void initialization(Context context)
```

### 3.2、图片向量解析

```
/**
 * 图片向量解析
 *
 * @param context   上下文
 * @param imagePath 图片本地地址
 * @return
 */
public static MnnInfo[] decode(Context context, String imagePath)
```