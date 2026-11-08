require "test_helper"

class UserTest < ActiveSupport::TestCase
  test "downcases and strips username" do
    user = User.new(username: " DOWNCASED@EXAMPLE.COM ")
    assert_equal("downcased@example.com", user.username)
  end
end
