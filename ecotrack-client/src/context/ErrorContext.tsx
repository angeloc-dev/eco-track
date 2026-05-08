import { createContext, useContext, useState } from "react";
import { ErrorContextType } from "../data/data-model.ts";

const ErrorContext = createContext<ErrorContextType | undefined>(undefined);

export function useError(): ErrorContextType {
    const context = useContext(ErrorContext);
    if (!context) {
        throw new Error("useError must be used within an ErrorProvider");
    }
    return context;
}

export const ErrorProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [messages, setMessages] = useState<{ id: number; text: string; isError: boolean }[]>([]);

    function addMessage(text: string, isError: boolean): void {
        const id = Date.now();
        setMessages((prev) => {
            if (prev.some(msg => msg.text === text)) {
                return prev;
            }
            return [...prev, { id, text, isError }];
        });

        const timeout = isError ? 5000 : 2000;
        setTimeout(() => removeMessage(id), timeout);
    }

    function removeMessage(id: number): void {
        setMessages((prev) => prev.filter((msg) => msg.id !== id));
    }

    function clearMessages(): void {
        setMessages([]);
    }

    return (
        <ErrorContext.Provider value={{ messages, addMessage, removeMessage, clearMessages }}>
            {children}
        </ErrorContext.Provider>
    );
};
