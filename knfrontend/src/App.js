import './App.css';
import {Route, Routes} from "react-router-dom";
import LoginPage from "./page/LoginPage";
import RegisterPage from "./page/RegisterPage";
import MainPage from "./page/MainPage";
import EditPopup from "./component/EditPopup";

function App() {

    return (
        <div className="App">
            <Routes>
                <Route path="/register" element={<RegisterPage/>}/>
                <Route path="/mainpanel" element={<MainPage/>}/>
                <Route path="/details" element={<EditPopup/>}/>
                <Route path="/" element={<LoginPage/>}/>
                <Route path="*" element={<LoginPage/>}/>
            </Routes>
        </div>
    );
}

export default App;
