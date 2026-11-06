package tech.tonyrowan.hotwirenativeexample

import dev.hotwire.core.turbo.visit.VisitProposal
import dev.hotwire.navigation.activities.HotwireActivity
import dev.hotwire.navigation.navigator.NavigatorConfiguration
import dev.hotwire.navigation.routing.Router

class SignOutRouteDecisionHandler: Router.RouteDecisionHandler {
    override val name = "sign-out"

    override fun matches(proposal: VisitProposal, configuration: NavigatorConfiguration): Boolean {
        return proposal.location == "http://localhost:3000/native/sign_out"
    }

    override fun handle(
        proposal: VisitProposal,
        configuration: NavigatorConfiguration,
        activity: HotwireActivity
    ): Router.Decision {
        (activity as? MainActivity)?.resetAllNavigation()
        return Router.Decision.CANCEL
    }
}