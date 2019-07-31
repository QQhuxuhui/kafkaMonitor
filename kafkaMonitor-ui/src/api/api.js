import request from '../request.js'

export function get(api, params) {
  return request.get(api, params);
}

export function post(api, params) {
  return request.post(api, params);
}

