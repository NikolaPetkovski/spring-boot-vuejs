import { createRouter, createWebHistory } from 'vue-router'
import Song from "../views/Song.vue";
import Editor from "../components/EditorComp.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [{
    path: '/',
    component: Song
  },{
    path: '/editor/:song',
    name: 'editor',
    component: Editor
  }]
})
export default router
