package tech.tonyrowan.hotwirenativeexample

import android.app.Application
import dev.hotwire.core.BuildConfig
import dev.hotwire.core.config.Hotwire
import dev.hotwire.core.logging.HotwireLogLevel
import dev.hotwire.core.turbo.config.PathConfiguration

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Hotwire.config.logger.logLevel = HotwireLogLevel.DEBUG
            Hotwire.config.webViewDebuggingEnabled = true
        }

        Hotwire.config.applicationUserAgentPrefix = "Hotwire Native Example;"

        Hotwire.loadPathConfiguration(
            context = this,
            location = PathConfiguration.Location(
                assetFilePath = "configuration.json",
                remoteFileUrl = "http://localhost:3000/api/v1/android/configuration"
            )
        )
    }
}