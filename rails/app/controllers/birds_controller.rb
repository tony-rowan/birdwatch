class BirdsController < ApplicationController
  allow_unauthenticated_access only: %i[ index show ]

  def index
    render locals: {
      birds: Bird.all.includes(:likes, :comments)
    }
  end

  def show
    render locals: {
      bird: Bird.find(params.expect(:id))
    }
  end
end
