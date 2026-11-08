module Birds
  class CommentsController < ApplicationController
    def new
      render locals: {
        bird: bird,
        comment: bird.comments.new
      }
    end

    def create
      comment = bird.comments.new(bird_comment_params)

      if comment.save
        redirect_to bird_path(bird), notice: "Comment added!"
      else
        render :new, status: :unprocessable_content, locals: {
          bird:,
          comment:
        }
      end
    end

    def destroy
      BirdComment.find_by(params.expect(:id))&.destroy
      head :no_content
    end

    private

    def bird_comment_params
      params.expect(bird_comment: :body).merge(user: Current.user)
    end
  end
end
