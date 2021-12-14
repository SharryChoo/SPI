plugins {
    // 生成一个 Jar 包, classpath 在 org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72 中
    id("org.jetbrains.kotlin.jvm")
}
// 用来发布版本, classpath 在 com.github.dcendents:android-maven-gradle-plugin:2.1 中
apply(from = "../install.gradle")

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
    // 在使用自定义插件时候，一定要引用org.gradle.api.Plugin
    implementation(gradleApi())
    // 引用 Kotlin 语法
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.72")
    // 引用 Android Transformer 依赖
    implementation("com.android.tools.build:gradle:3.2.1")
}