import "./ErrorContainer.css"
import {useError} from "./context/ErrorContext.tsx";
import {ReactElement} from "react";

interface MessageErrorProps{
     message: { id: number; text: string; isError: boolean };
}

function MessageError({message}: MessageErrorProps): ReactElement {
    return (
        <div className={message.isError ? "error-container" : "message-container"}>
            {message.text}
        </div>
    );
}

function ErrorContainer(): ReactElement {
    const { messages } = useError();

    if (messages.length === 0) {
        return <></>;
    }

    return (
        <div className="errors-container">
            {messages.map((msg) => (
                <MessageError key={msg.id} message={msg} />
            ))}
        </div>
    );
}

export default ErrorContainer;