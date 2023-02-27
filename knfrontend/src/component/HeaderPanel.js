import React from 'react';
import Button from "react-bootstrap/Button";
import {useNavigate} from "react-router-dom";

function HeaderPanel() {

    let navigate = useNavigate();

    const buttonClick = () => {
        sessionStorage.clear();
        navigate('/');
    }
    return (
        <>
            <div className="header-panel city-table-container">
                <Button className="lrbutton logout-button" onClick={() => buttonClick()}>Logout</Button>
            </div>
        </>
    );
}

export default HeaderPanel;
