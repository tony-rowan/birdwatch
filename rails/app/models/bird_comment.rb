class BirdComment < ApplicationRecord
  belongs_to :bird
  belongs_to :user

  validates :body, presence: true
end
