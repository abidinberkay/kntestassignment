import React, {useState} from 'react';
import '../style/MainStyle.css';
import '../App.css';
import Button from 'react-bootstrap/Button'
import * as authService from '../service/AuthService';
import {useNavigate} from "react-router-dom";
import {Card} from "react-bootstrap";

function LoginPage() {

    const [userName, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [isLoginErrorLabelVisible, setLoginLabelVisible] = useState(false);

    let navigate = useNavigate();

    const requestLogin = async (username, password) => {
        try {
            let response = await authService.login(username, password);
            sessionStorage.setItem('userrole', response.data.userDetails.authorities[0].authority);
            sessionStorage.setItem('jwtToken', response.data.jwtToken);

            navigate("/mainpanel")

        } catch (err) {
            if (err.response.data.message == 'INVALID_CREDENTIALS') {
                setErrorMessage('Invalid Username or Password');
            } else {
                setErrorMessage(err.response.data.message);
            }
            setLoginLabelVisible(true);
        }
    }

    const navigateToRegisterPage = () => {
        navigate("/register");
    }

    return (
        <>
            <div>
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
                                   hidden={!(userName.length === 0 || password.length === 0)}>Please enter both field</label>
                        </div>
                        <label className='login-register-error-msg'
                               hidden={!isLoginErrorLabelVisible}>{errorMessage}</label>
                    </Card>
                    <br/>
                    <br/>
                    <br/>
                    <Button className='lrbutton lrbutton-secondary' variant="primary"
                            disabled={userName.length === 0 || password.length === 0}
                            onClick={() => requestLogin(userName, password)}>Login</Button>

                    <Button className='lrbutton' variant="primary"
                            onClick={() => navigateToRegisterPage()}>Register New User</Button>
                </div>
            </div>
        </>
    )
}

export default LoginPage;
