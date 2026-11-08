# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# This file is the source Rails uses to define your schema when running `bin/rails
# db:schema:load`. When creating a new database, `bin/rails db:schema:load` tends to
# be faster and is potentially less error prone than running all of your
# migrations from scratch. Old migrations may fail to apply correctly if those
# migrations use external dependencies or application code.
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema[8.1].define(version: 2026_11_07_213052) do
  create_table "bird_comments", force: :cascade do |t|
    t.integer "user_id", null: false
    t.integer "bird_id", null: false
    t.string "body", null: false
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.index ["bird_id"], name: "index_bird_comments_on_bird_id"
    t.index ["user_id"], name: "index_bird_comments_on_user_id"
  end

  create_table "bird_likes", force: :cascade do |t|
    t.integer "user_id", null: false
    t.integer "bird_id", null: false
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.index ["bird_id"], name: "index_bird_likes_on_bird_id"
    t.index ["user_id", "bird_id"], name: "index_bird_likes_on_user_id_and_bird_id", unique: true
    t.index ["user_id"], name: "index_bird_likes_on_user_id"
  end

  create_table "birds", force: :cascade do |t|
    t.string "name", null: false
    t.string "description", null: false
    t.string "image_url", null: false
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  create_table "sessions", force: :cascade do |t|
    t.integer "user_id", null: false
    t.string "ip_address"
    t.string "user_agent"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.index ["user_id"], name: "index_sessions_on_user_id"
  end

  create_table "users", force: :cascade do |t|
    t.string "username", null: false
    t.string "password_digest", null: false
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.index ["username"], name: "index_users_on_username", unique: true
  end

  add_foreign_key "bird_comments", "birds"
  add_foreign_key "bird_comments", "users"
  add_foreign_key "bird_likes", "birds"
  add_foreign_key "bird_likes", "users"
  add_foreign_key "sessions", "users"
end
