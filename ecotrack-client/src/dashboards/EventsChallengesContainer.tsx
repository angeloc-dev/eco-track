import './EventsChallengesContainer.css'
import {ReactElement, useEffect, useMemo, useState} from "react";
import {useUser} from "../context/UserContext.tsx";
import {useError} from "../context/ErrorContext.tsx";
import {
    BadgeData,
    BadgeToUserData,
    EcologicalChallengeData,
    EventActivityData,
    EventData,
    GroupData,
    TrophyData,
    TrophyToGroupData,
    UserData,
    UserInEcologicalChallengeData,
    UserInEventActivityData,
    UserInEventData
} from "../data/data-model.ts";
import Trophy from "./Trophy.tsx";
import Badge from "./Badge.tsx";

interface FilterEventsProps {
    filters: {
        radio: string,
        searchText: string,
        category: string,
        onlyUserEvents: boolean,
    },
    onFilterChange: (key: string, value: string | boolean) => void
}

function FilterEvents({filters, onFilterChange}: FilterEventsProps): ReactElement {
    const {activePage} = useUser();

    const radioButtons = (): JSX.Element => {
        if (activePage === "Challenges") {
            if (filters.onlyUserEvents) {
                return <div className="flex-row-usergroup">
                    <label className="events-radio">
                        <input type="radio" name="eventFilter"
                               value="all"
                               checked={filters.radio === "all"}
                               onChange={(e) => onFilterChange("radio", e.target.value)}/>
                        <span className="radio-label"></span>
                        All
                    </label>
                    <label className="events-radio">
                        <input type="radio" name="eventFilter"
                               value="ongoing" checked={filters.radio === "ongoing"}
                               onChange={(e) => onFilterChange("radio", e.target.value)}/>
                        <span className="radio-label"></span>
                        On Going
                    </label>
                    <label className="events-radio">
                        <input type="radio" name="eventFilter"
                               value="terminated" checked={filters.radio === "terminated"}
                               onChange={(e) => onFilterChange("radio", e.target.value)}/>
                        <span className="radio-label"></span>
                        Terminated
                    </label>
                </div>;
            } else {
                return <></>;
            }
        } else {
            return <div className="flex-row-usergroup">
                <label className="events-radio">
                    <input type="radio" name="challengeFilter"
                           value="all"
                           checked={filters.radio === "all"}
                           onChange={(e) => onFilterChange("radio", e.target.value)}/>
                    <span className="radio-label"></span>
                    All
                </label>
                <label className="events-radio">
                    <input type="radio" name="challengeFilter"
                           value="ongoing" checked={filters.radio === "ongoing"}
                           onChange={(e) => onFilterChange("radio", e.target.value)}/>
                    <span className="radio-label"></span>
                    On Going
                </label>
                <label className="events-radio">
                    <input type="radio" name="challengeFilter"
                           value="terminated" checked={filters.radio === "terminated"}
                           onChange={(e) => onFilterChange("radio", e.target.value)}/>
                    <span className="radio-label"></span>
                    Terminated
                </label>
            </div>;
        }
    };

    return (
        <div className="filter-events container">
            <div className="flex-row-usergroup">
                <label className="label-filter"
                       htmlFor={activePage === "Events" ? "event" : "challenge"}>Search {activePage}:</label>
                <input type="text" id={activePage === "Events" ? "event" : "challenge"}
                       name={activePage === "Events" ? "event" : "challenge"}
                       className="search-input"
                       placeholder={activePage === "Events" ? "Search Events..." : "Search Challenges..."}
                       value={filters.searchText}
                       onChange={(e) => onFilterChange("searchText", e.target.value)}/>
            </div>
            {radioButtons()}
            <div className="flex-row-usergroup">
                <label htmlFor={activePage === "Events" ? "event-category" : "challenge-category"}>Category</label>
                <select className="category-select" value={filters.category}
                        onChange={(e) => onFilterChange("category", e.target.value)}>
                    {activePage === "Events" && (
                        <>
                            <option value="">All Categories</option>
                            <option value="Beach Cleaning">Beach Cleaning</option>
                            <option value="Environmental Education">Environmental Education</option>
                            <option value="Reforestation">Reforestation</option>
                            <option value="River Restoration">River Restoration</option>
                            <option value="Sustainable Energy">Sustainable Energy</option>
                            <option value="Waste Sorting">Waste Sorting</option>
                            <option value="Wildlife Conservation">Wildlife Conservation</option>
                        </>
                    )}
                    {activePage === "Challenges" && (
                        <>
                            <option value="">All Categories</option>
                            <option value="Community">Community</option>
                            <option value="Educational">Educational</option>
                            <option value="Energy">Energy</option>
                            <option value="Environmental">Environmental</option>
                            <option value="Recycle">Recycle</option>
                        </>
                    )}
                </select>
                <label className="custom-checkbox">
                    <input type="checkbox"
                           checked={filters.onlyUserEvents}
                           onChange={(e) => {
                               onFilterChange("onlyUserEvents", e.target.checked)}}/>
                    <span className="checkbox-label"></span>
                    My {activePage}
                </label>
            </div>
        </div>
    );
}

interface EventActivityProps{
    content: EventActivityData;
    progressContent: UserInEventActivityData | undefined;
    isEventOwnedByGroup: () => boolean;
    onActivityParticipation: (idactivity: number) => void;
    onActivityParticipationDelete: (idactivity: number, progressContent: UserInEventActivityData) => void;
}

