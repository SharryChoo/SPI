package com.sharry.android.spi.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.sharry.android.spi.plugin.core.SpiAutoRegisterTransform
import com.sharry.android.spi.plugin.util.Logger
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 自动注册插件的 Plugin
 *
 * @author Sharry <a href="sharrychoochn@gmail.com">Contact me.</a>
 * @version 1.0
 * @since 4/14/21
 */
class SpiAutoRegisterPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val isApp = project.plugins.hasPlugin(AppPlugin::class.java)
        // only application module needs to generate register code
        if (isApp) {
            Logger.print("Project enable spi-register")
            // register this com.sharry.srouter.plugin
            val android = project.extensions.getByType(AppExtension::class.java)
            android.registerTransform(SpiAutoRegisterTransform(project))
        }
    }

}