import './UserGroupContainer.css'
import {ReactElement, useEffect, useState} from "react";
import {useUser} from "../context/UserContext.tsx";
import {
    BadgeData,
    BadgeToUserData,
    TrophyData,
    UserData,
    GroupData,
    TrophyToGroupData,
    UserInGroupData
} from "../data/data-model.ts";
import {useError} from "../context/ErrorContext.tsx";

interface UserGroupCardProps{
    groups: GroupData[];
}

function UserGroupCard({groups}: UserGroupCardProps): ReactElement {
    const {isAdmin, user} = useUser();

    const avgLevelGroups = (): number => {
        const levels: number[] = groups.filter((group): group is GroupData => group !== undefined)
            .map(group => group.level ?? 0);

        return levels.length > 0 ? levels.reduce((sum, level) => sum + level, 0) / levels.length : 0;
    };

    const avgXpGroups = (): number => {
        const xps: number[] = groups.filter((group): group is GroupData => group !== undefined)
            .map(group => group.xp ?? 0);

        return xps.length > 0 ? xps.reduce((sum, xp) => sum + xp, 0) / xps.length : 0;
    };

    return (
        <div className={isAdmin ? "usergroup-card group-card container" :
            "usergroup-card user-card container"}>
            <div className="usergroup-cover">
                <img src="/users/user2.jpeg" alt="User cover"
                     className="usergroup-cover-image"/>
            </div>
            <div className="usergroup-card-first-row">
                {isAdmin ? (
                    <span>Admin</span>
                ) : (
                    <span>{user?.username}</span>
                )}
            </div>
            <div className="usergroup-card-second-row">
                <span>{user?.name} {user?.surname}</span>
            </div>
            {!isAdmin && (
                <>
                    <div className="usergroup-card-third-row">
                        {user?.age} year old
                    </div>
                    <div className="usergroup-card-level-xp">
                    <span className="usergroup-card-level-xp-label">
                        Level {user?.level}
                    </span>
                        <span className="usergroup-card-level-xp-label">
                        <img className="usergroup-icon-xp" src="/icons/xp.png" alt="Xp Image"/>
                            {user?.xp}
                    </span>
                    </div>
                </>
            )}
            {isAdmin && (
                <div className="usergroup-card-level-xp">
                    <span className="usergroup-card-level-xp-label">
                        Level {isAdmin ? avgLevelGroups() : user?.level}{}
                    </span>
                        <span className="usergroup-card-level-xp-label">
                        <img className="usergroup-icon-xp" src="/icons/xp.png" alt="Xp Image"/>
                            {isAdmin ? avgXpGroups() : user?.xp}
                    </span>
                </div>
            )}
        </div>
    );
}