function EventActivity({content, progressContent, isEventOwnedByGroup, onActivityParticipation, onActivityParticipationDelete} : EventActivityProps): ReactElement {
    const [isActivityExpanded, setIsActivityExpanded] = useState<boolean>(false);
    const {isAdmin} = useUser();

    const toggleExpandActivity = (): void => {
        setIsActivityExpanded(!isActivityExpanded);
    };

    const dateFormatted = (date: Date | undefined): string | undefined => {
        return date && new Date(date).toLocaleDateString('en-EN', {
            day: '2-digit',
            month: 'long',
            year: 'numeric',
        });
    };

    const handleButtonClick = (): void => {
        if (isAdmin) {
            // Eliminare attività
        } else {
            if (progressContent) {
                onActivityParticipationDelete(content.idactivity as number, progressContent as UserInEventActivityData);
            } else {
                onActivityParticipation(content.idactivity as number);
            }
        }
    };

    const button = (): JSX.Element => {
        if (content.event.state === "Terminated") return <></>
        if (isAdmin){
            return <></>
        } else {
            if (isEventOwnedByGroup()){
                return <div className={progressContent ? "btn-delete btn-delete-participation-activity"  : "btn btn-participate-activity"}
                            onClick={handleButtonClick}>
                    <img className="event-add" src={progressContent ? "/icons/cancel.png" : "/icons/add.png"}
                         alt={progressContent ? "Remove Participation in activity" : "Add Participation in activity"}
                    />
                    {progressContent ? "Delete participation" : "Participate"}
                </div>
            } else return <></>
        }
    };

    return (
        <div className={isActivityExpanded ? "event-activity-card card-open": "event-activity-card"}>
            <div className="flex-row-usergroup">
                <span className="activity-name">
                    {content.name}
                </span>
                <img onClick={toggleExpandActivity}
                     className={`toggle-activity-icon ${isActivityExpanded ? "rotate" : ""}`}
                     src="/icons/arrow.png"
                     alt={isActivityExpanded ? "Hide Activity" : "Show Activity"}/>
            </div>
            {!isActivityExpanded && (
                <div className="flex-row-usergroup">
                    <span className="activity-xp">
                        <img className="activity-icon-xp" src="/icons/xp.png" alt="Xp Image"/>
                        {content.xp}
                    </span>
                    <span className="activity-date">
                        {dateFormatted(content.datelineDate)}
                    </span>
                </div>
            )}
            {isActivityExpanded && (
                <>
                    <div className="flex-row flex-row-end">
                        <span className="activity-date">
                            {dateFormatted(content.datelineDate)}
                        </span>
                    </div>
                    <div className="flex-row-usergroup">
                        <span className="activity-xp">
                            <img className="activity-icon-xp" src="/icons/xp.png" alt="Xp Image"/>
                            {content.xp}
                        </span>
                        <span>
                            {content.state}
                        </span>
                    </div>
                    <div className="flex-row-usergroup">
                        <span className="activity-description">
                            {content.description}
                        </span>
                    </div>
                    <div className="flex-row flex-row-end">
                        {button()}
                    </div>
                </>
            )}
        </div>
    );
}

interface UserOfEventProps {
    content: EventData;
    progressContent: UserInEventData | undefined;
}

function UserOfEvent({content, progressContent}: UserOfEventProps): ReactElement {
    const [participants, setParticipants] = useState<UserData[]>([]);
    const {addMessage} = useError();
    const {user} = useUser();

    useEffect(() => {
        if (!user) return;
        fetch(`http://localhost:8080/events/${content.idevent}/participants`, { credentials: "include" })
            .then((response) => {
                if (!response.ok)
                    addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                return response.json();
            })
            .then((data: UserData[]) => {
                setParticipants(data);
            })
            .catch((err) => {
                addMessage(err.message, true);
            });
    }, [user, content, progressContent]);

    const topParticipants: (UserData | undefined)[] = participants.slice(0, 3);
    const remainingCount: number = participants.length - 3;

    return (
        <span className="user-of-challenge">
            <span className="avatar-stack">
                {topParticipants.map((user, index) =>
                    user ? (
                        <img
                            key={`user-c-${user.iduser}-cover`}
                            src="/users/user1.jpeg"
                            alt={`Foto ${index + 1} awarder`}
                            className="challenge-awarder-photo"
                            style={{zIndex: topParticipants.length - index}}
                        />
                    ) : null
                )}
            </span>
            {participants.length > 0 ? (
                <span className="remaining-users">
                    {participants.length <= 3
                        ? `attend the event`
                        : `and ${remainingCount} others attend the event`}
                </span>
            ) : (
                <span className="remaining-users">No one is attending this event
                </span>
            )}
        </span>
    )
}

interface EventProps{
    content: EventData;
    progressContent: UserInEventData | undefined;
    userGroups: GroupData[];
    onEventParticipation: (idevent: number) => void;
    onEventParticipationDelete: (idevent: number, progressContent: UserInEventData) => void;
    onActivityAdded: (event: EventData) => void;
}

