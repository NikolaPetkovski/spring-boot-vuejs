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
      genres: ['1a', 'a2', 'a3']
    }
  },
  validations() {
    return {
      /*
      song: {
        title: {required, alpha, minLength: minLength(2), maxLength: maxLength(30)},
        artist: {required, alpha, minLength: minLength(2), maxLength: maxLength(30)},
        length: {required, minLength: minLength(3), maxLength: maxLength(5), lengthRegex}
      }
       */

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
      console.log(this.artist)
    })
  },
  methods: {
    handleFileUpload(event) {
      let reader = new FileReader()
      reader.readAsDataURL(event.target.files[0])
      reader.onload = (event) => {
        this.song.musicData = event.target.files[0]
      }
      console.log(this.musicData)
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
        <select v-if="song.artist" v-model="song.artist" required>
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
        <input type="file" class="cursor-pointer w-fit border h-fit" accept=".wav,.mp3" id="upload"
               @change="handleFileUpload">
      </div>
    </div>
    <button class="border p-1" :class="{'cursor-not-allowed' : !v$.$pending && v$.$error}"
            :disabled="!v$.$pending && v$.$error" @click="updateSong">
      Speichern
    </button>

  </div>
</template>