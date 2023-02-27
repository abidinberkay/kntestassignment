import React, {useState} from 'react';
import '../style/MainStyle.css';
import '../App.css';
import Button from 'react-bootstrap/Button'
import * as authService from '../service/AuthService';
import {useNavigate} from "react-router-dom";
import {Card} from "react-bootstrap";

function RegisterPage() {

    const [userName, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [isEditor, setEditor] = useState(['ROLE_DISALLOW_EDIT']);

    const [errorMessage, setErrorMessage] = useState('');
    const [isRegisterLabelVisible, setRegisterLabelVisible] = useState(false);

    let navigate = useNavigate();

    const requestLogin = async (username, password) => {
        try {
            await authService.register(username, password, isEditor);

            setErrorMessage("User created successfully. Go to login page to sign in");
            setRegisterLabelVisible(true);
        } catch (err) {
            if (err.response.data.message === 'USER_EXISTS') {
                setErrorMessage('User with the same name already exists');
            } else {
                setErrorMessage(err.response.data.message);
            }

            setRegisterLabelVisible(true);
        }
    }

    const navigateToLoginPage = () => {
        navigate("/");
    }

    return (
        <>
            <div className='login-form'>
                <Card className="search-panel">
                    <div className="login-register-panel">
                        <input type="text" placeholder="Enter Username Here" value={userName}
                               onChange={e => setUsername(e.target.value)}></input>
                        <input type="password" placeholder="Enter Password Here" value={password}
                               onChange={e => setPassword(e.target.value)}></input>

                    </div>
                    <div>
                        <label className='login-register-error-msg'
                               hidden={!isRegisterLabelVisible}>{errorMessage}</label>
                    </div>
                    <div>
                        <label className='login-register-error-msg'
                               hidden={!(userName.length === 0 || password.length === 0)}>Please enter both
                            field</label>
                    </div>
                    <div className='radio-group'>
                        <input type="radio" name="usertype" value="ROLE_ALLOW_EDIT"
                               onChange={e => setEditor([e.target.value])}/> Editor
                        <input type="radio" name="usertype" value="ROLE_DISALLOW_EDIT"
                               defaultChecked={true}
                               onChange={e => setEditor([e.target.value])}/> Reader
                    </div>
                </Card>
                <br/>
                <br/>
                <Button className='lrbutton lrbutton-secondary' variant="primary"
                        disabled={(userName.length === 0 || password.length === 0)}
                        onClick={() => requestLogin(userName, password)}>Register</Button>
                <Button className='lrbutton' variant="primary" onClick={() => navigateToLoginPage()}>Back To
                    Login Page</Button>
            </div>
        </>
    )
}

export default RegisterPage;