function UserGroupAwards(): ReactElement {
    const {addMessage} = useError()
    const {isAdmin, user} = useUser();
    const [isAwardsVisible, setIsAwardsVisible] = useState<boolean>(true);
    const [badges, setBadges] = useState<BadgeData[]>([]);
    const [trophies, setTrophies] = useState<TrophyData[]>([])

    function fetchBadges(): void {
        if (!user) return;
        fetch(`http://localhost:8080/users/${user?.iduser}/badges`, { credentials: "include" })
            .then((response) => {
                if (!response.ok) {
                    addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                }
                return response.json();
            })
            .then((data: BadgeToUserData[]) => {
                const completedBadges: BadgeData[] = data
                    .filter((badgeToUser) => badgeToUser.isCompleted)
                    .map((badgeToUser) => badgeToUser.badge);
                setBadges(completedBadges);
            })
            .catch((err) => {
                addMessage(err.message, true);
            });
    }

    function fetchTrophies(): void {
        if (!user) return;
        fetch(`http://localhost:8080/users/${user?.iduser}/trophiesToGroups`, { credentials: "include" })
            .then((response) => {
                if (!response.ok) {
                    addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                }
                return response.json();
            })
            .then((data: TrophyToGroupData[]) => {
                const completedTrophies: TrophyData[] = data
                    .filter((trophyToGroup) => trophyToGroup.isCompleted)
                    .map((trophyToGroup) => trophyToGroup.trophy);
                setTrophies(completedTrophies);
            })
            .catch((err) => {
                addMessage(err.message, true);
            });
    }

    function toggleAwards(): void {
        setIsAwardsVisible(!isAwardsVisible);
    }

    useEffect(() => {
        if (user?.iduser) {
            if (isAdmin) {
                fetchTrophies();
            } else {
                fetchBadges();
            }
        }
    }, [user?.iduser, isAdmin]);

    return (
        <div className="usergroup-awards">
            <div className="flex-row-usergroup">
                {isAdmin ? "Trophies" : "Badges"}
                <span>
                    <img onClick={toggleAwards}
                         className={`toggle-awards-icon ${isAwardsVisible ? "rotate" : ""}`}
                         src="/icons/arrow.png"
                         alt={isAwardsVisible ? "Hide Awards" : "Show Awards"}/>
                </span>
            </div>
            <div className={`usergroup-awards-list ${isAwardsVisible ? "show" : ""}`}>
                {isAdmin ? (
                    <>
                        {trophies.map((trophy) => (
                            <span className="usergroup-award" key={`trophy-${trophy.idtrophy}`}>
                                <span className="usergroup-trophy-logo">
                                    <img className="usergroup-trophy-logo-image"
                                         src={`/trophies/trophies${trophy.type}.png`}
                                         alt={`Trophie ${trophy.type}`}/>
                                </span>
                                <div className="flex-column">
                                    <div className="usergroup-trophy-content usergroup-badge-name">
                                        {trophy.name}
                                    </div>
                                    <div className="usergroup-trophy-content usergroup-badge-description">{trophy.description}</div>
                                </div>
                            </span>
                        ))}
                    </>
                ) : (
                    <>
                        {badges.map((badge) => (
                            <span className="usergroup-award" key={`badge-${badge.idbadge}`}>
                                <div className="usergroup-trophy-logo">
                                    <img className="usergroup-trophy-logo-image"
                                         src="/icons/approved.png" alt="Icon badge"/>
                                </div>
                                <div className="flex-column">
                                    <div className="usergroup-trophy-content usergroup-badge-name">{badge.name}</div>
                                    <div className="usergroup-trophy-content usergroup-badge-description">{badge.description}</div>
                                </div>
                            </span>
                        ))}
                    </>
                )}
            </div>
        </div>
    );
}

function UserGroupRanking(): ReactElement {
    const {user, isAdmin} = useUser();
    const {addMessage} = useError();
    const [isRankingVisible, setIsRankingVisible] = useState<boolean>(false);
    const [userRanking, setUserRanking] = useState<UserData[]>([]);
    const [groupRanking, setGroupRanking] = useState<GroupData[]>([]);

    function fetchUsersRanking(): void {
        if (!user) return;
        fetch(`http://localhost:8080/users/ranking`, { credentials: "include" })
            .then((response) => {
                if (!response.ok) {
                    addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                }
                return response.json();
            })
            .then((data: UserData[]) => {
                setUserRanking(data);
            })
            .catch((err) => {
                addMessage(err.message, true);
            });
    }

    function fetchGroupsRanking(): void {
        if (!user) return;
        fetch(`http://localhost:8080/groups/ranking`, { credentials: "include" })
            .then((response) => {
                if (!response.ok) {
                    addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                }
                return response.json();
            })
            .then((data: GroupData[]) => {
                setGroupRanking(data);
            })
            .catch((err) => {
                addMessage(err.message, true);
            });
    }

    function toggleRanking(): void {
        setIsRankingVisible(!isRankingVisible);
    }

    useEffect(() => {
        if (user?.iduser) {
            if (isAdmin) {
                fetchGroupsRanking();
            } else {
                fetchUsersRanking();
            }
        }
    }, [user?.iduser, isAdmin]);

    return (
        <div className="usergroup-rankings">
            <div className="flex-row-usergroup">
                <span className="usergroup-ranking-title">
                    Ranking
                </span>
                <span>
                    <img onClick={toggleRanking}
                         className={`toggle-ranking-icon ${isRankingVisible ? "rotate" : ""}`}
                         src="/icons/arrow.png"
                         alt={isRankingVisible ? "Hide Ranking" : "Show Ranking"}/>
                </span>
            </div>
            <div className={`usergroup-ranking-list ${isRankingVisible ? "show" : ""}`}>
                {isAdmin ?
                    <>
                        {groupRanking.map((group) => (
                            <UserGroup key={`group-ranking-${group.idgroup}`} section={"Ranking"} content={group} />
                        ))}
                    </> : <>
                        {userRanking.map((user) => (
                            <UserGroup key={`user-ranking-${user.iduser}`} section={"Ranking"} content={user} />
                        ))}
                    </>
                }
            </div>
        </div>
    );
}

interface UserGroupResume{
    userGroups:GroupData[];
    setGroupWhoAddEvent: (group: GroupData) => void;
}

