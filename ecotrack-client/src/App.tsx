import './App.css'

import NavBar from "./NavBar.tsx";
import Dashboard from "./Dashboard.tsx";
import Footer from "./Footer";
import { useUser} from "./context/UserContext";
import {ServerLoginResponse, UserData} from "./data/data-model.ts";
import Login from "./Login.tsx";
import {useEffect} from "react";
import {useTheme} from "./context/ThemeContext.tsx";
import {useError} from "./context/ErrorContext.tsx";

function App() {
    const {user, setUser, isAdmin, activePage, handleUserLogout} = useUser();
    const {changeTheme, currentBackground, handleThemeLogout} = useTheme();
    const {addMessage, clearMessages} = useError();
    const urlLoginPost: string = isAdmin ? "http://localhost:8080/session/login?role=admin" : "http://localhost:8080/session/login";

    const classAppContainer: string = (() => {
        if (user === undefined) return `app-container-min ${currentBackground}`;
        else {
            switch (activePage) {
                case "Events":
                    return `app-container ${currentBackground}`;
                case "Challenges":
                    return `app-container ${currentBackground}`;
                case "Awards":
                    return `app-container-center ${currentBackground}`;
                case "User":
                    return `app-container-center ${currentBackground}`;
                case "Group":
                    return `app-container-center ${currentBackground}`;
                default:
                    return `app-container ${currentBackground}`;
            }
        }
    })();

    async function handleLogin(username: string, password: string): Promise<void> {
        addMessage("Attendere Prego", false);
        try {
            const response: Response = await fetch(urlLoginPost, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, password }),
                credentials: "include",
            }).catch(() => {
                // Blocca l'errore della rete e forza un errore personalizzato
                throw new Error("Network error - Unable to reach server");
            });

            let responseData: ServerLoginResponse | null;

            try {
                responseData = await response.json();
            } catch {
                responseData = null;
            }

            if (!response.ok) {
                const errorMessage: string =
                    responseData?.sessionData?.message || `Error ${response.status}`;
                throw new Error(errorMessage);
            }

            clearMessages();

            const loggedUser: UserData = await getUserLogged(responseData as ServerLoginResponse);
            setUser(loggedUser);

            const welcomeMessage: string = isAdmin
                ? `Welcome Back ${loggedUser.username}`
                : `Welcome Back ${loggedUser.name} ${loggedUser.surname}`;

            addMessage(welcomeMessage, false);
        } catch (error) {
            if (error instanceof Error) {
                addMessage(error.message, true);
            } else addMessage("Unknown error joining an event", true);
        }
    }

    function handleLogout(): void {
        fetch("http://localhost:8080/session/logout", { credentials: "include" })
            .then(() => {
                handleUserLogout();
                handleThemeLogout();
                clearMessages();
            })
            .catch((err) => {
                addMessage(err.message, true);
            })
    }

    async function getUserLogged(serverLoginResponse: ServerLoginResponse): Promise<UserData> {
        return UserData.fromServerResponse(serverLoginResponse);
    }

    function handleChangeLevel(): void {
        if (!user) return;
        if (user?.level !== undefined) {
            changeTheme(user.level);
        }
    }

    useEffect(handleChangeLevel, [user?.level]);

    return (
            <div className={classAppContainer}>
                <NavBar onLogout={handleLogout} />
                {user !== undefined ? (<Dashboard />) : (<Login onLogin={handleLogin}/>)}
                <Footer/>
            </div>
    )

}

export default App;
