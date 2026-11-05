import { Controller } from "@hotwired/stimulus"

// Connects to data-controller="auto-navigate"
export default class extends Controller {
  static values = {
    destination: String
  }

  #_onBeforeCache

  connect() {
    if (this.hasDestinationValue) {
      Turbo.visit(this.destinationValue)
    }

    this.#_onBeforeCache = this.#onBeforeCache.bind(this)
    document.addEventListener("turbo:before-cache", this.#_onBeforeCache)

  }

  disconnect() {
    if (this.#_onBeforeCache) {
      document.removeEventListener("turbo:before-cache", this.#_onBeforeCache)
      this.#_onBeforeCache = null
    }
  }

  #onBeforeCache() {
    delete this.element.dataset.autoNavigateDestinationValue
  }
}
