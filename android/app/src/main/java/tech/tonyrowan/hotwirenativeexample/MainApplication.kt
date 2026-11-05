package tech.tonyrowan.hotwirenativeexample

import android.app.Application
import dev.hotwire.core.bridge.BridgeComponentFactory
import dev.hotwire.core.bridge.KotlinXJsonConverter
import dev.hotwire.core.config.Hotwire
import dev.hotwire.core.logging.HotwireLogLevel
import dev.hotwire.core.turbo.config.PathConfiguration
import dev.hotwire.navigation.config.registerBridgeComponents

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Hotwire.config.logger.logLevel = HotwireLogLevel.DEBUG
            Hotwire.config.webViewDebuggingEnabled = true
        }

        Hotwire.config.applicationUserAgentPrefix = "Hotwire Native Example;"

        Hotwire.config.jsonConverter = KotlinXJsonConverter()

        Hotwire.loadPathConfiguration(
            context = this,
            location = PathConfiguration.Location(
                assetFilePath = "configuration.json",
                remoteFileUrl = "http://localhost:3000/api/v1/android/configuration"
            )
        )

        Hotwire.registerBridgeComponents(
            BridgeComponentFactory("auto-navigate", ::AutoNavigateComponent)
        )
    }
}