// 编译时期脚本的插件, 输出一个 gradle 插件的 aar 包
plugins {
    `kotlin-dsl`
}

/**
 * 工程代码依赖的仓库
 */
repositories {
    jcenter()
    google()
}

/**
 * 工程代码依赖
 */
dependencies {
//    // 引用 gradle 的 api
//    gradleApi()
//    // 引用 Kotlin 语法
//    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.72")
    // 引用 Android Transformer 依赖
    implementation("com.android.tools.build:gradle:3.2.1")
}