function UserGroupResume({userGroups, setGroupWhoAddEvent}: UserGroupResume): ReactElement {
    return (
        <div className="usergroup-resume-list-groups">
            {userGroups.map((group) => (
                <UserGroup key={`group-resume-${group.idgroup}`} section={"Resume"} content={group}
                           setGroupWhoAddEvent={setGroupWhoAddEvent}
                />
            ))}
        </div>
    );
}

function UserGroupNewGroup(): ReactElement{
    const {user} = useUser();
    const {addMessage} = useError();
    const [isContainerOpen, setIsContainerOpen] = useState<boolean>(false);
    const [newGroups, setNewGroups] = useState<GroupData[]>([]);

    function fetchNewGroups(): void {
        if (!user) return;
        fetch(`http://localhost:8080/users/${user?.iduser}/availableGroups`, { credentials: "include" })
            .then((response) => {
                if (!response.ok) {
                    addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                }
                return response.json();
            })
            .then((data: GroupData[]) => {
                setNewGroups(data);
            })
            .catch((err) => {
                addMessage(err.message, true);
            });
    }

    function toggleContainerOpen(): void {
        setIsContainerOpen(!isContainerOpen);
    }

    useEffect(() => {
        if (user?.iduser) {
            fetchNewGroups()
        }
    }, [user?.iduser]);

    return (
        <div className="usergroups-new-groups container">
            <div className="flex-row-usergroup">
                Joinable Groups
                <img onClick={toggleContainerOpen}
                     className={`toggle-ranking-icon ${isContainerOpen ? "rotate" : ""}`}
                     src="/icons/arrow.png"
                     alt={isContainerOpen ? "Hide New Group" : "Show New Group"}/>
            </div>
            <div className={isContainerOpen ? "usergroup-new-list-groups show" : "usergroup-new-list-groups"}>
                {newGroups.map((group) => (
                    <UserGroup key={`group-new-${group.idgroup}`} section={"New"} content={group} />
                ))}
            </div>
        </div>
    );
}

interface UserGroupProps {
    section: string;
    content: UserData | GroupData;
    setGroupWhoAddEvent?: (group: GroupData) => void;
}

