<script>
import axios from "axios";

export default {
  props: {
    song: Object
  },
  emits: ['reload'],
  data() {
    return {
    }
  },
  mounted() {
  },
  methods: {
    deleteSong(song) {
      axios.delete(song._links.song.href)
          .then(() => this.$emit('reload'))
    }
  }
}

</script>

<template>

  <div class="flex flex-row gap-4">
    <router-link :to="{name:'editor', params: {song: song._links.self.href}}" class="border border-black px-2 py-1 rounded-md">Edit</router-link>
    <button class="border border-black px-2 py-1 rounded-md" @click="deleteSong(this.song)">Delete</button>
  </div>

  <div>
    <p class="text-xl font-bold mb-2">{{ song.title }}</p>
    <p class="text-gray-700 mb-2">Artist: {{ song.artist.name }}</p>
    <p class="text-gray-700 mb-2">Genre: {{ song.genres.join() }}</p>
    <p class="text-gray-700">Length: {{ song.length }}</p>
  </div>
</template>
