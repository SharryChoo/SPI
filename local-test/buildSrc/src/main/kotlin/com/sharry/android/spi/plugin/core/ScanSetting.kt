package com.sharry.android.spi.plugin.core

import java.util.*

/**
 * register setting
 *
 * @author Sharry [Contact me.](sharrychoochn@gmail.com)
 * @version 1.0
 * @since 4/14/21
 */
class ScanSetting {

    /**
     * scan result for [.interfaceName]
     * class names in this list
     *
     * key: is impl class path.
     * value: is spi info
     */
    val injectMap = LinkedHashMap<String, AnnotationEntry>()

    companion object {
        const val PLUGIN_NAME = "com.sharry.spi"

        /**
         * The register code is generated into this class
         */
        const val GENERATE_TO_CLASS_NAME = "com/sharry/android/spi/ServiceManager"

        /**
         * you know. this is the class file(or entry in jar file) name
         */
        const val GENERATE_TO_CLASS_FILE_NAME = "$GENERATE_TO_CLASS_NAME.class"

        /**
         * The register code is generated into this method
         */
        const val GENERATE_TO_METHOD_NAME = "loadSpiMap"

        /**
         * register method name in class: [.GENERATE_TO_CLASS_NAME]
         */
        const val REGISTER_METHOD_NAME = "register"

        /**
         * The class name for ServiceImpl.class
         */
        const val SERVICE_IMPL_ANNOTATION_DESC = "Lcom/sharry/android/spi/ServiceImpl;"
        const val SERVICE_IMPL_ANNOTATION_API_FILED_NAME = "api"
        const val SERVICE_IMPL_ANNOTATION_DELAY_FILED_NAME = "delay"
        const val SERVICE_IMPL_ANNOTATION_SINGLETON_FILED_NAME = "singleton"
    }


}