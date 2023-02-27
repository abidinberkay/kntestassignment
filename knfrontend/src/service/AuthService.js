import axios from 'axios';

export async function login(username, password) {
    return (await axios({
        method: "post",
        url: "http://localhost:8080/auth/login",
        data: {username: username, password: password}
    }))
}

export async function register(username, password, role) {
    return (await axios({
        method: "post",
        url: "http://localhost:8080/auth/signup",
        data: {username: username, password: password, role}
    }))
}
