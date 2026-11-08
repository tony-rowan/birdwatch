package tech.tonyrowan.birdwatch

import android.app.Application
import dev.hotwire.core.bridge.BridgeComponentFactory
import dev.hotwire.core.bridge.KotlinXJsonConverter
import dev.hotwire.core.config.Hotwire
import dev.hotwire.core.logging.HotwireLogLevel
import dev.hotwire.core.turbo.config.PathConfiguration
import dev.hotwire.navigation.config.registerBridgeComponents
import dev.hotwire.navigation.config.registerRouteDecisionHandlers
import dev.hotwire.navigation.routing.AppNavigationRouteDecisionHandler
import dev.hotwire.navigation.routing.BrowserTabRouteDecisionHandler
import dev.hotwire.navigation.routing.SystemNavigationRouteDecisionHandler

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Hotwire.config.logger.logLevel = HotwireLogLevel.DEBUG
            Hotwire.config.webViewDebuggingEnabled = true
        }

        Hotwire.config.applicationUserAgentPrefix = "Birdwatch;"

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

        Hotwire.registerRouteDecisionHandlers(
            SignInRouteDecisionHandler(),
            SignOutRouteDecisionHandler(),
            AppNavigationRouteDecisionHandler(),
            BrowserTabRouteDecisionHandler(),
            SystemNavigationRouteDecisionHandler()
        )
    }
}