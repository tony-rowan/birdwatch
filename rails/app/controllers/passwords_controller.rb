class PasswordsController < ApplicationController
  allow_unauthenticated_access
  before_action :require_user_or_redirect, only: %i[edit update]

  def new
  end

  def edit
  end

  def update
    if user.update(params.permit(:password, :password_confirmation))
      user.sessions.destroy_all
      redirect_to account_path, notice: "Password has been reset. You can now sign in with the new password."
    else
      redirect_to edit_password_path(params[:token]), alert: "Passwords did not match."
    end
  end

  private

  def require_user_or_redirect
    user
  end

  def user
    @_user = User.find_by_password_reset_token!(params[:token])
  rescue ActiveSupport::MessageVerifier::InvalidSignature
    redirect_to new_password_path, alert: "Password reset link is invalid or has expired."
  end
end
