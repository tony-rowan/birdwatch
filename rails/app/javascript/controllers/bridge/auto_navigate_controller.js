import { BridgeComponent } from "@hotwired/hotwire-native-bridge"

// Connects to data-controller="bridge--auto-navigate"
export default class extends BridgeComponent {
  static component = "auto-navigate"

  static values = {
    destination: String
  }

  #_onBeforeCache

  connect() {
    super.connect()

    if (this.hasDestinationValue) {
      this.send("connect", { destination: this.destinationValue })
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
    delete this.element.dataset["bridge-AutoNavigateDestinationValue"]
  }
}
