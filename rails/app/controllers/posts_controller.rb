class PostsController < ApplicationController
  allow_unauthenticated_access only: %i[index]

  def index
    render locals: {
      posts: Post.all
    }
  end

  def new
    render locals: {
      post: Post.new
    }
  end

  def create
    post = Post.new(post_params)

    if post.save
      redirect_to posts_path, notice: "Post was successfully created."
    else
      render :new, status: :unprocessable_content, locals: {
        post:
      }
    end
  end

  def destroy
    @post.destroy!
    redirect_to posts_path, notice: "Post was successfully destroyed.", status: :see_other
  end

  private

  def post_params
    params.expect(post: [:body]).merge(user: Current.user)
  end
end
