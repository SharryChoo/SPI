## 功能介绍
Service Manager interface 框架

## 安装指南
![New Version](https://jitpack.io/v/SharryChoo/SPI.svg)

### Step 1
在工程的根目录的 build.gradle 中添加 jitpack 的 maven 仓库 和 插件依赖
```
allprojects {
    repositories {
    	...
	    maven { url 'https://jitpack.io' }
    }
    dependencies {
        ......
        classpath "com.github.SharryChoo.SPI:spi-plugin:1.0.0"
    }
}
```
### Step 2
在工程的 app 的 build.gradle 添加如下依赖
```
plugins {
    ......
    id 'com.sharry.android.spi'
}

dependencies {
    ......
    // SPI 依赖
    api "com.github.SharryChoo.SPI:spi-impl:1.0.0"
}
```

### Step 3
#### 定义接口
```
interface IFeedService {

    fun toast(context: Context)

}
```
#### 定义实现
```
@ServiceImpl(api = IFeedService::class, singleton = true)
class FeedServiceImpl: IFeedService {

    override fun toast(context: Context) {
        Toast.makeText(context, "FeedServiceImpl.invoke", Toast.LENGTH_SHORT).show()
    }

}
```
#### 使用方式
```
ServiceManager.getService(IFeedService::class.java).toast(view.context)
```