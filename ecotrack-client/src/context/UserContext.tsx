import {createContext, useContext, useState} from "react";
import {UserContextType, UserData} from "../data/data-model.ts";

const UserContext = createContext<UserContextType | undefined>(undefined);

export function useUser(): UserContextType {
    const context = useContext(UserContext);
    if (!context) {
        throw new Error("useUser must be used within a UserProvider");
    }
    return context;
}

export const UserProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [user, setUser] = useState<UserData | undefined>(undefined);
    const [activePage, setActivePage] = useState<string>("Events");
    const [isAdmin, setIsAdmin] = useState<boolean>(false);

    function handleChangePage(page: string): void {
        if (page !== activePage) {
            setActivePage(page);
        }
    }

    function handleUserLogout(): void {
        setUser(undefined);
        setActivePage("Events");
        setIsAdmin(false);
    }

    return (
        <UserContext.Provider value={{user, setUser, isAdmin, setIsAdmin, activePage, handleChangePage, handleUserLogout}}>
            {children}
        </UserContext.Provider>
    );
};