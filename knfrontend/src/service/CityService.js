import axios from "axios";

export async function getCityPage(name, page, size, token) {
    try {
        return await axios({
            method: "get",
            url: "http://localhost:8080/city/page",
            params: {name: name, page: page, size: size},
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });
    } catch (error) {
        console.log(error); // handle the error
    }
}

export async function updateCity(id, name, photoLink, token) {
    try {
        return await axios({
            method: "put",
            url: "http://localhost:8080/city",
            data: {id: id, name: name, photo: photoLink},
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });
    } catch (error) {
        console.log(error); // handle the error
    }
}
