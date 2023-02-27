import React, {useEffect, useState} from 'react';
import Button from "react-bootstrap/Button";
import * as cityService from "../service/CityService";
import {useNavigate} from "react-router-dom";
import {Card} from "react-bootstrap";
import HeaderPanel from "./HeaderPanel";

function EditPopup() {

    let navigate = useNavigate();

    const id = sessionStorage.getItem('id');
    const [name, setName] = useState(sessionStorage.getItem('cityname'));
    const [photo, setPhoto] = useState(sessionStorage.getItem('photo'));
    const [canEdit, changeCanEdit] = useState(false);

    useEffect(() => {
        if (sessionStorage.getItem('userrole') === 'ROLE_ALLOW_EDIT') {
            changeCanEdit(true);
        }
    }, [])

    const saveCity = async () => {
        let result = await cityService.updateCity(id, name, photo, sessionStorage.getItem('jwtToken'));
        if (result.status === 200) {
            navigateToMainPanel()
        }
    }

    const navigateToMainPanel = () => {
        navigate("/mainpanel");
    }

    const addDefaultSrc = (ev) => {
        ev.target.src = require('../assets/nophoto.png');
    }

    return (
        <>
            <HeaderPanel></HeaderPanel>
            <Card className="search-panel header-panel">
                <div className="edit-panel">
                    <input type={"text"}
                           placeholder={name}
                           defaultValue={name}
                           disabled={!canEdit}
                           onChange={e => setName(e.target.value)
                           }
                    ></input>
                    <input type={"text"}
                           defaultValue={photo}
                           disabled={!canEdit}
                           placeholder={photo}
                           onChange={e => setPhoto(e.target.value)}
                    ></input>
                    <Button className="lrbutton search-button gotopage-button" disabled={!canEdit} onClick={() => saveCity()}>Save</Button>
                </div>
                <div>
                    {
                        !canEdit && <Card className="edit-error-panel">You need to have 'EDITOR' role to update</Card>
                    }
                </div>
                <Button className="lrbutton search-button gotopage-button back-button" onClick={() => navigateToMainPanel()}>Back</Button>
            </Card>
            <img className="edit-screen-image" onError={addDefaultSrc} src={photo}/>
        </>
    );
}

export default EditPopup;