function Event({content, progressContent, userGroups, onEventParticipation, onEventParticipationDelete, onActivityAdded}: EventProps): ReactElement {
    const [isDescriptionVisible, setDescriptionVisible] = useState<boolean>(false);
    const [tasks, setTasks] = useState<EventActivityData[]>([]);
    const [progressTasks, setProgressTasks] = useState<UserInEventActivityData[]>([]);
    const {user, isAdmin, handleChangePage} = useUser();
    const {addMessage} = useError();

    const handleJoinActivity = async (idactivity: number): Promise<void> => {
        try {
            if (user === undefined) return;
            const response: Response = await fetch(`http://localhost:8080/users/${user?.iduser}/events/${content.idevent}/activities/${idactivity}/participate`, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ participationDate: new Date().toISOString().split("T")[0] })
            });

            if (!response.ok)
                addMessage(`500 Internal Server Error: Error on join event activity request: ${response.status} - ${response.statusText}`, true);

            const newParticipation: UserInEventActivityData = await response.json();
            if (newParticipation === undefined || newParticipation === null){
                addMessage(`500 Internal Server Error: Error on user / activity parameter`, true);
            } else {
                setProgressTasks(prev => [...prev, newParticipation]);
            }

        } catch (err) {
            if (err instanceof Error) {
                addMessage(err.message, true);
            } else addMessage("Unknown error joining an event", true);
        }
    };

    const handleDeleteJoinActivity = async (idactivity: number, progressTask: UserInEventActivityData): Promise<void> => {
        try {
            if (user === undefined) return;
            const response: Response = await fetch(`http://localhost:8080/users/${user?.iduser}/events/${content.idevent}/activities/${idactivity}/deleteParticipation`, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(progressTask)
            });

            if (!response.ok)
                addMessage(`Error on delete activity request: ${response.status} - ${response.statusText}`, true);

            const deleteParticipation: UserInEventActivityData = await response.json();
            if (deleteParticipation === undefined || deleteParticipation === null){
                addMessage(`500 Internal Server Error: Error on user / activity parameter`, true);
            } else {
                setProgressTasks(prev => prev.filter(
                    p => p.idusereventactivity !== progressTask.idusereventactivity
                ));
            }

        } catch (err) {
            if (err instanceof Error) {
                addMessage(err.message, true);
            } else addMessage("Unknown error deleting an event activity", true);
        }
    };

    useEffect(() => {
        let valid: boolean = true;

        if (user !== undefined && content.hasActivity) {
            fetch(`http://localhost:8080/events/${content.idevent}/activities`, { credentials: "include" })
                .then((response) => {
                    if (!response.ok)
                        addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                    return response.json();
                })
                .then((data: EventActivityData[]) => {
                    if (valid) setTasks(data);
                    else return;
                })
                .catch((err) => {
                    if (err.name === "AbortError") {
                        addMessage("Fetch annullata con successo", true);
                    } else {
                        addMessage(`Errore nella fetch: ${err}`, true);
                    }
                    addMessage(err.message, true);
                });

            fetch(`http://localhost:8080/users/${user?.iduser}/activities`, { credentials: "include" })
                .then((response) => {
                    if (!response.ok)
                        addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                    return response.json();
                })
                .then((data: UserInEventActivityData[]) => {
                    if (valid) setProgressTasks(data);
                    else return;
                })
                .catch((err) => {
                    if (err.name === "AbortError") {
                        addMessage("Fetch annullata con successo", true);
                    } else {
                        addMessage(`Errore nella fetch: ${err}`, true);
                    }
                    addMessage(err.message, true);
                });

        }
        return () => {
            valid = false;
        };
    }, [user, content]);

    const toggleDescription = (): void => {
        setDescriptionVisible(!isDescriptionVisible);
    };

    const dateFormatted = (date: Date): string => {
        return new Date(date).toLocaleDateString('en-EN', {
            day: '2-digit',
            month: 'long',
            year: 'numeric',
        });
    };

    const startDate = (): string => {
        return dateFormatted(content.startDate);
    };

    const endDate = (): string => {
        return content.endDate ? dateFormatted(content.endDate) : "";
    };

    const typeOrganizer = (): ReactElement => {
        switch (content.organizer.type) {
            case "Family":
                return <img className="event-type-organizer" src="/icons/family.png" alt="Add Image"/>;
            case "School":
                return <img className="event-type-organizer" src="/icons/school.png" alt="Add Image"/>;
            case "Business":
                return <img className="event-type-organizer" src="/icons/business.png" alt="Add Image"/>;
            case "EcoGroup":
                return <img className="event-type-organizer" src="/icons/ecogroup.png" alt="Add Image"/>;
        }
        return <></>
    };

    const pathEventImage= (): string => {
        switch (content.category) {
            case "Beach Cleaning": return "eventsBeachCleaning.jpeg";
            case "Environmental Education": return "eventsEnvironmentalEducation.jpg";
            case "Reforestation": return "eventsReforestation.jpeg";
            case "River Restoration": return "eventsRiverRestoration.jpeg";
            case "Sustainable Energy": return "eventsSustainableEnergy.jpeg";
            case "Waste Sorting": return "eventsWasteSorting.jpeg";
            case "Wildlife Conservation": return "eventsWildlifeConservation.jpeg";
        }
        return "";
    };

    const filteredData: { progress: UserInEventActivityData | undefined; content: EventActivityData }[] = useMemo(() => {
        return tasks.map((item) => ({
            content: item,
            progress: progressTasks.find((progress) =>
                progress.eventActivity.idactivity === item.idactivity
            )
        }));
    }, [tasks, progressTasks]);

    const setAddButton = (): ReactElement => {
        if (isAdmin && isEventOwnedByGroup() && content.state !== "Terminated") {
            return <div className="btn btn-add-activity" onClick={() => {
                onActivityAdded(content as EventData);
                handleChangePage("AddActivity")}}>
                <img className="activity-add" src="/icons/add.png" alt="Add Image"/>
                Activity
            </div>
        }
        return <></>
    };

    const setButton = (): ReactElement => {
        if (content.state === "Terminated") return <></>
        if (!isAdmin && isEventOwnedByGroup()) {
            if (!progressContent) {
                return <div className="btn btn-participate-event" onClick={() => onEventParticipation(content.idevent as number)}>
                    <img className="event-add" src="/icons/add.png" alt="Add Event Image"/>
                    Participate
                </div>

            } else {
                return <div className="btn-delete btn-remove-participation-event"
                            onClick={() => onEventParticipationDelete(content.idevent as number, progressContent as UserInEventData)} >
                    <img className="event-add" src="/icons/cancel.png" alt="Cancel Event Image"/>
                    Delete Participation
                </div>
            }
        } else if (isEventOwnedByGroup()) {
            if (!content.hasActivity) {
                return <div className="flex-row flex-row-end">
                    <div className="btn btn-event-add-activity" onClick={() => {
                        onActivityAdded(content as EventData);
                        handleChangePage("AddActivity");
                    }}>
                        <img className="event-add" src="/icons/add.png" alt="Add Image"/>
                        Organize Activity
                    </div>
                </div>
            } else {
                return <></>
            }
        } else return <></>
    };

    const isEventOwnedByGroup = (): boolean => {
        if (!userGroups) return false;

        return userGroups.some(
            (group) => group && content.organizer.idgroup === group.idgroup
        );
    };

    return (
        <div className="event-card container">
            <div className="flex-row flex-row-end first-row-event">
                <span className="event-date">{startDate()}</span>
                <span className="event-date">–</span>
                <span className="event-date">{endDate()}</span>
                {content.state === "Terminated" && (
                    <span className="event-terminated">
                        Terminated
                    </span>
                )}
                {content.state === "In Progress" && (
                    <span className="event-ongoing">
                        In Progress
                    </span>
                )}
            </div>
            <div className="flex-row-usergroup">
                <span className="event-organizer">
                    {typeOrganizer()} {content.organizer.name}
                </span>
                <span className="event-place">
                    <img className="event-icon-place" src="/icons/place.png" alt="Place Image"/>
                    {content.place}
                </span>
            </div>
            <div className="flex-row">
                <span className="event-name">{content.name}</span>
            </div>
            <div className="flex-row-usergroup">
                <span className="event-category">{content.category}</span>
                <span className="event-empty"></span>
                <span className="event-xp">
                    <img className="event-icon-xp" src="/icons/xp.png" alt="Xp Image"/>
                    {content.xp}
                </span>
            </div>
            <div className="flex-row">
                <span>
                    Description
                    <img onClick={toggleDescription}
                         className={`toggle-description-icon ${isDescriptionVisible ? "rotate" : ""}`}
                         src="/icons/arrow.png"
                         alt={isDescriptionVisible ? "Hide Description" : "Show Description"}/>
                </span>
            </div>
            <div className="flex-row">
                    <span className={`event-description ${isDescriptionVisible ? "show" : ""}`}>
                        {content.description}
                    </span>
            </div>
            {filteredData && filteredData.length > 0 && (
                <div className="event-activity-list">
                    <div className="activity-title">
                        <span>Tasks</span>
                    </div>
                    <div className="event-activity-carousel">
                        {filteredData.map(({content, progress}) =>
                            <EventActivity key={`event-${content?.event.idevent}-activity-${content?.idactivity}`} content={content as EventActivityData}
                                           progressContent={progress as UserInEventActivityData | undefined}
                                           isEventOwnedByGroup={isEventOwnedByGroup}
                                           onActivityParticipation={handleJoinActivity}
                                           onActivityParticipationDelete={handleDeleteJoinActivity}
                            />
                        )}
                        {setAddButton()}
                    </div>
                </div>
            )}
            <div className="flex-row-usergroup">
                <img className="event-image" src={`/events/${pathEventImage()}`} alt="Event Image"/>
            </div>
            <div className="flex-row-usergroup">
                <UserOfEvent content={content as EventData} progressContent={progressContent as UserInEventData}/>
                <span className="event-empty"></span>
                {setButton()}
            </div>
        </div>
    )
}

