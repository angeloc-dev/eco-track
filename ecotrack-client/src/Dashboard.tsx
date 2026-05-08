import './Dashboard.css'
import EventsChallengesContainer from "./dashboards/EventsChallengesContainer.tsx";
import ErrorContainer from "./ErrorContainer.tsx";
import UserGroupContainer from "./dashboards/UserGroupContainer.tsx";
import AddEvent from "./dashboards/AddEvent.tsx";
import AwardsContainer from "./dashboards/AwardsContainer.tsx";
import {useUser} from "./context/UserContext.tsx";
import {useTheme} from "./context/ThemeContext.tsx";
import {useError} from "./context/ErrorContext.tsx";
import {EventActivityData, EventData, GroupData} from "./data/data-model.ts";
import {ReactElement, useState} from "react";
import AddActivity from "./dashboards/AddActivity.tsx";

function Dashboard(): ReactElement {
    const {user, isAdmin, activePage, handleChangePage} = useUser();
    const {messages, addMessage} = useError();
    const {currentStyle} = useTheme()
    const [groupWhoAddEvent, setGroupWhoAddEvent] = useState<GroupData | undefined>(undefined);
    const [eventForActivity, setEventForActivity] = useState<EventData | undefined>(undefined);

    const handleAddEvent = async (content: EventData): Promise<void> => {
        try {
            if (user === undefined) return;
            if (groupWhoAddEvent) {
                const response: Response = await fetch(`http://localhost:8080/groups/${groupWhoAddEvent.idgroup}/events/organize`, {
                    method: "POST",
                    credentials: "include",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(content)
                });

                if (!response.ok)
                    addMessage(`Error on add event request: ${response.status} - ${response.statusText}`, true);

                const newEvent: EventData = await response.json();
                if (newEvent === undefined || newEvent === null){
                    addMessage(`500: Internal Server Error: Error to add an event`, true);
                }
                handleChangePage("Group");
            }

        } catch (err) {
            if (err instanceof Error) {
                addMessage(err.message, true);
            } else addMessage("Unknown error adding an event", true);
        }
    };

    const handleAddActivity = async (content: EventActivityData): Promise<void> => {
        try {
            if (user === undefined) return;
            const response: Response = await fetch(`http://localhost:8080/groups/${eventForActivity?.organizer.idgroup}/events/${eventForActivity?.idevent}/organizeActivity`, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(content)
            });

            if (!response.ok)
                addMessage(`Error on add event activity request: ${response.status} - ${response.statusText}`, true);

            const newActivity: EventActivityData = await response.json();
            if (newActivity === undefined || newActivity === null){
                addMessage(`Error: The event was not created in the user's groups`, true);
            }
            handleChangePage("Events");

        } catch (err) {
            if (err instanceof Error) {
                addMessage(err.message, true);
            } else addMessage("Unknown error adding an activity", true);
        }
    };

    const dashboardClasse: string = (() => {
        switch (activePage) {
            case "AddEvent":
                return `dashboard min themed-div ${currentStyle}`
            case "AddActivity":
                return `dashboard min-activity themed-div ${currentStyle}`;
            default:
                return `dashboard themed-div ${currentStyle}`;
        }
    })();

    return (
        <>
            <div className={dashboardClasse}>
                {(messages.length > 0) && <ErrorContainer />}
                {(activePage === "Events" || (!isAdmin && activePage === "Challenges")) &&
                    <EventsChallengesContainer onActivityAdded={setEventForActivity}
                    />
                }
                {(activePage === "User" || activePage === "Group") &&
                    <UserGroupContainer onEventAdded={setGroupWhoAddEvent}
                    />
                }
                {(activePage === "Awards") && (
                    <AwardsContainer />
                )}
                {(activePage === "AddEvent" && isAdmin) && (
                    <AddEvent onSubmit={handleAddEvent}
                              organizer={groupWhoAddEvent as GroupData}
                    />
                )}
                {(activePage === "AddActivity" && isAdmin) && (
                    <AddActivity onSubmit={handleAddActivity}
                                 event={eventForActivity as EventData}
                    />
                )}
            </div>
        </>
    )


}

export default Dashboard;