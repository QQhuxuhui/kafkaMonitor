import request from '../request.js'

const loginUrl = "/api/user/login";
const registerUrl = "/api/user/register";
const activeUrl = "/api/user/active";
const loginOutUrl = "/api/user/loginOut";

export function login(username, password) {
  return request.post(loginUrl, {
    "username": username,
    "password": password
  });
}

export function register(name, username, password, email) {
  return request.post(registerUrl, {
    "name": name,
    "username": username,
    "password": password,
    "email": email
  });
}

/**
 * 激活账号
 * @param username
 * @returns {*|Promise|Promise<any>}
 */
export function active(username, password) {
  return request.post(activeUrl, {
    "username": username,
    "password": password
  });
}

export function loginOut() {
  return request.get(loginOutUrl);
}