interface UserOfChallengeProps {
    content: EcologicalChallengeData;
    progressContent: UserInEcologicalChallengeData | undefined;
}

function UserOfChallenge({content, progressContent}: UserOfChallengeProps): ReactElement {
    const [holders, setHolders] = useState<UserData[]>([]);
    const {addMessage} = useError();
    const {user} = useUser();

    useEffect(() => {
        if (!user) return;
        fetch(`http://localhost:8080/challenges/${content.idecochallenge}/awardees`, {credentials: "include" })
            .then((response) => {
                if (!response.ok)
                    addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                return response.json();
            })
            .then((data: UserData[]) => {
                setHolders(data);
            })
            .catch((err) => {
                addMessage(err.message, true);
            });
    }, [user, content, progressContent]);

    const topHolders: (UserData | undefined)[] = holders.slice(0, 3);
    const remainingCount: number = holders.length - 3;

    return (
        <span className="user-of-challenge">
            <span className="avatar-stack">
                {topHolders.map((user, index) =>
                    user ? (
                        <img
                            key={`user-p-${user.iduser}-cover`}
                            src="/users/user1.jpeg"
                            alt={`Foto ${index + 1} awarder`}
                            className="challenge-awarder-photo"
                            style={{zIndex: topHolders.length - index}}
                        />
                    ) : null
                )}
            </span>
            {holders.length > 0 ? (
                <span className="remaining-users">
                    {holders.length <= 3
                        ? `have unlocked it`
                        : `and ${remainingCount} more have unlocked it`}
                </span>
            ) : (
                <span className="remaining-users">Nobody has unlocked it
                </span>
            )}
        </span>
    )
}

