package tech.tonyrowan.birdwatch

import androidx.core.net.toUri
import dev.hotwire.core.turbo.visit.VisitProposal
import dev.hotwire.navigation.activities.HotwireActivity
import dev.hotwire.navigation.navigator.NavigatorConfiguration
import dev.hotwire.navigation.routing.Router

class SignInRouteDecisionHandler : Router.RouteDecisionHandler {
    override val name = "sign-in"

    override fun matches(proposal: VisitProposal, configuration: NavigatorConfiguration): Boolean {
        return proposal.location.startsWith("http://localhost:3000/native/sign_in")
    }

    override fun handle(
        proposal: VisitProposal,
        configuration: NavigatorConfiguration,
        activity: HotwireActivity
    ): Router.Decision {
        val destinationUrl = proposal.location.toUri().getQueryParameter("destination_url")
        destinationUrl?.let {
            if (destinationUrl.startsWith("http://localhost:3000")) {
                activity.delegate.currentNavigator?.route(destinationUrl)
            }
        }
        (activity as? MainActivity)?.resetBackgroundNavigation()
        return Router.Decision.CANCEL
    }
}