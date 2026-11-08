class Bird < ApplicationRecord
  has_many :likes, class_name: "BirdLike"
  has_many :comments, class_name: "BirdComment"

  validates :name, presence: true
  validates :description, presence: true
  validates :image_url, presence: true
end
