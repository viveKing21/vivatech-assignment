import axios from 'axios';
import CookieHelper from './CookieHelper';

const token = CookieHelper.getCookie('_token')

const config = {
  withCredentials: true,
  baseURL: "http://localhost:8000",
}

if(token) config.headers = {
  'Authorization': 'Bearer ' + token
}

const instance = axios.create(config);

export default instance;