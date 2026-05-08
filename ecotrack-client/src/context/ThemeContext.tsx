import React, { createContext, useContext, useState } from "react";
import {ThemeContextType} from "../data/data-model.ts";

const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

export function useTheme(): ThemeContextType {
    const context = useContext(ThemeContext);
    if (!context) {
        throw new Error("useTheme must be used within a ThemeProvider");
    }
    return context;
}

export const ThemeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [currentBackground, setCurrentBackground] = useState<string>("background-beginner");
    const [currentLogoBackground, setCurrentLogoBackground] = useState<string>("navbar-beginner");
    const [currentStyle, setCurrentStyle] = useState<string>("theme-beginner");

    function changeTheme (level: number): void {
        if (level >= 0 && level < 10) {
            setCurrentBackground("background-beginner");
            setCurrentStyle("theme-beginner");
            setCurrentLogoBackground("header-logo navbar-beginner");
        } else if (level >= 10 && level < 20) {
            setCurrentBackground("background-intermediate");
            setCurrentStyle("theme-intermediate");
            setCurrentLogoBackground("navbar-intermediate");
        } else if (level >= 20 && level < 30) {
            setCurrentBackground("background-advanced");
            setCurrentStyle("theme-advanced");
            setCurrentLogoBackground("navbar-advanced");
        } else if (level >= 30 && level < 40) {
            setCurrentBackground("background-expert");
            setCurrentStyle("theme-expert");
            setCurrentLogoBackground("navbar-expert");
        } else if (level >= 40) {
            setCurrentBackground("background-master");
            setCurrentStyle("theme-master");
            setCurrentLogoBackground("navbar-master");
        }
    }

    function handleThemeLogout(): void {
        setCurrentBackground("background-beginner");
        setCurrentStyle("theme-beginner");
        setCurrentLogoBackground("header-logo navbar-beginner");
    }

    return (
        <ThemeContext.Provider value={{currentBackground, currentLogoBackground, currentStyle, changeTheme, handleThemeLogout}}>
            {children}
        </ThemeContext.Provider>
    );
};
