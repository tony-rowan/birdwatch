module Birds
  class ApplicationController < ::ApplicationController
    private

    def bird
      @_bird ||= Bird.find(params.expect(:bird_id))
    end
  end
end
