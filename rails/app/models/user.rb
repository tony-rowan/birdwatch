class User < ApplicationRecord
  has_secure_password
  has_many :sessions, dependent: :destroy
  has_many :likes, class_name: "BirdLike"
  has_many :comments, class_name: "BirdComment"

  normalizes :username, with: ->(e) { e.strip.downcase }
end
