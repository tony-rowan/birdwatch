class NativeController < ApplicationController
  allow_unauthenticated_access

  # Both of these actions render an empty template that will never be seen,
  # but without it Hotwire Native doesn't see the new destination and the
  # route decision handler can't see or react to the change in destination.

  def sign_in
  end

  def sign_out
  end
end