function UserGroup({section, content, setGroupWhoAddEvent}: UserGroupProps): ReactElement {
    const {user, isAdmin, activePage, handleChangePage} = useUser();
    const {addMessage} = useError();
    const [isMembersVisible, setIsMembersVisible] = useState<boolean>(false);
    const [membersGroup, setMembersGroup] = useState<UserData[]>([]);
    const [userRoles, setUserRoles ] = useState<UserInGroupData[]>([]);
    const isGroup: boolean = 'idgroup' in content;
    const isUser: boolean = 'iduser' in content;
    let idGroup: number;

    const pathImage: string | undefined = (() => {
        switch (section) {
            case "Ranking":
                if (isAdmin && isGroup) return `/groups/${content.cover}`
                else if (!isAdmin && isUser) return `/users/user1.jpeg`
                break;
            case "Resume":
                if (isGroup) return `/groups/${content.cover}`
                else if (isUser) return `/users/user1.jpeg`
                break;
            case "User":
                if (isUser) return `/users/user1.jpeg`
                break;
            case "New":
                if (isGroup) return `/groups/${content.cover}`
                break;

            default:
                return `/users/default-user`
        }
    })();

    const isUserAdminOfGroup: boolean = isGroup && userRoles.some(
        (role) => role.group.idgroup === (content as GroupData).idgroup && role.role === "Admin"
    );

    function fetchMembersGroup(): void {
        if (!user) return;
        if (section !== "Resume") return;
        if (isGroup) idGroup = (content as GroupData).idgroup;
        fetch(`http://localhost:8080/groups/${idGroup}/members`, { credentials: "include" })
            .then((response) => {
                if (!response.ok) {
                    addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                }
                return response.json();
            })
            .then((data: UserData[]) => {
                if (Array.isArray(data)) {
                    setMembersGroup(data);
                } else {
                    addMessage("Received data is not an array.", true);
                }
            })
            .catch((err) => {
                addMessage(err.message, true);
            });
    }

    function fetchUserRoles(): void {
        if (!user) return;
        fetch(`http://localhost:8080/users/${user?.iduser}/user-in-groups`, { credentials: "include" })
            .then((response) => {
                if (!response.ok) {
                    addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                }
                return response.json();
            })
            .then((data: UserInGroupData[]) => {
                setUserRoles(data);
            })
            .catch((err) => {
                addMessage(err.message, true);
            });
    }

    useEffect(() => {
        if (user?.iduser) {
            fetchMembersGroup();
            fetchUserRoles();
        }
    }, [user?.iduser]);

    function toggleMembers(): void {
        setIsMembersVisible(!isMembersVisible);
    }

    return (
        <div className={section === "Resume" ? "usergroup-resume" : "usergroup-ranking"}>
            {section === "Ranking" && (
                <>
                    <div className="usergroup-ranking-logo">
                        <img className="usergroup-ranking-logo-image"
                             src={pathImage}
                             alt={isAdmin ? `Group ${content.name} Ranking`: `User ${content.name} Ranking`}/>
                    </div>
                    <div className="usergroup-ranking-content">
                        <div className="flex-row">
                            <span className="usergroup-ranking-name">
                                {isGroup && content.name}
                                {isUser && `${content.name} ${(content as UserData).surname}`}
                            </span>
                            <span className="usergroup-ranking-xp">
                                <img className="usergroup-icon-xp" src="/icons/xp.png" alt="Xp Image"/>
                                {content.xp}
                            </span>
                        </div>
                        <div className="flex-row flex-row-end">
                            <span className="usergroup-ranking-level">
                                LV: {content.level}
                            </span>
                        </div>
                    </div>
                </>
            )}
            {(section === "Resume") && (
                <>
                    <div className="flex-row flex-row-end">
                        {(activePage === "Group" && isGroup && isUserAdminOfGroup) && (
                            <div className="btn btn-usergroup-add-event" onClick={() => {
                                if (setGroupWhoAddEvent) setGroupWhoAddEvent(content as GroupData);
                                handleChangePage("AddEvent");

                            }}>
                                <img className="event-add" src="/icons/add.png" alt="Add Image"/>
                                Organize New Event
                            </div>
                        )}
                    </div>
                    <div className="flex-row-usergroup">
                        <div className="usergroup-resume-logo">
                            <img className="usergroup-ranking-logo-image"
                                 src={pathImage}
                                 alt={isAdmin ? `Group ${content.name} Resume`: `User ${content.name} Resume`}/>
                        </div>
                        <div className="usergroup-ranking-content">
                            <div className="flex-row-usergroup">
                                <span className="usergroup-ranking-name">
                                     {isGroup && content.name}
                                </span>
                                <span className="usergroup-ranking-xp">
                                    <img className="usergroup-icon-xp" src="/icons/xp.png" alt="Xp Image"/>
                                    {isGroup && content.xp}
                                </span>
                            </div>
                            <div className="flex-row-usergroup">
                                <span className="usergroup-group-type">
                                    {isGroup && (content as GroupData).type}
                                </span>
                                <span className="usergroup-resume-empty"></span>
                                <span className="usergroup-resume-empty"></span>
                                <span className="usergroup-resume-level">
                                    LV: {isGroup && content.level}
                                </span>
                            </div>
                        </div>
                    </div>
                    <div className="flex-row-usergroup usergroup-resume-row-toggle">
                        <span className="usergroup-resume-title">
                            Members
                        </span>
                        <span className="usergroup-resume-empty"></span>
                        <span className="usergroup-resume-empty"></span>
                        <img onClick={toggleMembers}
                             className={`toggle-ranking-icon ${isMembersVisible ? "rotate" : ""}`}
                             src="/icons/arrow.png"
                             alt={isMembersVisible ? "Hide Members" : "Show Members"}/>
                    </div>
                    <div className={isMembersVisible ? "usergroup-resume-userlist show" : "usergroup-resume-userlist"}>
                        {membersGroup.map((user) => (
                            <UserGroup key={`user-new-${user.iduser}`} section={"User"} content={user} />
                        ))}
                    </div>
                </>
            )}
            {section === "User" && (
                <div className="flex-row-usergroup">
                    <div className="usergroup-ranking-logo">
                        <img className="usergroup-ranking-logo-image"
                             src={pathImage}
                             alt={isUser ? `User ${content.name} Member` : `User Member`}/>
                    </div>
                    <div className="usergroup-ranking-content">
                        <div className="flex-row-usergroup">
                            <span className="usergroup-ranking-name">
                                {isUser && `${content.name} ${(content as UserData).surname}`}
                            </span>
                            <span className="usergroup-ranking-xp">
                                <img className="usergroup-icon-xp" src="/icons/xp.png" alt="Xp Image"/>
                                {content.xp}
                            </span>
                        </div>
                        <div className="flex-row flex-row-end">
                            <span className="usergroup-ranking-level">
                                LV: {content.level}
                            </span>
                        </div>
                    </div>
                </div>
            )}
            {section === "New" && (
                <>
                    <div className="usergroup-new-group flex-row-usergroup">
                        <div className="usergroup-resume-logo">
                            <img className="usergroup-ranking-logo-image"
                                 src={pathImage}
                                 alt="Group Resume"/>
                        </div>
                        <div className="usergroup-resume-content">
                            <div className="flex-row-usergroup">
                                <span className="usergroup-ranking-name">
                                    {isGroup && content.name}
                                </span>
                                <span className="usergroup-ranking-xp">
                                    <img className="usergroup-icon-xp" src="/icons/xp.png" alt="Xp Image"/>
                                    {content.xp}
                                </span>
                            </div>
                            <div className="flex-row-usergroup">
                                <span className="usergroup-group-type">
                                    {isGroup && (content as GroupData).type}
                                </span>
                                <span className="usergroup-resume-empty"></span>
                                <span className="usergroup-resume-empty"></span>
                                <span className="usergroup-resume-level">
                                    LV: {content.level}
                                </span>
                            </div>
                        </div>
                        <div className="btn btn-group-add">
                            <img className="group-add" src="/icons/add.png" alt="Participate to Group"/>
                        </div>
                    </div>
                </>
            )}
        </div>
    )
}

