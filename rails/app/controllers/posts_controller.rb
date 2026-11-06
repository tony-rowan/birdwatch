class PostsController < ApplicationController
  allow_unauthenticated_access only: %i[ index ]

  before_action :set_post, only: %i[ edit destroy ]

  # GET /posts
  def index
    @posts = Post.all
  end

  # GET /posts/new
  def new
    @post = Post.new
  end

  # GET /posts/1/edit
  def edit
  end

  # POST /posts
  def create
    @post = Post.new(post_params)

    if @post.save
      redirect_to posts_path, notice: "Post was successfully created."
    else
      render :new, status: :unprocessable_content
    end
  end

  # DELETE /posts/1
  def destroy
    @post.destroy!
    redirect_to posts_path, notice: "Post was successfully destroyed.", status: :see_other
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_post
      @post = Post.find(params.expect(:id))
    end

    # Only allow a list of trusted parameters through.
    def post_params
      params.expect(post: [ :body ]).merge(user: Current.session.user)
    end
end
