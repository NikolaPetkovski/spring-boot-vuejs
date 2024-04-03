<script>
import axios from 'axios'
import Song from '../components/Song.vue'

export default {
  components: {
    Song
  },
  data() {
    return {
      songs: [],
      currentPage: 1,
      pageSize: 5,
      totalPages: 1,
      editedSong: [],
      showEdit: false,
      searchInput: "",
      searchArtist: [],
      searchTitle: [],
      searchNull: false,
      isLoading: true,
    }
  },
  methods:
      {
        async loadSongs() {
          try {
            const response = await axios.get(`http://localhost:8080/api/songs?page=${this.currentPage - 1}&size=${this.pageSize}`);
            this.songs = response.data._embedded.songs;
            this.totalPages = response.data.page.totalPages;
          } catch (error) {
            console.error("Error loading songs:", error.message);
          }
        },
        async next() {
          if (this.currentPage < this.totalPages) {
            this.currentPage++
            await this.loadSongs()
          }
        },
        async previous() {
          if (this.currentPage > 1) {
            this.currentPage--
            await this.loadSongs()
          }
        },
        async firstPage() {
          this.currentPage = 1
          await this.loadSongs()
        },
        async lastPage() {
          this.currentPage = this.totalPages
          await this.loadSongs()
        },
      },
  watch: {

    async searchInput() {
      if (this.searchInput.length > 0) {
        try {
          const searchArtistResponse = await axios.get('http://localhost:8080/api/songs/search/findByArtistContainingIgnoreCase?artist=' + this.searchInput)
          const searchTitleResponse = await axios.get('http://localhost:8080/api/songs/search/findByTitleContainingIgnoreCase?title=' + this.searchInput)

          this.songs = searchArtistResponse.data._embedded.songs.concat(searchTitleResponse.data._embedded.songs)

          this.searchNull = this.songs.length === 0

          console.log(searchArtistResponse)
          console.log(searchTitleResponse)

        } catch (error) {
          console.error(error)
        }
      }
      if (this.searchInput.length === 0) {
        await this.loadSongs()
      }
    }
  },
  mounted() {
    this.loadSongs()
  },
}
</script>

<template>
  <div>
    <div class="flex flex-row justify-center gap-2 mt-2 mx-8 sticky top-2 z-[10]">
      <input type="text" class="border border-black rounded-lg py-1 px-2 flex-1 hover:drop-shadow-xl"
             placeholder="Search a song name or artist" v-model="searchInput">
      <router-link class="p-1 bg-white border border-black rounded-md" :to="{name: 'editor', params:{song: 'add'}}">Add
        Song
      </router-link>
    </div>

    <div v-for="song in songs" class="my-4 mx-8 p-2 rounded-lg bg-gray-200">
      <Song :song="song" @reload="loadSongs()"  :key="song._links.self.href"/>
    </div>

    <p v-show="searchNull" class="text-center text-2xl font-bold my-4">No songs can be found. Please adjust your
      search</p>

    <div v-show="!this.isLoading && !searchNull" class="flex flex-row gap-4 justify-center my-4">
      <button :disabled="this.currentPage == 1" @click="firstPage" class="border">First Page</button>
      <button :disabled="this.currentPage == 1" @click="previous" class="border">previous</button>
      <p>{{ currentPage }} von {{ totalPages }}</p>
      <button :disabled="this.currentPage == this.totalPages" @click="next" class="border">next</button>
      <button :disabled="this.currentPage == this.totalPages" @click="lastPage" class="border">Last Page</button>
    </div>
  </div>
</template>

<style>
</style>
