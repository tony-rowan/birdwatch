class NativeController < ApplicationController
  allow_unauthenticated_access

  def sign_out
    # This renders a template that should never be seen, but without it
    # Hotwire Native doesn't see the new destination and the handler can't
    # see or react to the change.
  end
end
