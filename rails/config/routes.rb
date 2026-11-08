Rails.application.routes.draw do
  # Reveal health status on /up that returns 200 if the app boots with no exceptions, otherwise 500.
  # Can be used by load balancers and uptime monitors to verify that the app is live.
  get "up" => "rails/health#show", as: :rails_health_check

  root "birds#index"

  resource :session

  resources :birds, only: %i[ index show ] do
    scope module: :birds do
      resources :likes, only: %i[ create destroy ]
      resources :comments, only: %i[ new create destroy]
    end
  end

  resource :account, only: [:new, :create, :show]

  namespace :native do
    get "sign_in"
    get "sign_out"
  end

  namespace :api do
    namespace :v1 do
      namespace :android do
        resource :configuration, controller: :configuration, only: :show
      end
    end
  end
end