interface UserGroupContainerProps{
    onEventAdded: (group: GroupData) => void
}

function UserGroupContainer({onEventAdded}: UserGroupContainerProps): ReactElement {
    const {isAdmin, activePage, user, setUser} = useUser();
    const {addMessage, messages} = useError();
    const [isGroupsVisible, setIsGroupsVisible] = useState<boolean>(true);
    const [userGroups, setUserGroups] = useState<GroupData[]>([]);

    function fetchUser(): void {
        if (user === undefined) return;
        if (!isAdmin) {
            fetch(`http://localhost:8080/users/${user?.iduser}`, { credentials: "include" })
                .then((response) => {
                    if (!response.ok) {
                        addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                    }
                    return response.json();
                })
                .then((data: UserData) => {
                    setUser(data);
                })
                .catch((err) => {
                    addMessage(err.message, true);
                });
        }
    }

    function fetchUserGroups(): void {
        if (user === undefined) return;
        if (isAdmin) {
            fetch(`http://localhost:8080/users/${user?.iduser}/adminGroups`, { credentials: "include" })
                .then((response) => {
                    if (!response.ok) {
                        addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                    }
                    return response.json();
                })
                .then((data: GroupData[]) => {
                    setUserGroups(data);
                })
                .catch((err) => {
                    addMessage(err.message, true);
                });
        } else {
            fetch(`http://localhost:8080/users/${user?.iduser}/groups`, { credentials: "include" })
                .then((response) => {
                    if (!response.ok) {
                        addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                    }
                    return response.json();
                })
                .then((data: GroupData[]) => {
                    setUserGroups(data);
                })
                .catch((err) => {
                    addMessage(err.message, true);
                });
        }
    }

    useEffect(() => {
        if (user?.iduser) {
            fetchUser();
            fetchUserGroups();
        }
    }, [user?.iduser, onEventAdded]);


    function toggleGroups(): void {
        return setIsGroupsVisible(!isGroupsVisible);
    }

    return (
        <div className={messages.length > 0 ? "usergroup-container" : "usergroup-container usergroup-container-max"}>
            <div className="usergroup-left-column">
                <UserGroupCard groups={userGroups} />
                <UserGroupAwards />
                <UserGroupRanking />
            </div>
            <div className="usergroup-right-column">
                <div className={isAdmin ? "usergroups-resume-full container" : "usergroups-resume container"}>
                    <div className="flex-row-usergroup">
                        Groups
                        <img onClick={toggleGroups}
                             className={`toggle-ranking-icon ${isGroupsVisible ? "rotate" : ""}`}
                             src="/icons/arrow.png"
                             alt={isGroupsVisible ? "Hide Groups" : "Show Groups"}/>
                    </div>
                    <div className={isGroupsVisible ? "resume-groups show" : "resume-groups"}>
                        <UserGroupResume userGroups={userGroups} setGroupWhoAddEvent={onEventAdded}/>
                    </div>
                </div>
                {(activePage === "User" && !isAdmin) && (
                    <UserGroupNewGroup />
                )}
            </div>
        </div>
    )
}

export default UserGroupContainer;