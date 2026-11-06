# Hotwire Native Example

An example project, demonstrating how to use [Hotwire Native][hotwire-native-home],
covering what most real apps will need and not touched on in the 
[official documentation][hotwire-native-docs] or samples:

- Authentication (resetting other tabs on sign in or sign out)
- Bottom navigation driven by remote path configuration
- Authenticated tab roots

## Authentication

This is actually fairly simple, it's just not in the docs or samples at all so
you're left to your own devices. The approach demonstrated here is based on 
changing the after sign in/out redirect for the native apps to point to a 
special route: `/native/sign_in` and `/native/sign_out` and then detecting the
attempt to visit that route in the app and reacting accordingly.

### Rails

This is a simple router/controller/view setup. Note that the view is never seen
but needs to fully render a page for the native apps to actually pick up the
destination change and trigger the handlers. A simple `head` or even 
`render inline:` won't cut it.

### Android

This involves setting up a [`RouteDecisionHandler`][route-decision-handler] to
cancel the visit to the fake path and then resetting the navigation system
and re-routing on as necessary.

- [`SignInRouteDecisionHandler`][sign-in-route-decision-handler]
- [`SignOutRouteDecisionHandler`][sign-in-route-decision-handler]

### iOS

TODO

## Remote Configuration Driven Bottom Navigation

The official tabs and sample include bottom navigation, the de facto standard
layout for modern apps, but the configuration is hard-coded in the native apps
themselves. Not necessarily a problem - it's not something that changes that
often - but for a system so well designed to be entirely server driven so
changes don't need an app store release it feels like something that can an
should be dynamically configured.

### Rails

Create an API endpoint for the path configuration, versioned and different for
Android and iOS. Then include a block for you tab configuration within the
`settings` block. This demo uses `tabs`. Make sure to have an up to date copy
bundled along with the apps so first boots can use the disk version immediately.

```json
{
  "settings": {
    "tabs": [
      {
        "title": "Posts",
        "path": "/posts",
        "icon": "list"
      },
      {
        "title": "Profile",
        "path": "/account",
        "icon": "user"
      }
    ]
  },
  "rules": [
    ...
  ]
}
```

### Android

Create five (the maximum number of tabs supported by a
[`BottomNavigationView`][bottom-navigation-view]) blank `FragmentContainerView`s
in the [`activity_main.xml`][main-activity-layout] layout.

Then in [`MainActivity`][main-activity] add a [`NavigatorHost`][navigator-host]
fragment for each of the tabs listed in the configuration file, exposing
their configurations as the Activity's `navigatorConfigurations` method.
The bottom navigation is then set up as in the offical docs.

An additional complication exists where a changed configuration will be visible
when calling for the hotwire config, so a dynamically fetched tab list will
reflect a change in configuration during a rotation or other tear down and
rebuild event which would result in a crash. The restored fragments wouldn't
match the `navigatorConfigurations` method. To avoid this, once the tab
configuration has been loaded, it needs to be persisted and restored via the 
`Bundle` mechanism to ensure it is consistent throughout the apps lifetime. 
Only on a restart of the app (which rebuilds the fragments from scratch) will 
the new mechanisam appear.

### iOS

TODO

## Authenticated Tab Roots

Tab configuration driven by sign in state is not ideal, though possible but not
detailed here. By default, the app needs to be rebooted for changes to the path
configuration to take effect and the new configuration is only ever fetched at
boot. That's two full boot cycles to pick up new navigation configuration.

This means that if you have tabs whose roots are authenticated (such as the
account tab) then it will redirect to the sign in page on tap. If the sign in
page is (like many apps) displayed in a modal, this is not supported. If a
bottom navigation tab root is a dialog (including redirects) and the the dialog
is cancelled, the tab root is left with a blank and unrecoverably broken
navigation tree.

To get around this, tab roots should always present a default context page (not
a modal context one). For authenticated pages, this means rendering a page that
explains the page requires to user to sign in and to present a CTA to allow 
them to do so. This demo makes this a little easier on the user by automatically
opening the sign in page via a [bridge component][bridge-components].

- [Rails: `bridge--auto-navigate-controller`][rails-bridge-auto-navigate-controller]
- [Android: `AutoNavigateComponent`][android-auto-navigate-component]
- iOS: TODO

[hotwire-native-home]: https://native.hotwired.dev/
[hotwire-native-docs]: https://native.hotwired.dev/overview/how-it-works
[route-decision-handler]: https://github.com/hotwired/hotwire-native-android/blob/main/navigation-fragments/src/main/java/dev/hotwire/navigation/routing/Router.kt
[bottom-navigation-view]: https://developer.android.com/reference/com/google/android/material/bottomnavigation/BottomNavigationView
[sign-in-route-decision-handler]: ./android/app/src/main/java/tech/tonyrowan/hotwirenativeexample/SignInRouteDecisionHandler.kt
[sign-out-route-decision-handler]: ./android/app/src/main/java/tech/tonyrowan/hotwirenativeexample/SignOutRouteDecisionHandler.kt
[navigator-host]: https://github.com/hotwired/hotwire-native-android/blob/main/navigation-fragments/src/main/java/dev/hotwire/navigation/navigator/NavigatorHost.kt
[main-activity]: ./android/app/src/main/java/tech/tonyrowan/hotwirenativeexample/MainActivity.kt
[main-activity-layout]: ./android/app/src/main/res/layout/activity_main.xml
[rails-bridge-auto-navigate-controller]: ./rails/app/javascript/controllers/bridge/auto_navigate_controller.js
[android-auto-navigate-component]: ./android/app/src/main/java/tech/tonyrowan/hotwirenativeexample/AutoNavigateComponent.kt
[bridge-components]: https://native.hotwired.dev/reference/bridge-components