interface ChallengeProps {
    content: EcologicalChallengeData;
    progressContent: UserInEcologicalChallengeData | undefined;
    onChallengeParticipation: (idecochallenge: number) => void;
    onChallengeUpdating: (idecochallenge: number, progressContent: UserInEcologicalChallengeData) => void;
    onChallengeParticipationDelete: (idecochallenge: number, progressContent: UserInEcologicalChallengeData) => void;
}

function Challenge({content, progressContent, onChallengeParticipation, onChallengeUpdating, onChallengeParticipationDelete}: ChallengeProps): ReactElement {
    const [isDescriptionVisible, setDescriptionVisible] = useState<boolean>(false);

    const toggleDescription = (): void => {
        setDescriptionVisible(!isDescriptionVisible);
    };

    const isChallengeTerminated = (state: string): boolean => {
        const parts: string[] = state.split("-");
        const n: number = Number(parts[0]);
        const m: number = Number(parts[1]);
        return n === m;
    };

    const targetCountLabel = (): ReactElement => {
        if (progressContent === undefined) {
            return <span className="challenge-targetcount-not-started">Not Started</span>
        } else if (isChallengeTerminated(progressContent.state)) {
            return <span className="challenge-targetcount-terminated">
                        Terminated
                    </span>
        } else return <span className="challenge-targetcount">
                        {progressContent.state}
                    </span>
    };

    const difficulty = (difficulty: number): ReactElement[] => {
        return Array.from({ length: difficulty }, (_, i) => (
            <img key={`img-difficulty-${i}`} className="challenge-difficulty-icon" src="/icons/star.png" alt="Difficulty Image"/>
        ));
    };

    const challengeButton = (): ReactElement => {
        if (progressContent === undefined) {
            return (
                <>
                    <span className="event-empty"></span>
                    <span className="btn btn-participate-challenge" onClick={() => onChallengeParticipation(content.idecochallenge)}>
                        <img className="event-add" src="/icons/add.png" alt="Add Image"/>
                        Participe to Challenge
                    </span>
                </>
            )
        } else if (isChallengeTerminated(progressContent.state)) {
            return <></>
        } else
            return (
                <>
                    <span className="event-empty"></span>
                    <span className="btn-delete btn-remove-challenge" onClick={() => onChallengeParticipationDelete(content.idecochallenge, progressContent)}>
                        <img className="event-add" src="/icons/cancel.png" alt="Add Image"/>
                        Delete
                    </span>
                    <span className="btn-progress btn-progress-challenge" onClick={() => onChallengeUpdating(content.idecochallenge, progressContent)}>
                        <img className="event-add" src="/icons/progress.png" alt="Add Image"/>
                        Get a Goal
                    </span>
                </>
            )
    };

    return (
        <div className="event-card container">
            <div className="flex-row-usergroup">
                <span className="challenge-name">{content.name}</span>
                <span className="challenge-empty"></span>
                {targetCountLabel()}
            </div>
            <div className="flex-row-usergroup">
                <span className="event-category">{content.category}</span>
                <span className="challenge-empty"></span>
                <span className="challenge-difficulty">
                    {difficulty(content.difficulty)}
                </span>
                <span className="event-xp">
                    <img className="event-icon-xp" src="/icons/xp.png" alt="Xp Image"/>
                    {content.xp}
                </span>
            </div>
            <div className="flex-row-usergroup">
                <span>
                    Description
                    <img onClick={toggleDescription}
                         className={`toggle-description-icon ${isDescriptionVisible ? "rotate" : ""}`}
                         src="/icons/arrow.png"
                         alt={isDescriptionVisible ? "Hide Description" : "Show Description"}/>
                </span>
            </div>
            <div className="flex-row-usergroup">
                <span className={`event-description ${isDescriptionVisible ? "show" : ""}`}>
                    {content.description}
                </span>
            </div>
            <div className="flex-row-usergroup">
                <img className="event-image" src={`/challenges/${content.category}.jpeg`} alt="Challenge Image"/>
            </div>
            <div className="flex-row-usergroup">
                <UserOfChallenge content={content as EcologicalChallengeData} progressContent={progressContent as UserInEcologicalChallengeData | undefined}/>
                {challengeButton()}
            </div>
        </div>
    )
}

interface ExpiringAwardsProps{
    trophiesProgress: TrophyToGroupData[];
    badgesProgress: BadgeToUserData[];
}

