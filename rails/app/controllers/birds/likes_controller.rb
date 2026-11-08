module Birds
  class LikesController < ApplicationController
    def create
      bird.likes.create(user: Current.user)
      redirect_to bird_path(bird)
    end

    def destroy
      bird.likes.where(user: Current.user).destroy_all
      redirect_to bird_path(bird)
    end
  end
end
