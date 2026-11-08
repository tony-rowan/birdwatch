class CreateBirdLikes < ActiveRecord::Migration[8.1]
  def change
    create_table :bird_likes do |t|
      t.references :user, null: false, foreign_key: true
      t.references :bird, null: false, foreign_key: true

      t.timestamps

      t.index [:user_id, :bird_id], unique: true
    end
  end
end
