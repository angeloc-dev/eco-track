import './NavBar.css'
import {useUser} from "./context/UserContext.tsx";
import {useTheme} from "./context/ThemeContext.tsx";
import {ReactElement} from "react";

interface NavBarProps {
    onLogout: () => void;
}

function NavBar({onLogout}: NavBarProps): ReactElement {
    const {user, isAdmin, activePage, handleChangePage} = useUser();
    const {currentBackground, currentLogoBackground, currentStyle} = useTheme();
    const headerClass: string = `themed-div ${currentStyle} ${currentBackground}`;
    const navbarClass: string = `header-logo ${currentLogoBackground}`;

    return (
        <header className={headerClass}>
            <div className={navbarClass}>
                EcoTrack
                <img className="header-logo-image" src="/icons/leaf.png" alt="EcoTrack"/>
            </div>
            {user !== undefined && (
                <nav className="navbar">
                    <div className={activePage === "Events" ? "nav-item active" : "nav-item"}
                         onClick={() => handleChangePage("Events")}>Events
                    </div>
                    {!isAdmin &&
                        <div className={activePage === "Challenges" ? "nav-item active" : "nav-item"}
                             onClick={() => handleChangePage("Challenges")}>Challenges
                        </div>}
                    <div className={activePage === "Awards" ? "nav-item active" : "nav-item"}
                         onClick={() => handleChangePage("Awards")}>Awards
                    </div>
                    {isAdmin && (
                        <div
                            className={(activePage === "User" || activePage === "Group") ? "nav-item nav-item-user active-usergroup" : "nav-item nav-item-user"}
                            onClick={() => handleChangePage("Group")}>
                            {user.username}
                            <br/>
                            <span className="nav-user">type : group admin</span>
                        </div>
                    )}
                    {!isAdmin && (
                        <div
                            className={(activePage === "User" || activePage === "Group") ? "nav-item nav-item-user active-usergroup" : "nav-item nav-item-user"}
                            onClick={() => handleChangePage("User")}>
                            {user.username}
                            <br/>
                            <span className="nav-user">type : user</span>
                        </div>
                    )}
                    <div className="nav-item" onClick={() => onLogout()}>Logout</div>
                </nav>
            )}
        </header>
    );
}

export default NavBar;