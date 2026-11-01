require "test_helper"

class Api::V1::Android::ConfigurationControllerTest < ActionDispatch::IntegrationTest
  test "should get show" do
    get api_v1_android_configuration_show_url
    assert_response :success
  end
end
