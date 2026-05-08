import './Footer.css'
import {useTheme} from "./context/ThemeContext.tsx";
import {ReactElement} from "react";

function Footer(): ReactElement {
    const {currentStyle} = useTheme();
    const footerClasses: string = `themed-div ${currentStyle}`;
    return (
        <footer className={footerClasses}>
            <div className="footer-div extra">
                EcoTrack è un progetto no profit realizzato per il corso di Tecnologie Web, CdL in Informatica, Università di Torino.
            </div>
            <div className="footer-div">&copy; 2025 Cannella Angelo.
                <a
                    href="https://creativecommons.org/licenses/by-nc-sa/4.0/deed.it"><img alt="creative commons license"
                                                                                          className="cclicense"
                                                                                          src="/icons/by-nc-sa.png"/></a>
            </div>
        </footer>
    );
}

export default Footer;