class CreateBirdComments < ActiveRecord::Migration[8.1]
  def change
    create_table :bird_comments do |t|
      t.references :user, null: false, foreign_key: true
      t.references :bird, null: false, foreign_key: true
      t.string :body, null: false

      t.timestamps
    end
  end
end
