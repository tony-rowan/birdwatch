class AccountsController < ApplicationController
  # /account is a tab root and needs to display an unathenticated page
  allow_unauthenticated_access only: %i[new create show]
  before_action :save_after_authentication_url, only: %i[show]

  def new
    render locals: {
      user: User.new
    }
  end

  def create
    user = User.new(user_params)

    if user.save
      start_new_session_for(user)
      redirect_to after_authentication_url, notice: "Successfully signed up!"
    else
      render :new, status: :unprocessable_content, locals: {
        user:
      }
    end
  end

  def show
  end

  private

  def user_params
    params.expect(user: [:username, :password])
  end
end
