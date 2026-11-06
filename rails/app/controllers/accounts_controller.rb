class AccountsController < ApplicationController
  # /account is a tab root and needs to display an unathenticated page
  allow_unauthenticated_access only: %i[ new create show ]

  def new
    render locals: {
      user: User.new
    }
  end

  def create
    user = User.new(user_params)

    if user.save
      start_new_session_for(user)
      redirect_to posts_path, notice: "Successfully signed up!"
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
    params.expect(user: [:email_address, :password])
  end
end
