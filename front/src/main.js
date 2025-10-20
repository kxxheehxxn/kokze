import './assets/main.css';
import 'bootstrap/dist/css/bootstrap.css';
import 'vue-awesome-paginate/dist/style.css';
import { createApp } from 'vue';
import { createPinia } from 'pinia';

import App from './App.vue';
import router from './router';
import VueAwesomePaginate from 'vue-awesome-paginate';
const app = createApp(App);
app.use(VueAwesomePaginate);
app.use(createPinia());
app.use(router);

app.mount('#app');
