class AccountsController < ApplicationController
  # /account is a tab root and needs to display an unathenticated page
  allow_unauthenticated_access only: :show

  def show
  end
end