function ExpiringAwards({trophiesProgress, badgesProgress}: ExpiringAwardsProps): ReactElement {
    const {activePage} = useUser();
    const {addMessage} = useError();
    const {user} = useUser();
    const [trophies, setTrophies] = useState<TrophyData[]>([]);
    const [badges, setBadges] = useState<BadgeData[]>([]);

    useEffect(() => {
        if (!user) return;
        if (activePage === "Events") {
            fetch(`http://localhost:8080/trophies`, {credentials: "include"})
                .then((response) => {
                    if (!response.ok)
                        addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                    return response.json();
                })
                .then((data: TrophyData[]) => setTrophies(data))
                .catch((err) => {
                    addMessage(err.message, true);
                });
        } else {
            fetch(`http://localhost:8080/badges`, { credentials: "include" })
                .then((response) => {
                    if (!response.ok)
                        addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                    return response.json();
                })
                .then((data: BadgeData[]) => setBadges(data))
                .catch((err) => {
                    addMessage(err.message, true);
                });
        }
    }, [user, activePage]);

    const filteredData: {
        content: TrophyData | BadgeData;
        progress: TrophyToGroupData | BadgeToUserData | undefined;
    }[] = useMemo(() => {
        const data = activePage === "Events" ? trophies : badges;
        const progressData = activePage === "Events" ? trophiesProgress : badgesProgress;

        return data.map((item) => ({
            content: item,
            progress: progressData.find((progress) => {
                if (activePage === "Events") {
                    return (progress as TrophyToGroupData).trophy?.idtrophy === (item as TrophyData).idtrophy;
                } else {
                    return (progress as BadgeToUserData).badge?.idbadge === (item as BadgeData).idbadge;
                }
            }) || undefined,
        }));
    }, [activePage, trophies, badges, trophiesProgress, badgesProgress]);

    return (
        <div className={activePage === "Events" ? "expiring-trophies-list container" : "expiring-badge-list container"}>
            {activePage === "Events" && (
                <>
                    {filteredData.map(({content, progress}) =>
                        <Trophy key={`trophy-${(content as TrophyData).idtrophy}`} content={content as TrophyData}
                                progressContent={progress as TrophyToGroupData | undefined}/>
                    )}
                </>
            )}
            {activePage === "Challenges" && (
                <>
                    {filteredData.map(({content, progress}) =>
                        <Badge key={`badge-${(content as BadgeData).idbadge}`} content={content as BadgeData}
                               progressContent={progress as BadgeToUserData | undefined}/>
                    )}
                </>
            )}
        </div>
    )
}

interface EventsChallengesContainerProps{
    onActivityAdded: (event: EventData) => void;
}

