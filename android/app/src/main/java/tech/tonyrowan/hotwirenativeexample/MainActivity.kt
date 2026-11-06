package tech.tonyrowan.hotwirenativeexample

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import dev.hotwire.core.config.Hotwire
import dev.hotwire.navigation.activities.HotwireActivity
import dev.hotwire.navigation.navigator.NavigatorConfiguration
import dev.hotwire.navigation.navigator.NavigatorHost
import dev.hotwire.navigation.tabs.HotwireBottomNavigationController
import dev.hotwire.navigation.tabs.HotwireBottomTab
import dev.hotwire.navigation.tabs.navigatorConfigurations
import dev.hotwire.navigation.util.applyDefaultImeWindowInsets
import kotlinx.parcelize.Parcelize

class MainActivity : HotwireActivity() {
    private lateinit var bottomNavigationController: HotwireBottomNavigationController

    /**
     * Reset all non-active navigators, skipping navigators that haven't started.
     * Will cause started navigators to route to a dialog, if their root is so configured.
     *
     * Used after sign in.
     */
    fun resetBackgroundNavigation() {
        delegate.resetNavigators()
        navigatorConfigurations().forEach { configuration ->
            val navigatorHost = delegate.findNavigatorHost(configuration.navigatorHostId)
            if (delegate.currentNavigator != navigatorHost) {
                if (navigatorHost?.isReady() == true) {
                    navigatorHost.navigator.reset()
                }
            }
        }
    }

    /**
     * Completely tear down and restart all navigators by destroying the host fragments and
     * creating new ones. Tabs that have been lazily loaded will lazy load again, meaning tabs
     * whose root triggers a dialog will not trigger the dialog when this is called.
     *
     * Selects the primary tab again.
     *
     * Used after sign out.
     */
    fun resetAllNavigation() {
        bottomNavigationController.selectTab(0)

        tabConfiguration.forEachIndexed { index, _ ->
             supportFragmentManager.beginTransaction().also { transaction ->
                 supportFragmentManager.findFragmentById(navigatorHostIds[index])?.let {
                     transaction.remove(it)
                 }
                 transaction.add(navigatorHostIds[index], NavigatorHost())
                 transaction.commitNow()
            }
        }
    }

    override fun navigatorConfigurations(): List<NavigatorConfiguration> {
        return bottomNavigationTabs.navigatorConfigurations
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        // We have to do this before the call to super, since that will attempt to
        // restore any pre-existing navigator fragments, and if the configuration has
        // changed since boot the mismatch will cause a crash
        restoreTabConfiguration(savedInstanceState)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.root).applyDefaultImeWindowInsets()

        createNavigatorHostFragmentsForTabs()
        configureBottomNavigation(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putSerializable(
            SAVED_STATE_KEY_TAB_CONFIGURATION,
            ArrayList<Tab>(tabConfiguration)
        )
        outState.putInt(
            SAVED_STATE_KEY_SELECTED_TAB_INDEX,
            navigatorHostIds.indexOf(bottomNavigationController.currentTab.configuration.navigatorHostId)
        )
    }

    private fun restoreTabConfiguration(savedInstanceState: Bundle?) {
        savedInstanceState?.getSerializable(
            SAVED_STATE_KEY_TAB_CONFIGURATION,
            ArrayList::class.java
        )?.let {
            restoredTabConfiguration = it.filterIsInstance<Tab>()
        }
    }

    private fun createNavigatorHostFragmentsForTabs() {
        tabConfiguration.forEachIndexed { index, _ ->
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(navigatorHostIds[index], NavigatorHost())
            transaction.commitNow()
        }
    }

    private fun configureBottomNavigation(savedInstanceState: Bundle?) {
        bottomNavigationController = HotwireBottomNavigationController(
            activity = this,
            view = findViewById(R.id.bottom_navigation),
            lazyLoadTabs = true,
        )
        bottomNavigationController.load(
            bottomNavigationTabs,
            selectedTabIndex = savedInstanceState?.getInt(SAVED_STATE_KEY_SELECTED_TAB_INDEX) ?: 0
        )
    }

    private val bottomNavigationTabs: List<HotwireBottomTab> by lazy {
        tabConfiguration.mapIndexed { index, tab ->
            HotwireBottomTab(
                title = tab.title,
                iconResId = tab.iconResourceId,
                configuration = NavigatorConfiguration(
                    name = "tab-${tab.title}",
                    navigatorHostId = navigatorHostIds[index],
                    startLocation = "http://localhost:3000${tab.path}"
                )
            )
        }
    }

    private val tabConfiguration: List<Tab> by lazy {
        restoredTabConfiguration ?: loadedTabConfiguration
    }

    private var restoredTabConfiguration: List<Tab>? = null

    private val loadedTabConfiguration: List<Tab> by lazy {
        (Hotwire.config.pathConfiguration.settings["tabs"] as? List<*>)
            .orEmpty()
            .filterIsInstance<Map<String, String>>()
            .map {
                Tab(
                    title = it["title"] ?: "",
                    icon = it["icon"] ?: "",
                    path = it["path"] ?: ""
                )
            }
    }

    private val navigatorHostIds = intArrayOf(
        R.id.navigator_host_0,
        R.id.navigator_host_1,
        R.id.navigator_host_2,
        R.id.navigator_host_3,
        R.id.navigator_host_4
    )

    companion object {
        const val SAVED_STATE_KEY_TAB_CONFIGURATION = "MainActivity_TabConfiguration"
        const val SAVED_STATE_KEY_SELECTED_TAB_INDEX = "MainActivity_SelectedTabIndex"
    }

    @Parcelize
    data class Tab(val title: String, val icon: String, val path: String) : Parcelable {
        val iconResourceId: Int
            @DrawableRes get() = R.drawable.ic_launcher_foreground
    }
}