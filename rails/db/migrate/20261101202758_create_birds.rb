class CreateBirds < ActiveRecord::Migration[8.1]
  def change
    create_table :birds do |t|
      t.string :name, null: false
      t.string :description, null: false
      t.string :image_url, null: false

      t.timestamps
    end
  end
end
