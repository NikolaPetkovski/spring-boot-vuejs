<script>
import axios from "axios";

import {useVuelidate} from '@vuelidate/core'
import {required, minLength, maxLength, helpers} from '@vuelidate/validators'

const lengthRegex = helpers.regex("^\\d{2}[:]\\d{2}$")

export default {
  data() {
    return {
      v$: useVuelidate(),
      song: {},
      artist: {},
      version: 0,
      genres: ['1a', 'a2', 'a3'],
      confirmDelete: false,
    }
  },
  validations() {
    return {
      song: {
        title: {required, minLength: minLength(2), maxLength: maxLength(30)},
        length: {required}
      }
    }
  },
  mounted() {
    // console.log(this.$route.params.song)
    if (this.$route.params.song !== 'add') {
      axios.get(this.$route.params.song + "?projection=kompakt")
          .then(response => {
                this.song = response.data
                this.version = this.song.etag
              }
          ).catch(error => {
        console.error('Error fetching song data:', error);
        alert('Error fetching song data. Please try again.');
      });
    }
    axios.get('http://localhost:8080/api/artist').then(response => {
      this.artist = response.data._embedded.artist
      //console.log(this.artist)
    })

    if (!this.song.artist && this.artist.length > 0) {
      this.song.artist = this.artist[0]._links.self.href;
    }
  },
  methods: {
    handleFileUpload(event) {
      const reader = new FileReader()

      reader.onload = () => {
        this.song.data = reader.result
      }

      if (event.target.files[0]) {
        reader.readAsDataURL(event.target.files[0])
      }
    },
    updateSong() {
      this.v$.$validate()

      if (this.v$.$error) {
        return;
      }

      if (this.song['_links']) {
        axios.patch(this.song._links.self.href, this.song, {
          headers: {
            'If-Match': this.song.version
          }
        })
            .then(response => {
              console.log(response);
              console.log('Song erfolgreich geändert');
            })
            .catch(error => {
              console.log(error);
              console.log('Error updating song. Please try again.');
            })
      } else {
        axios.post('http://localhost:8080/api/songs', this.song)
            .then(function (response) {
              console.log(response);
            }).then(() =>
            alert('Song erfolgreich hinzugefügt')
        ).catch(function (error) {
          console.log(error);
        });
      }
    },
    deleteMusicData() {
      this.song.data = null
      this.confirmDelete = false
    }
  }
}
</script>

<template>
  <div class="m-2">
    <router-link to="/" class="p-2 border rounded-md mt-2">Zurück</router-link>
    <div class="flex flex-row gap-8 mt-8 mb-2">
      <div class="flex flex-col gap-2 w-1/3">
        <input class="border p-1" type="text" placeholder="title" v-model="song.title">
        <div v-if="v$.song.title.$error">{{ v$.song.title.$errors[0].$message }}</div>

        <!-- Artist -->
        <select class="border" v-model="song.artist" required>
          <option v-for="(item,index) in this.artist" :key="index" :value="item._links.self.href">{{
              item.name
            }}
          </option>
        </select>

        <!-- Genre -->
        <select multiple v-model="song.genres" required>
          <option v-for="(item,index) in this.genres" :key="index">{{ item }}</option>
        </select>

        <input class="border p-1" type="text" placeholder="length" v-model="song.length">
        <div v-if="v$.song.length.$error">{{ v$.song.length.$errors[0].$message }}</div>
      </div>

      <div class="flex flex-col gap-4">
        <input type="file" class="hidden" accept=".wav,.mp3,.mpeg,.m4a" id="upload"
               @change="handleFileUpload">
        <label for="upload" class="cursor-pointer w-fit border h-fit">Song hochladen</label>
        <div v-show="this.song.data" class="flex flex-col gap-4">
          <audio :src="this.song.data" controls v-show="this.song.data !== null">
          </audio>
          <div>
            <button v-if="this.song.data !== null && !confirmDelete" class="cursor-pointer w-fit border h-fit"
                    @click="confirmDelete = true">Song
              löschen
            </button>

            <div class="flex flex-col gap-2">
              <p v-show="confirmDelete">Willst du die Song Data wirklich löschen?</p>
              <div class="flex flex-row justify-around">
                <button v-if="this.confirmDelete" class="cursor-pointer w-fit border h-fit"
                        @click="this.confirmDelete = false">
                  Nein
                </button>
                <button v-if="this.confirmDelete" class="cursor-pointer w-fit border h-fit"
                        @click="deleteMusicData">Ja
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
    <button class="border p-1" :class="{'cursor-not-allowed' : !v$.$pending && v$.$error}"
            :disabled="!v$.$pending && v$.$error && song.artist === null" @click="updateSong">
      Speichern
    </button>

  </div>
</template>