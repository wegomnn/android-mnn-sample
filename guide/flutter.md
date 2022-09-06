# 微购推理 flutter sdk 文档

目前仅支持 android 端。

## 1、开发环境

- 开发工具: Android Studio 2020.3.1
- minSdkVersion: 21 
- NDK: android-ndk-r19c
- cmake: version 3.10.2

## 2、集成方式

微购图片向量解析库暂时发布到第三方渠道，集成方式如下。集成后的 demo
工程地址: [flutter_mnn_sample](https://github.com/wegomnn/flutter_mnn_sample.git)

### 2.1、根目录 pubspec.yaml 文件配置

```
dependencies:
  wego_mnn:
    git: https://github.com/wegomnn/flutter_mnn_plugin.git
```


## 3、API 介绍

### 3.1、应用初始化的同时调用 mnn 初始化方法

```
/// mnn 初始化
static void initialization()
```

### 3.2、图片向量解析之前申请模型文件资源

```
/// 推理模型文件资源申请
static void create()
```

### 3.3、图片向量解析之后释放模型文件资源

```
/// 推理模型文件资源释放
static void release()
```

### 3.4、图片向量解析

```
/// 图片向量解析
///
/// @param imagePath 图片本地地址
/// @return 1000纬度的图片向量集合
static Future<Float32List> detect(String imagePath)
```

### 3.5、图片向量保存手机本地文件缓存

```
/// 图片向量的批量插入
///
/// @param context    上下文
/// @param collection 单张或者多张图片向量集合
/// @return 插入结果 [id, id)
static Future<Int32List> vectorInsert(Float32List collection)
```

### 3.6、图片向量搜索

```
/// 图片搜索
///
/// @param context 上下文
/// @param target  单张图片向量集合
/// @param size    预期搜索返回结果的数量
/// @return 搜索结果 [id, id)
static Future<Float32List> vectorSearch(Float32List target, int size)
```

### 3.7、清空图片向量文件手机本地文件缓存

```
/// 清空手机本地缓存的全部向量集
static void vectorClear()
```