function EventsChallengesContainer({onActivityAdded} : EventsChallengesContainerProps): ReactElement {
    const {messages, addMessage} = useError();
    const {activePage, user, isAdmin} = useUser();
    const [events, setEvents] = useState<EventData[]>([]);
    const [eventsProgress, setEventsProgress] = useState<UserInEventData[]>([]);
    const [challenges, setChallenges] = useState<EcologicalChallengeData[]>([]);
    const [challengesProgress, setChallengesProgress] = useState<UserInEcologicalChallengeData[]>([]);
    const [userGroups, setUserGroups] = useState<GroupData[]>([]);
    const [trophiesProgress, setTrophiesProgress] = useState<TrophyToGroupData[]>([]);
    const [badgesProgress, setBadgesProgress] = useState<BadgeToUserData[]>([]);
    const [filters, setFilters] = useState<{
        radio: string;
        searchText: string;
        category: string;
        onlyUserEvents: boolean;
    }>({
        radio: "all", // Opzioni: 'all', 'ongoing', 'terminated'
        searchText: "",
        category: "",
        onlyUserEvents: true,
    });

    useEffect(() => {
        if (!user) return;
        if (activePage === "Events") {
            fetch(`http://localhost:8080/events`, { credentials: "include" })
                .then((response) => {
                    if (!response.ok)
                        addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                    return response.json();
                })
                .then((data: EventData[]) => {
                    setEvents(data);
                })
                .catch((err) => {
                    addMessage(err.message, true);
                });
            if (isAdmin) {
                fetch(`http://localhost:8080/users/${user?.iduser}/trophiesToGroupsAdmin?isExpiring=true`, { credentials: "include" })
                    .then((response) => {
                        if (!response.ok)
                            addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                        return response.json();
                    })
                    .then((data: TrophyToGroupData[]) => setTrophiesProgress(data))
                    .catch((err) => {
                        addMessage(err.message, true);
                    });

                fetch(`http://localhost:8080/users/${user?.iduser}/adminGroups`, {credentials: "include"})
                    .then((response) => {
                        if (!response.ok)
                            addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                        return response.json();
                    })
                    .then((data: GroupData[]) => {
                        setUserGroups(data);
                    })
                    .catch((err) => {
                        addMessage(err.message, true);
                    });
            } else {
                fetch(`http://localhost:8080/users/${user?.iduser}/trophiesToGroups?isExpiring=true`, { credentials: "include" })
                    .then((response) => {
                        if (!response.ok)
                            addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                        return response.json();
                    })
                    .then((data: TrophyToGroupData[]) => setTrophiesProgress(data))
                    .catch((err) => {
                        addMessage(err.message, true);
                    });
                fetch(`http://localhost:8080/users/${user?.iduser}/events`, {credentials: "include"})
                    .then((response) => {
                        if (!response.ok)
                            addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                        return response.json();
                    })
                    .then((data: UserInEventData[]) => {
                        setEventsProgress(data);
                    })
                    .catch((err) => {
                        addMessage(err.message, true);
                    });

                fetch(`http://localhost:8080/users/${user?.iduser}/groups`, {credentials: "include"})
                    .then((response) => {
                        if (!response.ok)
                            addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                        return response.json();
                    })
                    .then((data: GroupData[]) => {
                        setUserGroups(data);
                    })
                    .catch((err) => {
                        addMessage(err.message, true);
                    });
            }
        } else {
            fetch(`http://localhost:8080/challenges`, { credentials: "include" })
                .then((response) => {
                    if (!response.ok)
                        addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                    return response.json();
                })
                .then((data: EcologicalChallengeData[]) => {
                    setChallenges(data);
                })
                .catch((err) => {
                    addMessage(err.message, true);
                });

            fetch(`http://localhost:8080/users/${user?.iduser}/challenges`, { credentials: "include" })
                .then((response) => {
                    if (!response.ok)
                        addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                    return response.json();
                })
                .then((data: UserInEcologicalChallengeData[]) => {
                    setChallengesProgress(data);
                })
                .catch((err) => {
                    addMessage(err.message, true);
                });

            fetch(`http://localhost:8080/users/${user?.iduser}/badges?isExpiring=true`, { credentials: "include" })
                .then((response) => {
                    if (!response.ok)
                        addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                    return response.json();
                })
                .then((data: BadgeToUserData[]) => setBadgesProgress(data))
                .catch((err) => {
                    addMessage(err.message, true);
                });
        }
    }, [user, activePage]);

    const handleJoinEvent = async (idevent: number): Promise<void> => {
        try {
            if (user === undefined) return;
            const response: Response = await fetch(`http://localhost:8080/users/${user?.iduser}/events/${idevent}/participate`, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ participationDate: new Date().toISOString().split("T")[0] }) // Formato YYYY-MM-DD
            });

            if (!response.ok)
                addMessage(`Error on join event request: ${response.status} - ${response.statusText}`, true);

            const newParticipation: UserInEventData = await response.json();
            if (newParticipation === undefined || newParticipation === null){
                addMessage(`Error: The event was not created in the user's groups`, true);
            } else {
                setEventsProgress((prev) => [...prev, newParticipation]);
            }

        } catch (err) {
            if (err instanceof Error) {
                addMessage(err.message, true);
            } else addMessage("Unknown error joining an event", true);
        }
    };

    const handleDeleteJoinEvent = async (idevent: number, progressEvent: UserInEventData): Promise<void> => {
        try {
            if (user === undefined) return;
            const response: Response = await fetch(`http://localhost:8080/users/${user?.iduser}/events/${idevent}/deleteParticipation`, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(progressEvent)
            });

            if (!response.ok)
                addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);

            const deleteParticipation: UserInEventData = await response.json();
            if (deleteParticipation === undefined || deleteParticipation === null){
                addMessage(`Error in the server to delete the event participation`, true);
            } else {
                setEventsProgress((prev) =>
                    prev.filter((participation) => participation.iduserevent !== progressEvent.iduserevent)
                );
            }

        } catch (err) {
            if (err instanceof Error) {
                addMessage(err.message, true);
            } else addMessage("Unknown error deleting an event", true);
        }
    };

    const handleJoinChallenge = async (idchallenge: number): Promise<void> => {
        try {
            if (user === undefined) return;
            const response: Response = await fetch(`http://localhost:8080/users/${user?.iduser}/challenges/${idchallenge}/participate`, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ updateDate: new Date().toISOString().split("T")[0] })
            });

            if (!response.ok)
                addMessage(`Error on join challenge request: ${response.status} - ${response.statusText}`, true);

            const newParticipation: UserInEcologicalChallengeData = await response.json();
            if (newParticipation === undefined || newParticipation === null){
                addMessage(`Error: The challenge was not created in the user's groups`, true);
            } else {
                setChallengesProgress((prev) => [...prev, newParticipation]);
            }

        } catch (err) {
            if (err instanceof Error) {
                addMessage(err.message, true);
            } else addMessage("Unknown error joining a challenge", true);
        }
    };

    const handleUpdateChallenge = async (idchallenge: number, progressChallenge: UserInEcologicalChallengeData): Promise<void> => {
        try {
            if (user === undefined) return;
            const response: Response = await fetch(`http://localhost:8080/users/${user?.iduser}/challenges/${idchallenge}/update`, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(progressChallenge)
            });

            if (!response.ok)
                addMessage(`Error on update request: ${response.status} - ${response.statusText}`, true);

            const updateProgress: UserInEcologicalChallengeData = await response.json();
            if (updateProgress === undefined || updateProgress === null){
                addMessage(`Error in the server to update the challenge participation`, true);
            } else {
                setChallengesProgress((prev) => {
                    // Trova l'indice dell'elemento da aggiornare
                    const index = prev.findIndex(
                        (item) => item.id === updateProgress.id
                    );
                    if (index === -1) {
                        // Se non esiste, aggiungilo
                        return [...prev, updateProgress];
                    } else {
                        // Altrimenti, sostituisci l'elemento aggiornato
                        return prev.map((item, idx) =>
                            idx === index ? updateProgress : item
                        );
                    }
                });
                if (isChallengeTerminated(updateProgress.state)){
                    await handleUpdateBadges();
                }
            }

        } catch (err) {
            if (err instanceof Error) {
                addMessage(err.message, true);
            } else addMessage("Unknown error updating a challenge", true);
        }
    };

    const handleDeleteJoinChallenge = async (idchallenge: number, progressChallenge: UserInEcologicalChallengeData): Promise<void> => {
        try {
            if (user === undefined) return;
            const response: Response = await fetch(`http://localhost:8080/users/${user?.iduser}/challenges/${idchallenge}/deleteParticipation`, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(progressChallenge)
            });

            if (!response.ok)
                addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);

            const deleteParticipation: UserInEcologicalChallengeData = await response.json();
            if (deleteParticipation === undefined || deleteParticipation === null){
                addMessage(`Error in the server to delete the challenge participation`, true);
            } else {
                setChallengesProgress((prev) =>
                    prev.filter((participation) => participation.id !== progressChallenge.id)
                );
            }

        } catch (err) {
            if (err instanceof Error) {
                addMessage(err.message, true);
            } else addMessage("Unknown error deleting a challenge", true);
        }
    };

    const handleUpdateBadges = async (): Promise<void> => {
        try {
            if (user === undefined) return;
            const response: Response = await fetch(`http://localhost:8080/users/${user?.iduser}/badges?isExpiring=true`, { credentials: "include" });

            if (!response.ok) {
                addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                return;
            }

            const data: BadgeToUserData[] = await response.json();
            setBadgesProgress(data);
        } catch (err) {
            addMessage(err instanceof Error ? err.message : "Unknown error fetching badges", true);
        }
    };

    const isChallengeTerminated = (state: string | undefined): boolean => {
        if (!state || !state.includes("-")) return false;
        const parts: string[] = state.split("-");
        const n: number = Number(parts[0]);
        const m: number = Number(parts[1]);
        return !isNaN(n) && !isNaN(m) && n === m;
    };

    const isChallengeOnGoing = (state: string | undefined): boolean => {
        if (!state || !state.includes("-")) return false;
        const parts: string[] = state.split("-");
        const n: number = Number(parts[0]);
        const m: number = Number(parts[1]);
        return !isNaN(n) && !isNaN(m) && n > 0 && n < m;
    };

    const filteredData: {
        content: EventData | EcologicalChallengeData;
        progress: GroupData | UserInEventData | UserInEcologicalChallengeData | undefined;
    }[] = useMemo(() => {
        const data = activePage === "Events" ? events : challenges;
        const progressData = activePage === "Events"
            ? (isAdmin ? userGroups : eventsProgress)
            : challengesProgress;

        let filtered = data.map((item) => ({
            content: item,
            progress: progressData.find((progress) => {
                if (activePage === "Events") {
                    if (isAdmin) {
                        return (progress as GroupData)?.idgroup === (item as EventData)?.organizer.idgroup;
                    } else {
                        return (progress as UserInEventData)?.event?.idevent === (item as EventData)?.idevent;
                    }
                } else {
                    return (progress as UserInEcologicalChallengeData)?.ecologicalChallenge?.idecochallenge === (item as EcologicalChallengeData)?.idecochallenge;
                }
            })
        }));

        if (filters.onlyUserEvents) {
            filtered = filtered.filter(({ progress }) => !!progress);
        }

        if (filters.radio !== "all") {
            if (activePage === "Challenges") {
                filtered = filtered.filter(({ progress }) => !!progress);
                filtered = filtered.filter(({ progress }) => {
                    if (filters.radio === "ongoing") {
                        return isChallengeOnGoing((progress as UserInEcologicalChallengeData).state);
                    } else {
                        return isChallengeTerminated((progress as UserInEcologicalChallengeData).state);
                    }
                });
            } else if (filters.radio === "ongoing") {
                filtered = filtered.filter(({ content }) => {
                    return (content as EventData).state === "In Progress";
                });
            } else {
                filtered = filtered.filter(({ content }) => {
                    return (content as EventData).state === "Terminated";
                });
            }
        }

        if (filters.category) {
            filtered = filtered.filter(({ content }) => content.category === filters.category);
        }

        if (filters.searchText) {
            const searchTextLower = filters.searchText.toLowerCase();
            filtered = filtered.filter(({ content }) =>
                (content as EventData | EcologicalChallengeData).name.toLowerCase().includes(searchTextLower)
            );
        }

        return filtered;
    }, [activePage, events, challenges, eventsProgress, challengesProgress, userGroups, filters, isAdmin]);

    function handleFilterChange(key: string, value: string | boolean): void {
        setFilters((prev) => {
            if (activePage === "Challenges" && key === "onlyUserEvents") {
                return {
                    ...prev,
                    radio: "all",
                    searchText: "",
                    category: "",
                    onlyUserEvents: Boolean(value),
                };
            }
            return { ...prev, [key]: value };
        });
    }

    return (
        <div className="events-container">
            <div className={messages.length > 0 ? "events-left-column" : "events-left-column max-container"}>
                {activePage}
                <FilterEvents filters={filters} onFilterChange={handleFilterChange}/>
                <div className="events-list">
                    {filteredData.map(({content, progress}) =>
                        activePage === "Events" ? (
                            <Event key={`event-${(content as EventData).idevent}`} content={content as EventData}
                                   progressContent={progress as UserInEventData | undefined}
                                   userGroups={userGroups as GroupData[]}
                                   onEventParticipation={handleJoinEvent}
                                   onEventParticipationDelete={handleDeleteJoinEvent}
                                   onActivityAdded={onActivityAdded}
                            />
                        ) : (
                            <Challenge key={`challenge-${(content as EcologicalChallengeData).idecochallenge}-${(progress as UserInEcologicalChallengeData)?.state ?? "none"}`}
                                       content={content as EcologicalChallengeData}
                                       progressContent={progress as UserInEcologicalChallengeData | undefined}
                                       onChallengeParticipation={handleJoinChallenge}
                                       onChallengeUpdating={handleUpdateChallenge}
                                       onChallengeParticipationDelete={handleDeleteJoinChallenge}

                            />
                        ))}

                </div>
            </div>
            <div className="events-right-column">
                Expiring {activePage === "Events" ? "Trophies" : "Badges"}
                <ExpiringAwards trophiesProgress={trophiesProgress}
                                badgesProgress={badgesProgress}
                />
            </div>
        </div>
    );
}

export default EventsChallengesContainer;