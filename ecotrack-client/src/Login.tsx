import {ReactElement, useState} from "react";
import './Login.css'
import ErrorContainer from "./ErrorContainer.tsx";
import {useUser} from "./context/UserContext.tsx";
import {useTheme} from "./context/ThemeContext.tsx";

interface LoginProps {
    onLogin: (name: string, password: string) => void
}

function Login({onLogin}: LoginProps): ReactElement {
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const {isAdmin, setIsAdmin} = useUser();
    const {currentStyle} = useTheme();
    const classDiv: string = `themed-div ${currentStyle} log-container`

    return (
        <div className={classDiv}>
            <ErrorContainer />
            Login
            <div className="flex-row">
                <label className="label-custom" htmlFor="username">Username</label>
                <input type="text"
                       className="login-input"
                       id="username"
                       placeholder="Username"
                       name="username"
                       value={username}
                       onChange={(e) => setUsername(e.target.value)}/>
            </div>
            <div className="flex-row">
                <label className="label-custom" htmlFor="password">Password</label>
                <input type="password"
                       className="login-input"
                       id="password"
                       placeholder="Password"
                       name="password"
                       value={password}
                       onChange={(e) => setPassword(e.target.value)}/>
            </div>
            <div className="flex-row flex-row-end login-row-end">
                <label className="custom-checkbox">
                    <input type="checkbox"
                           checked={isAdmin}
                           onChange={(e) => setIsAdmin(e.target.checked)}/>
                    <span className="checkbox-label"></span>
                    Group Admin
                </label>
            </div>
            <div className="flex-row">
                <div className="btn actions">
                    <div className="form-action login"
                         onClick={() => onLogin(username, password)}>Login
                    </div>
                </div>
            </div>
        </div>
    )

}

export default Login;
