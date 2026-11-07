module Api
  module V1
    module Android
      class ConfigurationController < ApplicationController
        allow_unauthenticated_access

        def show
          render json: {
            settings: {
              tabs: [
                {
                  title: "Posts",
                  path: "/posts",
                  icon: "list"
                },
                {
                  title: "Profile",
                  path: "/account",
                  icon: "user"
                }
              ]
            },
            rules: [
              {
                patterns: [
                  ".*"
                ],
                properties: {
                  pull_to_refresh_enabled: true
                }
              },
              {
                patterns: [
                  "/new$",
                  "/edit$"
                ],
                properties: {
                  context: "modal",
                  uri: "hotwire://fragment/web/modal/sheet",
                  pull_to_refresh_enabled: false
                }
              }
            ]
          }
        end
      end
    end
  end
end
